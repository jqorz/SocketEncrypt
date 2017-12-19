package com.jqorz.demo.socketencrypt.frame;

import android.app.ProgressDialog;
import android.util.Log;
import android.view.View;
import android.widget.EditText;


import com.jqorz.demo.socketencrypt.R;
import com.jqorz.demo.socketencrypt.base.BaseEventActivity;
import com.jqorz.demo.socketencrypt.thread.SendDataThread;
import com.jqorz.demo.socketencrypt.util.AESUtil;
import com.jqorz.demo.socketencrypt.util.ConstantUtil;
import com.jqorz.demo.socketencrypt.util.ToastUtil;
import com.jqorz.demo.socketencrypt.util.ToolUtil;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.OnClick;


/**
 * 客户端界面
 */
public class Function_Socket_Client extends BaseEventActivity {
    @BindView(R.id.edtTxt_Content)
    EditText edtTxt_Content;
    @BindView(R.id.edtTxt_serverAddress)
    EditText edtTxt_serverAddress;

    private ProgressDialog mProgressDialog;//加载的小菊花

    /**
     * 初始化
     */
    @Override
    protected void init() {
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.function_socket_client;
    }


    @OnClick(R.id.btn_encryptAndSend)
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_encryptAndSend:
                String s = edtTxt_Content.getText().toString().trim();
                String ip = edtTxt_serverAddress.getText().toString().trim();
                if (ToolUtil.IsIpv4(ip)) {
                    new SendDataThread(ip, AESUtil.encrypt(ConstantUtil.password, s), ConstantUtil.port).start();//消息发送方启动线程发送消息
                    showProgressDialog("尝试发送数据到\n\t\t" + ip, true);
                } else {
                    ToastUtil.showToast(this, "IP不合法!");
                }
                break;
        }
    }

    /**
     * 连接结果
     *
     * @param resultCode 0：连接超时；1：发送成功 2:失败
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void sendResult(Integer resultCode) {
        Log.i("succ", "=" + resultCode);
        dismissProgressDialog();
        switch (resultCode) {
            case ConstantUtil.CODE_SUCCESS:
                ToastUtil.showToast(this, "发送成功");
                break;
            case ConstantUtil.CODE_TIMEOUT:
                ToastUtil.showToast(this, "连接超时");
                break;
            case ConstantUtil.CODE_UNKNOWN_HOST:
                ToastUtil.showToast(this, "错误-未知的host");
                break;

        }

    }

    /**
     * 数据加载小菊花
     *
     * @param msg      内容
     * @param isCancel 是否允许关闭 true - 允许  false - 不允许
     */
    public void showProgressDialog(final String msg, final boolean isCancel) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    if (mProgressDialog == null) {
                        mProgressDialog = new ProgressDialog(Function_Socket_Client.this);
                    }
                    if (mProgressDialog.isShowing()) {
                        return;
                    }
                    mProgressDialog.setMessage(msg);
                    mProgressDialog.setCancelable(isCancel);
                    mProgressDialog.setCanceledOnTouchOutside(false);
                    mProgressDialog.setOnCancelListener(null);
                    mProgressDialog.show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 隐藏数据加载的进度小菊花
     **/
    public void dismissProgressDialog() {

        try {
            if (mProgressDialog != null && mProgressDialog.isShowing()) {
                runOnUiThread(
                        new Runnable() {
                            @Override
                            public void run() {
                                mProgressDialog.dismiss();
                            }
                        }
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



}
