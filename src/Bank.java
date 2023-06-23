import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Bank {

    private Map<String, Account> accounts;
    private final Random random = new Random();
    private boolean isFraud = false;

    private synchronized boolean isFraud() throws InterruptedException {
        Thread.sleep(1000);
        isFraud = random.nextBoolean();
        return isFraud;
    }

    /**
     * TODO: реализовать метод. Метод переводит деньги между счетами. Если сумма транзакции > 50000,
     * то после совершения транзакции, она отправляется на проверку Службе Безопасности – вызывается
     * метод isFraud. Если возвращается true, то делается блокировка счетов (как – на ваше
     * усмотрение)
     */
    public void transfer(String fromAccountNum, String toAccountNum, long amount)
            throws InterruptedException
    {
        if ((amount > 50_000 && isFraud())) {
            fraudRemove(fromAccountNum, toAccountNum);
        } else {
            refactorAccounts(fromAccountNum, toAccountNum, amount);
        }
    }

    private void refactorAccounts(String fromAccountNum, String toAccountNum, long amount) {
        Map<String, Account> accounts2 = accounts;
        accounts = new HashMap<>();
        for (Map.Entry<String, Account> entry: accounts2.entrySet())
        {
            if(entry.getValue().getAccNumber().equals(fromAccountNum)){
                long resultAmount = entry.getValue().getMoney() - amount;
                Account account = new Account(resultAmount, fromAccountNum);
                accounts.put(entry.getKey(), account);
            }
            else if (entry.getValue().getAccNumber().equals(toAccountNum)) {
                long resultAmount = entry.getValue().getMoney() + amount;
                Account account = new Account(resultAmount, toAccountNum);
                accounts.put(entry.getKey(), account);
            }
            else {
                accounts.put(entry.getKey(), entry.getValue());
            }
        }
    }

    private void fraudRemove(String fromAccountNum, String toAccountNum) {
        Map<String, Account> accounts2 = accounts;
        accounts = new HashMap<>();
        for (Map.Entry<String, Account> entry: accounts2.entrySet())
        {
            if(entry.getValue().getAccNumber().equals(fromAccountNum)
                    || entry.getValue().getAccNumber().equals(toAccountNum)){
                continue;
            }
            accounts.put(entry.getKey(), entry.getValue());
        }
    }

    /**
     * TODO: реализовать метод. Возвращает остаток на счёте.
     */
    public long getBalance(String accountNum) {
        return 0;
    }

    public long getSumAllAccounts() {
        return 0;
    }


    public Map<String, Account> getAccounts() {
        return accounts;
    }

    public void setAccounts(Map<String, Account> accounts) {
        this.accounts = accounts;
    }

    public boolean ifFraud() {
        return isFraud;
    }

}
