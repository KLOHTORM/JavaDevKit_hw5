import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;

public class Table extends Thread{
    private final int PHILOSOPHER_COUNT = 5;
    private Fork[] forks;
    private Philosopher[] philosophers;
    private CountDownLatch cdl;

    public Table() {
        forks = new Fork[PHILOSOPHER_COUNT];
        philosophers = new Philosopher[PHILOSOPHER_COUNT];
        cdl = new CountDownLatch(PHILOSOPHER_COUNT);
        init();
    }

    @Override
    public void run() {
        System.out.println(" ");
        System.out.println("Все приготовились к трапезе!");
        System.out.println(" ");
        try {
            thinkingProcess();
            cdl.await();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println(" ");
        System.out.println("Все сыты, трапеза окончена!");
        System.out.println(" ");
    }

    public synchronized boolean tryGetForks(int leftFork, int rightFork) {
        if (!forks[leftFork].isUsing() && !forks[rightFork].isUsing()) {
            forks[leftFork].setUsing(true);
            forks[rightFork].setUsing(true);
            return true;
        }
        return false;
    }

    public void putForks(int leftFork, int rightFork) {
        forks[leftFork].setUsing(false);
        forks[rightFork].setUsing(false);
    }

    private void init() {
        for (int i = 0; i < PHILOSOPHER_COUNT; i++) {
            forks[i] = new Fork();
        }

        for (int i = 0; i < PHILOSOPHER_COUNT; i++) {
            philosophers[i] = new Philosopher("Philosopher N" + i, this, i, (i + 1) % PHILOSOPHER_COUNT, cdl);
        }
    }

    private void thinkingProcess() {
        for (Philosopher philosoph1 : philosophers) {
            philosoph1.start();
        }
    }
}
