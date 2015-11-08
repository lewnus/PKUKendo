package pkukendoclub.pkukendo.fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;


import pkukendoclub.pkukendo.Article;
import pkukendoclub.pkukendo.EditPage;
import pkukendoclub.pkukendo.R;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

public class mMessage2 extends Fragment   {


    public static Map<String, Bitmap> name2img = new HashMap<String, Bitmap>();

    private List<Map<String, Object>> mData;

    public PullToRefreshListView listView;
    public  MyAdapter adapter;
    private TextView  add;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {




        final View contentView = inflater.inflate(R.layout.activity_m_message2, container, false);


        listView = (PullToRefreshListView) contentView.findViewById(R.id.rotate_header_list_view);
        listView.setMode(PullToRefreshBase.Mode.BOTH);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                position--;
                if (position >= 0) {
                    //intent
                    // 把结果显示在album活动中
                    Intent myIntent =new Intent(getActivity(), Article.class);
                    Bundle mybundle = new Bundle();
                    mybundle.putString("title",(String)mData.get(position).get("title"));
                    mybundle.putString("content",(String)mData.get(position).get("info"));
                    mybundle.putString("likeNum",Integer.toString((int)mData.get(position).get("likeNum")));
                    mybundle.putParcelable("img",(Bitmap)mData.get(position).get("img"));
                    mybundle.putString("name",(String)mData.get(position).get("name"));
                    mybundle.putString("objectId",(String)mData.get(position).get("objectId"));
                    mybundle.putString("class","a");
                    myIntent.putExtras(mybundle);
                    startActivityForResult(myIntent, 1);
                }
            }
        });

        listView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>(){

            // 下拉Pulling Down
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                // 下拉的时候数据重置
                mAsyncTask asyncTask=new mAsyncTask();
                asyncTask.execute();

            }

            // 上拉Pulling Up
            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                // 上拉的时候添加选项
                mAsyncTask2 asyncTask2=new mAsyncTask2();
                asyncTask2.execute();

            }

        });



       // listView.getSwipeToRefresh().setRefreshing(false);
       /* listView.setupSwipeToDismiss(new SwipeDismissListViewTouchListener.DismissCallbacks() {
            @Override
            public boolean canDismiss(int position) {
                return true;
            }

            @Override
            public void onDismiss(ListView listView, int[] reverseSortedPositions) {
            }
        }, true);

*/
        mData = new ArrayList<Map<String, Object>>();

        mAsyncTask asyncTask=new mAsyncTask();
        asyncTask.execute();

        return contentView;


    }


    int first = 0;
    @Override
    public void onStart() {
        super.onStart();


        if (first == 0){
            first =1;
            add = (TextView)getActivity().findViewById(R.id.add_message);
            add.setClickable(true);
            add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(getActivity(), EditPage.class);
                    startActivityForResult(intent, 0);
                }
            });
        }
    }





int loading = 1;
int listNum = 0;            // list当前“num”最小的数是多少

    //-----------------------------------------------------------------------

    class mAsyncTask extends AsyncTask<ListView, Integer, List<Map<String, Object>> >{

        //该方法并不运行在UI线程内，所以在方法内不能对UI当中的控件进行设置和修改
        //主要用于进行异步操作
        @Override
        protected List<Map<String, Object>> doInBackground(ListView ... params) {
            try {

                List<Map<String, Object>> mData;
                mData = new ArrayList<Map<String, Object>>();

                AVQuery<AVObject> query = new AVQuery<AVObject>("Article");
                query.whereGreaterThan("num",0);
                 query.orderByDescending("num");
                query.setLimit(7);

                List<AVObject> postList =query.find();
                int num = postList.size();
                for (int i = 0; i < num; i++) {
                    Map<String, Object> map = new HashMap<String, Object>();

                    listNum = postList.get(i).getInt("num") ;


                    map.put("title",postList.get(i).getString("title"));
                    map.put("info", postList.get(i).getString("content"));
                    map.put("commentNum", Integer.toString(postList.get(i).getInt("commentNum")));
                    map.put("likeNum",postList.get(i).getInt("likeNum"));
                    map.put("objectId",postList.get(i).getObjectId());
                    AVUser tempUser = (AVUser)postList.get(i).getAVUser("user").fetch();
                    map.put("name", tempUser.getString("NickName"));
                    AVFile avFile = tempUser.getAVFile("Avartar");
                    if (avFile == null){
                        if (tempUser.getString("gender").equals("男"))
                        {
                            Bitmap tempBitmap = BitmapFactory.decodeResource(getResources(),R.drawable.man);
                            map.put("img",tempBitmap);
                        }else{
                            Bitmap tempBitmap = BitmapFactory.decodeResource(getResources(),R.drawable.woman);
                            map.put("img",tempBitmap);
                        }
                    }
                    else {
                        String tempUrl = avFile.getThumbnailUrl(false, 200, 200);
                        map.put("img", getImgByUrl(tempUrl));
                    }
                    mData.add(map);
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
                adapter = new MyAdapter(getActivity());
                listView.setAdapter(adapter);
                listView.onRefreshComplete();

            }else {
                // exception

            }

        }
    }



    class mAsyncTask2 extends AsyncTask<ListView, Integer, List<Map<String, Object>> >{

        //该方法并不运行在UI线程内，所以在方法内不能对UI当中的控件进行设置和修改
        //主要用于进行异步操作
        @Override
        protected List<Map<String, Object>> doInBackground(ListView ... params) {
            try {

                List<Map<String, Object>> mData;
                mData = new ArrayList<Map<String, Object>>();

                AVQuery<AVObject> query = new AVQuery<AVObject>("Article");
                query.whereLessThan("num", listNum);
                query.orderByDescending("num");
                query.setLimit(7);

                List<AVObject> postList =query.find();
                int num = postList.size();
                for (int i = 0; i < num; i++) {
                    Map<String, Object> map = new HashMap<String, Object>();

                    listNum = postList.get(i).getInt("num") ;


                    map.put("title",postList.get(i).getString("title"));
                    map.put("info", postList.get(i).getString("content"));
                    map.put("commentNum", Integer.toString(postList.get(i).getInt("commentNum")));
                    map.put("likeNum",postList.get(i).getInt("likeNum"));
                    map.put("objectId",postList.get(i).getObjectId());
                    AVUser tempUser = (AVUser)postList.get(i).getAVUser("user").fetch();
                    map.put("name", tempUser.getString("NickName"));
                    AVFile avFile = tempUser.getAVFile("Avartar");
                    if (avFile == null){
                        if (tempUser.getString("gender").equals("男"))
                        {
                            Bitmap tempBitmap = BitmapFactory.decodeResource(getResources(),R.drawable.man);
                            map.put("img",tempBitmap);
                        }else{
                            Bitmap tempBitmap = BitmapFactory.decodeResource(getResources(),R.drawable.woman);
                            map.put("img",tempBitmap);
                        }
                    }
                    else {
                        String tempUrl = avFile.getThumbnailUrl(false, 200, 200);
                        map.put("img", getImgByUrl(tempUrl));
                    }
                    mData.add(map);
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
                loading = 0;
                for (int i=0;i<result.size();i++)
                mData.add(result.get(i));
                //handle
                if (result.size()>0) {
                    loading = 1;
                    adapter.notifyDataSetChanged();

                }

            }else {
                // exception

            }

            listView.onRefreshComplete();
        }
    }
//-----------------------------------------------------------------------



    public void onActivityResult(int requestCode, int resultCode, Intent data) {


        if (requestCode==0){


            mAsyncTask asyncTask=new mAsyncTask();
            asyncTask.execute();


        } else if (requestCode==1&&Article.isedit == 1){

            Article.isedit = 0;
            mAsyncTask asyncTask=new mAsyncTask();
            asyncTask.execute();


        }
        super.onActivityResult(requestCode, resultCode, data);
    }



    public final class ViewHolder{
        public ImageView img;
        public TextView title;
        public TextView info;
        public TextView viewBtn;
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

                convertView = mInflater.inflate(R.layout.message_listchild, null);
                holder.img = (ImageView)convertView.findViewById(R.id.img);
                holder.title = (TextView)convertView.findViewById(R.id.title);
                holder.info = (TextView)convertView.findViewById(R.id.info);
                holder.viewBtn = (TextView)convertView.findViewById(R.id.view_btn);
                convertView.setTag(holder);

            }else {

                holder = (ViewHolder)convertView.getTag();
            }


            //holder.img.setBackgroundResource((Integer)mData.get(position).get("img"));
            holder.img.setImageBitmap((Bitmap)mData.get(position).get("img"));
            holder.title.setText((String)mData.get(position).get("title"));
            holder.info.setText((String)mData.get(position).get("info"));
            holder.viewBtn.setText("评论数："+((String)mData.get(position).get("commentNum")));




            return convertView;
        }

    }




    public static Bitmap getImgByUrl(String name){
        if (name2img.containsKey(name)){
            return name2img.get(name);
        }else {
            try {
                URL myFileUrl = new URL(name);

                HttpURLConnection conn = (HttpURLConnection) myFileUrl
                        .openConnection();
                conn.setDoInput(true);
                conn.connect();
                InputStream is = conn.getInputStream();
                Bitmap bitmap = BitmapFactory.decodeStream(is);
                name2img.put(name,bitmap);
                is.close();
                return bitmap;
            }catch (MalformedURLException e) {
                e.printStackTrace();
            }catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }


}
