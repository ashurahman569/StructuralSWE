// Concrete Decorator for Add-Ons (adds Live Mentor Support)
public class LiveMentorDecorator extends ItemDecorator {

    private static final double PRICE = 20.0;
    private static final String LABEL = "Live Mentor Support";

    public LiveMentorDecorator(EducationalItem item) { 
        super(item); 
    }

    @Override
    public EducationalItem addItem(EducationalItem item) { 
        throw new UnsupportedOperationException("Cannot add item to an add-on.");
    }

    @Override
    public double calculatePrice() { 
        return item.calculatePrice() + PRICE; 
    }

    @Override
    public void display(String indent) {
        item.display(indent);
        System.out.printf("%s  [Add-on]  %-35s              $%.2f%n",
                indent, LABEL, PRICE);
    }
}
