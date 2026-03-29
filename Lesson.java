//Composite Pattern - leaf level
public class Lesson implements EducationalItem {

    private final String name;
    private final int duration;
    private final double price;

    public Lesson(String name, int duration, double price) {
        this.name = name;
        this.duration = duration;
        this.price = price;
    }
    @Override
    public EducationalItem addItem(EducationalItem item) {
        throw new UnsupportedOperationException("Cannot add item to a lesson.");
    }

    @Override public String getName() { 
        return name; 
    }
    @Override public int getDurationInMinutes() { 
        return duration; 
    }
    @Override public double calculatePrice() { 
        return price;
    }   

    @Override
    public void display(String indent) {
        System.out.printf("%s    [Lesson]  %-33s  %3d min   $%.2f%n",
                indent, name, duration, calculatePrice());
    }
}
