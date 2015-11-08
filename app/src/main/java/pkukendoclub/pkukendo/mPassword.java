package pkukendoclub.pkukendo;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.UpdatePasswordCallback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


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
                if (!newPassword.getText().toString().equals(newPassword2.getText().toString())) { setDig("两次新密码不一样！");  return; }

                mAsyncTask asyncTask=new mAsyncTask();
                asyncTask.execute();



            }
        });





    }


    private Handler handler = new Handler() {
        public void handleMessage (Message msg) {//此方法在ui线程运行
            switch(msg.what) {
                case 2:
                    setDig("原密码错误！");
                    break;

                case 1:
                    setDig("网络异常！");
                    break;

                case 0:
                    setDig("保存成功");
                    break;
            }
        }
    };


    ProgressDialog dialog;


    //-----------------------------------------------------------------------
    //  异步操作
    class mAsyncTask extends AsyncTask<Void, Integer, Void > {

        //该方法并不运行在UI线程内，所以在方法内不能对UI当中的控件进行设置和修改
        //主要用于进行异步操作


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = ProgressDialog.show(mPassword.this, "保存提示", "正在保存，请稍等...", false);//创建ProgressDialog

        }

        @Override
        protected Void doInBackground(Void ... params) {
            try {


                AVUser currentUser = AVUser.getCurrentUser();
                currentUser.updatePassword(oldPassword.getText().toString(), newPassword.getText().toString());

            }catch (AVException e) {
                dialog.dismiss();

                if (e.getCode() == AVException.USERNAME_PASSWORD_MISMATCH){
                    Message message = new Message();
                    message.what = 2;
                    handler.sendMessage(message);
                }else {
                    Message message = new Message();
                    message.what = 1;
                    handler.sendMessage(message);
                }return null;
            }

            // done
            dialog.dismiss();
            Message message = new Message();
            message.what = 0;
            handler.sendMessage(message);
            return  null;
        }




    }

//-----------------------------------------------------------------------






    private void setDig(final String message){
        new AlertDialog.Builder(mPassword.this)
                .setTitle(message)
                .setNegativeButton("确认", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();
                        if (message.equals("保存成功"))
                            finish();
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
