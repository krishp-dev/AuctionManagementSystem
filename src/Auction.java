

class Auction {
    int auctionId;
    String auctionName;
    int auctionDuration;
    boolean isActive;
    double currentBid;
    String highestBidder;
    int numberOfUsers;

    public Auction(int auctionId, String auctionName, int auctionDuration, boolean isActive,
            int numUser) {
        this.auctionId = auctionId;
        this.auctionName = auctionName;
        this.auctionDuration = auctionDuration;
        this.isActive = isActive;
        this.currentBid = 0;
        this.numberOfUsers = numUser;
    }

    public synchronized double getCurrentBid() {
        return currentBid;
    }

    public synchronized void setCurrentBid(double bidAmount, String bidder) {
        if (bidAmount > currentBid) {
            currentBid = bidAmount;
            highestBidder = bidder;
        }
    }

    public synchronized void DisplayWinner() {
        System.out.println("\u001B[43m" + "\u001B[31m" + " Auction ended! Winner: " + highestBidder + " with bid: " + currentBid + "  \u001B[0m");

    }

    public int getAuctionId() {
        return auctionId;
    }

    public String getAuctionName() {
        return auctionName;
    }

    public int getAuctionDuration() {
        return auctionDuration;
    }

    public boolean isActive() {
        return isActive;
    }

    public String toString() {
        return "Auction Details:\n" +
                "Auction ID: " + auctionId + "\n" +
                "Auction Name: " + auctionName + "\n" +
                "Auction Duration: " + auctionDuration + " minutes\n" +
                "Auction Status: " + (isActive ? "Active" : "Inactive") + "\n" +
                "Number of Users Required : " + (numberOfUsers == -1 ? "0" : numberOfUsers) + "\n";
    }
}
