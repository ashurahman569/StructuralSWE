// ZBazar is a subscription-based grocery delivery service that lets customers set up a recurring monthly bazar bundle 
// that is automatically delivered to their homes. The platform offers preset packages (such as Small, Family, and Mega) 
// and single grocery items (individual items like rice, oil, pulse, etc.). Preset packages consist of multiple single items. 
// Each item has its specific name, price, and weight. Users can create or modify their own Custom Bazar by combining 
// one or more preset packages and/or single items. A custom package may include only individual items, only preset packages, 
// or a mixture of both. Users can also include their previously created custom packages. The system must support calculating 
// the total price and weight of any configuration and displaying the complete structure of the custom package.
// Task: Choose the appropriate design pattern to solve this problem and implement a minimal demonstration.

import java.util.ArrayList;
import java.util.List;

// ─────────────────────────────────────────
// Component Interface
// ─────────────────────────────────────────
interface BazarComponent {
    String getName();
    double getPrice();
    double getWeight();
    void display(String indent);
}

// ─────────────────────────────────────────
// Leaf — Single Grocery Item
// ─────────────────────────────────────────
class GroceryItem implements BazarComponent {
    private String name;
    private double price;
    private double weight;

    public GroceryItem(String name, double price, double weight) {
        this.name   = name;
        this.price  = price;
        this.weight = weight;
    }

    @Override public String getName()        { return name; }
    @Override public double getPrice()       { return price; }
    @Override public double getWeight()      { return weight; }

    @Override
    public void display(String indent) {
        System.out.printf("%s[Item] %s | Price: %.2f BDT | Weight: %.2f kg%n",
                indent, name, price, weight);
    }
}

// ─────────────────────────────────────────
// Composite — Any kind of Package
// (Preset Small/Family/Mega OR Custom Bazar)
// ─────────────────────────────────────────
class BazarPackage implements BazarComponent {
    private String name;
    private List<BazarComponent> children = new ArrayList<>();

    public BazarPackage(String name) {
        this.name = name;
    }

    public void add(BazarComponent component) {
        children.add(component);
    }

    public void remove(BazarComponent component) {
        children.remove(component);
    }

    @Override public String getName() { return name; }

    @Override
    public double getPrice() {
        return children.stream()
                       .mapToDouble(BazarComponent::getPrice)
                       .sum();
    }

    @Override
    public double getWeight() {
        return children.stream()
                       .mapToDouble(BazarComponent::getWeight)
                       .sum();
    }

    @Override
    public void display(String indent) {
        System.out.printf("%s[Package] %s | Total Price: %.2f BDT | Total Weight: %.2f kg%n",
                indent, name, getPrice(), getWeight());
        for (BazarComponent child : children) {
            child.display(indent + "    ");
        }
    }
}

// ─────────────────────────────────────────
// Main — Demonstration
// ─────────────────────────────────────────
public class Online22A2 {
    public static void main(String[] args) {

        // --- Single grocery items ---
        GroceryItem rice   = new GroceryItem("Rice",   80.0,  5.0);
        GroceryItem oil    = new GroceryItem("Oil",    150.0, 1.0);
        GroceryItem pulse  = new GroceryItem("Pulse",  60.0,  2.0);
        GroceryItem sugar  = new GroceryItem("Sugar",  50.0,  1.0);
        GroceryItem flour  = new GroceryItem("Flour",  45.0,  2.0);
        GroceryItem salt   = new GroceryItem("Salt",   20.0,  0.5);

        // --- Preset: Small Package ---
        BazarPackage smallPackage = new BazarPackage("Small Package");
        smallPackage.add(rice);
        smallPackage.add(oil);

        // --- Preset: Family Package ---
        BazarPackage familyPackage = new BazarPackage("Family Package");
        familyPackage.add(rice);
        familyPackage.add(oil);
        familyPackage.add(pulse);
        familyPackage.add(sugar);

        // --- Preset: Mega Package ---
        BazarPackage megaPackage = new BazarPackage("Mega Package");
        megaPackage.add(rice);
        megaPackage.add(oil);
        megaPackage.add(pulse);
        megaPackage.add(sugar);
        megaPackage.add(flour);
        megaPackage.add(salt);

        // --- Custom Bazar: mix of preset package + single item ---
        BazarPackage customBazar = new BazarPackage("My Custom Bazar");
        customBazar.add(smallPackage);   // adding a preset package
        customBazar.add(pulse);          // adding a single item
        customBazar.add(salt);           // adding another single item

        // --- Custom Bazar 2: includes another custom bazar (nested) ---
        BazarPackage superCustom = new BazarPackage("Super Custom Bazar");
        superCustom.add(customBazar);    // previously created custom package
        superCustom.add(familyPackage);  // preset package
        superCustom.add(flour);          // single item

        // ── Display ──
        System.out.println("======= ZBazar Structure =======\n");

        smallPackage.display("");
        System.out.println();

        familyPackage.display("");
        System.out.println();

        megaPackage.display("");
        System.out.println();

        customBazar.display("");
        System.out.println();

        superCustom.display("");
        System.out.println();

        // ── Summary ──
        System.out.println("======= Summary =======");
        System.out.printf("Super Custom Bazar → Total Price : %.2f BDT%n", superCustom.getPrice());
        System.out.printf("Super Custom Bazar → Total Weight: %.2f kg%n",  superCustom.getWeight());
    }
}