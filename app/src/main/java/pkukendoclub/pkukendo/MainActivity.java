package pkukendoclub.pkukendo;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVOSCloud;
import com.avos.avoscloud.AVUser;

import java.util.Timer;
import java.util.TimerTask;


public class MainActivity extends ActionBarActivity {


    private Timer timer;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            if (msg.what == 1) {

                AVUser currentUser = AVUser.getCurrentUser();
                int x=1;
                if (currentUser != null) {
                    Intent myIntent =new Intent(MainActivity.this, Kendo.class);
                    startActivity(myIntent);
                } else {
                    Intent myIntent =new Intent(MainActivity.this, FirstPage.class);
                    startActivity(myIntent);
                }

            } else timer.cancel();
        }
    };
    TimerTask task = new TimerTask(){
        public void run() {
            Message message = new Message();
            message.what = 1;
            handler.sendMessage(message);
        }
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AVOSCloud.initialize(this, "ql84x2woif2u3xk7p3qoska4i558v3ornikfkfga1l3ad59n", "frzrwer3k3demoxounucm0ubfqzlvongad1h30avewweycd9");

        timer = new Timer(true);
        timer.schedule(task,10000);


    }

    @Override
    protected void onRestart(){
        super.onRestart();
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
