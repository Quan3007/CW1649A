// This class represents a book in the store.
package CW_NDQ;
public class Book {
    private String title, author, keyword;
    private int price, rating, stock;
    public Book(String title, String author, int price, int rating, String keyword, int stock){
        this.title = title; this.author = author; this.price = price; this.rating = rating; this.keyword = keyword; this.stock = stock;
    }   

    public String getTitle(){
         return title; }
    public int getRating(){ 
        return rating; }
    public int getPrice() { 
        return price; }
    public String getAuthor() {
         return author; }
    public String getKeyword() {
         return keyword; }
    public int getStock() { 
        return stock; }
    public void setStock(int stock) {
         this.stock = stock; }
}
