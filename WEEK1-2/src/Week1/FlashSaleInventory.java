package Week1;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class FlashSaleInventory {

    // productId -> stock
    private ConcurrentHashMap<String, AtomicInteger> stockMap;

    // productId -> waiting queue
    private ConcurrentHashMap<String, Queue<Integer>> waitingList;

    public FlashSaleInventory() {
        stockMap = new ConcurrentHashMap<>();
        waitingList = new ConcurrentHashMap<>();
    }

    // Add product with stock
    public void addProduct(String productId, int stock) {
        stockMap.put(productId, new AtomicInteger(stock));
        waitingList.put(productId, new ConcurrentLinkedQueue<>());
    }

    // Check stock
    public int checkStock(String productId) {
        AtomicInteger stock = stockMap.get(productId);
        return (stock != null) ? stock.get() : 0;
    }

    // Purchase item (thread-safe)
    public String purchaseItem(String productId, int userId) {
        AtomicInteger stock = stockMap.get(productId);

        if (stock == null) {
            return "Product not found";
        }

        while (true) {
            int currentStock = stock.get();

            // Out of stock → add to waiting list
            if (currentStock <= 0) {
                waitingList.get(productId).add(userId);
                return "Added to waiting list, position: " + waitingList.get(productId).size();
            }

            // Try atomic decrement
            if (stock.compareAndSet(currentStock, currentStock - 1)) {
                return "Success! Remaining stock: " + (currentStock - 1);
            }
        }
    }

    // Process waiting list when stock refilled
    public void restock(String productId, int quantity) {
        AtomicInteger stock = stockMap.get(productId);

        if (stock == null) return;

        stock.addAndGet(quantity);

        Queue<Integer> queue = waitingList.get(productId);

        while (stock.get() > 0 && !queue.isEmpty()) {
            int userId = queue.poll();
            stock.decrementAndGet();
            System.out.println("User " + userId + " got item from waiting list!");
        }
    }

    // Demo
    public static void main(String[] args) throws InterruptedException {
        FlashSaleInventory system = new FlashSaleInventory();

        system.addProduct("IPHONE15_256GB", 5);

        ExecutorService executor = Executors.newFixedThreadPool(10);

        for (int i = 1; i <= 10; i++) {
            int userId = i;
            executor.submit(() -> {
                String result = system.purchaseItem("IPHONE15_256GB", userId);
                System.out.println("User " + userId + ": " + result);
            });
        }

        executor.shutdown();
        executor.awaitTermination(5, TimeUnit.SECONDS);

        System.out.println("Final stock: " + system.checkStock("IPHONE15_256GB"));
    }
}