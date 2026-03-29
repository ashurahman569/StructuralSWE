
public interface EducationalItem {

    String getName();

    public EducationalItem addItem(EducationalItem item);

    int getDurationInMinutes();

    double calculatePrice();

    void display(String indent);

    default boolean isModule() {
        return false;
    }
    
}
