import bank.Bank;

import java.util.Random;
import java.util.Scanner;

public class Loader {
    public static void main(String[] args) {
        Random random = new Random();
        Bank bank = new Bank();
        for (int i =0; i<100; i++) {
            bank.addAccount(i+"", 50000 + random.nextInt(50000));
        }
        for (int k =0; k<99; k++) {
            int value = random.nextInt(50000);
            bank.transfer(k + "", "" +(k+1), 50000);
        }
//        for (;;) {
//            Scanner scanner = new Scanner(System.in);
//            System.out.println("������� ������� � �������� ���������� ��������� �������");
//            String accFrom = scanner.nextLine();
//            System.out.println(bank.getAccountInformation(accFrom));
//            System.out.println("������� ������� �� ������� ���������� ��������� �������");
//            String accTo = scanner.nextLine();
//            System.out.println(bank.getAccountInformation(accTo));
//            System.out.println("������� ����� ��� ��������");
//            String sum = scanner.nextLine();
//            bank.transfer(accFrom, accTo, Long.parseLong(sum));
//
//        }
    }
}
