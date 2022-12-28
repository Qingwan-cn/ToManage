import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

/**
 * @author XingYuan Guo
 */
public class SocketTransmitter {

    private Socket socket;

    public SocketTransmitter(Socket socket) {
        this.socket = socket;
    }

    public boolean send(String content){

        DataOutputStream out;
        try {
            byte[] contents = content.getBytes(StandardCharsets.UTF_8);
            out = new DataOutputStream(socket.getOutputStream());
            out.write(contents);
            out.flush();
            socket.shutdownOutput();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Socket数据的接收
     * @return 接收到的字符串信息
     * 客户端以GBK编码发送Bytes数组
     */
    public String receive(){
        InputStream is;
        try {
            is= socket.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader bf = new BufferedReader(isr);
            String temp;
            String message = "";
            while (true) {
                temp = bf.readLine();
                if (temp.contains("#ffff#")) {
                    break;
                }else{
                    message+=temp;
                }
                return message;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return e.getMessage();
        }
        return null;
    }



}
