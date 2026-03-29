public class DevelopingCountryDiscountDecorator extends CartDecorator {

    private static final double DISCOUNT = 10.0;
    
    @Override
    public void add(EducationalItem item) { }

    public DevelopingCountryDiscountDecorator(Billable billable) { 
        super(billable); 
    }

    @Override
    public double getSubtotal() { 
        return billable.getSubtotal() - DISCOUNT; 
    }

    @Override
    public void displayDiscountLine() {
        billable.displayDiscountLine();
        System.out.printf("  %-42s -$%6.2f%n", "Developing Country Discount", DISCOUNT);
    }
}
