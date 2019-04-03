package bank;

import org.junit.Test;
import org.junit.Assert;

public class bankTest {

        Bank bank = new Bank();

        @Test
        public void Test1 () throws InterruptedException {
            bank.addAccount("1", 50000);
            bank.addAccount("2", 30000);
            bank.transfer("1", "2", 50000);
            Thread.currentThread().sleep(50);
            Assert.assertEquals(80000, bank.getBalance("2"));
        }

        @Test
        public void test2() throws InterruptedException {
            bank.addAccount("3", 4000);
            bank.addAccount("4", 3000);
            bank.transfer("3", "4", 2000);
            Thread.currentThread().sleep(50);
            Assert.assertEquals(5000, bank.getBalance("4"));
        }

        @Test
        public void test3() throws InterruptedException {
            bank.addAccount("1", 5000);
            bank.blockAccount("1");
            bank.addAccount("2", 5000);
            bank.transfer("1", "2", 2000);

        }
}
