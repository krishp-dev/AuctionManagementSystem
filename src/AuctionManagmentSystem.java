import java.util.*;
import java.sql.*;
public class AuctionManagmentSystem {
    public static void handleAdmin() throws Exception {
        Connection con = DataBaseConnection.getConnection();
        Scanner sc = new Scanner(System.in);
        int adminId;
        while (true) {
            System.out.println("Enter the auctioneer id : ");
            adminId = sc.nextInt();
            sc.nextLine();

            String query = "SELECT COUNT(*) FROM admin WHERE adminId  = ?";
            PreparedStatement countQueryPs = con.prepareStatement(query);
            countQueryPs.setInt(1, adminId);
            ResultSet rs = countQueryPs.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) {
    
                break;
            } else {
                System.out.println("Admin Id not found ");
            }
        }
        System.out.println("Enter the auctioneer email : ");
        String email = sc.nextLine().toLowerCase();

        if (email.contains("@")) {
            String partBefore = email.substring(0, email.indexOf("@"));
            email = partBefore + "@gmail.com";
        } else {
            email = email + "@gmail.com";
        }
        System.out.println("Enter the auctioneer password");
        String password = sc.nextLine();

        PreparedStatement pst = con.prepareStatement("Select * from admin where adminId=?");
        pst.setInt(1, adminId);
        ResultSet rst = pst.executeQuery();
        Boolean equals = true;
        String auctioneerName = "";
        while (rst.next()) {
            auctioneerName = rst.getString("adminName");
            if (!email.equals(rst.getString("adminEmail"))) {
                System.out.println("Email is wrong");
                equals = false;
                break;
            } else if (!(password.equals(rst.getString("adminPassword")))) {
                System.out.println("Password is wrong ");
                equals = false;
                break;
            }

        }
        if (equals) {
            boolean flag = true;
            while (flag) {
                Admin admin = new Admin(adminId, auctioneerName, email, password);

                System.out.println(
                        "Enter \n1.Create Auction\n2.Start Bidding \n3.View Auction\n4.View Ongoing Auctions\n5.View Finished Auctions\n6.Close Auction\n7.Exit : ");
                int choose = sc.nextInt();
                sc.nextLine();
                switch (choose) {
                    case 1:
                        System.out.println("Auction Creation ..");
                        admin.createAuction();
                        break;
                    case 2:
                        System.out.println("Starting Bidding ...");
                        admin.startBidding();
                        break;

                    case 3:
                        admin.viewAuctionDetails();
                        break;

                    case 4:
                        System.out.println("Displaying All Ongoing Auctions : ");
                        admin.viewOngoingAndFinishedAuctions(true);
                        break;

                    case 5:
                        System.out.println("Displaying All Finished Auctions : ");
                        admin.viewOngoingAndFinishedAuctions(false);
                        break;
                    case 6:
                        System.out.println("Enter Auction Id :");
                        int auctionId = sc.nextInt();
                        sc.nextLine();

                        admin.closeAuction(auctionId);
                        break;
                    case 7:
                        System.out.println("Exiting..");
                        flag = false;
                        break;

                    default:
                        System.out.println("Invalid input");
                        break;
                }

            }

        }

    }

    public static void handleSeller() throws Exception {
        Scanner sc = new Scanner(System.in);
        boolean flag = true;

        while (flag) {
            System.out.println("Enter \n1.Register As Seller,\n2.Add item\n3.Update Item\n4.Exit");
            int choose = sc.nextInt();
            sc.nextLine();

            switch (choose) {
                case 1:
                    Seller seller = new Seller();
                    seller.registerAsSeller();
                    break;
                case 2:
                    Seller sellerForAdd = authenticateSeller();
                    if (sellerForAdd != null) {
                        sellerForAdd.addItemToAuction(sellerForAdd.getSellerId());
                    } else {
                        System.out.println("Seller Not found");
                    }
                    break;

                case 3:
                    Seller sellerForUpdate = authenticateSeller();
                    if (sellerForUpdate != null) {
                        sellerForUpdate.updateItem(sellerForUpdate.getSellerId());
                    } else {
                        System.out.println("Seller Not found");
                    }
                    break;

                case 4:
                    System.out.println("Exiting ..");
                    flag = false;
                    break;

                default:
                    System.out.println("Invalid input, Enter a valid input");
                    break;
            }
        }

    }

    public static Seller authenticateSeller() throws Exception {
        Scanner sc = new Scanner(System.in);
        Connection con = DataBaseConnection.getConnection();
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
                break;
            } else {
                System.out.println("Seller ID not exist ");
            }
        }
        System.out.println("Enter Seller Email : ");
        String sellerEmail = sc.nextLine();
        if (sellerEmail.contains("@")) {
            String partBefore = sellerEmail.substring(0, sellerEmail.indexOf("@"));
            sellerEmail = partBefore + "@gmail.com";
        } else {
            sellerEmail = sellerEmail + "@gmail.com";
        }
        System.out.println("Enter the Seller password: ");
        String password = sc.nextLine();

        String query = "SELECT sellerName, sellerEmail, sellerPassword FROM sellerDetails WHERE sellerId = ?";

        PreparedStatement pstmt = con.prepareStatement(query);
        pstmt.setInt(1, sellerId);

        ResultSet rs = pstmt.executeQuery();

        if (rs.next()) {
            String dbName = rs.getString("sellerName");
            String dbEmail = rs.getString("sellerEmail");
            String dbPassword = rs.getString("sellerPassword");
            if (dbEmail.equals(sellerEmail) && dbPassword.equals(password)) {
                Seller seller = new Seller(sellerId, dbName, dbEmail, dbPassword);
                return seller;
            } else {
                return null;
            }
        }
        return null;

    }

    public static void handleUser() throws Exception {
        Scanner sc = new Scanner(System.in);
        Connection con = DataBaseConnection.getConnection();
        boolean flag = true;

        while (flag) {
            System.out.println("Enter Choice :\n1.SignUp\n2.LogIn\n3.Exit ");
            int choice = sc.nextInt();
            sc.nextLine();
            if (choice == 1) {
                signUp();
            } else if (choice == 2) {
                logIn();
            } else if (choice == 3) {
                System.out.println("Exiting..");
                break;
            } else {
                System.out.println("Invalid Choice ");
            }

        }

    }

    public static void signUp() throws Exception {
        System.out.println("Sign Up..");
        Scanner sc = new Scanner(System.in);
        Connection con = DataBaseConnection.getConnection();
        int userId;
        while (true) {
            System.out.println("Enter User Id ");
            userId = sc.nextInt();
            sc.nextLine();

            String query = "SELECT COUNT(*) FROM users WHERE userId = ?";
            PreparedStatement preparedStatement = con.prepareStatement(query);
            preparedStatement.setInt(1, userId);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next() && resultSet.getInt(1) == 0) {

                break;
            } else {
                System.out.println("User ID " + userId + " exists.");
            }
        }
        System.out.println("Enter User Name : ");
        String userName = sc.nextLine();
        System.out.println("Enter User Password : ");
        String userPassword = sc.nextLine();
        User user = new User(userName, userId, userPassword);
        user.registerUser();
        logIn();

    }

    public static void logIn() throws Exception {
        System.out.println("Log In ..");
        Scanner sc = new Scanner(System.in);
        Connection con = DataBaseConnection.getConnection();
        int userId;
        while (true) {
            System.out.println("Enter User Id ");
            userId = sc.nextInt();
            sc.nextLine();

            String query = "SELECT COUNT(*) FROM users WHERE userId = ?";
            PreparedStatement preparedStatement = con.prepareStatement(query);
            preparedStatement.setInt(1, userId);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next() && resultSet.getInt(1) > 0) {
                break;
            } else {
                System.out.println("User ID " + userId + " does not exist.");
            }
        }

        System.out.println("Enter user Password: ");
        String userPassword = sc.nextLine();

        String passwordQuery = "SELECT userName FROM users WHERE userId = ? AND userPassword = ?";
        PreparedStatement passwordStatement = con.prepareStatement(passwordQuery);
        passwordStatement.setInt(1, userId);
        passwordStatement.setString(2, userPassword);
        ResultSet passwordResult = passwordStatement.executeQuery();

        if (passwordResult.next()) {
            String userName = passwordResult.getString("userName");
            User user = new User(userName, userId, userPassword);
            boolean flag = true;
            while (flag) {
                System.out.println("Enter Choice :\n1.View Ongoing Auction\n2.Register in Auction\n3.Exit");
                int choice = sc.nextInt();
                sc.nextLine();
                switch (choice) {
                    case 1:
                        user.viewOngoingAuction();
                        break;
                    case 2:
                        user.registerInAuction();
                        break;
                    case 3:
                        System.out.println("Exiting..");
                        flag = false;
                        break;
                    default:
                        System.out.println("Enter a Valid Option ");
                        break;
                }
            }
        } else {
            System.out.println("Incorrect password for User ID " + userId);
        }

    }

    public static Auction getAuctionById(int auctionId) throws Exception {
        Auction auction = null;

        Connection con = DataBaseConnection.getConnection();
        PreparedStatement pst = con.prepareStatement("SELECT * FROM auctionDetails WHERE auctionId = ?");
        pst.setInt(1, auctionId);
        ResultSet rs = pst.executeQuery();

        if (rs.next()) {
            String auctionName = rs.getString("auctionName");
            int auctionDuration = rs.getInt("auctionDuration");
            boolean isActive = rs.getBoolean("isActive");
            int numUser = rs.getInt("numberOfUsers");

            auction = new Auction(auctionId, auctionName, auctionDuration, isActive, numUser);
        }

        return auction;
    }

    public static void main(String[] args) throws Exception {

        Scanner sc = new Scanner(System.in);
        boolean flag = true;
        while (flag) {
            System.out.println("Choose an option:\n1.Auctioneer\n2.Seller\n3.Buyers\n4.Exit");
            int choice = sc.nextInt();
            sc.nextLine();
            switch (choice) {
                case 1:
                    handleAdmin();
                    break;
                case 2:
                    handleSeller();
                    break;
                case 3:
                    handleUser();
                    break;
                case 4:
                    System.out.println("Exiting...");
                    sc.close();
                    flag = false;
                    break;

                default:
                    System.out.println("Invalid choice. Please try again.");
                    break;
            }
        }
    }
}
