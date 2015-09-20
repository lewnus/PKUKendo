package pkukendoclub.pkukendo;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.UpdatePasswordCallback;


public class mPassword extends ActionBarActivity {

    private TextView oldPassword;
    private TextView newPassword;
    private TextView newPassword2;
    private ImageButton mBack;
    private TextView    save;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_m_password);

        oldPassword = (TextView)findViewById(R.id.oldpassword);
        newPassword = (TextView)findViewById(R.id.newpassword);
        newPassword2 = (TextView) findViewById(R.id.newpassword2);

        mBack = (ImageButton) findViewById(R.id.back_pass);
        save = (TextView) findViewById(R.id.save_pass);

        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (oldPassword.getText().toString().equals("")) { setDig("原密码为空！"); return; }
                if (newPassword.getText().toString().equals("")) { setDig("新密码为空！"); return; }
                if (!newPassword.getText().toString().equals(newPassword2.getText().toString())) { setDig("两次密码不相符！");  return; }

                try {
                    AVUser currentUser = AVUser.getCurrentUser();
                    AVUser userA = AVUser.logIn(currentUser.getUsername(),oldPassword.getText().toString() );//请确保用户当前的有效登录状态
                    userA.updatePasswordInBackground(oldPassword.getText().toString(), newPassword.getText().toString(), new UpdatePasswordCallback() {

                        @Override
                        public void done(AVException e) {
                            if (e!=null){
                                if (e.getCode()==AVException.INTERNAL_SERVER_ERROR)
                                    setDig("网络异常！");
                                else
                                if (e.getCode()==AVException.CONNECTION_FAILED)
                                    setDig("网络异常！");
                                else
                                if (e.getCode()==AVException.USERNAME_PASSWORD_MISMATCH)
                                    setDig("原密码错误！");
                                else
                                    setDig("登陆失败，错误码："+e.getCode());
                            }else {
                                setDig("修改成功");
                                finish();
                            }
                        }
                    });
                }catch (AVException e){
                    if (e.getCode()==AVException.INTERNAL_SERVER_ERROR)
                        setDig("网络异常！");
                    else
                    if (e.getCode()==AVException.CONNECTION_FAILED)
                        setDig("网络异常！");
                    else
                    if (e.getCode()==AVException.USERNAME_PASSWORD_MISMATCH)
                        setDig("原密码错误！");
                    else
                        setDig("登陆失败，错误码："+e.getCode());
                }
            }
        });





    }


    private void setDig(String message){
        new AlertDialog.Builder(mPassword.this)
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
        getMenuInflater().inflate(R.menu.menu_m_password, menu);
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
