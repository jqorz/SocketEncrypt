package com.jqorz.demo.socketencrypt.util;

import android.content.Context;
import android.widget.Toast;

public class ToastUtil {


    private static Toast mToast = null;

    /**
     * Toast方法
     *
     * @param text    需要展示的文本
     * @param context 所需上下文
     */
    public static void showToast(Context context, String text) {
        if (text != null) {
            if (mToast == null) {
                mToast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
            } else {
                mToast.setText(text);
                mToast.setDuration(Toast.LENGTH_SHORT);
            }
            mToast.show();
        }
    }

}
