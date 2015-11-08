package pkukendoclub.pkukendo;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.SaveCallback;
import com.avos.avoscloud.UpdatePasswordCallback;


public class mName extends ActionBarActivity {

    private TextView newName;
    private ImageButton mBack;
    private TextView    save;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_m_name);

        newName = (TextView)findViewById(R.id.username);
        mBack = (ImageButton) findViewById(R.id.back_name);
        save = (TextView) findViewById(R.id.save_name);

        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (newName.getText().toString().equals("")) { setDig("昵称不能为空！"); return; }

                mAsyncTask asyncTask=new mAsyncTask();
                asyncTask.execute();

            }
        });

    }

    private Handler handler = new Handler() {
        public void handleMessage (Message msg) {//此方法在ui线程运行
            switch(msg.what) {

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
            dialog = ProgressDialog.show(mName.this, "保存提示", "正在保存，请稍等...", false);//创建ProgressDialog

        }

        @Override
        protected Void doInBackground(Void ... params) {
            try {

                AVUser currentUser = AVUser.getCurrentUser();
                currentUser.put("NickName",newName.getText().toString());
                currentUser.save();

            }catch (AVException e){
                Message message = new Message();
                message.what = 1;
                handler.sendMessage(message);
                return null;
            }

            // done
            dialog.dismiss(); Message message = new Message();
            message.what = 0;
            handler.sendMessage(message);
            return  null;
        }




    }

//-----------------------------------------------------------------------






    private void setDig(final String message){

        new AlertDialog.Builder(mName.this)
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
        getMenuInflater().inflate(R.menu.menu_m_name, menu);
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
