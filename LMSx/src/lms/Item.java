package lms;

public abstract class Item {
    protected int itemId;
    protected String title;
    protected boolean available;

    public Item(int itemId, String title) {
        this.itemId = itemId;
        this.title = title;
        this.available = true; // Items are available by default when created
    }

    public int getItemId() {
        return itemId;
    }

    public String getTitle() {
        return title;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public abstract String getType();

    @Override
    public String toString() {
        return "Item ID: " + itemId + "\nTitle: " + title + "\nAvailable: " + available;
    }
}
