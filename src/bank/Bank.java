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
     * TODO: ����������� �����. ����� ��������� ������ ����� �������.
     * ���� ����� ���������� > 50000, �� ����� ���������� ����������,
     * ��� ������������ �� �������� ������ ������������ � ����������
     * ����� isFraud. ���� ������������ true, �� �������� ����������
     * ������ (��� � �� ���� ����������)
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
                    //���� ����� ��������� �� �������� - ��� ���� ��� ����������
                    while (inCheck.contains(fromAccountNum) || inCheck.contains(toAccountNum)) {
                    Thread.sleep(20);
                    }
                //��������� ��������������� �� �����
                    if (!isBlocked(fromAccount) && !isBlocked(toAccount)) {
                    //��������� ������� ������� �� �������� � �������� ��������� �������
                        if (amount <= fromAccount.getMoney()) {
                        //��������� �� ��������� �� ����� ���������� ��� ��������
                            if (amount >= maxSum) {
                                //��������� �������� � ���
                                accountsChecking(fromAccountNum, toAccountNum);
                                //��������� ��������
                                if (isFraud(fromAccountNum, toAccountNum, amount)) {
                                    message = String.format("�������� �������������, ����� %s � %s �������������", fromAccountNum, toAccountNum);
                                    blockAccounts(fromAccountNum, toAccountNum);
                                } else {
                                    moveMoney(amount, fromAccount, toAccount);
                                    message = String.format("�������� �������� ������� � %s �� %s ���������", fromAccountNum, toAccountNum);
                                }
                                accountsChecked(fromAccountNum, toAccountNum);
                            }
                            else{
                                moveMoney(amount, fromAccount, toAccount);
                                message = String.format("�������� �������� ������� � %s �� %s ���������", fromAccountNum, toAccountNum);
                            }
                        } else {
                        message = "�� ����� ������������ �������";
                        }
                    } else {
                        if (isBlocked(fromAccount) && isBlocked(toAccount)) {
                            message = String.format("������� %s � %s ������������", fromAccount.getAccNumber(), toAccount.getAccNumber());
                        } else if (isBlocked(fromAccount)) {
                            message = String.format("��� ������� �������� �� %s, ��������� ��� ������� %s ������������", toAccountNum, fromAccountNum);
                        } else {
                            message = String.format("��� ������� ������� � %s, ��������� ��� ������� %s ������������ ", fromAccountNum, toAccount.getAccNumber());
                        }
                    }
                } catch(InterruptedException e){
                    System.out.println("���-�� ����� �� ���");
                    Thread.currentThread().interrupt();
                }
                //������� ��������� � ������� ��������
                if (message!=null) {
                    System.out.println(message);
                }
            }
        });
    }

    //����� ���������� ��������
    private void accountsChecked(String fromAccountNum, String toAccountNum) {
        inCheck.remove(fromAccountNum);
        inCheck.remove(toAccountNum);
    }

    //����� �������� ���������
    private void accountsChecking(String fromAccountNum, String toAccountNum) {
        inCheck.add(fromAccountNum);
        inCheck.add(toAccountNum);
        System.out.println("�������� �������� � " + fromAccountNum + " �� " + toAccountNum + " ��������� �� ��������");
    }

    private void blockAccounts(String fromAccountNum, String toAccountNum) {
        blockAccount(fromAccountNum);
        blockAccount(toAccountNum);
    }

    void blockAccount(String accountName) {
        blockedAccounts.add(accountName);
    }

    //����� ��� ���������� � �������� �������
    private void moveMoney(long amount, Account fromAccount, Account toAccount) {
        removeBalance(fromAccount, amount);
        addBalance(toAccount, amount);
    }

    /**
     * TODO: ����������� �����. ���������� ������� �� �����.
     */
    synchronized long getBalance(String account)
    {
        if (accounts.containsKey(account)) {
                return accounts.get(account).getMoney();
        } else {
            System.out.println("������ �������� �� ����������");
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
            System.out.println("������� � ����� ������� ��� ����������");
        }
    }

    public synchronized String getAccountInformation (String account) {
        if (accounts.containsKey(account)) {
            return "������� ��� �������: " + accounts.get(account).getAccNumber() +" �� ����� �����:" + accounts.get(account).getMoney();
        } else {
            return "������ �������� �� ����������";
        }
    }
 }
