package com.jqorz.demo.socketencrypt.frame;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.jqorz.demo.socketencrypt.R;
import com.jqorz.demo.socketencrypt.base.BaseEventActivity;
import com.jqorz.demo.socketencrypt.service.LocalService;
import com.jqorz.demo.socketencrypt.thread.GetUserThread;
import com.jqorz.demo.socketencrypt.util.AESUtil;
import com.jqorz.demo.socketencrypt.util.ConstantUtil;
import com.jqorz.demo.socketencrypt.util.ToastUtil;
import com.jqorz.demo.socketencrypt.util.ToolUtil;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;

/**
 * 服务器界面
 */
public class Function_Socket_Server extends BaseEventActivity {
    @BindView(R.id.tv_localAddress)
    TextView tv_localAddress;
    @BindView(R.id.tv_receivedContent)
    TextView tv_receivedContent;
    @BindView(R.id.tv_decryptContent)
    TextView tv_decryptContent;
    @BindView(R.id.mSwitch)
    Switch mSwitch;
    @BindView(R.id.tv_connectedUser)
    TextView tv_connectedUser;
    boolean isBind = false;//标记是否已绑定
    private LocalService localService;//用于启动监听的服务
    private ServiceConnection sc;//服务连接
    private GetUserThread thread;

    @Override
    protected void init() {
        checkState();
        thread = new GetUserThread();
        thread.start();
        tv_localAddress.setText(ToolUtil.getHostIP());
        sc = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                isBind = true;
                LocalService.LocalBinder localBinder = (LocalService.LocalBinder) service;
                localService = localBinder.getService();
                localService.startWaitDataThread();
                ToastUtil.showToast(Function_Socket_Server.this, "监听已启动");
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                isBind = false;
                ToastUtil.showToast(Function_Socket_Server.this, "监听启动失败");
            }
        };
        connection();
    }

    private void checkState() {
        boolean isChecked = mSwitch.isChecked();
        mSwitch.setText(isChecked ? "正在监听端口" + ConstantUtil.port : "已停止监听");
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getData(String data) {
        tv_receivedContent.setText(data);
        tv_decryptContent.setText(AESUtil.decrypt(ConstantUtil.password, data));
    }

    @OnCheckedChanged(R.id.mSwitch)
    public void onChecked(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            connection();
            thread.start();
        } else {
            if (sc != null) {
                unbindService(sc);
                isBind = false;
                thread.interrupt();
            }
        }
        checkState();
    }

    /**
     * 绑定service
     */
    private void connection() {
        Intent intent = new Intent(this, LocalService.class);
        bindService(intent, sc, BIND_AUTO_CREATE);
        isBind = true;
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.function_socket_server;
    }

    @OnClick(R.id.btn_getUser)
    public void get(View v) {
        ArrayList<String> connectedIP = new ArrayList<>();

        //通过读取配置文件实现
        BufferedReader br;
        try {
            br = new BufferedReader(new FileReader(
                    "/proc/net/arp"));

            String line;

            while ((line = br.readLine()) != null) {
                String[] splitted = line.split(" +");
                if (splitted.length >= 4) {
                    String ip = splitted[0];
                    connectedIP.add(ip);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        StringBuilder resultList = new StringBuilder();

        for (String ip : connectedIP) {
            resultList.append(ip);
            resultList.append("\n");
        }
        ToastUtil.showToast(this, "ip=" + resultList.toString());

    }

    public void onDestroy() {
        if (sc != null && isBind) {
            unbindService(sc);
        }
        if (thread.isAlive()) {
            thread.interrupt();
        }
        super.onDestroy();
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void OnChanged(ArrayList<String> arrayList) {
        StringBuilder resultList = new StringBuilder();

        for (String ip : arrayList) {
            resultList.append(ip);
            resultList.append("\n");
        }
        tv_connectedUser.setText(resultList.toString());
    }
}