package com.jqorz.demo.socketencrypt.thread;

import org.greenrobot.eventbus.EventBus;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;


/**
 * 此线程用于后台读取路由器下的所有用户
 */
public class GetUserThread extends Thread {

    @Override
    public void run() {
        while (true) {
            try {
                ArrayList<String> connectedIP = new ArrayList<>();

                //通过读取配置文件实现
                BufferedReader br = new BufferedReader(new FileReader(
                        "/proc/net/arp"));
                String line;
                while ((line = br.readLine()) != null) {
                    String[] splitted = line.split(" +");
                    if (splitted.length >= 4) {
                        String ip = splitted[0];
                        connectedIP.add(ip);
                    }
                }
                EventBus.getDefault().post(connectedIP);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


}
