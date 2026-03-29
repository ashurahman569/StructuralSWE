import java.util.*;

public class CommandHandler {

    private final Map<String, EducationalItem> modules = new LinkedHashMap<>();
    private final Map<String, EducationalItem> courses = new LinkedHashMap<>();
    private final List<EducationalItem> cart = new ArrayList<>();
    private boolean devCountry = false;

    public void handleAddModule(String name) {
        if (name.isEmpty()) { System.out.println("Name cannot be empty."); return; }
        if (modules.containsKey(key(name))) { System.out.println("Module \"" + name + "\" already exists."); return; }
        modules.put(key(name), new Module(name));
        System.out.println("Module \"" + name + "\" created.");
    }

    public void handleAddCourse(String moduleName, String courseName) {
        EducationalItem module = findModule(moduleName);
        if (module == null) return;
        EducationalItem course = new Course(courseName);
        module.addItem(course);
        courses.put(key(courseName), course);
        System.out.printf("Course \"%s\" added to module \"%s\".%n", courseName, moduleName);
    }

    public void handleAddLesson(String courseName, String lessonName, String durationStr, String priceStr) {
        EducationalItem course = findCourse(courseName);
        if (course == null) return;
        int duration = parseDuration(durationStr);
        if (duration < 0) return;
        double price = parsePrice(priceStr);
        if (price < 0) return;
        EducationalItem lesson = new Lesson(lessonName, duration, price);
        course.addItem(lesson);
        System.out.printf("Lesson \"%s\" (%d min, $%.2f) added to course \"%s\".%n", lessonName, duration, price, courseName);
    }

    public void handleListCatalog() {
        if (modules.isEmpty()) { System.out.println("Catalog is empty. Run demo (12) to load sample data."); return; }
        header("CATALOG");
        modules.values().forEach(m -> { m.display(""); System.out.println(); });
    }

    public void handleShowModule(String name) {
        EducationalItem m = findModule(name);
        if (m == null) return;
        System.out.println();
        header("MODULE - " + m.getName());
        m.display("");
        System.out.printf("%n  Duration : %.1f hrs (%d min)%n", m.getDurationInMinutes() / 60.0, m.getDurationInMinutes());
        System.out.printf("  Price    : $%.2f%n%n", m.calculatePrice());
    }

    public void handleCartAddModule(String name, boolean practice, boolean mentor) {
        EducationalItem module = findModule(name);
        if (module == null) return;
        EducationalItem item = module;
        if (practice) item = new PracticeSetDecorator(item);
        if (mentor)   item = new LiveMentorDecorator(item);
        cart.add(item);
        System.out.println("Module \"" + name + "\" added to cart" + addOnSuffix(practice, mentor) + ".");
    }

    public void handleCartAddCourse(String name) {
        EducationalItem course = findCourse(name);
        if (course == null) return;
        cart.add(course);
        System.out.println("Course \"" + name + "\" added to cart.");
    }

    public void handleCartShow() {
        if (cart.isEmpty()) { System.out.println("Cart is empty."); return; }
        System.out.println();
        header("CART");
        int i = 1;
        for (EducationalItem item : cart)
            System.out.printf("  %2d.  %-42s  $%6.2f  |  %d min%n",
                    i++, item.getName(), item.calculatePrice(), item.getDurationInMinutes());
        System.out.printf("%n  %d item(s) in cart.%n%n", cart.size());
    }

    public void handleCartClear() {
        cart.clear();
        System.out.println("Cart cleared.");
    }

    public void handleSetCountry(String type) {
        switch (type.toLowerCase()) {
            case "dev", "developing" -> { devCountry = true;  System.out.println("Developing Country profile active (-$10 discount)."); }
            case "standard", "std" -> { devCountry = false; System.out.println("Standard profile active."); }
            default -> System.out.println("Enter: dev  or  standard");
        }
    }

    public void handleCheckout() {
    if (cart.isEmpty()) { System.out.println("Cart is empty."); return; }

    // Start with the raw cart
    Billable billable = new Cart();
    for (EducationalItem item : cart) {
        billable.add(item);
    }

    // Wrap with applicable discounts (order = innermost → outermost)
    long moduleCount = billable.getItems().stream()
                           .filter(EducationalItem::isModule).count();
    if (moduleCount >= 2)
        billable = new MultiModuleDiscountDecorator(billable);

    boolean longEnough = billable.getItems().stream()
                             .anyMatch(i -> i.getDurationInMinutes() >= 300);
    if (longEnough)
        billable = new DurationDiscountDecorator(billable);

    if (devCountry)
        billable = new DevelopingCountryDiscountDecorator(billable);

    // Pass the fully-decorated cart straight to checkout
    new CheckoutService().checkout(billable);
    cart.clear();
    System.out.println("Cart cleared after checkout.");
}

    public void handleDemo() {
        modules.clear(); courses.clear(); cart.clear();

        EducationalItem html = new Course("HTML & CSS Fundamentals")
                .addItem(new Lesson("Intro to HTML",  45, 9.99))
                .addItem(new Lesson("CSS Box Model",  60, 12.99))
                .addItem(new Lesson("Flexbox & Grid", 75, 14.99));

        EducationalItem js = new Course("JavaScript Essentials")
                .addItem(new Lesson("Variables & Types", 40, 15.99))
                .addItem(new Lesson("Functions & Scope",  55, 17.99))
                .addItem(new Lesson("DOM Manipulation",   70, 19.99))
                .addItem(new Lesson("Async / Promises",   60, 22.99));

        EducationalItem react = new Course("React Fundamentals")
                .addItem(new Lesson("Components & Props", 50, 18.99))
                .addItem(new Lesson("State & Hooks",      65, 20.99))
                .addItem(new Lesson("Routing & Context",  55, 24.99));

        EducationalItem webDev = new Module("Web Development Bootcamp")
                .addItem(html).addItem(js).addItem(react);

        EducationalItem python = new Course("Python for Data Science")
                .addItem(new Lesson("NumPy Arrays",       50, 16.99))
                .addItem(new Lesson("Pandas DataFrames",  65, 18.99))
                .addItem(new Lesson("Data Visualisation", 55, 20.99));

        EducationalItem ml = new Course("Machine Learning Basics")
                .addItem(new Lesson("Supervised Learning",  70, 25.99))
                .addItem(new Lesson("Model Evaluation",     60, 22.99))
                .addItem(new Lesson("Feature Engineering",  75, 27.99));

        EducationalItem dataSci = new Module("Data Science Foundations")
                .addItem(python).addItem(ml);

        modules.put(key("Web Development Bootcamp"), webDev);
        modules.put(key("Data Science Foundations"),  dataSci);
        courses.put(key("HTML & CSS Fundamentals"),   html);
        courses.put(key("JavaScript Essentials"),     js);
        courses.put(key("React Fundamentals"),        react);
        courses.put(key("Python for Data Science"),   python);
        courses.put(key("Machine Learning Basics"),   ml);

        System.out.println("Demo catalog loaded - 2 modules, 5 courses.");
    }

    public void handleStatus() {
        System.out.println();
        header("STATUS");
        System.out.printf("  Modules  : %d%n", modules.size());
        System.out.printf("  Courses  : %d%n", courses.size());
        System.out.printf("  Cart     : %d item(s)%n", cart.size());
        System.out.printf("  Country  : %s%n", devCountry ? "Developing (-$10 active)" : "Standard");
        if (!cart.isEmpty())
            System.out.printf("  Subtotal : $%.2f%n",
                    cart.stream().mapToDouble(EducationalItem::calculatePrice).sum());
        System.out.println();
    }

    private EducationalItem findModule(String name) {
        EducationalItem m = modules.get(key(name));
        if (m == null) System.out.println("Module not found: \"" + name + "\". Use 4 to see available modules.");
        return m;
    }

    private EducationalItem findCourse(String name) {
        EducationalItem c = courses.get(key(name));
        if (c == null) System.out.println("Course not found: \"" + name + "\". Use 4 to see available courses.");
        return c;
    }

    private double parsePrice(String s) {
        try {
            double p = Double.parseDouble(s);
            if (p < 0) { System.out.println("Price cannot be negative."); return -1; }
            return p;
        } catch (NumberFormatException e) { System.out.println("Invalid price: \"" + s + "\""); return -1; }
    }

    private int parseDuration(String s) {
        try {
            int d = Integer.parseInt(s);
            if (d <= 0) { System.out.println("Duration must be a positive integer."); return -1; }
            return d;
        } catch (NumberFormatException e) { System.out.println("Invalid duration: \"" + s + "\""); return -1; }
    }

    private String addOnSuffix(boolean practice, boolean mentor) {
        if (!practice && !mentor) return "";
        List<String> addOns = new ArrayList<>();
        if (practice) addOns.add("Practice Set");
        if (mentor)   addOns.add("Live Mentor");
        return " with " + String.join(" + ", addOns);
    }

    private String key(String name) { 
        return name.trim().toLowerCase(); 
    }

    private void header(String title) {
        System.out.println("-".repeat(60));
        System.out.println("  " + title);
        System.out.println("-".repeat(60));
    }
}