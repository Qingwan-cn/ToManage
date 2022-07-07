import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author XingYuan Guo
 */
public class ServerListener extends Thread{

    private final int port = 10001;

    @Override
    public void run() {
        try {
            ServerSocket serverSocket = new ServerSocket(port);
            while (true) {
                Socket socket = serverSocket.accept();
                //显示连接到服务器的IP
                System.out.println("[IP of Client]: "+socket.getInetAddress());
                //传递套接字Socket实例给子线程
                ChatSocket cs = new ChatSocket(socket);
                cs.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
