import java.util.List;

public class CheckoutService {

    public void checkout(Billable billable) {
        List<EducationalItem> items = billable.getItems();
        if (items.isEmpty()) { System.out.println("Cart is empty."); return; }

        System.out.println();
        System.out.println("=".repeat(55));
        System.out.println("          EduLearn - ORDER RECEIPT");
        System.out.println("=".repeat(55));

        double subtotal = 0;
        for (EducationalItem item : items) {
            item.display("");
            subtotal += item.calculatePrice();
        }

        System.out.println("-".repeat(55));
        System.out.printf("  %-42s  $%6.2f%n", "Subtotal", subtotal);
        billable.displayDiscountLine();   // each decorator adds its own line

        double grandTotal = Math.max(0, billable.getSubtotal());
        System.out.println("=".repeat(55));
        System.out.printf("  %-42s  $%6.2f%n", "GRAND TOTAL", grandTotal);
        System.out.println("=".repeat(55));
        System.out.println();
    }
}