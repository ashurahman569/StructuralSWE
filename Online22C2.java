// ZBazar is an online grocery platform that offers multiple delivery options, as given below:

// Standard Delivery (delivered within 24 hours)
// Express Delivery (delivered within 4 hours)
// Scheduled Delivery (delivered at a chosen time slot)

// Each delivery type has its own pricing logic and estimated delivery time calculation. 
// Regardless of the type, every order must ultimately be dispatched using a physical transportation method. 
// Until now, ZBazar has relied on bike couriers and van deliveries to fulfill orders. Recently, however, 
// the company development team now finds itself updating multiple classes just to support this new transportation method, 
// such as adding drone-specific dispatch logic, tracking integration, safety checks, etc., in several places. 
// The situation is becoming harder to manage because, in the near future, ZBazar is also considering introducing 
// robot delivery for gated communities and smart neighborhoods.
// The challenge is that business policies for delivery types may change frequently, while transport technologies may expand over time. 
// However, the current design tightly couples everything and thus forces code duplication and repeated modifications 
// whenever either dimension changes. Your task is to redesign the system with an appropriate design pattern so that 
// new transport technologies like drones or robots to be introduced without rewriting existing delivery-type logic, and vice versa.
// Task: Choose the appropriate design pattern to solve this problem and implement a minimal demonstration.

// ─────────────────────────────────────────────────────
// IMPLEMENTOR — Transport (the "how it physically moves")
// ─────────────────────────────────────────────────────
interface Transport {
    void dispatch(String orderId);
    String getEstimatedArrival();
    double getTransportCost();
}

// ── Concrete Implementors ──

class BikeTransport implements Transport {
    @Override
    public void dispatch(String orderId) {
        System.out.println("  [Bike] Dispatching order " + orderId + " via bike courier.");
    }
    @Override public String getEstimatedArrival() { return "30-45 minutes"; }
    @Override public double getTransportCost()    { return 30.0; }
}

class VanTransport implements Transport {
    @Override
    public void dispatch(String orderId) {
        System.out.println("  [Van] Dispatching order " + orderId + " via delivery van.");
    }
    @Override public String getEstimatedArrival() { return "1-2 hours"; }
    @Override public double getTransportCost()    { return 80.0; }
}

class DroneTransport implements Transport {
    @Override
    public void dispatch(String orderId) {
        System.out.println("  [Drone] Running safety checks...");
        System.out.println("  [Drone] Dispatching order " + orderId + " via drone.");
        System.out.println("  [Drone] Tracking enabled.");
    }
    @Override public String getEstimatedArrival() { return "15-20 minutes"; }
    @Override public double getTransportCost()    { return 150.0; }
}

class RobotTransport implements Transport {
    @Override
    public void dispatch(String orderId) {
        System.out.println("  [Robot] Authenticating gated community access...");
        System.out.println("  [Robot] Dispatching order " + orderId + " via delivery robot.");
    }
    @Override public String getEstimatedArrival() { return "20-30 minutes"; }
    @Override public double getTransportCost()    { return 120.0; }
}


// ─────────────────────────────────────────────────────
// ABSTRACTION — Delivery Type (the "business policy")
// ─────────────────────────────────────────────────────
abstract class DeliveryService {
    protected Transport transport;   // ← the Bridge

    public DeliveryService(Transport transport) {
        this.transport = transport;
    }

    // Change transport at runtime if needed
    public void setTransport(Transport transport) {
        this.transport = transport;
    }

    public abstract void placeOrder(String orderId);
    public abstract double calculateTotalCost(double basePrice);
}

// ── Refined Abstractions ──

class StandardDelivery extends DeliveryService {
    public StandardDelivery(Transport transport) { super(transport); }

    @Override
    public void placeOrder(String orderId) {
        System.out.println("Standard Delivery (within 24 hours)");
        transport.dispatch(orderId);
        System.out.println("  ETA: " + transport.getEstimatedArrival());
    }

    @Override
    public double calculateTotalCost(double basePrice) {
        // Standard: no delivery surcharge, just transport cost
        return basePrice + transport.getTransportCost();
    }
}

class ExpressDelivery extends DeliveryService {
    public ExpressDelivery(Transport transport) { super(transport); }

    @Override
    public void placeOrder(String orderId) {
        System.out.println("Express Delivery (within 4 hours)");
        transport.dispatch(orderId);
        System.out.println("  ETA: " + transport.getEstimatedArrival());
    }

    @Override
    public double calculateTotalCost(double basePrice) {
        // Express: 20% surcharge on base price + transport cost
        return (basePrice * 1.20) + transport.getTransportCost();
    }
}

class ScheduledDelivery extends DeliveryService {
    private String timeSlot;

    public ScheduledDelivery(Transport transport, String timeSlot) {
        super(transport);
        this.timeSlot = timeSlot;
    }

    @Override
    public void placeOrder(String orderId) {
        System.out.println("Scheduled Delivery (time slot: " + timeSlot + ")");
        transport.dispatch(orderId);
        System.out.println("  Scheduled ETA: " + timeSlot);
    }

    @Override
    public double calculateTotalCost(double basePrice) {
        // Scheduled: flat 15 BDT scheduling fee + transport cost
        return basePrice + transport.getTransportCost() + 15.0;
    }
}


// ─────────────────────────────────────────────────────
// Main — Demonstration
// ─────────────────────────────────────────────────────
public class Online22C2 {
    public static void main(String[] args) {
        double basePrice = 500.0;

        System.out.println("========== ZBazar Delivery System ==========\n");

        // Standard + Bike
        DeliveryService order1 = new StandardDelivery(new BikeTransport());
        order1.placeOrder("ORD-001");
        System.out.printf("  Total Cost: %.2f BDT%n%n", order1.calculateTotalCost(basePrice));

        // Express + Drone  (new transport, zero changes to ExpressDelivery)
        DeliveryService order2 = new ExpressDelivery(new DroneTransport());
        order2.placeOrder("ORD-002");
        System.out.printf("  Total Cost: %.2f BDT%n%n", order2.calculateTotalCost(basePrice));

        // Scheduled + Van
        DeliveryService order3 = new ScheduledDelivery(new VanTransport(), "6:00 PM - 8:00 PM");
        order3.placeOrder("ORD-003");
        System.out.printf("  Total Cost: %.2f BDT%n%n", order3.calculateTotalCost(basePrice));

        // Standard + Robot  (newest transport, still zero changes to StandardDelivery)
        DeliveryService order4 = new StandardDelivery(new RobotTransport());
        order4.placeOrder("ORD-004");
        System.out.printf("  Total Cost: %.2f BDT%n%n", order4.calculateTotalCost(basePrice));

        // Runtime bridge swap — switch order1 from Bike to Drone on the fly
        System.out.println("--- Switching ORD-001 transport to Drone at runtime ---");
        order1.setTransport(new DroneTransport());
        order1.placeOrder("ORD-001");
        System.out.printf("  Total Cost: %.2f BDT%n", order1.calculateTotalCost(basePrice));
    }
}