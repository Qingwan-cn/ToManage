import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Copyright (c) 2022. XingYuan Guo
 * ToManage for Windows
 * Created by XingYuan Guo on 2022/9/30
 * OpenSource License:MIT License
 * OpenSource Download Address on Github:https://github.com/Qingwan-cn/ToManage
 * @author XingYuan Guo
 * @version 1.0
 * 服务器监听类，继承自Thread类，作为子线程运行。
 */


public class ServerListener extends Thread{

    /**
     * 创建服务器用的端口号
     */
    int port;

    /**
     *
     * @param port 待创建的服务器端口号
     */
    public ServerListener(int port) {
        this.port=port;
    }

    /**
     * 重写Run方法，监听服务器的连接，将套接字对象传给处理器对象子线程处理
     */
    @Override
    public void run() {
        try {
            ServerSocket server = new ServerSocket(port);
            Log.log("ToManage软件创建了一个服务器:"+server);
            while (true){
                //获取到套接字连接
                Socket socket = server.accept();
                System.out.println(socket.getInetAddress()+"连接到了服务器。");
                //将套接字对象传递给子线程
                Processor processor = new Processor(socket);
                processor.start();
            }
        } catch (IOException e) {
            Log.log("[Exception]:"+e);
        }
    }
}
