/*
Copyright (c) 2022. XingYuan Guo
 */

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by XingYuan Guo on 2022/9/30
 * OpenSource License:MIT License
 * 开源部分的代码可根据上述许可证在https://github.com/Qingwan-cn/ToManage上获取。
 * @author XingYuan Guo
 * @version 1.0
 */

public class Log {

    private Log(){

    }


    private static final String ROOT_PATH = "C:\\ProgramData\\ToManage\\";
    /**
     * ToManage for Windows在系统中的日志目录
     */
    private static final String LOG_PATH = "C:\\ProgramData\\ToManage\\log\\";
    /**
     * ToManage for Windows通过隔空投送模式收到的照片的存储目录
     */
    private static final String IMAGE_PATH = "C:\\ProgramData\\ToManage\\image\\";


    /**
     * 初始化目录
     */
    private static void init(){
        File log = new File(LOG_PATH);
        if (!log.exists()){
            log.mkdirs();
        }
        File image = new File(IMAGE_PATH);
        if (!image.exists()){
            image.mkdirs();
        }
    }

    /*
    日志处理逻辑：
    1.获取Date对象，初始化后得到当前日期、时间。
    2.根据当前日期生成文件名，检测文件名是否已经存在。
      若已经存在，则直接追加写入。
      若不存在，则创建文件后写入。
    3.写入的内容作为形式参数传入。

    日志记录格式：
    yyyy-MM-dd HH:mm:ss 处理内容
     */
    /**
     * 日志写入方法
     * @param content 写入的文件内容
     */
    public static void log(String content){
        init();
        Date date = new Date();
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMMdd");
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String fileName = LOG_PATH+sdf1.format(date)+".log";
        File file = new File(fileName);
        String finalContent = sdf2.format(date)+content;
        write(file,finalContent,0);
    }

    /**
     * 一般性写入
     * @param file     文件对象
     * @param content  文件内容
     * @param mode     换行模式
     * @return         写入结果
     */
    private static boolean write(File file, String content, int mode) {
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException var10) {
                var10.printStackTrace();
                throw new IllegalArgumentException("文件不存在,在尝试创建时出现异常.");
            }
        }

        FileWriter fileWriter;
        try {
            fileWriter = new FileWriter(file.getAbsoluteFile(), true);
        } catch (IOException var9) {
            var9.printStackTrace();
            return false;
        }

        BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

        try {
            if (mode == 0) {
                bufferedWriter.write(content + "\r\n");
            } else {
                if (mode != 1) {
                    throw new IllegalArgumentException("写入模式异常.");
                }

                bufferedWriter.write(content);
            }
        } catch (IOException var8) {
            var8.printStackTrace();
        }

        try {
            bufferedWriter.close();
        } catch (IOException var7) {
            var7.printStackTrace();
        }

        return true;
    }

    public static void upload(){
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String dateToday = sdf.format(date);
        System.out.println("dateToday = " + dateToday);
        long date1 = Long.parseLong(dateToday);
        long date2 = date1-1L;
        long date3 = date2-1L;
        long date4 = date3-1L;
        File[] files = new File[4];
        files[0] =  new File(LOG_PATH+date2 +".log");
        files[1] =  new File(LOG_PATH+date3 +".log");
        files[2] =  new File(LOG_PATH+date4 +".log");
        files[3] =  new File(LOG_PATH+date1 +".log");
        for (File file: files) {
            if (file.exists()){

                String logContent = ReaderUtils.read(file.getAbsolutePath());
                try {
                    MailSender.send(logContent);
                    Log.log("尝试上传日志。");
                } catch (Exception e) {
                    Log.log(e.getMessage());
                }
                break;
            }
        }
    }

}
