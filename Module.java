import java.util.ArrayList;
import java.util.List;

// Composite Pattern — top level
public class Module implements EducationalItem {

    private final String name;
    private final List<EducationalItem> items = new ArrayList<>();

    public Module(String name) { 
        this.name = name; 
    }
    @Override
    public EducationalItem addItem(EducationalItem item) {
        items.add(item);
        return this;
    }

    @Override public String getName() { 
        return name; 
    }

    @Override
    public int getDurationInMinutes() {
        int total = 0;
        for (EducationalItem item : items) {
            total += item.getDurationInMinutes();
        }
        return total;
    }

    @Override
    public double calculatePrice() {
        double totalPrice = 0;
        for (EducationalItem item : items) {
            totalPrice += item.calculatePrice();
        }
        return totalPrice;
    }

    @Override
    public void display(String indent) {
        System.out.printf("%s[Module]  %-37s  %.1f hrs  $%.2f%n",
                indent, name, getDurationInMinutes() / 60.0, calculatePrice());
        items.forEach(c -> c.display(indent + "  "));
    }

    @Override
    public boolean isModule() {
        return true;
    }
}