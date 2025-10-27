# Java Interrupt Controller Simulation (Event-Driven)

## Description

This is a multithreaded Java program built to simulate a computer's Interrupt Controller. It manages asynchronous interrupt requests from three different I/O devices (Keyboard, Mouse, and Printer).

This simulation uses an **event-driven, queue-based** model. Instead of polling, it uses a `PriorityBlockingQueue` to store incoming interrupts. This queue automatically sorts all requests by their priority, ensuring the most critical interrupt is always processed first. The controller thread remains in an efficient sleep state, waking only when an interrupt is available in the queue.

## Features

* **Event-Driven Model**: Uses a `PriorityBlockingQueue` to efficiently manage interrupts. The controller thread consumes no CPU while idle, as it waits for items to appear in the queue.
* **Automatic Prioritization**: A custom `InterruptComparator` ensures that interrupts are sorted by their priority value (Keyboard: 1, Mouse: 2, Printer: 3).
* **Asynchronous Devices**: Each device (Keyboard, Mouse, Printer) runs on its own thread, randomly generating and queuing interrupt requests.
* **Runtime Masking**: An interactive console menu allows the user to mask (disable) or unmask (enable) any device in real-time.

## How to Compile and Run

### 1. Compile

```bash
javac InterruptSimulator.java
````

### 2\. Run

```bash
java InterruptSimulator
```

## How to Use (Controls)

Once running, the program is fully interactive:

  * `1`: Toggle (mask/unmask) the **Keyboard**
  * `2`: Toggle (mask/unmask) the **Mouse**
  * `3`: Toggle (mask/unmask) the **Printer**
  * `q`: **Quit** the simulation

### Sample Output

Here is an example of the program running, showing priority and masking.

```
🚀 Event-Driven Simulation Starting...
Controls: [1] Kbd [2] Mouse [3] Ptr [q] Quit

>>> [PRINTER] requests interrupt! <<<

>>> [KEYBOARD] requests interrupt! <<<

✅ Keyboard Interrupt Triggered -> Handling ISR...
🏁 Keyboard ISR Completed.
✅ Printer Interrupt Triggered -> Handling ISR...
🏁 Printer ISR Completed.

1
--- Keyboard is now MASKED ---

>>> [KEYBOARD] requests interrupt! <<<

🚫 Keyboard Interrupt Ignored (Masked)

q
🛑 Shutting down...
Interrupt Controller shutting down.
```

```
```
