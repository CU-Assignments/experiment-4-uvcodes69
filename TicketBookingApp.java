import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class TicketBookingSystem {
    private int availableSeats = 10;
    private final Lock lock = new ReentrantLock();

    public void bookSeat(String customer) {
        lock.lock();
        try {
            if (availableSeats > 0) {
                System.out.println(customer + " booked a seat. Remaining seats: " + (--availableSeats));
            } else {
                System.out.println(customer + " tried to book, but no seats are available.");
            }
        } finally {
            lock.unlock();
        }
    }
}

class BookingThread extends Thread {
    private TicketBookingSystem system;
    private String customer;

    public BookingThread(TicketBookingSystem system, String customer, int priority) {
        this.system = system;
        this.customer = customer;
        setPriority(priority);
    }

    @Override
    public void run() {
        system.bookSeat(customer);
    }
}

public class TicketBookingApp {
    public static void main(String[] args) {
        TicketBookingSystem system = new TicketBookingSystem();
        Thread[] threads = new Thread[15];

        // Simulating VIP and regular bookings
        for (int i = 0; i < 5; i++) {
            threads[i] = new BookingThread(system, "VIP Customer " + (i + 1), Thread.MAX_PRIORITY);
        }
        for (int i = 5; i < 15; i++) {
            threads[i] = new BookingThread(system, "Regular Customer " + (i - 4), Thread.MIN_PRIORITY);
        }

        for (Thread thread : threads) {
            thread.start();
        }

        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
