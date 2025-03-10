
class LList {
    class Node {
        Node next;
        Auction auction;

        Node(Auction auction) {
            this.auction = auction;
        }
    }

    Node head = null;

    public void insert(Auction auction) {
        if (head == null) {
            Node n = new Node(auction);
            head = n;
        } else {
            Node n = new Node(auction);
            Node curr = head;
            while (curr.next != null) {
                curr = curr.next;

            }
            curr.next = n;
        }
    }

    public void displayMethod() {
        if (head == null) {
            System.out.println("No Ongoing auctions");

        } else {
            Node curr = head;
            while (curr != null) {
                System.out.println(curr.auction);
                curr = curr.next;
                System.out.println(
                        "--------------------------------------------------------------------------------------------------");

            }
        }
    }
}
