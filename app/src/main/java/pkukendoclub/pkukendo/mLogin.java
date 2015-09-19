package pkukendoclub.pkukendo;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.LogInCallback;
import com.avos.avoscloud.SignUpCallback;

import java.io.File;


public class mLogin extends ActionBarActivity {

    EditText mUsrname;
    EditText mPassword;
    ImageButton mBack;
    TextView    mLogin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_m_login);

        init();
    }

    private void init(){

        mUsrname = (EditText) findViewById(R.id.username_login);
        mPassword = (EditText) findViewById(R.id.password_login);

        mBack = (ImageButton) findViewById(R.id.back_login);
        mLogin = (TextView) findViewById(R.id.button_login);


        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mUsrname.getText().toString().equals("")) { setDig("用户名为空！"); return; }
                if (mPassword.getText().toString().equals("")) { setDig("密码为空！"); return; }

                AVUser.logInInBackground(mUsrname.getText().toString(), mPassword.getText().toString(), new LogInCallback() {
                    public void done(AVUser user, AVException e) {
                        if (user != null) {
                            // 登录成功

                            Intent intent = new Intent(mLogin.this, Kendo.class);
                            startActivity(intent);
                        } else {
                            // 登录失败
                            if (e.getCode()==AVException.INTERNAL_SERVER_ERROR)
                                setDig("网络异常！");
                            else
                            if (e.getCode()==AVException.CONNECTION_FAILED)
                                setDig("网络异常！");
                            else
                            if (e.getCode()==AVException.USERNAME_PASSWORD_MISMATCH)
                                setDig("用户名或密码错误！");
                            else
                                setDig("登陆失败，错误码："+e.getCode());
                        }
                    }
                });



            }
        });

    }


    private void setDig(String message){
        new AlertDialog.Builder(mLogin.this)
                .setTitle(message)
                .setNegativeButton("确认", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_m_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
