/*
*   editpage  是新建日志的界面
 *  editpage2 是编辑日志的界面
*/

package pkukendoclub.pkukendo;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.SaveCallback;


public class EditPage2 extends ActionBarActivity {

    ImageButton back;
    TextView    save;
    EditText    title;
    EditText    content;
    String articleId;
    String stitle;
    String scontent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_page2);


        Bundle bundle = this.getIntent().getExtras();
        articleId = bundle.getString("articleId");
        stitle = bundle.getString("title");
        scontent = bundle.getString("content");
        init();

    }

    void init(){

        back = (ImageButton) findViewById(R.id.back_editpage);
        save = (TextView)    findViewById(R.id.save_editpage);
        title= (EditText)    findViewById(R.id.title_editpage);
        content = (EditText) findViewById(R.id.content_editpage);

        title.setText(stitle);
        content.setText(scontent);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AVObject post = AVObject.createWithoutData("Article", articleId);
                post.put("content",  content.getText());
                post.put("title",    title.getText());
                if (title.getText().toString().equals(""))  {
                    setDig("标题不能为空");
                }else if (content.getText().toString().equals("")) {
                    setDig("日志不能为空");
                } else {
                    post.saveInBackground(new SaveCallback() {
                        public void done(AVException e) {
                            if (e == null) {
                                // 保存成功
                                setDig("保存成功");
                            } else {
                                // failed
                                if (e.getCode() == AVException.INTERNAL_SERVER_ERROR)
                                    setDig("网络异常！");
                                else if (e.getCode() == AVException.CONNECTION_FAILED)
                                    setDig("网络异常！");
                                else if (e.getCode() == AVException.USERNAME_TAKEN)
                                    setDig("用户名已存在！");
                                else
                                    setDig("注册失败，错误码：" + e.getCode());

                            }
                        }
                    });

                }
            }
        });





    }


    private void setDig(final String message){
        new AlertDialog.Builder(EditPage2.this)
                .setTitle(message)
                .setNegativeButton("确认", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        if (message.equals("保存成功"))
                            edit_finish();
                    }
                }).show();

    }

    void edit_finish(){
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_edit_page, menu);
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
