package com.waka.workspace.smalldianping.Activity.Main.Fragment;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.waka.workspace.smalldianping.Activity.Main.MainActivity;
import com.waka.workspace.smalldianping.DataBase.User;
import com.waka.workspace.smalldianping.DataBase.UserManager;
import com.waka.workspace.smalldianping.R;

/**
 * 登录
 * Created by waka on 2015/12/22.
 */
public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "LoginActivity";
    private Toolbar mToolbar;
    private EditText etUserName,etPassword;
    private  UserManager userManager;

    /**
     * onCreate
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initView();
        initData();
        initEvent();
        userManager= new UserManager(this);
        userManager.add(new User(0, "123", "123"));
    }

    private void initView() {
        //设置Toolbar
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle(getString(R.string.activity_register_title));
        findViewById(R.id.btnLogin).setOnClickListener(this);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        etPassword= (EditText) findViewById(R.id.etPassword);
        etUserName= (EditText) findViewById(R.id.etUserName);

    }

    private void initData() {

    }

    private void initEvent() {

    }

    /**
     * 标题栏按钮选中事件处理
     *
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            //如果是按返回按钮
            case android.R.id.home:
                finish();
                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);//由左向右滑入的效果
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 监听返回键
     *
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                finish();
                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);//由左向右滑入的效果
                break;
        }
        //继续执行父类的其他点击事件
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnLogin:
                User user=    new User(0,etUserName.getText().toString(),etPassword.getText().toString());
                userManager.add(user);

                    Log.i("debug:", "注册");
                    Toast.makeText(this,"注册成功",Toast.LENGTH_SHORT).show();
                    MainActivity.login_user_id=user.getName();
                setResult(0x10021);
                    this.finish();



                break;
        }
    }
}
