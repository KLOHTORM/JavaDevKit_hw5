import java.util.Random;
import java.util.concurrent.CountDownLatch;

public class Philosopher extends Thread{
    private int leftFork;
    private int rightFork;
    private int countEat;
    private Random random;
    private CountDownLatch cdl;
    private String name;
    private Table table;


    public Philosopher(String name, Table table, int leftFork, int rightFork, CountDownLatch cdl) {
        this.table = table;
        this.name = name;
        this.leftFork = leftFork;
        this.rightFork = rightFork;
        this.cdl = cdl;
        countEat = 0;
        random = new Random();
    }

    public CountDownLatch getCdl() {
        return cdl;
    }

    public void think() throws InterruptedException {
        sleep(random.nextInt(100, 2000));
    }

    public void eat() throws InterruptedException {
        if (table.tryGetForks(leftFork, rightFork)) {
            System.out.println(name + " использует вилки " + leftFork + " и " + rightFork);
            sleep(random.nextLong(3000, 6000));
            table.putForks(leftFork, rightFork);
            System.out.println(name + " доел и приступил к размышлениям, вернув вилки "
                    + leftFork + " и " + rightFork);
            countEat++;
        }
    }

    @Override
    public void run() {
        while (countEat < 3) {
            try {
                think();
                eat();
            } catch (InterruptedException e) {
                e.fillInStackTrace();
            }
        }
        System.out.println(name + " теперь полностью сыт! ");
        cdl.countDown();
    }
}
