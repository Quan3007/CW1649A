package CW_NDQ;
import java.util.Scanner;

public class BookstoreApp {
    static ArrayList<Book> catalog = new ArrayList<>();
    static LinkedQueue<Order> orderQueue = new LinkedQueue<>();
    static ArrayList<Order> orderHistory = new ArrayList<>();

    public static void main(String[] args) {
        initCatalog();
        Scanner sc = new Scanner(System.in);
        while (true) {
            System.out.println(" Book Store App\n1. Place Order\n2. View Orders\n3. Search Order\n4. Process Next Order\n5. Update Order Status\n6. Exit");
            int ch = sc.nextInt(); sc.nextLine();
            switch (ch) {
                case 1: placeOrder(sc); break;
                case 2: viewOrders(); break;
                case 3: searchOrder(sc); break;
                case 4: processOrder(); break;
                case 5: updateOrderStatus(sc); break;
                case 6: return;
                default: System.out.println("Invalid");
            }
        }
    }

    static void initCatalog() {
        catalog.add(new Book("Thinking, Fast and Slow", "Daniel Kahneman", 100, 5));
        catalog.add(new Book("The Railway Children", "E. Nesbit", 80, 4));
        catalog.add(new Book("Atomic Habits", "James Clear", 100, 5));
        catalog.add(new Book("Educated", "Tara Westover", 100, 5));
        catalog.add(new Book("Where the Crawdads Sing", "Delia Owens", 80, 4));
        catalog.add(new Book("The Midnight Library", "Matt Haig", 80, 4));
        catalog.add(new Book("The Da Vinci Code", "Dan Brown", 50, 3));
    }

    static void placeOrder(Scanner sc) {
        String name;
        while (true) {
            System.out.print("Name: ");
            name = sc.nextLine().trim();
            if (name.isEmpty()) {
                System.out.println("Name cannot be empty.");
                continue;
            }
            if (name.matches(".*\\d+.*")) {
                System.out.println("Name cannot contain numbers. Please enter a valid name.");
                continue;
            }
            break;
        }
        String addr;
        while (true) {
            System.out.print("Address: ");
            addr = sc.nextLine().trim();
            if (addr.isEmpty()) {
                System.out.println("Address cannot be empty.");
                continue;
            }
            break;
        }
        ArrayList<OrderItem> items = new ArrayList<>();
        boolean addMore = true;
        while (addMore) {
            for (int i = 0; i < catalog.size(); i++)
                System.out.println((i+1)+". "+catalog.get(i).getTitle());
            System.out.print("Choose this book: ");
            int idx;
            try {
                idx = Integer.parseInt(sc.nextLine().trim()) - 1;
            } catch (Exception e) {
                System.out.println("Invalid input. Try again.");
                continue;
            }
            if (idx < 0 || idx >= catalog.size()) {
                System.out.println("Invalid book number.");
                continue;
            }
            System.out.print("Quantity: ");
            int qty;
            try {
                qty = Integer.parseInt(sc.nextLine().trim());
            } catch (Exception e) {
                System.out.println("Invalid input. Try again.");
                continue;
            }
            if (qty <= 0) {
                System.out.println("Quantity must be positive.");
                continue;
            }
            items.add(new OrderItem(catalog.get(idx), qty));
            System.out.print("Add another book? (y/n): ");
            String ans = sc.nextLine().trim().toLowerCase();
            addMore = ans.equals("y");
        }
        if (items.size() == 0) {
            System.out.println("No items in order. Order cancelled.");
            return;
        }
        sortItemsByTitle(items);
        Order order = new Order(name, addr, items);
        orderQueue.enqueue(order);
        orderHistory.add(order);
        System.out.println("Order placed.");
    }

    static void viewOrders() {
        for (int i = 0; i < orderHistory.size(); i++) {
            Order o = orderHistory.get(i);
            System.out.println("Order "+o.getOrderNumber()+": Name: "+o.getCustomerName()+", Address: "+o.getShippingAddress()+", Status: "+o.getStatus());
            System.out.print("Books: ");
            ArrayList<OrderItem> items = o.getItems();
            for (int j = 0; j < items.size(); j++) {
                Book b = items.get(j).getBook();
                System.out.print(b.getTitle() + " (Rating: " + b.getRating() + ")");
                if (j < items.size() - 1) System.out.print(", ");
            }
            System.out.println();
        }
    }

    static void searchOrder(Scanner sc) {
        System.out.println("Search by: 1. Order Number  2. Customer Name  3. Book Name");
        int type;
        try {
            type = Integer.parseInt(sc.nextLine().trim());
        } catch (Exception e) {
            System.out.println("Invalid input.");
            return;
        }
        boolean found = false;
        if (type == 1) {
            System.out.print("Order number: ");
            String numStr = sc.nextLine().trim();
            int num;
            try {
                num = Integer.parseInt(numStr);
            } catch (Exception e) {
                System.out.println("Invalid order number. Please enter digits only.");
                return;
            }
            for (int i = 0; i < orderHistory.size(); i++) {
                Order o = orderHistory.get(i);
                if (o.getOrderNumber() == num) {
                    System.out.println("Order number: " + o.getOrderNumber());
                    printOrderDetails(o);
                    found = true;
                }
            }
            if (!found) {
                System.out.println("Order not found.");
            }
            return;
        } else if (type == 2) {
            // Prompt for a valid customer name (not a number, not empty, no digits)
            String customerName;
            while (true) {
                System.out.print("Customer name: ");
                customerName = sc.nextLine().trim();
                if (customerName.isEmpty()) {
                    System.out.println("Search cancelled.");
                    return;
                }
                if (customerName.matches(".*\\d+.*")) {
                    System.out.println("Name cannot contain numbers. Please enter a valid name.");
                    continue;
                }
                break; // Valid name
            }
            String searchName = customerName.toLowerCase();
            boolean foundName = false;
            for (int i = 0; i < orderHistory.size(); i++) {
                Order order = orderHistory.get(i);
                String orderName = order.getCustomerName().toLowerCase();
                if (orderName.contains(searchName)) {
                    System.out.println("Order number: " + order.getOrderNumber());
                    printOrderDetails(order);
                    foundName = true;
                }
            }
            if (!foundName) {
                System.out.println("Not found");
            }
            return;
        } else if (type == 3) {
            System.out.print("Book name: ");
            String bookName = sc.nextLine().trim().toLowerCase();
            boolean foundBook = false;
            for (int i = 0; i < orderHistory.size(); i++) {
                Order o = orderHistory.get(i);
                ArrayList<OrderItem> items = o.getItems();
                for (int j = 0; j < items.size(); j++) {
                    if (items.get(j).getBook().getTitle().toLowerCase().contains(bookName)) {
                        System.out.println("Order number: " + o.getOrderNumber());
                        printOrderDetails(o);
                        foundBook = true;
                        break;
                    }
                }
            }
            if (!foundBook) System.out.println("Not found");
            return;
        }
        if (!found) System.out.println("Not found");
    }

    static void printOrderDetails(Order o) {
        System.out.println("Order"+o.getOrderNumber()+": Name: "+o.getCustomerName()+", Address: "+o.getShippingAddress()+", Status: "+o.getStatus());
        System.out.print("Books: ");
        ArrayList<OrderItem> items = o.getItems();
        for (int j = 0; j < items.size(); j++) {
            Book b = items.get(j).getBook();
            System.out.print(b.getTitle() + " (Rating: " + b.getRating() + ")");
            if (j < items.size() - 1) System.out.print(", ");
        }
        System.out.println();
    }

    static void processOrder() {
        if (orderQueue.size() == 0) { 
            System.out.println("No orders"); 
            return; 
        }
        Order o = orderQueue.dequeue();
        o.setStatus("Shipped");
        System.out.println("Processed Order#"+o.getOrderNumber());
    }

    static void updateOrderStatus(Scanner sc) {
        System.out.print("Enter order number: ");
        int num;
        try {
            num = Integer.parseInt(sc.nextLine().trim());
        } catch (Exception e) {
            System.out.println("Invalid input.");
            return;
        }
        for (int i = 0; i < orderHistory.size(); i++) {
            Order o = orderHistory.get(i);
            if (o.getOrderNumber() == num) {
                System.out.println("Choose status: 1. Processing  2. Pending  3. Complete");
                int statusChoice;
                try {
                    statusChoice = Integer.parseInt(sc.nextLine().trim());
                } catch (Exception e) {
                    System.out.println("Invalid input.");
                    return;
                }
                String newStatus;
                switch (statusChoice) {
                    case 1: newStatus = "Processing"; break;
                    case 2: newStatus = "Pending"; break;
                    case 3: newStatus = "Complete"; break;
                    default: 
                        System.out.println("Invalid status choice.");
                        return;
                }
                o.setStatus(newStatus);
                System.out.println("Order status updated to: " + newStatus);
                return;
            }
        }
        System.out.println("Order not found.");
    }

    static void sortItemsByTitle(ArrayList<OrderItem> items) {
        for (int i = 0; i < items.size()-1; i++) {
            int min = i;
            for (int j = i+1; j < items.size(); j++) {
                if (items.get(j).getBook().getTitle().compareTo(items.get(min).getBook().getTitle()) < 0)
                    min = j;
            }
            if (min != i) {
                OrderItem tmp = items.get(i);
                items.set(i, items.get(min));
                items.set(min, tmp);
            }
        }
    }
}


