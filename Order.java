// This class represents a customer's order.
package CW_NDQ;

public class Order {
    private static int nextOrderNumber = 1;
    private int orderNumber;
    private String customerName;
    private String shippingAddress;
    private MyArrayList<OrderItem> items;
    private String status;

    public Order(String customerName, String shippingAddress, MyArrayList<OrderItem> items) {
        this.orderNumber = nextOrderNumber++;
        this.customerName = customerName;
        this.shippingAddress = shippingAddress;
        this.items = items;
        this.status = "Pending";
    }

    public int getOrderNumber() { return orderNumber; }
    public String getCustomerName() { return customerName; }
    public String getShippingAddress() { return shippingAddress; }
    public MyArrayList<OrderItem> getItems() { return items; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public int getTotalPrice() {
        int total = 0;
        for (int i = 0; i < items.size(); i++) {
            OrderItem item = items.get(i);
            total += item.getBook().getPrice() * item.getQuantity();
        }
        return total;
    }
}
