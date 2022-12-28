/**
 * Copyright (c) 2022. XingYuan Guo
 * ToManage for Windows
 * Created by XingYuan Guo on 2022/9/30
 * OpenSource License:MIT License
 * OpenSource Download Address on Github:https://github.com/Qingwan-cn/ToManage
 *
 * @author XingYuan Guo
 * @version 1.0
 */
public class Application {
    public static void main(String[] args) {
        Thread thread = new Thread(Log::upload);
        thread.start();
        ServerListener server = new ServerListener(10001);
        System.out.println("Server已被启动。");
        server.start();
    }
}
