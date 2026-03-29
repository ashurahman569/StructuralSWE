public abstract class CartDecorator implements Billable {

    protected final Billable billable;

    public CartDecorator(Billable billable) { this.billable = billable; }

    @Override
    public java.util.List<EducationalItem> getItems() { return billable.getItems(); }

    // Subclasses override getSubtotal() to subtract their own discount
    // and override displayDiscountLine() to print their line
}
