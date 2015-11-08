package pkukendoclub.pkukendo;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.SignUpCallback;

import pkukendoclub.pkukendo.R;

/*
    注册界面

    根据用户填写的信息，生成新的用户对象，上传服务器。
 */

public class mRegister extends ActionBarActivity {

    EditText mUsername;
    EditText mNickname;
    EditText mPassword;
    EditText mPass2;
    int      gender;                //  0 = man     1 = woman

    TextView male;
    TextView female;
    //TextView mAgreement;
    TextView Register;

    ImageButton mBack;

    View Tab1,Tab2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_m_register);

        init();

        //init
        Tab2.setBackgroundColor(0xffffffff);        // 白色
        female.setTextColor(0x20129aff);            // 蓝色 半透明

        Tab1.setBackgroundColor(0xff129aff);        // 蓝色
        male.setTextColor(0xffffffff);              // 白色
    }

    private void init(){
        mUsername = (EditText)findViewById(R.id.username_register);
        mNickname = (EditText)findViewById(R.id.nickname_register);
        mPassword = (EditText)findViewById(R.id.password_register);
        mPass2    = (EditText)findViewById(R.id.password2_register);

        Tab1 = findViewById(R.id.firstpage_tab1);
        Tab2 = findViewById(R.id.firstpage_tab2);
        male = (TextView)findViewById(R.id.male_text);
        female = (TextView)findViewById(R.id.female_text);

        //mAgreement = (TextView)findViewById(R.id.agreement_register);
        Register = (TextView)findViewById(R.id.button_register);

        mBack = (ImageButton) findViewById(R.id.back_register);


        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        /*mAgreement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(mRegister.this, Agreement.class);
                startActivity(intent);
            }
        });*/

        Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    if (mUsername.getText().toString().equals("")) { setDig("用户名为空！"); return; }
                    if (mNickname.getText().toString().equals("")) { setDig("昵称为空！"); return; }
                    if (mPassword.getText().toString().equals("")) { setDig("密码为空！"); return; }
                    if (!mPassword.getText().toString().equals(mPass2.getText().toString())) { setDig("两次密码不相符！");  return; }

                AVUser user = new AVUser();
                user.setUsername(mUsername.getText().toString());
                user.setPassword(mPassword.getText().toString());

                if (gender == 0)
                user.put("gender", "男");
                else
                user.put("gender", "女");

                user.put("NickName",mNickname.getText().toString());

                user.signUpInBackground(new SignUpCallback() {
                    public void done(AVException e) {
                        if (e == null) {
                            /*
                                这里如果成功直接跳转。
                                但是没考虑网络不好的情况，如果网速慢，点击按钮没反应。
                            */
                            Intent intent = new Intent(mRegister.this, Kendo.class);
                            startActivity(intent);
                        } else {
                            // failed
                            if (e.getCode()==AVException.INTERNAL_SERVER_ERROR)
                                setDig("网络异常！");
                            else
                            if (e.getCode()==AVException.CONNECTION_FAILED)
                                setDig("网络异常！");
                            else
                            if (e.getCode()==AVException.USERNAME_TAKEN)
                                setDig("用户名已存在！");
                            else
                                setDig("注册失败，错误码："+e.getCode());


                        }
                    }
                });


            }
        });

        Tab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Tab2.setBackgroundColor(0xffffffff);
                female.setTextColor(0x20129aff);

                Tab1.setBackgroundColor(0xff129aff);
                male.setTextColor(0xffffffff);

                gender = 0;

            }
        });

        Tab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Tab1.setBackgroundColor(0xffffffff);
                male.setTextColor(0x20129aff);

                Tab2.setBackgroundColor(0xff129aff);
                female.setTextColor(0xffffffff);

                gender = 1;

            }
        });
    }

    private void setDig(String message){
        new AlertDialog.Builder(mRegister.this)
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
        getMenuInflater().inflate(R.menu.menu_m_register, menu);
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
