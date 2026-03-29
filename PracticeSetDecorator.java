// Concrete Decorator for Add-Ons (adds Practice set)
public class PracticeSetDecorator extends ItemDecorator {

    private static final double PRICE = 10.0;
    private static final String LABEL = "Practice Question Set";

    public PracticeSetDecorator(EducationalItem item) { 
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