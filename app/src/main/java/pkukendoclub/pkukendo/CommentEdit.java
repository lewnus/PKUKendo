package pkukendoclub.pkukendo;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
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


public class CommentEdit extends ActionBarActivity {


    public static int isadd = 0;  // 确认是否添加成功  （父活动使用）
    ImageButton back;
    TextView    save;
    EditText    content;
    String articleId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment_edit);

        Bundle bundle = this.getIntent().getExtras();

        articleId = bundle.getString("articleId");

       init();

    }

    void init(){

        back = (ImageButton) findViewById(R.id.back_cedit);
        save = (TextView)    findViewById(R.id.save_cedit);
        content = (EditText) findViewById(R.id.content_cedit);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AVObject post = new AVObject("Comment");

                if (content.getText().toString().equals("")) {
                    setDig("回复不能为空");
                } else {
                    post.put("content",  content.getText());
                    post.put("articleId",articleId);
                    post.put("user", AVUser.getCurrentUser());
                    post.saveInBackground(new SaveCallback() {
                        public void done(AVException e) {
                            if (e == null) {
                                // 保存成功
                                isadd = 1;
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
        new AlertDialog.Builder(CommentEdit.this)
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
