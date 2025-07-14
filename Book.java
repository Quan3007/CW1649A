package CW_NDQ;

public class Book {
    private String title;
    private String author;
    private int price;
    private int rating;

    public Book(String title, String author, int price, int rating){
        this.title = title;
        this.author = author;
        this.price = price;
        this.rating = rating;
    }

    public String getTitle(){
        return title;
    }

    public int getRating(){
        return rating;
    }
    public void setRating(int rating){
        this.rating = rating;
    }
}
