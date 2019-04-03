package bank;

import threadpool.ThreadPool;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

/**
 * Created by Danya on 18.02.2016.
 */
public class Bank
{
    private HashMap<String, Account> accounts;
    private final Random random = new Random();
    private final static long maxSum = 50000;
    private ArrayList<String> blockedAccounts;
    private ArrayList<String> inCheck;
    private ThreadPool threadPool;

    {
        accounts = new HashMap<>();
        blockedAccounts = new ArrayList<>();
        threadPool = new ThreadPool(Runtime.getRuntime().availableProcessors()*2);
        inCheck = new ArrayList<>();
    }
    public synchronized boolean isFraud(String fromAccountNum, String toAccountNum, long amount)
        throws InterruptedException
    {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        Thread.interrupted();
                    }
        return random.nextBoolean();
    }

    /**
     * TODO: реализовать метод. Метод переводит деньги между счетами.
     * Если сумма транзакции > 50000, то после совершения транзакции,
     * она отправляется на проверку Службе Безопасности – вызывается
     * метод isFraud. Если возвращается true, то делается блокировка
     * счетов (как – на ваше усмотрение)
     */
    public void transfer(String fromAccountNum, String toAccountNum, long amount)
    {
        threadPool.addTask(new Runnable() {
            @Override
            public void run() {
                Account fromAccount = accounts.get(fromAccountNum);
                Account toAccount = accounts.get(toAccountNum);
                String message = null ;
                try {
                    //Если счета находятся на проверке - ждём пока она завершится
                    while (inCheck.contains(fromAccountNum) || inCheck.contains(toAccountNum)) {
                    Thread.sleep(20);
                    }
                //Проверяем незаблокированы ли счета
                    if (!isBlocked(fromAccount) && !isBlocked(toAccount)) {
                    //Проверяем наличие средств на аккаунте с которого выполняем перевод
                        if (amount <= fromAccount.getMoney()) {
                        //Проверяем не превышает ли сумма допустимую для проверки
                            if (amount >= maxSum) {
                                //Добавляем аккаунты в спи
                                accountsChecking(fromAccountNum, toAccountNum);
                                //Проверяем операцию
                                if (isFraud(fromAccountNum, toAccountNum, amount)) {
                                    message = String.format("Операция подозрительна, счета %s и %s заблокированы", fromAccountNum, toAccountNum);
                                    blockAccounts(fromAccountNum, toAccountNum);
                                } else {
                                    moveMoney(amount, fromAccount, toAccount);
                                    message = String.format("Операция перевода средств с %s на %s выполнена", fromAccountNum, toAccountNum);
                                }
                                accountsChecked(fromAccountNum, toAccountNum);
                            }
                            else{
                                moveMoney(amount, fromAccount, toAccount);
                                message = String.format("Операция перевода средств с %s на %s выполнена", fromAccountNum, toAccountNum);
                            }
                        } else {
                        message = "На счету недостаточно средств";
                        }
                    } else {
                        if (isBlocked(fromAccount) && isBlocked(toAccount)) {
                            message = String.format("Аккаунт %s и %s заблокирован", fromAccount.getAccNumber(), toAccount.getAccNumber());
                        } else if (isBlocked(fromAccount)) {
                            message = String.format("При попытке перевода на %s, оказалось что аккаунт %s заблокирован", toAccountNum, fromAccountNum);
                        } else {
                            message = String.format("При попытке перевод с %s, оказалось что аккаунт %s заблокирован ", fromAccountNum, toAccount.getAccNumber());
                        }
                    }
                } catch(InterruptedException e){
                    System.out.println("Что-то пошло не так");
                    Thread.currentThread().interrupt();
                }
                //Выводим сообщение о статусе операции
                if (message!=null) {
                    System.out.println(message);
                }
            }
        });
    }

    //Метод завершения проверки
    private void accountsChecked(String fromAccountNum, String toAccountNum) {
        inCheck.remove(fromAccountNum);
        inCheck.remove(toAccountNum);
    }

    //Метод проверки аккаунтов
    private void accountsChecking(String fromAccountNum, String toAccountNum) {
        inCheck.add(fromAccountNum);
        inCheck.add(toAccountNum);
        System.out.println("Операция перевода с " + fromAccountNum + " на " + toAccountNum + " находится на проверке");
    }

    private void blockAccounts(String fromAccountNum, String toAccountNum) {
        blockAccount(fromAccountNum);
        blockAccount(toAccountNum);
    }

    void blockAccount(String accountName) {
        blockedAccounts.add(accountName);
    }

    //Метод для начисления и списания средств
    private void moveMoney(long amount, Account fromAccount, Account toAccount) {
        removeBalance(fromAccount, amount);
        addBalance(toAccount, amount);
    }

    /**
     * TODO: реализовать метод. Возвращает остаток на счёте.
     */
    synchronized long getBalance(String account)
    {
        if (accounts.containsKey(account)) {
                return accounts.get(account).getMoney();
        } else {
            System.out.println("Такого аккаунта не существует");
            return 0;
        }
    }

    private synchronized void setBalance(Account account, long amount) {
            account.setMoney(amount);
    }

    private synchronized void addBalance(Account account, long amount) {
            account.setMoney(account.getMoney() + amount);
    }

    private synchronized void removeBalance (Account account, long amount) {
            account.setMoney(account.getMoney() - amount);
    }


    private synchronized boolean isBlocked(Account account) {
        return blockedAccounts.contains(account.getAccNumber());
    }

    public void addAccount (String accountNum, long amount) {
        if (!accounts.containsKey(accountNum)) {
            accounts.put(accountNum, new Account(accountNum, amount));
        } else {
            System.out.println("Аккаунт с таким номером уже существует");
        }
    }

    public synchronized String getAccountInformation (String account) {
        if (accounts.containsKey(account)) {
            return "Аккаунт под номером: " + accounts.get(account).getAccNumber() +" на счету сумма:" + accounts.get(account).getMoney();
        } else {
            return "Такого аккаунта не существует";
        }
    }
 }
