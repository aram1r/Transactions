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
//            System.out.println("¬ведите аккаунт с которого необходимо выполнить перевод");
//            String accFrom = scanner.nextLine();
//            System.out.println(bank.getAccountInformation(accFrom));
//            System.out.println("¬ведите аккаунт на который необходимо выполнить перевод");
//            String accTo = scanner.nextLine();
//            System.out.println(bank.getAccountInformation(accTo));
//            System.out.println("¬ведите сумму дл€ перевода");
//            String sum = scanner.nextLine();
//            bank.transfer(accFrom, accTo, Long.parseLong(sum));
//
//        }
    }
}
