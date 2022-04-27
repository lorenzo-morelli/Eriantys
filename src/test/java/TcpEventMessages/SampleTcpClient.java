package TcpEventMessages;

public class SampleTcpClient{

    public static void main(String args[]) throws InterruptedException {
        TcpClient sock = new TcpClient("localhost", 5000);

        // add handler
        final TcpClient that_sock = sock;
        sock.addEventHandler(new TcpClientEventHandler(){
            public void onMessage(String line){
                System.out.println(" > "+line);
            }
            public void onOpen(){
                System.out.println("* socket connected");
            }
            public void onClose(){
                System.out.println("* socket closed");
            }
        });
        while(true){
            sock.send("hello!!");
        }
    }
}