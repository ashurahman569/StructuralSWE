// Abstract Decorator for Add-Ons
public abstract class ItemDecorator implements EducationalItem {

    protected final EducationalItem item;

    protected ItemDecorator(EducationalItem item) {
        this.item = item;
    }

    @Override 
    public String getName() { 
        return item.getName(); 
    }

    @Override 
    public int getDurationInMinutes() { 
        return item.getDurationInMinutes(); 
    }

    @Override 
    public double calculatePrice() { 
        return item.calculatePrice(); 
    }

    @Override 
    public void display(String indent) { 
        item.display(indent); 
    }

    @Override
    public boolean isModule() {
        return item.isModule();
    }
}
