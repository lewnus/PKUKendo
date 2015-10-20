package pkukendoclub.pkukendo.fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;
import pkukendoclub.pkukendo.Article;
import pkukendoclub.pkukendo.R;

import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;

public class mNotice extends Fragment {


    private List<Map<String, Object>> mData;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {




        final View contentView = inflater.inflate(R.layout.activity_m_notice, container, false);


        final ListView listView = (ListView) contentView.findViewById(R.id.rotate_header_list_view);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
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
                    mybundle.putString("class","n");
                    myIntent.putExtras(mybundle);
                    startActivity(myIntent);
                }
            }
        });


        mData = new ArrayList<Map<String, Object>>();

        //-----------------------------------------------------------------------

        class mAsyncTask extends AsyncTask<ListView, Integer, List<Map<String, Object>> >{

            //该方法并不运行在UI线程内，所以在方法内不能对UI当中的控件进行设置和修改
            //主要用于进行异步操作
            @Override
            protected List<Map<String, Object>> doInBackground(ListView ... params) {
                try {

                    List<Map<String, Object>> mData;
                    mData = new ArrayList<Map<String, Object>>();

                    AVQuery<AVObject> query = new AVQuery<AVObject>("Notice");
                    query.whereGreaterThan("num",0);

                    query.orderByDescending("num");

                    List<AVObject> postList =query.find();
                    int num = postList.size();
                    for (int i = 0; i < num; i++) {
                        Map<String, Object> map = new HashMap<String, Object>();
                        map.put("title",postList.get(i).getString("title"));
                        map.put("info", postList.get(i).getString("content"));
                        map.put("commentNum", Integer.toString(postList.get(i).getInt("commentNum")));
                        map.put("likeNum",postList.get(i).getInt("likeNum"));
                        map.put("objectId",postList.get(i).getObjectId());
                        String name = postList.get(i).getString("userName");
                        map.put("name",name );
                        Bitmap bmp;
                        if(name.equals("社长"))
                            bmp=BitmapFactory.decodeResource(getResources(),R.drawable.shezhang);
                        else if (name.equals("财务"))
                            bmp=BitmapFactory.decodeResource(getResources(),R.drawable.caiwu);
                        else if (name.equals("训练组"))
                            bmp=BitmapFactory.decodeResource(getResources(),R.drawable.xunlian);
                        else
                            bmp=BitmapFactory.decodeResource(getResources(),R.drawable.lianluo);


                        map.put("img",bmp);
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
                    MyAdapter adapter = new MyAdapter(getActivity());
                    listView.setAdapter(adapter);

                }else {
                    // exception

                }
            }
        }
//-----------------------------------------------------------------------

        mAsyncTask asyncTask=new mAsyncTask();
        asyncTask.execute();



        final PtrClassicFrameLayout ptrFrame = (PtrClassicFrameLayout) contentView.findViewById(R.id.fragment_rotate_header_with_text_view_frame);
        ptrFrame.setLastUpdateTimeRelateObject(this);
        ptrFrame.setPtrHandler(new PtrDefaultHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {

                mAsyncTask asyncTask=new mAsyncTask();
                asyncTask.execute();
                ptrFrame.refreshComplete();

            }

            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return PtrDefaultHandler.checkContentCanBePulledDown(frame, content, header);
            }
        });
        return contentView;


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


            //holder.img.setBackgroundResource(R.drawable.win10tx);
            holder.img.setImageBitmap((Bitmap)mData.get(position).get("img"));
            holder.title.setText((String)mData.get(position).get("title"));
            holder.info.setText((String)mData.get(position).get("info"));
            holder.viewBtn.setText("评论数："+((String)mData.get(position).get("commentNum")));

            return convertView;
        }

    }







}

