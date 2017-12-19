package com.jqorz.demo.socketencrypt.thread;

import android.util.Log;

import com.jqorz.demo.socketencrypt.util.ConstantUtil;

import org.greenrobot.eventbus.EventBus;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

/**
 * 数据发送线程
 */
public class SendDataThread extends Thread {
    private Socket socket;
    private String ip;//接收方的IP
    private int port;//接收方的端口号
    private String data;//准备发送的数据

    public SendDataThread(String ip, String data, int port) {
        this.ip = ip;
        this.data = data;
        this.port = port;
    }

    @Override
    public void run() {
        try {
            socket = new Socket();
            socket.connect(new InetSocketAddress(ip, port), ConstantUtil.TIME_MILLIS);//设置超时时间
        } catch (UnknownHostException e) {
            EventBus.getDefault().post(ConstantUtil.CODE_UNKNOWN_HOST);
            Log.d("error", "SendDataThread.init() has UnknownHostException" + e.getMessage());
        } catch (SocketTimeoutException e) {
            EventBus.getDefault().post(ConstantUtil.CODE_TIMEOUT);
            Log.d("error", "SendDataThread.init() has TimeoutException:" + e.getMessage());
        } catch (IOException e) {
            Log.d("error", "SendDataThread.init() has IOException:" + e.getMessage());
        }
        if (socket != null && socket.isConnected()) {
            try {
                OutputStream ops = socket.getOutputStream();
                OutputStreamWriter opsw = new OutputStreamWriter(ops);
                BufferedWriter writer = new BufferedWriter(opsw);
                writer.write(data + "\r\n\r\n");//由于socket使用缓冲区进行读写数据，因此使用\r\n\r\n用于表明数据已写完.不加这个会导致数据无法发送
                EventBus.getDefault().post(ConstantUtil.CODE_SUCCESS);
                writer.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
