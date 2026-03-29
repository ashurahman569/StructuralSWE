import java.util.*;

public class CLI {

    private final CommandHandler handler = new CommandHandler();
    private final Scanner scanner = new Scanner(System.in);

    public void start() {
        printBanner();
        while (true) {
            printMenu();
            String choice = scanner.nextLine().trim();
            if (!dispatch(choice)) break;
        }
        System.out.println("\n  Goodbye!\n");
        scanner.close();
    }

    private void printMenu() {
        System.out.println("""
  1.  Add module
  2.  Add course
  3.  Add lesson
  4.  List catalog
  5.  Show module
  6.  Cart - add module
  7.  Cart - add course
  8.  Cart - show
  9.  Cart - clear
  10. Set country
  11. Checkout
  12. Demo
  13. Status
  0.  Exit""");
        System.out.print("\n  > ");
    }

    private boolean dispatch(String choice) {
        System.out.println();
        switch (choice) {
            case "1"  -> handler.handleAddModule(prompt("Module name"));
            case "2"  -> handler.handleAddCourse(
                            prompt("Module name"),
                            prompt("Course name"));
                            
            case "3"  -> handler.handleAddLesson(
                            prompt("Course name"),
                            prompt("Lesson name"),
                            prompt("Duration (minutes)"),
                            prompt("Price"));
            case "4"  -> handler.handleListCatalog();
            case "5"  -> handler.handleShowModule(prompt("Module name"));
            case "6"  -> handler.handleCartAddModule(
                            prompt("Module name"),
                            confirm("Add Practice Set? (y/n)"),
                            confirm("Add Live Mentor?  (y/n)"));
            case "7"  -> handler.handleCartAddCourse(prompt("Course name"));
            case "8"  -> handler.handleCartShow();
            case "9"  -> handler.handleCartClear();
            case "10" -> handler.handleSetCountry(prompt("Country type (developing / standard)"));
            case "11" -> handler.handleCheckout();
            case "12" -> handler.handleDemo();
            case "13" -> handler.handleStatus();
            case "0"  -> { return false; }
            default   -> System.out.println("Invalid option. Enter a number from the menu.");
        }
        return true;
    }


    private String prompt(String label) {
        System.out.print("  " + label + ": ");
        return scanner.nextLine().trim();
    }

    private boolean confirm(String label) {
        System.out.print("  " + label + " ");
        return scanner.nextLine().trim().equalsIgnoreCase("y");
    }

    private void printBanner() {
        System.out.println("   Welcome to EduLearn!");
    }
}