public class DurationDiscountDecorator extends CartDecorator {

    private static final double DISCOUNT = 12.0;
    private static final int THRESHOLD_MINS = 300;

    public DurationDiscountDecorator(Billable billable) { 
        super(billable); 
    }

    private boolean eligible() {
        return getItems().stream().anyMatch(i -> i.getDurationInMinutes() >= THRESHOLD_MINS);
    }

    @Override
    public void add(EducationalItem item) { }

    @Override
    public double getSubtotal() {
        return billable.getSubtotal() - (eligible() ? DISCOUNT : 0);
    }

    @Override
    public void displayDiscountLine() {
        billable.displayDiscountLine();
        if (eligible())
            System.out.printf("  %-42s -$%6.2f%n", "Special Duration Discount", DISCOUNT);
    }
}