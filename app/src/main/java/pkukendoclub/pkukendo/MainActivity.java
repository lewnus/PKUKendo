package pkukendoclub.pkukendo;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVOSCloud;
import com.avos.avoscloud.AVUser;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AVOSCloud.initialize(this, "ql84x2woif2u3xk7p3qoska4i558v3ornikfkfga1l3ad59n", "frzrwer3k3demoxounucm0ubfqzlvongad1h30avewweycd9");

        AVUser currentUser = AVUser.getCurrentUser();
        int x=1;
        if (currentUser != null) {
            Intent myIntent =new Intent(MainActivity.this, Kendo.class);
            startActivity(myIntent);
        } else {
            Intent myIntent =new Intent(MainActivity.this, FirstPage.class);
            startActivity(myIntent);
        }



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
