/*
 * Copyright (c) 2022. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

import java.util.Date;

public class Test {
    public static void main(String[] args) {
        while (true){
            try {
                MailSender.send("ToManage软件为您报时。"+"\r\n"+
                        "现在是北京时间:"+new Date());
                Thread.sleep(300000L);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
}
