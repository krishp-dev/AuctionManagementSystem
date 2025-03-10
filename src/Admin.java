

import java.util.*;
import java.sql.*;

public class Admin {
    AuctionManagmentSystem auctionManagmentSystem;
    int adminId;
    String adminName;
    String adminEmail;
    String adminPassword;

    public Admin(int adminId, String adminName, String adminEmail, String adminPassword) {
        this.adminId = adminId;
        this.adminName = adminName;
        this.adminPassword = adminPassword;
        this.adminEmail = adminEmail;
    }

    public Auction createAuction() throws Exception {
        
        Connection con = DataBaseConnection.getConnection();
        Scanner sc = new Scanner(System.in);
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
                System.out.println("Already auction id present , Enter new one ");

            } else {
                break;
            }
        }
        System.out.println("Enter auction name : ");
        String auctionName = sc.nextLine();
        System.out.println("Enter auction Duration in min (Must not be greater than 5 min)");
        int auctionDuration;
        while (true) {
            auctionDuration = sc.nextInt();
            sc.nextLine();
            if (auctionDuration > 6) {
                System.out.println("Auction Duration must be less than 5 min");
            } else if (auctionDuration < 0) {
                System.out.println("Auction Duration must not be negative");
            } else {
                break;
            }
        }
        Auction auction = new Auction(auctionId, auctionName, auctionDuration, true, -1);

        PreparedStatement pst = con.prepareStatement(
                "Insert into auctionDetails(auctionid,auctionName,auctionDuration ,isActive,numberOfUsers) values(?,?,?,?,?)");
        pst.setInt(1, auctionId);
        pst.setString(2, auctionName);
        pst.setInt(3, auctionDuration);
        pst.setBoolean(4, true);
        pst.setInt(5, -1);
        int rowAffected = pst.executeUpdate();
        if (rowAffected > 0) {
            System.out.println("Auction created successFull Whose id is : " + auctionId);
        } else {
            System.out.println("Auction creation failed ");
        }
        return auction;
    }

    public void viewAuctionDetails() throws Exception {
        Connection con = DataBaseConnection.getConnection();
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter the Auction Id : ");
        int auctionId = sc.nextInt();
        sc.nextLine();
        PreparedStatement viewAuction = con.prepareStatement("Select * from auctionDetails where auctionId=?");
        viewAuction.setInt(1, auctionId);
        ResultSet viewResultSet = viewAuction.executeQuery();
        if (!viewResultSet.next()) {
            System.out.println("Auction with " + auctionId + " doesn't exist");
        } else {
            String auctionName = viewResultSet.getString("auctionName");
            int auctionDuration = viewResultSet.getInt("auctionDuration");
            boolean isActive = viewResultSet.getBoolean("isActive");

            int numUsers = viewResultSet.getInt("numberOfUsers");

            // Displaying the auction details`
            System.out.println("-----------------------------------------------------------------------------------");
            System.out.println("Auction Details:");
            System.out.println("Auction ID: " + auctionId);
            System.out.println("Auction Name: " + auctionName);
            System.out.println("Auction Duration: " + auctionDuration + " minutes");
            System.out.println("Auction Status: " + (isActive ? "Active" : "Inactive"));

            if (numUsers == -1) {
                System.out.println("Number of Users: 0");
            } else {
                System.out.println("Number of Users: " + numUsers);
            }
            System.out.println("-----------------------------------------------------------------------------------");

        }

    }

    public void viewOngoingAndFinishedAuctions(boolean flag) throws Exception {
        Connection con = DataBaseConnection.getConnection();
        Scanner sc = new Scanner(System.in);
        PreparedStatement onGoingQuery = con.prepareStatement("Select * from auctionDetails where isActive=?");
        onGoingQuery.setBoolean(1, flag);
        ResultSet onGoingResultSet = onGoingQuery.executeQuery();
        List<Auction> auctions = new ArrayList<>();
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
            auctions.add(auction);
        }
        if (!haveValues) {
            System.out.println("NO AUCTION PRESENT");
        } else {
            System.out.println(
                    "--------------------------------------------------------------------------------------------------");

            for (Auction auction : auctions) {
                System.out.println(auction);
                System.out.println(
                        "--------------------------------------------------------------------------------------------------");

            }
        }

    }

    public void startBidding() throws Exception {
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter auction Id : ");
        int auctionId = sc.nextInt();
        sc.nextLine();
        Connection con = DataBaseConnection.getConnection();
        PreparedStatement pst1 = con.prepareStatement("SELECT * FROM auctionDetails WHERE auctionId = ?");
        pst1.setInt(1, auctionId);
        ResultSet rst1 = pst1.executeQuery();
        if (!rst1.next()) {
            System.out.println("Auction with ID " + auctionId + " doesn't exist.");
            return;
        }
        boolean isActive = rst1.getBoolean("isActive");
        if (!isActive) {
            System.out.println("Auction with ID " + auctionId + " is not active.");
            return;
        }
        String query = "SELECT COUNT(*) FROM auctionItemMapping WHERE auctionID = ?";
        PreparedStatement stmt = con.prepareStatement(query);
        stmt.setInt(1, auctionId);
        ResultSet rs = stmt.executeQuery();

        if (rs.next() && rs.getInt(1) == 0) {
            System.out.println("No items in auction.");
            return;
        }

        Auction auction = AuctionManagmentSystem.getAuctionById(auctionId);
        int auctionDuration = rst1.getInt("auctionDuration");
        int numUsersRequired = rst1.getInt("numberOfUsers");

        PreparedStatement pst2 = con
                .prepareStatement("SELECT COUNT(*) AS user_count FROM userAuctionMapping WHERE auctionId = ?");
        pst2.setInt(1, auctionId);
        ResultSet rst2 = pst2.executeQuery();
        int userCount = 0;
        if (rst2.next()) {
            userCount = rst2.getInt("user_count");
        }
        if (userCount < numUsersRequired) {
            System.out.println("Cannot start auction. Only " + userCount + " users are enrolled, but "
                    + numUsersRequired + " are required.");
            return;
        }
        User[] users = new User[userCount];
        PreparedStatement pst3 = con.prepareStatement(
                "SELECT users.userId, users.userName, users.userPassword FROM users JOIN userAuctionMapping ON users.userId = userAuctionMapping.userId WHERE userAuctionMapping.auctionId = ?");
        pst3.setInt(1, auctionId);
        ResultSet rst3 = pst3.executeQuery();

        int i = 0;
        while (rst3.next()) {
            String userName = rst3.getString("userName");
            int userId = rst3.getInt("userId");
            String userPassword = rst3.getString("userPassword");
            users[i] = new User(userName, userId, userPassword, auction);
            i++;
        }

        long startTime = System.currentTimeMillis();
        long endTime = startTime + (auctionDuration * 60) * 1000;
        int lastNumber = -1;
        while (true) {
            if (System.currentTimeMillis() >= endTime) {
                break;
            }
            int randomIndex;
            do {
                randomIndex = (int) (Math.random() * users.length);
            } while (randomIndex == lastNumber);

            lastNumber = randomIndex;
            User selectedUser = new User(users[randomIndex].getUserName(), users[randomIndex].getUserId(),
                    users[randomIndex].getUserPassword(), auction);
            System.out.println("-------------------------------------------------------------");
            selectedUser.start();
            try {
                selectedUser.join();
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        auction.DisplayWinner();
        closeAuction(auctionId);
    }

    public void closeAuction(int auctionId) throws Exception {

        Connection con = DataBaseConnection.getConnection();
        PreparedStatement checkIdExist = con.prepareStatement("Select isActive from auctionDetails where auctionId=?");
        checkIdExist.setInt(1, auctionId);
        ResultSet checkResultSet = checkIdExist.executeQuery();
        if (!checkResultSet.next()) {
            System.out.println("Auction with " + auctionId + " doesn't exist");

        } else if (checkResultSet.getBoolean("isActive") == false) {
            System.out.println("Already Closed Auction");
        } else {
            PreparedStatement pst = con.prepareStatement("Update auctionDetails set isActive=? where auctionId=?");
            pst.setBoolean(1, false);
            pst.setInt(2, auctionId);
            int rowAffected = pst.executeUpdate();
            if (rowAffected > 0) {
                System.out.println("Auction close successfully Whose id is : " + auctionId);
            } else {
                System.out.println("Something Went Wrong ");
            }
        }

    }
}
