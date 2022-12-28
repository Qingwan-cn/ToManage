/*
Copyright (c) 2022. XingYuan Guo
 */

import java.awt.*;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by XingYuan Guo on 2022/9/30
 * OpenSource License:MIT License
 * 开源部分的代码可根据上述许可证在https://github.com/Qingwan-cn/ToManage上获取。
 *
 * @author XingYuan Guo
 * @version 1.0
 * 处理器，继承Thread类，每个线程获得一个socket。
 */

public class Processor extends Thread {


    /**
     * 文件模式请求码
     */
    private static final int MODE_CODE_FILE = 1;
    /**
     * 指令模式请求码
     */
    private static final int MODE_CODE_DEMAND = 0;
    /**
     * 隔空投送模式请求码
     */
    private static final int MODE_CODE_DELIVER = 2;
    /**
     * 删除文件模式请求码
     */
    private static final int MODE_CODE_DEL_FILE = 3;

    /**
     * 套接字对象
     */
    Socket socket;

    /**
     * @param socket 待处理的套接字对象
     */
    public Processor(Socket socket) {
        this.socket = socket;
        Log.log("[" + socket + "]:作为套接字对象被传入处理器。");
    }

    /**
     * 重写Run方法，处理传入的套接字对象的请求
     */
    @Override
    public void run() {
        //将套接字对象传给传输器，构造传输器对象
        SocketTransmitter transmitter = new SocketTransmitter(socket);
        Log.log("[" + transmitter + "]:作为一个传输器对象被构造。");
        //接收来自客户端的消息
        String receive = transmitter.receive();
        Log.log("[Message]:" + receive + "来自于" + socket);
        //将消息打印到控制台
        System.out.println("receive = " + receive);
        /*
        开始分割字符串
        字符串组成：MODE_CODE&MESSAGE
         */
        String[] arr = receive.split("&");
        //获取到模式请求码
        String mode = arr[0];
        //获取到对应信息
        String content = arr[1];
        switch (Integer.parseInt(mode)) {
            case MODE_CODE_FILE:
                openFile(content, transmitter);
                break;
            case MODE_CODE_DELIVER:
                String fileName = content + ".jpg";
                receiveImage(fileName);
                break;
            case MODE_CODE_DEMAND:
                Runtime runtime = Runtime.getRuntime();
                try {
                    runtime.exec(content);
                } catch (IOException e) {
                    e.printStackTrace();
                    transmitter.send("ToManage软件将这条命令成功发送了，但未能得到有效的执行。");
                }
                transmitter.send("ToManage软件已发送了这个命令。");
                break;
            case MODE_CODE_DEL_FILE:
                File file = new File(content);
                if (file.exists()) {
                    System.out.println(file.delete());
                    transmitter.send(file.getAbsolutePath() + "已被ToManage软件删除");
                } else {
                    transmitter.send("ToManage软件并没有找到这个文件。");
                }
            default:
                break;
        }
    }


    public void openFile(String fileName, SocketTransmitter transmitter) {
        if (Desktop.isDesktopSupported()) {
            Desktop desktop = Desktop.getDesktop();
            File file = new File(fileName);
            try {
                if (file.exists()) {
                    desktop.open(file);
                    transmitter.send("ToManage软件成功地打开了这个文件。");
                } else {
                    transmitter.send("ToManage软件并没有找到这个文件。");
                    throw new IllegalArgumentException("文件不存在");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * 打开文件
     * @param fileName 文件名
     */
    public void openFile(String fileName) {

        if (Desktop.isDesktopSupported()) {
            Desktop desktop = Desktop.getDesktop();
            File file = new File(fileName);
            try {
                if (file.exists()) {
                    desktop.open(file);
                } else {
                    Log.log(file+"不存在，无法打开");
                    throw new IllegalArgumentException("文件不存在");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * ToManage软件图片存储路径
     */
    private static final String MY_IMAGE_PATH = "C:\\ProgramData\\ToManage\\image\\";

    /**
     * 隔空投送模式接收图片的方法
     * @param fileName 文件名
     */
    public void receiveImage(String fileName) {

        Log.log("准备接收隔空投送模式传来的图片。");

        File folder = new File(MY_IMAGE_PATH);
        if (!folder.exists()) {
            boolean b = folder.mkdir();
            Log.log("图片存储路径不存在,创建结果:"+b);
        }

        try {
            //在另一个端口开启服务
            ServerSocket serverSocket = new ServerSocket(30000);
            Log.log("在[30000]端口开启了接收服务");
            Socket imageSocket = serverSocket.accept();
            InputStream is = imageSocket.getInputStream();
            OutputStream os;
            String finalFileName = MY_IMAGE_PATH+fileName;
            Log.log("隔空投送模式接收到的文件:"+finalFileName);
            os = new FileOutputStream(finalFileName);
            byte[] byteStr = new byte[1024];
            int len;
            while ((len = is.read(byteStr)) > 0) {
                os.write(byteStr, 0, len);
            }
            is.close();
            os.flush();
            os.close();
            openFile(finalFileName);
            serverSocket.close();
            imageSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
