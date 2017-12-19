package com.jqorz.demo.socketencrypt.frame;

import android.content.Intent;
import android.view.View;
import android.widget.Button;

import com.jqorz.demo.socketencrypt.R;
import com.jqorz.demo.socketencrypt.base.BaseActivity;

import butterknife.BindView;
import butterknife.OnClick;

public class App_Main extends BaseActivity {


    @Override
    protected void init() {

    }
@OnClick({R.id.btn_Server,R.id.btn_Client})
public void OnClick(View view){
    switch (view.getId()){
        case R.id.btn_Client:
            startActivity(new Intent(this,Function_Socket_Client.class));
            break;
        case R.id.btn_Server:
            startActivity(new Intent(this,Function_Socket_Server.class));
            break;
    }
}
    @Override
    protected int getLayoutResId() {
        return R.layout.app_main;
    }
}
