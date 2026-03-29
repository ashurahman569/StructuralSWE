import java.util.List;

public interface Billable {
    List<EducationalItem> getItems();
    void add(EducationalItem item);  // for Cart only otherwise can't add items
    double getSubtotal();           // after this decorator's discount
    void displayDiscountLine();   // prints own discount line
}