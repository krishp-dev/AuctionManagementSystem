

public class Item {
    private int itemId;
    private String itemName;
    private String description;
    private double startBid;

    public Item(int itemId, String itemName, String description, double startBid) {
        this.itemId = itemId;
        this.itemName = itemName;
        this.description = description;
        this.startBid = startBid;
    }

    public int getItemId() {
        return itemId;
    }

    public String getItemName() {
        return itemName;
    }

    public String getDescription() {
        return description;
    }

    public double getStartBid() {
        return startBid;
    }
}

