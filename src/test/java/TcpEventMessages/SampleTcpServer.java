package TcpEventMessages;

public class SampleTcpServer{

    public static void main(String[] args) {
        TcpServer server = new TcpServer(5000);

        // add event handler, response to client
        final TcpServer that_server = server;
        server.addEventHandler(new TcpServerEventHandler(){
            public void onMessage(int client_id, String line){
                System.out.println("* <"+client_id+"> "+ line);
                that_server.getClient(client_id).send("echo : <"+client_id+"> "+line);
            }
            public void onAccept(int client_id){
                System.out.println("* <"+client_id+"> connection accepted");
            }
            public void onClose(int client_id){
                System.out.println("* <"+client_id+"> closed");
            }
        });

        server.listen();

        server.close();
    }
}