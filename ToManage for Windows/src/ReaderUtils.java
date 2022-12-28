/*
 * Copyright (c) 2022. XingYuan Guo
 * ToManage for Windows
 * Created by XingYuan Guo on 2022/10/1
 * OpenSource License:MIT License
 * OpenSource Download Address on Github:https://github.com/Qingwan-cn/ToManage
 * @author XingYuan Guo
 * @version 1.0
 */

import java.io.*;

public class ReaderUtils {
    public ReaderUtils() {
    }

    public static String read(String fileName) {
        File file = new File(fileName);
        if (!file.exists()){
            throw new IllegalArgumentException("读取错误：文件不存在");
        }else{
            FileInputStream fis = null;
            try {
                fis = new FileInputStream(file);
                InputStreamReader isr = new InputStreamReader(fis,"utf-8");
                BufferedReader br = new BufferedReader(isr);

                String result = "";
                String line;
                while((line = br.readLine()) != null){
                    //process the line
                    result += (line+"\r\n");;
                }
                br.close();
                return result;
            } catch (IOException e) {
                e.printStackTrace();
                return "文件读取错误:"+e.getMessage();
            }
        }
    }
}
