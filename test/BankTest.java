import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("Транзакции в многопоточной среде")
public class BankTest {

    private static Map<String, Account> expectedMap = new HashMap<>();
    private static Map<String, Account> actualMap = new HashMap<>();
    private static Bank bank = new Bank();
    private static boolean isFraud = false;
    private List<Thread> threads = new ArrayList<>();


    private Map<String, Account> startMap(){

        Map<String, Account> start = new HashMap<>();
        start.put("Василий", new Account(156671, "156"));
        start.put("Пётр", new Account(305615, "157"));
        start.put("Михаил", new Account(1000000, "158"));
        start.put("Мария", new Account(650000, "159"));
        start.put("Александр", new Account(1650302, "160"));

        return start;
    };

    @Test
    @DisplayName("Сумма транзакции больше 50.000. Проверка на удаление даных после проверки СБ")
    public void transferTest() throws InterruptedException {

        bank.setAccounts(startMap());
        for (int i = 0; i < 10; i++) {
            threads.add(new Thread(BankTest::runTransfers));
        }
        threads.forEach(Thread::start);
        for (Thread thread:
             threads) {
            thread.join();
        }

        if (isFraud) {
            expectedMap.put("Василий", new Account(156671, "156"));
            expectedMap.put("Пётр", new Account(305615, "157"));
            expectedMap.put("Александр", new Account(1650302, "160"));
        } else {
            expectedMap.put("Василий", new Account(156671, "156"));
            expectedMap.put("Пётр", new Account(305615, "157"));
            expectedMap.put("Михаил", new Account(495000, "158"));
            expectedMap.put("Мария", new Account(1155000, "159"));
            expectedMap.put("Александр", new Account(1650302, "160"));
        }

        String expected = mapToString(expectedMap);
        String actual = mapToString(actualMap);

        assertEquals(expected, actual);

    }

    private static void runTransfers() {

        try {
            bank.transfer("158", "159", 50500);
            if (bank.ifFraud()) {
                isFraud = true;
            }
            actualMap = bank.getAccounts();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
    
    private String mapToString(Map<String, Account> stringAccountMap)
    {
        StringBuffer buffer = new StringBuffer();
        for (Map.Entry<String, Account> entry:
             stringAccountMap.entrySet()) {
            buffer.append(entry.getKey() + ":\n" + entry.getValue() + "\n");
        }
        return buffer.toString();
    }
}
