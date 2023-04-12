import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class SalesTaxCalculator {

    public static void main(String[] args) {
    	
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please hit enter twice after all items are added" + "\n");
        System.out.println("Please follow sample format '1 book at 9.99' for each entry" + "\n");
        System.out.println("Please enter items for checkout :" + "\n");
        List<Item> items = new ArrayList<>();
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            if (line.isEmpty()) {
                break;
            }
            items.add(parseItem(line));
        }
        printReceipt(items);
    }

    private static Item parseItem(String line) {
    	String name = "";
        String[] parts = line.split(" ");
        int quantity = Integer.parseInt(parts[0]);
        for (int i = 1; i < parts.length-2; i++) 
        {
         name = name.concat(parts[i] + " ");
        }
        BigDecimal price = new BigDecimal(parts[parts.length-1]);
        boolean isExempt = name.equalsIgnoreCase("book")|| name.equalsIgnoreCase("books") || name.equalsIgnoreCase("food") || name.equalsIgnoreCase("medical") || name.equalsIgnoreCase("chocolates") || name.equalsIgnoreCase("chocolate") || name.equalsIgnoreCase("pills");
        boolean isImported = name.equalsIgnoreCase("imported");
        return new Item(name, quantity, price, isExempt, isImported);
    }

    private static void printReceipt(List<Item> items) {
        BigDecimal totalSalesTax = BigDecimal.ZERO;
        BigDecimal totalAmount = BigDecimal.ZERO;
        BigDecimal totalPrice = BigDecimal.ZERO;
        for (Item item : items) {
            BigDecimal itemPrice = item.getPrice();
            BigDecimal Qunatity = new BigDecimal(item.getQuantity());
            BigDecimal itemSalesTax = item.calculateSalesTax();
            totalPrice =itemPrice.add(item.getPrice().multiply(Qunatity));
            totalSalesTax = totalSalesTax.add(itemSalesTax);
            totalAmount = totalAmount.add(totalPrice.add(itemSalesTax));
            System.out.println(item.getQuantity() + " " + item.getName() + ": " + totalPrice.add(itemSalesTax));
        }
        System.out.println("Sales Taxes: " + totalSalesTax.setScale(2, RoundingMode.HALF_UP));
        System.out.println("Total: " + totalAmount.setScale(2, RoundingMode.HALF_UP));
    }

    private static class Item {
        private String name;
        private int quantity;
        private BigDecimal price;
        private boolean isExempt;
        private boolean isImported;

        public Item(String name, int quantity, BigDecimal price, boolean isExempt, boolean isImported) {
            this.name = name;
            this.quantity = quantity;
            this.price = price;
            this.isExempt = isExempt;
            this.isImported = isImported;
        }

        public String getName() {
            return name;
        }

        public int getQuantity() {
            return quantity;
        }

        public BigDecimal getPrice() {
            return price;
        }

        public boolean isExempt() {
            return isExempt;
        }

        public boolean isImported() {
            return isImported;
        }

        public BigDecimal calculateSalesTax() {
            BigDecimal salesTax = BigDecimal.ZERO;
            if (!isExempt) {
                salesTax = salesTax.add(price.multiply(new BigDecimal("0.1")));
            }
            if (isImported) {
                salesTax = salesTax.add(price.multiply(new BigDecimal("0.05")));
            }
            salesTax = roundToNearestFiveCents(salesTax);
            return salesTax;
        }

        private BigDecimal roundToNearestFiveCents(BigDecimal value) {
            BigDecimal divided = value.multiply(new BigDecimal("20"));
            BigDecimal rounded = new BigDecimal(Math.ceil(divided.doubleValue()));
            return rounded.divide(new BigDecimal("20"), 2, RoundingMode.HALF_UP);
        }
    }
}
