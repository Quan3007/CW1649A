package CW_NDQ;

import CW_NDQ.ADT.LinkedQueue;
import CW_NDQ.ADT.LinkedStack;
import CW_NDQ.ADT.MyArrayList;

import java.util.Scanner;

public class BookstoreApp {
    private Book[] catalog;
    private int catalogSize;
    private LinkedQueue<Order> orderQueue;
    private MyArrayList<Order> orderHistory;
    private LinkedStack<Order> orderStack;
    private Scanner sc;

    public BookstoreApp() {
        this.catalog = new Book[10];
        this.catalogSize = 0;
        this.orderQueue = new LinkedQueue<>();
        this.orderHistory = new MyArrayList<>();
        this.orderStack = new LinkedStack<>();
        this.sc = new Scanner(System.in);
        initCatalog();
    }

    public void run() {
        while (true) {
            int ch = showMenuAndGetChoice();
            switch (ch) {
                case 1: placeOrder(); break;
                case 2: viewOrders(); break;
                case 3: searchOrder(); break;
                case 4: processOrder(); break;
                case 5: updateOrderStatus(); break;
                case 6: undoLastOrder(); break;
                case 7: return;
                default: System.out.println("Invalid");
            }
        }
    }

    private int showMenuAndGetChoice() {
        while (true) {
            System.out.println(" Book Store App\n --------------\n1. Place Order\n2. View Orders\n3. Search Order\n4. Process Next Order\n5. Update Order Status\n6. Undo Last Order\n7. Exit");
            System.out.print("Enter your choice: ");
            String input = sc.nextLine().trim();
            try { return Integer.parseInt(input); }
            catch (NumberFormatException e) { System.out.println("Invalid input. Please enter a number only."); }
        }
    }

    private String prompt(String message) {
        System.out.print(message);
        return sc.nextLine().trim();
    }

    private void showMessage(String message) {
        System.out.println(message);
    }

    private void addBook(Book book) {
        if (catalogSize == catalog.length) {
            Book[] newCatalog = new Book[catalog.length * 2];
            for (int i = 0; i < catalogSize; i++) newCatalog[i] = catalog[i];
            catalog = newCatalog;
        }
        catalog[catalogSize++] = book;
    }

    private void listCatalog() {
        for (int i = 0; i < catalogSize; i++) System.out.println((i+1) + ". " + catalog[i].getTitle());
    }

    private Book findBookByKeyword(String keyword) {
        for (int i = 0; i < catalogSize; i++) if (catalog[i].getKeyword().equalsIgnoreCase(keyword)) return catalog[i];
        return null;
    }

    private void initCatalog() {
        addBook(new Book("Thinking, Fast and Slow", "Daniel Kahneman", 100, 5, "TFAS", 5));
        addBook(new Book("The Railway Children", "E. Nesbit", 80, 4, "TRC", 5));
        addBook(new Book("Atomic Habits", "James Clear", 100, 5, "AH", 5));
        addBook(new Book("Educated", "Tara Westover", 100, 5, "ED", 5));
        addBook(new Book("Where the Crawdads Sing", "Delia Owens", 80, 4, "WTC", 5));
        addBook(new Book("The Midnight Library", "Matt Haig", 80, 4, "TML", 5));
        addBook(new Book("The Da Vinci Code", "Dan Brown", 50, 3, "TDC", 5));
        addBook(new Book("Clean Code", "Robert C. Martin", 120, 5, "CC", 5));
    }

    private void placeOrder() {
        String name = getCustomerName();
        String addr = getShippingAddress();
        MyArrayList<OrderItem> items = new MyArrayList<>();
        boolean addMore = true;
        while (addMore) {
            listCatalog();
            String input = prompt("Choose this book: ");
            int bookIndex = -1;
            try { bookIndex = Integer.parseInt(input) - 1; }
            catch (NumberFormatException e) {
                Book found = findBookByKeyword(input);
                if (found == null) { showMessage("No book found with that keyword."); continue; }
                for (int i = 0; i < catalogSize; i++) if (catalog[i] == found) { bookIndex = i; break; }
                if (bookIndex == -1) { showMessage("No book found with that keyword."); continue; }
            }
            if (bookIndex < 0 || bookIndex >= catalogSize) { showMessage("Invalid book number."); continue; }
            Book selectedBook = catalog[bookIndex];
            showBookDetails(selectedBook);
            int qty = getQuantity();
            if (qty <= 0) { showMessage("Quantity must be positive."); continue; }
            if (qty > selectedBook.getStock()) { showMessage("Not enough stock. Only " + selectedBook.getStock() + " available."); continue; }
            items.add(new OrderItem(selectedBook, qty));
            selectedBook.setStock(selectedBook.getStock() - qty);
            while (true) {
                String more = askToAddMore();
                if (more.equals("y")) { addMore = true; break; }
                else if (more.equals("n")) { addMore = false; break; }
                else if (more.equals("u")) {
                    if (!items.isEmpty()) {
                        OrderItem undone = items.remove(items.size() - 1);
                        Book book = undone.getBook();
                        book.setStock(book.getStock() + undone.getQuantity());
                        showMessage("Last book addition undone.");
                    } else showMessage("Nothing to undo.");
                } else showMessage("Please enter 'y' for yes, 'n' for no, or 'u' to undo last.");
            }
        }
        if (items.isEmpty()) { showMessage("No items in order. Order cancelled."); return; }
        sortItemsByTitle(items);
        Order order = new Order(name, addr, items);
        orderQueue.offer(order);
        orderHistory.add(order);
        orderStack.push(order);
        showMessage("Order placed.");
        int totalQuantity = 0;
        for (int i = 0; i < items.size(); i++) totalQuantity += items.get(i).getQuantity();
        showMessage("Quantity: " + totalQuantity + "  Total price: $" + order.getTotalPrice());
        showMessage(" -----------------");
        showMessage(" ");
    }

    private void viewOrders() {
        for (int i = 0; i < orderHistory.size(); i++) printOrderDetails(orderHistory.get(i));
    }

    private void searchOrder() {
        int type = getSearchType();
        if (type == 1) {
            while (true) {
                int num = getOrderNumber();
                if (num == 4) { showMessage("Search cancelled."); return; }
                boolean foundNum = false;
                for (int i = 0; i < orderHistory.size(); i++) {
                    Order o = orderHistory.get(i);
                    if (o.getOrderNumber() == num) {
                        showMessage("Order number: " + o.getOrderNumber());
                        printOrderDetails(o);
                        foundNum = true;
                    }
                }
                if (foundNum) return;
                showMessage("Not found: " + num);
            }
        } else if (type == 2) {
            while (true) {
                String customerName = getCustomerName();
                if (customerName.isEmpty()) { showMessage("Search cancelled."); return; }
                if (customerName.matches(".*\\d+.*")) { showMessage("Name cannot contain numbers. Please enter a valid name."); continue; }
                boolean foundName = false;
                for (int i = 0; i < orderHistory.size(); i++) {
                    Order order = orderHistory.get(i);
                    if (order.getCustomerName().equalsIgnoreCase(customerName)) {
                        showMessage("Order number: " + order.getOrderNumber());
                        printOrderDetails(order);
                        foundName = true;
                    }
                }
                if (foundName) return;
                showMessage("Not found: " + customerName);
            }
        } else if (type == 3) {
            while (true) {
                String bookName = getBookName();
                if (bookName.isEmpty()) { showMessage("Search cancelled."); return; }
                boolean foundBook = false;
                for (int i = 0; i < orderHistory.size(); i++) {
                    Order o = orderHistory.get(i);
                    MyArrayList<OrderItem> items = o.getItems();
                    for (int j = 0; j < items.size(); j++) {
                        Book book = items.get(j).getBook();
                        if (book.getKeyword().equalsIgnoreCase(bookName)) {
                            showMessage("Order number: " + o.getOrderNumber());
                            printOrderDetails(o);
                            foundBook = true;
                            break;
                        }
                    }
                }
                if (foundBook) return;
                showMessage("Not found: " + bookName);
            }
        }
    }

    private void printOrderDetails(Order o) {
        showMessage("Order"+o.getOrderNumber()+": Name: "+o.getCustomerName()+", Address: "+o.getShippingAddress()+", Status: "+o.getStatus());
        showMessage("Books: ");
        MyArrayList<OrderItem> items = o.getItems();
        for (int j = 0; j < items.size(); j++) {
            Book b = items.get(j).getBook();
            int qty = items.get(j).getQuantity();
            int price = b.getPrice();
            int total = qty * price;
            showMessage(b.getTitle() + " (Quantity: " + qty + ", Price: $" + price + ", Rating: " + b.getRating() + ", Total: $" + total + ")");
            if (j < items.size() - 1) showMessage(", ");
        }
        showMessage("\n");
    }

    private void processOrder() {
        if (orderQueue.size() == 0) { showMessage("No orders"); return; }
        Order o = orderQueue.poll();
        o.setStatus("Shipped");
        showMessage("Processed Order#"+o.getOrderNumber());
    }

    private void updateOrderStatus() {
        int num = getOrderNumber();
        for (int i = 0; i < orderHistory.size(); i++) {
            Order o = orderHistory.get(i);
            if (o.getOrderNumber() == num) {
                int statusChoice = getStatusChoice();
                String newStatus;
                switch (statusChoice) {
                    case 1: newStatus = "Processing"; break;
                    case 2: newStatus = "Pending"; break;
                    case 3: newStatus = "Complete"; break;
                    default: showMessage("Invalid status choice."); return;
                }
                o.setStatus(newStatus);
                if (newStatus.equals("Pending")) {
                    showMessage("Order status set to Pending.");
                }
                showMessage("Order status updated to: " + newStatus);
                return;
            }
        }
        showMessage("Order not found.");
    }

    private void sortItemsByTitle(MyArrayList<OrderItem> items) {
        for (int i = 0; i < items.size()-1; i++) {
            int min = i;
            for (int j = i+1; j < items.size(); j++) {
                if (items.get(j).getBook().getTitle().compareTo(items.get(min).getBook().getTitle()) < 0) min = j;
            }
            if (min != i) {
                OrderItem tmp = items.get(i);
                items.set(i, items.get(min));
                items.set(min, tmp);
            }
        }
    }

    private String getCustomerName() {
        while (true) {
            String name = prompt("Name: ");
            if (name.matches(".*\\d+.*")) { showMessage("Name cannot contain numbers. Please enter a valid name."); continue; }
            return name;
        }
    }

    private String getShippingAddress() {
        return prompt("Address: ");
    }

    private String getBookName() {
        return prompt("Book name: ");
    }

    private int getOrderNumber() {
        while (true) {
            String input = prompt("Order number: ");
            if (input.isEmpty()) return -1;
            try { return Integer.parseInt(input); }
            catch (NumberFormatException e) { showMessage("Invalid order number. Please enter digits only."); }
        }
    }

    private int getQuantity() {
        return Integer.parseInt(prompt("Quantity: "));
    }

    private int getSearchType() {
        return Integer.parseInt(prompt("Search by: 1. Order Number  2. Customer Name  3. Book Name 4. Cancel\n"));
    }

    private int getStatusChoice() {
        return Integer.parseInt(prompt("Status : 1. Processing, 2. Pending, 3. Complete: "));
    }

    private String askToAddMore() {
        return prompt("Add another book? (y = yes, n = no, u = undo last): ").toLowerCase();
    }

    private void showBookDetails(Book book) {
        showMessage("Selected Book: " + book.getTitle());
        showMessage(" Author: " + book.getAuthor());
        showMessage(" In Stock: " + book.getStock());
    }

    // Add undoLastOrder method
    private void undoLastOrder() {
        if (orderStack.isEmpty()) {
            showMessage("No order to undo.");
            return;
        }
        Order lastOrder = orderStack.pop();
        int lastOrderNum = lastOrder.getOrderNumber();
        // Remove from orderQueue if present (only if not yet processed)
        LinkedQueue<Order> newQueue = new LinkedQueue<>();
        boolean removedFromQueue = false;
        while (!orderQueue.isEmpty()) {
            Order o = orderQueue.poll();
            if (!removedFromQueue && o.getOrderNumber() == lastOrderNum) {
                removedFromQueue = true;
                continue;
            }
            newQueue.offer(o);
        }
        orderQueue = newQueue;
        // Remove from orderHistory
        for (int i = 0; i < orderHistory.size(); i++) {
            if (orderHistory.get(i).getOrderNumber() == lastOrderNum) {
                orderHistory.remove(i);
                break;
            }
        }
        // Restore book stock
        MyArrayList<OrderItem> items = lastOrder.getItems();
        for (int i = 0; i < items.size(); i++) {
            OrderItem item = items.get(i);
            Book orderedBook = item.getBook();
            for (int k = 0; k < catalogSize; k++) {
                Book catalogBook = catalog[k];
                if (catalogBook.getKeyword().equalsIgnoreCase(orderedBook.getKeyword())) {
                    catalogBook.setStock(catalogBook.getStock() + item.getQuantity());
                }
            }
        }
        showMessage("Last order has been undone and stock restored.");
    }
}


