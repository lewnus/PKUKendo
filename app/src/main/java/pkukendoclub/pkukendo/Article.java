package pkukendoclub.pkukendo;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.SaveCallback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pkukendoclub.pkukendo.fragments.mMessage;
import pkukendoclub.pkukendo.tools.mListView;


public class Article extends ActionBarActivity {

    private TextView title;
    private TextView content;
    private TextView name;
    private TextView like;
    private ImageButton back;
    private ImageView iLikeIt;
    private ImageView   img;
    private String articleId;
    private AVObject currentArticle;
    private int likeFlag;

    private List<Map<String, Object>> mData;
    private Bundle bundle;

    private String mclass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article);

        //init

        bundle = this.getIntent().getExtras();

        title = (TextView)findViewById(R.id.title_article);
        title.setText(bundle.getString("title"));

        back = (ImageButton)findViewById(R.id.back_article);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        img = (ImageView) findViewById(R.id.img_article);
        img.setImageBitmap((Bitmap) bundle.getParcelable("img"));

        articleId = bundle.getString("objectId");
        mclass   = bundle.getString("class");
        final mListView listView = (mListView) findViewById(R.id.list_view_comment);
        mData = new ArrayList<Map<String, Object>>();

        //-----------------------------------------------------------------------

        class mAsyncTask extends AsyncTask<mListView, Integer, List<Map<String, Object>> > {

            //该方法并不运行在UI线程内，所以在方法内不能对UI当中的控件进行设置和修改
            //主要用于进行异步操作
            @Override
            protected List<Map<String, Object>> doInBackground(mListView ... params) {
                try {

                    List<Map<String, Object>> mData;
                    mData = new ArrayList<Map<String, Object>>();

                    AVQuery<AVObject> query = new AVQuery<AVObject>("Comment");
                    query.whereEqualTo("articleId", articleId);
                    query.orderByAscending("num");

                    List<AVObject> postList =query.find();
                    int num = postList.size();
                    for (int i = 0; i < num; i++) {
                        Map<String, Object> map = new HashMap<String, Object>();
                        map.put("content", postList.get(i).getString("content"));
                        AVUser tempUser = (AVUser)postList.get(i).getAVUser("user").fetch();
                        map.put("name", tempUser.getString("NickName"));
                        AVFile avFile = tempUser.getAVFile("Avartar");
                        if (avFile == null){
                            if (tempUser.getString("gender").equals("男"))
                            {
                                Bitmap tempBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.man);
                                map.put("img",tempBitmap);
                            }else{
                                Bitmap tempBitmap = BitmapFactory.decodeResource(getResources(),R.drawable.woman);
                                map.put("img",tempBitmap);
                            }
                        } else {
                            String tempUrl = avFile.getThumbnailUrl(false, 200, 200);

                            map.put("img", mMessage.getImgByUrl(tempUrl));
                        }
                        mData.add(map);
                    }

                    AVQuery<AVObject> query2 = new AVQuery<AVObject>("Like");
                    query2.whereEqualTo("article", articleId);
                    query2.whereEqualTo("user", AVUser.getCurrentUser().getObjectId());
                    if (query2.find().size()>0) likeFlag = 1;
                        else likeFlag = 0;

                    if (mclass.equals("a")) {
                        AVQuery<AVObject> query3 = new AVQuery<AVObject>("Article");
                        query3.whereEqualTo("objectId", articleId);
                        currentArticle = query3.find().get(0);
                    }
                    else {
                        AVQuery<AVObject> query3 = new AVQuery<AVObject>("Notice");
                        query3.whereEqualTo("objectId", articleId);
                        currentArticle = query3.find().get(0);
                    }

                    return mData;
                }catch (AVException e){
                    return null;
                }

            }


            @Override
            protected void onPostExecute(List<Map<String, Object>> result) {
                super.onPostExecute(result);
                if (result!=null){

                    mData = result;
                    //handle
                    MyAdapter adapter = new MyAdapter(Article.this);
                    listView.setAdapter(adapter);

                    content = (TextView)findViewById(R.id.content_article);
                    content.setText(bundle.getString("content"));

                    like = (TextView)findViewById(R.id.like);
                    like.setText(currentArticle.getInt("likeNum")+"人已赞");

                    iLikeIt = (ImageView) findViewById(R.id.img_like);
                    if (likeFlag==0){
                        iLikeIt.setImageResource(R.drawable.like);
                        iLikeIt.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (likeFlag==0){
                                    // 计数器加一
                                    currentArticle.setFetchWhenSave(true);
                                    currentArticle.increment("likeNum");
                                    currentArticle.saveInBackground(new SaveCallback() {
                                        public void done(AVException e) {
                                            if (e == null) {
                                                like.setText(currentArticle.getInt("likeNum")+"人已赞");
                                            } else {
                                               //todo network error
                                            }
                                        }
                                    });

                                    // 生成like对象
                                    AVObject like = new AVObject("Like");
                                    like.put("article", articleId);
                                    like.put("user", AVUser.getCurrentUser().getObjectId());
                                    like.saveInBackground();

                                    // 变更图像
                                    iLikeIt.setImageResource(R.drawable.likefilled);

                                    likeFlag = 1;
                                }
                            }
                        });
                    } else {
                        iLikeIt.setImageResource(R.drawable.likefilled);
                    }

                    name =  (TextView)findViewById(R.id.content_name);
                    name.setText(bundle.getString("name"));

                }else {
                    // exception

                }
            }
        }
//-----------------------------------------------------------------------

        mAsyncTask asyncTask=new mAsyncTask();
        asyncTask.execute();



    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_article, menu);
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


    public final class ViewHolder{
        public ImageView img;
        public TextView content;
        public TextView name;
    }


    public class MyAdapter extends BaseAdapter {

        private LayoutInflater mInflater;


        public MyAdapter(Context context){
            this.mInflater = LayoutInflater.from(context);
        }
        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return mData.size();
        }

        @Override
        public Object getItem(int arg0) {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public long getItemId(int arg0) {
            // TODO Auto-generated method stub
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ViewHolder holder = null;
            if (convertView == null) {

                holder=new ViewHolder();

                convertView = mInflater.inflate(R.layout.message_listchild3, null);
                holder.img = (ImageView)convertView.findViewById(R.id.img);
                holder.content = (TextView)convertView.findViewById(R.id.content);
                holder.name = (TextView)convertView.findViewById(R.id.name);
                convertView.setTag(holder);

            }else {

                holder = (ViewHolder)convertView.getTag();
            }


            //holder.img.setBackgroundResource(R.drawable.win10tx);
            holder.img.setImageBitmap((Bitmap)mData.get(position).get("img"));
            holder.content.setText((String) mData.get(position).get("content"));
            holder.name.setText((String) mData.get(position).get("name"));

            return convertView;
        }

    }





}
