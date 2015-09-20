package pkukendoclub.pkukendo;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;

import pkukendoclub.pkukendo.fragments.mMe;
import pkukendoclub.pkukendo.tools.ClipImageLayout;


public class Clip extends ActionBarActivity {

    private ClipImageLayout mClipImageLayout;

    private TextView save;
    private TextView cancel;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clip);

        mClipImageLayout = (ClipImageLayout) findViewById(R.id.id_clipImageLayout);
        save = (TextView)findViewById(R.id.save_clip);
        cancel = (TextView)findViewById(R.id.cancel_clip);


        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMe.clipflag = 0;
                finish();
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMe.clipflag = 1;
                mMe.bitmap_from_clip = mClipImageLayout.clip();
                finish();
            }
        });


        Bundle bundle = this.getIntent().getExtras();
        mClipImageLayout.setImage( bundle.getString("message"));


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_clip, menu);
        return true;
    }

}
