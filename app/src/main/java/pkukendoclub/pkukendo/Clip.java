package pkukendoclub.pkukendo;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import java.io.ByteArrayOutputStream;

import pkukendoclub.pkukendo.tools.ClipImageLayout;


public class Clip extends ActionBarActivity {

    private ClipImageLayout mClipImageLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clip);

        mClipImageLayout = (ClipImageLayout) findViewById(R.id.id_clipImageLayout);

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
