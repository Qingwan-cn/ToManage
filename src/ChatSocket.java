import java.awt.*;
import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

/**
 * @author XingYuan Guo
 */
public class ChatSocket extends Thread{
    Socket socket;

    public ChatSocket(Socket socket) {
        this.socket=socket;
    }

    @Override
    public void run() {
        try{
            InputStream is = socket.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader bf = new BufferedReader(isr);
            String demand;
            while ((demand = bf.readLine()) != null) {
                System.out.println(demand);
                if (demand.contains("file")){
                    String fileName = demand.replace("file","").trim();
                    System.out.println(fileName);
                    openFile(fileName);
                }else if (demand.contains("exe")&&!(demand.contains("taskkill"))){
                    System.out.println(demand);
                    openFile(demand);
                }else{
                    Runtime runtime = Runtime.getRuntime();
                    runtime.exec(demand);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }
    public  void openFile(String fileName) {
        if (Desktop.isDesktopSupported()){
            Desktop desktop = Desktop.getDesktop();
            File file = new File(fileName);
            try {
                if (file.exists()){
                    desktop.open(file);
                }else{
                    throw new IllegalArgumentException("文件不存在");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
