public class MultiModuleDiscountDecorator extends CartDecorator {

    private static final double DISCOUNT = 15.0;

    public MultiModuleDiscountDecorator(Billable billable) { super(billable); }

    private boolean eligible() {
        return getItems().stream().filter(EducationalItem::isModule).count() >= 2;
    }

    @Override
    public void add(EducationalItem item) { }

    @Override
    public double getSubtotal() {
        return billable.getSubtotal() - (eligible() ? DISCOUNT : 0);
    }

    @Override
    public void displayDiscountLine() {
        billable.displayDiscountLine();          // chain inward first
        if (eligible())
            System.out.printf("  %-42s -$%6.2f%n", "Multi-Module Discount", DISCOUNT);
    }
}
