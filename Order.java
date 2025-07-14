package CW_NDQ;

public class Order {
    private static int nextOrderNumber = 1;
    private int orderNumber;
    private String customerName;
    private String shippingAddress;
    private ArrayList<OrderItem> items;
    private String status;

    public Order(String customerName, String shippingAddress, ArrayList<OrderItem> items) {
        this.orderNumber = nextOrderNumber++;
        this.customerName = customerName;
        this.shippingAddress = shippingAddress;
        this.items = items;
        this.status = "Pending";
    }

    public int getOrderNumber() { return orderNumber; }
    public String getCustomerName() { return customerName; }
    public String getShippingAddress() { return shippingAddress; }
    public ArrayList<OrderItem> getItems() { return items; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
