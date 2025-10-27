import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class InterruptSimulator {

    enum Device {
        KEYBOARD(1, "Keyboard"),
        MOUSE(2, "Mouse"),
        PRINTER(3, "Printer");

        final int priority;
        final String label;
        Device(int priority, String label) {
            this.priority = priority;
            this.label = label;
        }
    }

    static class InterruptEvent {
        final Device device;
        InterruptEvent(Device device) { this.device = device; }
    }

    static class InterruptComparator implements Comparator<InterruptEvent> {
        public int compare(InterruptEvent a, InterruptEvent b) {
            return Integer.compare(a.device.priority, b.device.priority);
        }
    }

    private final PriorityBlockingQueue<InterruptEvent> irqQueue =
            new PriorityBlockingQueue<>(11, new InterruptComparator());

    private final EnumMap<Device, AtomicBoolean> maskMap = new EnumMap<>(Device.class);

    private volatile boolean running = true;

    
    public InterruptSimulator() {
        for (Device d : Device.values()) {
            maskMap.put(d, new AtomicBoolean(false));
        }
    }

    private void interruptGenerator(Device device) {
        Random rnd = new Random();
        try {
            while (running) {
                Thread.sleep(1000 + rnd.nextInt(2000));
                irqQueue.put(new InterruptEvent(device));
                System.out.printf("\n>>> [%s] requests interrupt! <<<\n", device.label);
            }
        } catch (InterruptedException e) { }
    }

    private void interruptController() {
        try {
            while (running) {
                InterruptEvent evt = irqQueue.take();
                Device d = evt.device;

                if (maskMap.get(d).get()) {
                    System.out.printf("🚫 %s Interrupt Ignored (Masked)\n", d.label);
                    continue;
                }

                System.out.printf("✅ %s Interrupt Triggered -> Handling ISR...\n", d.label);
                
                long workTime = (d == Device.KEYBOARD) ? 500 : 1000;
                Thread.sleep(workTime);

                System.out.printf("🏁 %s ISR Completed.\n", d.label);
            }
        } catch (InterruptedException e) {
            System.out.println("Interrupt Controller shutting down.");
        }
    }
    
    public void start() {
        for (Device d : Device.values()) {
            new Thread(() -> interruptGenerator(d)).start();
        }
        new Thread(this::interruptController).start();
        userMenu();
    }

    private void userMenu() {
        Scanner scanner = new Scanner(System.in);
        while (running) {
            System.out.println("Controls: [1] Kbd [2] Mouse [3] Ptr [q] Quit");
            String input = scanner.nextLine();
            Device dev = null;

            switch (input.toLowerCase()) {
                case "1": dev = Device.KEYBOARD; break;
                case "2": dev = Device.MOUSE; break;
                case "3": dev = Device.PRINTER; break;
                case "q":
                    System.out.println("🛑 Shutting down...");
                    running = false;
                    scanner.close();
                    return;
                default:
                    System.out.println("Invalid.");
                    continue;
            }

            if (dev != null) {
                boolean isNowMasked = maskMap.get(dev).compareAndSet(false, true);
                if (!isNowMasked) {
                    maskMap.get(dev).set(false);
                }
                isNowMasked = maskMap.get(dev).get();
                System.out.printf("\n--- %s is now %s ---\n", 
                    dev.label, isNowMasked ? "MASKED" : "ENABLED");
            }
        }
    }

    public static void main(String[] args) {
        System.out.println("🚀 Event-Driven Simulation Starting...");
        InterruptSimulator sim = new InterruptSimulator();
        sim.start();
    }
}