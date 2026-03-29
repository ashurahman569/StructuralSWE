import java.util.ArrayList;
import java.util.List;

public class Cart implements Billable {

    private final List<EducationalItem> items = new ArrayList<>();
    
    @Override
    public void add(EducationalItem item) { 
        items.add(item); 
    }
    public void clear() { 
        items.clear();   
    }
    public boolean isEmpty() { 
        return items.isEmpty(); 
    }
    public int size() { 
        return items.size();   
     }

    @Override
    public List<EducationalItem> getItems() { 
        return items; 
    }

    @Override
    public double getSubtotal() {
        return items.stream().mapToDouble(EducationalItem::calculatePrice).sum();
    }

    @Override
    public void displayDiscountLine() { /* base — no discount line */ }
}
