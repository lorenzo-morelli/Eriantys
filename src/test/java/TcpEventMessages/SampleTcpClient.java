package TcpEventMessages;

public class SampleTcpClient {

    public static void main(String[] args) {
        TcpClient sock = new TcpClient("localhost", 5000);

        // add handler
        sock.addEventHandler(new TcpClientEventHandler() {
            public void onMessage(String line) {
                System.out.println(" > " + line);
            }

            public void onOpen() {
                System.out.println("* socket connected");
            }

            public void onClose() {
                System.out.println("* socket closed");
            }
        });
        while (true) {
            sock.send("hello!!");
        }
    }
}