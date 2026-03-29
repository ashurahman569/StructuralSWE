
public class EduLearnDemo {

    public static void main(String[] args) {

        EducationalItem htmlCourse = new Course("HTML & CSS Fundamentals")
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

        EducationalItem webDevModule = new Module("Web Development Bootcamp")
                .addItem(htmlCourse).addItem(js).addItem(react);

        EducationalItem python = new Course("Python for Data Science")
                .addItem(new Lesson("NumPy Arrays",       50, 16.99))
                .addItem(new Lesson("Pandas DataFrames",  65, 18.99))
                .addItem(new Lesson("Data Visualisation", 55, 20.99));

        EducationalItem ml = new Course("Machine Learning Basics")
                .addItem(new Lesson("Supervised Learning",  70, 25.99))
                .addItem(new Lesson("Model Evaluation",     60, 22.99))
                .addItem(new Lesson("Feature Engineering",  75, 27.99));

        EducationalItem dataScienceModule = new Module("Data Science Foundations")
                .addItem(python).addItem(ml);


        // ============================================================
        // SCENARIO 1 — Single Course (No Discounts)
        // ============================================================
        System.out.println("\n*** SCENARIO 1: Single Course (No Discounts) ***\n");

        Billable bill1 = new Cart();
        bill1.add(htmlCourse);

        new CheckoutService().checkout(bill1);


        // ============================================================
        // SCENARIO 2 — Module + Add-ons (Duration Discount Applies)
        // ============================================================
        System.out.println("\n*** SCENARIO 2: Module + Add-ons (Duration Discount) ***\n");

        EducationalItem webDevWithAddOns =
                new LiveMentorDecorator(
                        new PracticeSetDecorator(webDevModule));

        Billable bill2 = new Cart();
        bill2.add(webDevWithAddOns);

        // Apply duration discount manually (since >= 300 min)
        bill2 = new DurationDiscountDecorator(bill2);

        new CheckoutService().checkout(bill2);


        // ============================================================
        // SCENARIO 3 — Multi-Module + Developing Country
        // ============================================================
        System.out.println("\n*** SCENARIO 3: Multi-Module + Developing Country ***\n");

        EducationalItem webDevWithPractice =
                new PracticeSetDecorator(webDevModule);

        Billable bill3 = new Cart();
        bill3.add(webDevWithPractice);
        bill3.add(dataScienceModule);

        // Apply discounts (same order as CommandHandler)
        bill3 = new MultiModuleDiscountDecorator(bill3);
        bill3 = new DurationDiscountDecorator(bill3);
        bill3 = new DevelopingCountryDiscountDecorator(bill3);

        new CheckoutService().checkout(bill3);


        // ============================================================
        // SCENARIO 4 — Composite Transparency
        // ============================================================
        System.out.println("\n*** SCENARIO 4: Full Module Breakdown ***\n");

        System.out.printf("%-43s  Duration       Price%n", "Item");
        System.out.println("-".repeat(70));

        webDevModule.display("");

        System.out.println("-".repeat(70));
        System.out.printf("%-43s  Total: %.1f hrs  $%.2f%n",
                "",
                webDevModule.getDurationInMinutes() / 60.0,
                webDevModule.calculatePrice());
    }
}