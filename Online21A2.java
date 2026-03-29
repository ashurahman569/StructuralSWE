// A computer system is composed of several hardware components, such as the CPU,
// memory, storage, and a graphics card. Each of these components has a price. Some
// customers may buy individual components to build their own custom computer, while
// others may prefer pre-configured bundles (e.g., a gaming setup or a workstation setup). A
// bundle can contain several individual components or even another bundle (e.g., an
// "Ultimate Gaming Setup" may include a smaller "Basic Gaming Setup" along with some
// extra hardware). Each bundle or individual component must be able to calculate its total
// price. Customers may also want to add or remove components from a bundle.
// Now, implement this system using an appropriate design pattern, where individual
// components and bundles are treated uniformly. Your driver code should show the system's
// functionalities.

import java.util.ArrayList;
import java.util.List;

// ─────────────────────────────────────────────────
// Component Interface — uniform treatment
// ─────────────────────────────────────────────────
interface HardwareComponent {
    String getName();
    double getPrice();
    void display(String indent);
}

// ─────────────────────────────────────────────────
// Leaf — Individual Hardware Component
// ─────────────────────────────────────────────────
class HardwarePart implements HardwareComponent {
    private String name;
    private double price;

    public HardwarePart(String name, double price) {
        this.name  = name;
        this.price = price;
    }

    @Override public String getName()  { return name; }
    @Override public double getPrice() { return price; }

    @Override
    public void display(String indent) {
        System.out.printf("%s[Component] %-25s $%.2f%n", indent, name, price);
    }
}

// ─────────────────────────────────────────────────
// Composite — Bundle (can hold parts OR other bundles)
// ─────────────────────────────────────────────────
class Bundle implements HardwareComponent {
    private String name;
    private List<HardwareComponent> children = new ArrayList<>();

    public Bundle(String name) {
        this.name = name;
    }

    // Add a component or sub-bundle
    public void add(HardwareComponent component) {
        children.add(component);
        System.out.println("  ✔ Added \"" + component.getName() + "\" to [" + name + "]");
    }

    // Remove a component or sub-bundle
    public void remove(HardwareComponent component) {
        if (children.remove(component)) {
            System.out.println("  ✘ Removed \"" + component.getName() + "\" from [" + name + "]");
        } else {
            System.out.println("  ! \"" + component.getName() + "\" not found in [" + name + "]");
        }
    }

    @Override public String getName() { return name; }

    @Override
    public double getPrice() {
        return children.stream()
                       .mapToDouble(HardwareComponent::getPrice)
                       .sum();
    }

    @Override
    public void display(String indent) {
        System.out.printf("%s[Bundle]    %-25s $%.2f%n", indent, name, getPrice());
        for (HardwareComponent child : children) {
            child.display(indent + "    ");
        }
    }
}

// ─────────────────────────────────────────────────
// Driver / Main
// ─────────────────────────────────────────────────
public class Online21A2 {
    public static void main(String[] args) {

        // ── Individual Components (Leaves) ──
        HardwarePart cpu        = new HardwarePart("Intel Core i5",       350.00);
        HardwarePart cpuHigh    = new HardwarePart("Intel Core i9",       700.00);
        HardwarePart ram8       = new HardwarePart("RAM 8GB DDR4",         60.00);
        HardwarePart ram32      = new HardwarePart("RAM 32GB DDR5",       180.00);
        HardwarePart ssd512     = new HardwarePart("SSD 512GB",            80.00);
        HardwarePart ssd2tb     = new HardwarePart("SSD 2TB NVMe",        200.00);
        HardwarePart gpuBasic   = new HardwarePart("GPU RTX 3060",        400.00);
        HardwarePart gpuUltra   = new HardwarePart("GPU RTX 4090",       1600.00);
        HardwarePart psu        = new HardwarePart("PSU 750W",             90.00);
        HardwarePart mobo       = new HardwarePart("Motherboard ATX",     150.00);
        HardwarePart cooling    = new HardwarePart("Liquid Cooling Kit",  120.00);
        HardwarePart monitor    = new HardwarePart("4K Gaming Monitor",   500.00);

        System.out.println("============================================");
        System.out.println("     COMPUTER SYSTEM — Composite Pattern    ");
        System.out.println("============================================");

        // ────────────────────────────────────────
        // 1. Individual purchase
        // ────────────────────────────────────────
        System.out.println("\n--- Single Component Purchase ---");
        cpu.display("");
        System.out.printf("    Total: $%.2f%n", cpu.getPrice());

        // ────────────────────────────────────────
        // 2. Basic Gaming Setup (Preset Bundle)
        // ────────────────────────────────────────
        System.out.println("\n--- Building: Basic Gaming Setup ---");
        Bundle basicGaming = new Bundle("Basic Gaming Setup");
        basicGaming.add(cpu);
        basicGaming.add(ram8);
        basicGaming.add(ssd512);
        basicGaming.add(gpuBasic);
        basicGaming.add(psu);
        basicGaming.add(mobo);

        System.out.println();
        basicGaming.display("");
        System.out.printf("    ► Total Price: $%.2f%n", basicGaming.getPrice());

        // ────────────────────────────────────────
        // 3. Workstation Setup (Preset Bundle)
        // ────────────────────────────────────────
        System.out.println("\n--- Building: Workstation Setup ---");
        Bundle workstation = new Bundle("Workstation Setup");
        workstation.add(cpuHigh);
        workstation.add(ram32);
        workstation.add(ssd2tb);
        workstation.add(psu);
        workstation.add(mobo);

        System.out.println();
        workstation.display("");
        System.out.printf("    ► Total Price: $%.2f%n", workstation.getPrice());

        // ────────────────────────────────────────
        // 4. Ultimate Gaming Setup
        //    (Bundle containing another Bundle!)
        // ────────────────────────────────────────
        System.out.println("\n--- Building: Ultimate Gaming Setup ---");
        Bundle ultimateGaming = new Bundle("Ultimate Gaming Setup");
        ultimateGaming.add(basicGaming);   // ← nested bundle
        ultimateGaming.add(gpuUltra);      // extra GPU upgrade
        ultimateGaming.add(ram32);         // extra RAM
        ultimateGaming.add(cooling);       // liquid cooling
        ultimateGaming.add(monitor);       // 4K monitor

        System.out.println();
        ultimateGaming.display("");
        System.out.printf("    ► Total Price: $%.2f%n", ultimateGaming.getPrice());

        // ────────────────────────────────────────
        // 5. Custom Bundle — add & remove demo
        // ────────────────────────────────────────
        System.out.println("\n--- Customer Custom Build ---");
        Bundle customBuild = new Bundle("My Custom PC");
        customBuild.add(cpuHigh);
        customBuild.add(ram32);
        customBuild.add(ssd512);
        customBuild.add(gpuBasic);

        System.out.println();
        System.out.println("  [Before removal]");
        customBuild.display("  ");
        System.out.printf("  ► Total: $%.2f%n", customBuild.getPrice());

        // Customer decides to remove basic GPU and upgrade
        System.out.println();
        customBuild.remove(gpuBasic);
        customBuild.add(gpuUltra);

        System.out.println();
        System.out.println("  [After GPU upgrade]");
        customBuild.display("  ");
        System.out.printf("  ► Total: $%.2f%n", customBuild.getPrice());

        // ────────────────────────────────────────
        // 6. Price Summary
        // ────────────────────────────────────────
        System.out.println("\n============================================");
        System.out.println("            PRICE SUMMARY                   ");
        System.out.println("============================================");
        HardwareComponent[] catalog = {
            cpu, basicGaming, workstation, ultimateGaming, customBuild
        };
        for (HardwareComponent item : catalog) {
            System.out.printf("  %-30s → $%.2f%n", item.getName(), item.getPrice());
        }
    }
}
