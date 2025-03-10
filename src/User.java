

import java.util.*;
import java.sql.*;
class User extends Thread {
    String userName;
    int userId;

    String userPassword;
    Auction auction;

    public User(String userName, int userId, String userPassword) {
        this.userName = userName;
        this.userId = userId;

        this.userPassword = userPassword;
    }

    public User(String userName, int userId, String userPassword, Auction auction) {
        this.userName = userName;
        this.userId = userId;

        this.userPassword = userPassword;
        this.auction = auction;
    }

    public String getUserName() {
        return userName;
    }

    public int getUserId() {
        return userId;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public Auction getAuction() {
        return auction;
    }

    public void viewOngoingAuction() throws Exception {
        Connection con = DataBaseConnection.getConnection();
        Scanner sc = new Scanner(System.in);
        PreparedStatement onGoingQuery = con.prepareStatement("Select * from auctionDetails where isActive=true ");
        ResultSet onGoingResultSet = onGoingQuery.executeQuery();
        LList auctions = new LList();
        boolean haveValues = false;
        while (onGoingResultSet.next()) {
            haveValues = true;
            int auctionId = onGoingResultSet.getInt("auctionId");
            String auctionName = onGoingResultSet.getString("auctionName");
            int auctionDuration = onGoingResultSet.getInt("auctionDuration");
            boolean isActive = onGoingResultSet.getBoolean("isActive");
            int numUsers = onGoingResultSet.getInt("numberOfUsers");

            Auction auction = new Auction(auctionId, auctionName, auctionDuration, isActive,
                    numUsers);
            if (numUsers != -1) {
                auctions.insert(auction);
            }
        }
        if (!haveValues) {
            System.out.println("No Ongoing Auctions");
        } else {
            System.out.println(
                    "--------------------------------------------------------------------------------------------------");
            auctions.displayMethod();
        }
    }

    public void registerInAuction() throws Exception {
        Connection con = DataBaseConnection.getConnection();
        Scanner sc = new Scanner(System.in);

        String query1 = "SELECT auctionId, auctionName, auctionDuration, numberOfUsers FROM auctionDetails WHERE isActive = true";
        PreparedStatement pstmt1 = con.prepareStatement(query1);
        ResultSet rs1 = pstmt1.executeQuery();
        Map<Integer, String> auctionMap = new HashMap<>();
        System.out
                .println("-------------------------------------------------------------------------------------------");
        while (rs1.next()) {
            int auctionId = rs1.getInt("auctionId");
            String auctionName = rs1.getString("auctionName");
            int auctionDuration = rs1.getInt("auctionDuration");
            int numberOfUsers = rs1.getInt("numberOfUsers");

            if (numberOfUsers < 1) {
                continue;
            }
            auctionMap.put(auctionId, auctionName);
            String query2 = "SELECT COUNT(*) FROM userAuctionMapping WHERE auctionId = ?";
            PreparedStatement pstmt2 = con.prepareStatement(query2);
            pstmt2.setInt(1, auctionId);
            ResultSet rs2 = pstmt2.executeQuery();

            if (rs2.next()) {
                int registeredUsersCount = rs2.getInt(1);
                int remainingSlots = numberOfUsers - registeredUsersCount;
                if (remainingSlots < 1) {
                    continue;
                }
                System.out.println("Auction ID: " + auctionId);
                System.out.println("Auction Name: " + auctionName);
                System.out.println("Auction Duration: " + auctionDuration);
                System.out.println("Remaining Slots: " + remainingSlots);
                System.out.println(
                        "-------------------------------------------------------------------------------------------");

            }
        }
        if (auctionMap.isEmpty()) {
            System.out.println("No Auction Present ");
            return;
        }
        int selectedAuctionId;
        System.out.println("Enter the Auction ID you want to register for:");
        selectedAuctionId = sc.nextInt();
        sc.nextLine();
        if (auctionMap.containsKey(selectedAuctionId) && isUserAlreadyInAuction(this.userId, selectedAuctionId)) {
            String query = "INSERT INTO userAuctionMapping (userId, auctionId) VALUES (?, ?)";
            PreparedStatement insertPS = con.prepareStatement(query);
            insertPS.setInt(1, userId);
            insertPS.setInt(2, selectedAuctionId);
            int rowsAffected = insertPS.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println(
                        "User ID " + userId + " successfully registered for Auction ID " + selectedAuctionId);
            } else {
                System.out.println(
                        "Registration failed for User ID " + userId + " and Auction ID " + selectedAuctionId);
            }
        } else {
            System.out.println("User Already registered ");
        }

    }

    public boolean isUserAlreadyInAuction(int userId, int auctionId) throws Exception {
        Connection con = DataBaseConnection.getConnection();
        String query = "SELECT COUNT(*) FROM userAuctionMapping WHERE userId = ? AND auctionId = ?";
        PreparedStatement preparedStatement = con.prepareStatement(query);
        preparedStatement.setInt(1, userId);
        preparedStatement.setInt(2, auctionId);

        ResultSet resultSet = preparedStatement.executeQuery();
        resultSet.next();

        return resultSet.getInt(1) == 0;
    }

    public void registerUser() throws Exception {
        Connection con = DataBaseConnection.getConnection();
        String sql = "INSERT INTO users (userId, userName, userPassword) VALUES (?, ?, ?)";
        PreparedStatement pst = con.prepareStatement(sql);
        pst.setInt(1, this.userId);
        pst.setString(2, this.userName);
        pst.setString(3, this.userPassword);

        int rowsAffected = pst.executeUpdate();
        if (rowsAffected > 0) {
            System.out.println("User " + userName + " has been successfully enrolled ");
        } else {
            System.out.println("Failed to enroll user " + userName);
        }

    }

    public synchronized void run() {
        Scanner sc = new Scanner(System.in);

        int itemId = -1;
        String query = "SELECT itemId FROM auctionItemMapping WHERE auctionID = ?";
        double startBid = 0;
        try {
            Connection con = DataBaseConnection.getConnection();
            PreparedStatement stmt = con.prepareStatement(query);
            stmt.setInt(1, auction.getAuctionId());
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                itemId = rs.getInt("itemId");
            } else {
                System.out.println("No item found for the given auction.");
                return;
            }
            String startBidQuery = "Select startBid from item where itemId=?";
            PreparedStatement startBidPs = con.prepareStatement(startBidQuery);
            startBidPs.setInt(1, itemId);
            ResultSet startBidRs = startBidPs.executeQuery();
            if (startBidRs.next()) {
                startBid = startBidRs.getDouble("startBid");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }

        double bidAmount = 0;
        while (true) {
            System.out.println(this.userName + ", enter your bid amount: ");
            bidAmount = sc.nextDouble();
            sc.nextLine();
            if (bidAmount < startBid) {
                System.out.println("Bid must be greater than start Bid ");
            } else {
                break;
            }

        }

        synchronized (this) {
            if (bidAmount > auction.getCurrentBid()) {
                auction.setCurrentBid(bidAmount, userName);
                System.out.println(userName + " placed a bid of " + bidAmount);

                insertBidIntoDatabase(this.userId, itemId, bidAmount);
            } else {
                System.out.println(userName + " placed a bid of " + bidAmount);
                insertBidIntoDatabase(this.userId, itemId, bidAmount);
            }
        }

    }

    private void insertBidIntoDatabase(int userId, int itemId, double bidAmount) {
        String sql = "{call InsertBid(?, ?, ?)}";

        try {
            Connection con = DataBaseConnection.getConnection();
            CallableStatement cstmt = con.prepareCall(sql);
            cstmt.setInt(1, userId);
            cstmt.setInt(2, itemId);
            cstmt.setDouble(3, bidAmount);
            cstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}
