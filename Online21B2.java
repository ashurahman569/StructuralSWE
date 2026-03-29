// A computer system is composed of several basic hardware components, such as a CPU,
// memory, storage, and a graphics card. Each component has a base price. However,
// customers can choose to add optional features to these components, such as an
// extended warranty, installation service, or performance boost, each of which increases
// the price of the component. Use an appropriate design pattern to add features to
// components without altering the core classes. The core classes are given below.

// Component interface representing the basic hardware component
interface Component {

    double getPrice();

    String getDescription();
}
// Concrete Component representing individual hardware components

class HardwareComponent implements Component {

    private String name;
    private double price;

    public HardwareComponent(String name, double price) {
        this.name = name;
        this.price = price;
    }

    @Override
    public double getPrice() {
        return price;
    }

    @Override
    public String getDescription() {
        return name;
    }
}

// ─────────────────────────────────────────────────
// Abstract Decorator — wraps any Component
// ─────────────────────────────────────────────────
abstract class FeatureDecorator implements Component {
    protected Component wrappedComponent;   // ← the Bridge / core object

    public FeatureDecorator(Component component) {
        this.wrappedComponent = component;
    }

    @Override
    public double getPrice() {
        return wrappedComponent.getPrice();  // delegate to inner
    }

    @Override
    public String getDescription() {
        return wrappedComponent.getDescription(); // delegate to inner
    }
}

// ─────────────────────────────────────────────────
// Concrete Decorator 1 — Extended Warranty
// ─────────────────────────────────────────────────
class ExtendedWarranty extends FeatureDecorator {
    private static final double WARRANTY_COST = 50.00;

    public ExtendedWarranty(Component component) {
        super(component);
    }

    @Override
    public double getPrice() {
        return wrappedComponent.getPrice() + WARRANTY_COST;
    }

    @Override
    public String getDescription() {
        return wrappedComponent.getDescription() + " + Extended Warranty";
    }
}

// ─────────────────────────────────────────────────
// Concrete Decorator 2 — Installation Service
// ─────────────────────────────────────────────────
class InstallationService extends FeatureDecorator {
    private static final double INSTALL_COST = 30.00;

    public InstallationService(Component component) {
        super(component);
    }

    @Override
    public double getPrice() {
        return wrappedComponent.getPrice() + INSTALL_COST;
    }

    @Override
    public String getDescription() {
        return wrappedComponent.getDescription() + " + Installation Service";
    }
}

// ─────────────────────────────────────────────────
// Concrete Decorator 3 — Performance Boost
// ─────────────────────────────────────────────────
class PerformanceBoost extends FeatureDecorator {
    private static final double BOOST_COST = 100.00;

    public PerformanceBoost(Component component) {
        super(component);
    }

    @Override
    public double getPrice() {
        return wrappedComponent.getPrice() + BOOST_COST;
    }

    @Override
    public String getDescription() {
        return wrappedComponent.getDescription() + " + Performance Boost";
    }
}

// ─────────────────────────────────────────────────
// Helper — pretty print any Component
// ─────────────────────────────────────────────────
class Receipt {
    static void print(Component c) {
        System.out.println("  Description : " + c.getDescription());
        System.out.printf ("  Total Price : $%.2f%n", c.getPrice());
        System.out.println("  ─────────────────────────────────────────");
    }
}

public class Online21B2 {
    public static void main(String[] args) {

        System.out.println("==========================================");
        System.out.println("    Computer Shop — Decorator Pattern     ");
        System.out.println("==========================================\n");

        // ── Base Components (Leaves — no features) ──
        Component cpu     = new HardwareComponent("Intel Core i9",   700.00);
        Component ram     = new HardwareComponent("RAM 32GB DDR5",   180.00);
        Component storage = new HardwareComponent("SSD 2TB NVMe",    200.00);
        Component gpu     = new HardwareComponent("GPU RTX 4090",   1600.00);

        // ── 1. Plain purchase — no extras ──
        System.out.println("1. Plain CPU (no extras):");
        Receipt.print(cpu);

        // ── 2. CPU + Extended Warranty ──
        System.out.println("2. CPU + Extended Warranty:");
        Component cpuWithWarranty = new ExtendedWarranty(cpu);
        Receipt.print(cpuWithWarranty);

        // ── 3. RAM + Installation Service ──
        System.out.println("3. RAM + Installation Service:");
        Component ramWithInstall = new InstallationService(ram);
        Receipt.print(ramWithInstall);

        // ── 4. GPU + Performance Boost + Extended Warranty ──
        System.out.println("4. GPU + Performance Boost + Extended Warranty:");
        Component gpuBoostedWarranty = new ExtendedWarranty(
                                           new PerformanceBoost(gpu));
        Receipt.print(gpuBoostedWarranty);

        // ── 5. Storage + ALL three features stacked ──
        System.out.println("5. SSD + All Three Features (Warranty + Install + Boost):");
        Component fullPackage = new PerformanceBoost(
                                    new InstallationService(
                                        new ExtendedWarranty(storage)));
        Receipt.print(fullPackage);

        // ── 6. Full custom PC build — each part decorated differently ──
        System.out.println("6. Full Custom PC Build Summary:");
        Component[] build = {
            new ExtendedWarranty(new PerformanceBoost(cpu)),     // CPU: Boost + Warranty
            new InstallationService(ram),                         // RAM: Install only
            new ExtendedWarranty(storage),                        // SSD: Warranty only
            new PerformanceBoost(
                new InstallationService(
                    new ExtendedWarranty(gpu)))                   // GPU: all three
        };

        double buildTotal = 0;
        for (Component part : build) {
            System.out.println("  → " + part.getDescription());
            System.out.printf ("     $%.2f%n", part.getPrice());
            buildTotal += part.getPrice();
        }
        System.out.println("  ─────────────────────────────────────────");
        System.out.printf ("  BUILD TOTAL  :  $%.2f%n", buildTotal);
    }
}
