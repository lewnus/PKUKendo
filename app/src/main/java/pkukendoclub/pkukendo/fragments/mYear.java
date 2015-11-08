package pkukendoclub.pkukendo.fragments;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import pkukendoclub.pkukendo.R;

public class mYear extends Fragment {


    private List<Map<String, Object>> mData;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View contentView = inflater.inflate(R.layout.activity_m_year, container, false);


        final ListView listView = (ListView) contentView.findViewById(R.id.list_view_year);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position >= 0) {

                }
            }
        });


        mData = new ArrayList<Map<String, Object>>();

        //-----------------------------------------------------------------------

        class mAsyncTask extends AsyncTask<ListView, Integer, List<Map<String, Object>> > {

            //该方法并不运行在UI线程内，所以在方法内不能对UI当中的控件进行设置和修改
            //主要用于进行异步操作
            @Override
            protected List<Map<String, Object>> doInBackground(ListView ... params) {
                try {

                    List<Map<String, Object>> mData;
                    mData = new ArrayList<Map<String, Object>>();

                    AVQuery<AVObject> query = new AVQuery<AVObject>("Event");
                    query.whereGreaterThan("num",0);
                    query.orderByAscending("num");

                    List<AVObject> postList =query.find();
                    int num = postList.size();
                    for (int i = 0; i < num; i++) {
                        Map<String, Object> map = new HashMap<String, Object>();
                        map.put("title", postList.get(i).getString("content"));
                        map.put("time", postList.get(i).getString("time"));
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


        return contentView;


    }







    public final class ViewHolder{
        public ImageView img;
        public TextView title;
        public TextView time;
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

                convertView = mInflater.inflate(R.layout.message_listchild2, null);
                holder.img = (ImageView)convertView.findViewById(R.id.img);
                holder.title = (TextView)convertView.findViewById(R.id.title);
                holder.time = (TextView)convertView.findViewById(R.id.time);
                convertView.setTag(holder);

            }else {

                holder = (ViewHolder)convertView.getTag();
            }


            holder.img.setBackgroundResource(R.drawable.sword);
            //holder.img.setImageBitmap((Bitmap)mData.get(position).get("img"));
            holder.title.setText((String)mData.get(position).get("title"));
            holder.time.setText((String)mData.get(position).get("time"));

            return convertView;
        }

    }

}