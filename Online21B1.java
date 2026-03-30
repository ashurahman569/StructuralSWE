// A delivery service offers discounts to its customers based on certain conditions. When a customer makes a purchase, 
// they may be eligible for one or more of the following discounts:

// Loyalty Discount: If the customer is a premium member, they receive a 10% discount on their purchase.
// Seasonal Discount: During special promotional seasons, an additional flat discount of 100 units is applied to the purchase price.
// High-Value Purchase Discount: If the purchase amount exceeds 10,000 units, an additional 2% discount is applied.

// Your task is to design a solution that calculates the final price after applying all applicable discounts. 
// Each discount can be applied independently, and multiple discounts may be combined if the customer qualifies. Implement this solution in Java.
// Same ques in 21 B1

// ─────────────────────────────────────────────
// Step 1: Component Interface
// ─────────────────────────────────────────────
interface Purchase {
    double calculatePrice();
}

// ─────────────────────────────────────────────
// Step 2: Concrete Component — Base Purchase
// ─────────────────────────────────────────────
class BasePurchase implements Purchase {
    private double price;

    public BasePurchase(double price) {
        this.price = price;
    }

    @Override
    public double calculatePrice() {
        return price;
    }
}

// ─────────────────────────────────────────────
// Step 3: Abstract Decorator
// ─────────────────────────────────────────────
abstract class DiscountDecorator implements Purchase {
    protected Purchase wrappedPurchase;   // ← holds the inner object

    public DiscountDecorator(Purchase purchase) {
        this.wrappedPurchase = purchase;
    }

    @Override
    public double calculatePrice() {
        return wrappedPurchase.calculatePrice();
    }
}

// ─────────────────────────────────────────────
// Step 4: Concrete Decorators (one per discount)
// ─────────────────────────────────────────────

// Discount 1: Loyalty — 10% off for premium members
class LoyaltyDiscount extends DiscountDecorator {
    public LoyaltyDiscount(Purchase purchase) {
        super(purchase);
    }

    @Override
    public double calculatePrice() {
        double price = wrappedPurchase.calculatePrice();
        double discount = price * 0.10;
        System.out.printf("  Loyalty Discount (10%%):        -%.2f%n", discount);
        return price - discount;
    }
}

// Discount 2: Seasonal — flat 100 units off
class SeasonalDiscount extends DiscountDecorator {
    public SeasonalDiscount(Purchase purchase) {
        super(purchase);
    }

    @Override
    public double calculatePrice() {
        double price = wrappedPurchase.calculatePrice();
        System.out.printf("  Seasonal Discount (flat):      -100.00%n");
        return price - 100;
    }
}

// Discount 3: High-Value — 2% off if price > 10,000
class HighValueDiscount extends DiscountDecorator {
    public HighValueDiscount(Purchase purchase) {
        super(purchase);
    }

    @Override
    public double calculatePrice() {
        double price = wrappedPurchase.calculatePrice();
        if (price > 10000) {
            double discount = price * 0.02;
            System.out.printf("  High-Value Discount (2%%):      -%.2f%n", discount);
            return price - discount;
        } else {
            System.out.println("  High-Value Discount:           Not applicable (price <= 10,000)");
            return price;
        }
    }
}

// ─────────────────────────────────────────────
// Step 5: Main — fills in the ?? blanks
// ─────────────────────────────────────────────
public class Online21B1 {
    public static void main(String[] args) {

        double basePrice = 12000; // Initial price of the product

        System.out.println("========== ZBazar Discount Calculator ==========");
        System.out.printf("Base Price:                        %.2f%n", basePrice);
        System.out.println("-------------------------------------------------");

        // Create a base purchase
        Purchase purchase = new BasePurchase(basePrice);

        // Apply discounts conditionally (all three here — wrap like layers)
        boolean isPremiumMember = true;   // customer is premium → Loyalty applies
        boolean isPromotionalSeason = true; // seasonal promo is active

        Purchase discountedPurchase = purchase;

        if (isPremiumMember) {
            discountedPurchase = new LoyaltyDiscount(discountedPurchase);
        }
        if (isPromotionalSeason) {
            discountedPurchase = new SeasonalDiscount(discountedPurchase);
        }
        // High-Value checks price internally, so always wrap it
        discountedPurchase = new HighValueDiscount(discountedPurchase);

        // Calculate final price after all applicable discounts
        double finalPrice = discountedPurchase.calculatePrice();

        System.out.println("-------------------------------------------------");
        System.out.printf("Final price after all discounts:   %.2f%n", finalPrice);


        // ── Extra demo: non-premium, non-seasonal customer ──
        System.out.println("\n========== Customer 2 (no membership, no season) ==========");
        System.out.printf("Base Price:                        %.2f%n", basePrice);
        System.out.println("-------------------------------------------------");

        Purchase purchase2 = new BasePurchase(basePrice);
        // Only High-Value discount applies
        Purchase discounted2 = new HighValueDiscount(purchase2);
        double finalPrice2 = discounted2.calculatePrice();

        System.out.println("-------------------------------------------------");
        System.out.printf("Final price after all discounts:   %.2f%n", finalPrice2);
    }
}

