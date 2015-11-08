package pkukendoclub.pkukendo;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;


public class Info4list extends ActionBarActivity {


    private TextView  name;
    private TextView  duty;
    private TextView  gender;
    private TextView  phone;
    private TextView  weixin;
    private TextView  jiebie;
    private TextView  dan;
    private TextView  school;
    private TextView  grade;
    private ImageView pic;

    private ImageButton back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info4list);

        init();

        Bundle bundle = this.getIntent().getExtras();
        name.setText(bundle.getString("name"));
        duty.setText(bundle.getString("duty"));
        gender.setText(bundle.getString("gender"));
        phone.setText(bundle.getString("phone"));
        weixin.setText(bundle.getString("weixin"));
        dan.setText(bundle.getString("dan"));
        jiebie.setText(bundle.getString("jiebie"));
        school.setText(bundle.getString("school"));
        grade.setText(bundle.getString("grade"));


        if (bundle.getString("gender").equals("女"))
            pic.setImageResource(R.drawable.woman);
        else
            pic.setImageResource(R.drawable.man);

    }

    private void init(){

        name = (TextView) findViewById(R.id.name_info);
        duty = (TextView) findViewById(R.id.duty_info);
        gender = (TextView) findViewById(R.id.gender_info);
        phone = (TextView) findViewById(R.id.phone_info);
        weixin = (TextView) findViewById(R.id.weixin_info);
        jiebie = (TextView) findViewById(R.id.jiebie_info);
        dan = (TextView) findViewById(R.id.dan_info);
        school = (TextView) findViewById(R.id.school_info);
        grade = (TextView) findViewById(R.id.grade_info);
        pic = (ImageView) findViewById(R.id.pic);


        back = (ImageButton) findViewById(R.id.back_info);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_info4list, menu);
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
