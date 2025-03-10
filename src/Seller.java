

import java.util.*;
import java.sql.*;
class Seller {
    int sellerId;
    String sellerName;
    String email;
    String password;

    Seller() {

    }

    public Seller(int sellerId, String sellerName, String email, String password) {
        this.sellerId = sellerId;
        this.sellerName = sellerName;
        this.email = email;
        this.password = password;
    }

    public void registerAsSeller() throws Exception {
        Connection con = DataBaseConnection.getConnection();
        Scanner sc = new Scanner(System.in);

        int sellerId;
        while (true) {
            System.out.println("Enter Seller ID: ");
            sellerId = sc.nextInt();
            sc.nextLine();

            String query = "SELECT COUNT(*) FROM sellerDetails WHERE sellerId =?;";

            PreparedStatement countQueryPs = con.prepareStatement(query);
            countQueryPs.setInt(1, sellerId);
            ResultSet rs = countQueryPs.executeQuery();

            if (rs.next() && rs.getInt(1) > 0) {
                System.out.println("Seller ID already exists. Please enter a different Seller ID.");
            } else {
                break;
            }
        }

        System.out.println("Enter Seller Name : ");
        String sellerName = sc.nextLine();
        System.out.println("Enter Seller Email : ");
        String sellerEmail = sc.nextLine();
        if (sellerEmail.contains("@")) {
            String partBefore = sellerEmail.substring(0, sellerEmail.indexOf("@"));
            sellerEmail = partBefore + "@gmail.com";
        } else {
            sellerEmail = sellerEmail + "@gmail.com";
        }

        System.out.println("Enter seller Password : ");
        String password = sc.nextLine();

        Seller seller = new Seller(sellerId, sellerName, sellerEmail, password);
        PreparedStatement pst = con.prepareStatement("Insert into sellerDetails values(?,?,?,?)");
        pst.setInt(1, sellerId);
        pst.setString(2, sellerName);
        pst.setString(3, sellerEmail);
        pst.setString(4, password);
        int rowAffected = pst.executeUpdate();
        if (rowAffected > 0) {
            System.out.println("Seller Registered Successfully ..");
        } else {
            System.out.println("Something Went wrong Try again ...");
        }

    }

    public void addItemToAuction(int sellerId) throws Exception {

        Scanner sc = new Scanner(System.in);
        Connection con = DataBaseConnection.getConnection();

        int auctionId;

        while (true) {
            System.out.println("Enter the auction id : ");
            auctionId = sc.nextInt();
            sc.nextLine();

            String query = "SELECT COUNT(*) FROM auctionDetails WHERE auctionId = ?";
            PreparedStatement countQueryPs = con.prepareStatement(query);
            countQueryPs.setInt(1, auctionId);
            ResultSet rs = countQueryPs.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) {
                break;
            } else {
                System.out.println("Auction Id doesn`t Exist , Enter new one ");
            }
        }
        PreparedStatement checkActivePS = con.prepareStatement("Select isActive from auctionDetails where auctionId=?");
        checkActivePS.setInt(1, auctionId);
        ResultSet checkActiveResultSet = checkActivePS.executeQuery();

        if (checkActiveResultSet.next() && checkActiveResultSet.getBoolean("isActive") == false) {
            System.out.println("Already Closed Auction");
            return;
        }

        String checkAuctionQuery = "SELECT COUNT(*) FROM AuctionItemMapping WHERE auctionID = ?";
        PreparedStatement checkAuctionStmt = con.prepareStatement(checkAuctionQuery);
        checkAuctionStmt.setInt(1, auctionId);
        ResultSet checkAuctionResultSet = checkAuctionStmt.executeQuery();

        if (checkAuctionResultSet.next() && checkAuctionResultSet.getInt(1) > 0) {
            System.out.println("Item already exists in this auction. Please choose a different auction.");
            return;
        }
        System.out.println("Enter New Item Details : ");
        int itemId;
        while (true) {
            System.out.println("Enter the item ID: ");
            itemId = sc.nextInt();
            sc.nextLine();

            String checkItemIdQuery = "SELECT COUNT(*) FROM Item WHERE itemId = ?";
            PreparedStatement checkItemIdStmt = con.prepareStatement(checkItemIdQuery);
            checkItemIdStmt.setInt(1, itemId);
            ResultSet itemIdResult = checkItemIdStmt.executeQuery();
            if (itemIdResult.next() && itemIdResult.getInt(1) > 0) {
                System.out.println("Item ID already exists. Please enter a different item ID.");
            } else {
                break;
            }
        }
        System.out.println("Enter the item name: ");
        String itemName = sc.nextLine();
        System.out.println("Enter the starting bid amount: ");
        double startingBid = sc.nextDouble();
        sc.nextLine();
        int numberOfUsers;
        while (true) {
            System.out.println("Enter the number of users for this auction: ");
            numberOfUsers = sc.nextInt();
            sc.nextLine();

            if (numberOfUsers >= 2) {
                break;
            } else {
                System.out.println("Invalid input. Please enter exactly More than 1 ");
            }
        }

        String insertItemQuery = "INSERT INTO Item (itemId ,itemName, auctionId,startBid,sellerId) VALUES (?, ?, ?,?,?)";
        PreparedStatement insertItemPs = con.prepareStatement(insertItemQuery);
        insertItemPs.setInt(1, itemId);
        insertItemPs.setString(2, itemName);
        insertItemPs.setInt(3, auctionId);

        insertItemPs.setDouble(4, startingBid);
        insertItemPs.setInt(5, sellerId);

        int rowAffected = insertItemPs.executeUpdate();
        if (rowAffected > 0) {
            System.out.println("Item insert successfully ");
        } else {
            System.out.println("Something went wrong ");
        }

        String insertMappingQuery = "INSERT INTO AuctionItemMapping (auctionID, sellerID, itemID) VALUES (?, ?, ?)";
        PreparedStatement insertMappingStmt = con.prepareStatement(insertMappingQuery);
        insertMappingStmt.setInt(1, auctionId);
        insertMappingStmt.setInt(2, sellerId);
        insertMappingStmt.setInt(3, itemId);
        insertMappingStmt.executeUpdate();

        String updateUsersQuery = "UPDATE auctionDetails SET numberOfUsers = ? WHERE auctionID = ?";
        PreparedStatement updateUsersStmt = con.prepareStatement(updateUsersQuery);
        updateUsersStmt.setInt(1, numberOfUsers);
        updateUsersStmt.setInt(2, auctionId);
        updateUsersStmt.executeUpdate();

        System.out.println("Item successfully added to the auction.");

    }

    public void updateItem(int sellerId) throws Exception {
        Scanner sc = new Scanner(System.in);
        Connection con = DataBaseConnection.getConnection();

        int auctionId;
        while (true) {
            System.out.println("Enter the auction id where you have to update: ");
            auctionId = sc.nextInt();
            sc.nextLine();

            String query = "SELECT COUNT(*) FROM auctionDetails WHERE auctionID = ?";
            PreparedStatement countQueryPs = con.prepareStatement(query);
            countQueryPs.setInt(1, auctionId);
            ResultSet rs = countQueryPs.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) {
                break;
            } else {
                System.out.println("Invalid auction ID. Please try again.");
            }
        }

        PreparedStatement pst = con.prepareStatement("Select * from auctionDetails Where auctionId=?");
        pst.setInt(1, auctionId);
        ResultSet st = pst.executeQuery();
        if (st.next() && st.getBoolean("isActive") == false) {
            System.out.println("Auction is already closed !!");
        } else {

            String query = "SELECT itemId, itemName FROM item WHERE auctionID = ? AND sellerID = ?";
            PreparedStatement itemPs = con.prepareStatement(query);
            itemPs.setInt(1, auctionId);
            itemPs.setInt(2, sellerId);
            ResultSet itemResultSet = itemPs.executeQuery();
            if (itemResultSet.next()) {
                int itemId = itemResultSet.getInt("itemId");
                String itemName = itemResultSet.getString("itemName");

                System.out.println("Item found: " + itemName);
                System.out.println("Do you want to change the item name? (yes/no)");
                String response = sc.nextLine();

                if ("yes".equalsIgnoreCase(response)) {
                    System.out.println("Enter new item name: ");
                    String newItemName = sc.nextLine();

                    String updateQuery = "UPDATE item SET itemName = ? WHERE itemID = ?";
                    PreparedStatement updatePstmt = con.prepareStatement(updateQuery);
                    updatePstmt.setString(1, newItemName);
                    updatePstmt.setInt(2, itemId);
                    updatePstmt.executeUpdate();
                    System.out.println("Item name updated successfully.");

                } else {
                    System.out.println("Item name not changed.");
                }

                System.out.println("Do you want to change start bid ? (yes/no)");
                String startBidding = sc.nextLine();
                if ("yes".equalsIgnoreCase(startBidding)) {
                    System.out.println("Enter new start bid amount: ");
                    double newStartBid = sc.nextDouble();
                    sc.nextLine();
                    String updateBidQuery = "UPDATE item SET startBid = ? WHERE itemID = ?";
                    PreparedStatement updateBidPstmt = con.prepareStatement(updateBidQuery);
                    updateBidPstmt.setDouble(1, newStartBid);
                    updateBidPstmt.setInt(2, itemId);
                    updateBidPstmt.executeUpdate();
                    System.out.println("Start bid updated successfully.");
                } else {
                    System.out.println("Start bid not changed.");
                }

            } else {
                System.out.println("No item found with the given auction ID and seller ID.");
            }

        }

    }

    public int getSellerId() {
        return sellerId;
    }

    public String getSellerName() {
        return sellerName;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

}
