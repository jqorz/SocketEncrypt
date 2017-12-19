package com.jqorz.demo.socketencrypt.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import com.jqorz.demo.socketencrypt.thread.ListenThread;
import com.jqorz.demo.socketencrypt.util.ConstantUtil;


/**
 * 此服务用于启动监听线程
 */
public class LocalService extends Service {
    private IBinder iBinder = new LocalService.LocalBinder();

    @Override
    public IBinder onBind(Intent intent) {
        return iBinder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    public void startWaitDataThread() {
        new ListenThread(ConstantUtil.port).start();
    }

    //定义内容类继承Binder
    public class LocalBinder extends Binder {
        //返回本地服务
        public LocalService getService() {
            return LocalService.this;
        }
    }
}