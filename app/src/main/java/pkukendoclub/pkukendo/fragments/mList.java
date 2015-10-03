package pkukendoclub.pkukendo.fragments;


        import pkukendoclub.pkukendo.Clip;
        import pkukendoclub.pkukendo.Info4list;
        import pkukendoclub.pkukendo.R;
        import pkukendoclub.pkukendo.tools.PinnedHeaderExpandableListView;
        import pkukendoclub.pkukendo.tools.StickyLayout;
        import pkukendoclub.pkukendo.tools.PinnedHeaderExpandableListView.OnHeaderUpdateListener;
        import pkukendoclub.pkukendo.tools.StickyLayout.OnGiveUpTouchEventListener;

        import android.app.Activity;
        import android.app.Fragment;
        import android.content.Context;
        import android.content.Intent;
        import android.os.Bundle;
        import android.view.LayoutInflater;
        import android.view.MotionEvent;
        import android.view.View;
        import android.view.View.OnClickListener;
        import android.view.ViewGroup;
        import android.widget.AbsListView.LayoutParams;
        import android.widget.BaseExpandableListAdapter;
        import android.widget.Button;
        import android.widget.ExpandableListView;
        import android.widget.ImageView;
        import android.widget.TextView;
        import android.widget.Toast;

        import com.avos.avoscloud.AVException;
        import com.avos.avoscloud.AVObject;
        import com.avos.avoscloud.AVQuery;
        import com.avos.avoscloud.FindCallback;

        import java.util.ArrayList;
        import java.util.List;



public class mList extends Fragment implements
        ExpandableListView.OnChildClickListener,
        ExpandableListView.OnGroupClickListener,
        OnHeaderUpdateListener, OnGiveUpTouchEventListener {




    private PinnedHeaderExpandableListView expandableListView;
   // private StickyLayout stickyLayout;
    private ArrayList<Group> groupList;
    private ArrayList<List<People>> childList;

    private MyexpandableListAdapter adapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.main, container, false);
    }


boolean flag = false;

    @Override
    public void onStart(){
        super.onStart();
        if (flag == false) {
            // first time
            expandableListView = (PinnedHeaderExpandableListView) getActivity().findViewById(R.id.expandablelist);
            initData();
            flag = true;
        }


    }



    /***
     * InitData
     */

    String[] data1 = new String[]{"aaa","aaa","bbb","ccc","ccc","ccc",};
    //String[] data1;


    void initData() {

        groupList = new ArrayList<Group>();
        childList = new ArrayList<List<People>>();

        AVQuery<AVObject> query = new AVQuery<AVObject>("Contact");
        query.whereGreaterThan("sort",0);
        query.orderByAscending("sort");
        query.findInBackground(new FindCallback<AVObject>() {
            public void done(List<AVObject> postList, AVException e) {
                int ma = 0;
                for (int i = 0; i < postList.size(); i++)
                    ma = Math.max(ma, postList.get(i).getInt("sort"));

                //get group title
                data1 = new String[ma];
                for (int i = 0; i < postList.size(); i++)
                    data1[postList.get(i).getInt("sort") - 1] = postList.get(i).getString("group");
                for (int i = 0; i < ma; i++) {
                    Group group = null;
                    group = new Group();
                    group.setTitle(data1[i]);
                    groupList.add(group);
                }

                //get child
                for (int i = 0; i < postList.size(); i++) {
                    People people = new People();
                    people.people = postList.get(i);
                    groupList.get(postList.get(i).getInt("sort") - 1).child.add(people);
                }
                for (int i = 0; i < ma; i++)
                    childList.add(groupList.get(i).child);



                adapter = new MyexpandableListAdapter(mList.this.getActivity());
                expandableListView.setAdapter(adapter);

                // 展开所有group
                for (int i = 0, count = expandableListView.getCount(); i < count; i++) {
                    expandableListView.expandGroup(i);
                }

                expandableListView.setOnHeaderUpdateListener(mList.this);
                expandableListView.setOnChildClickListener(mList.this);
                expandableListView.setOnGroupClickListener(mList.this);
            }
        });




    }



    /***
     * 数据源
     *
     * @author Administrator
     *
     */
    class MyexpandableListAdapter extends BaseExpandableListAdapter {
        private Context context;
        private LayoutInflater inflater;

        public MyexpandableListAdapter(Context context) {
            this.context = context;
            inflater = LayoutInflater.from(context);
        }

        // 返回父列表个数
        @Override
        public int getGroupCount() {
            return groupList.size();
        }

        // 返回子列表个数
        @Override
        public int getChildrenCount(int groupPosition) {
            return childList.get(groupPosition).size();
        }

        @Override
        public Object getGroup(int groupPosition) {

            return groupList.get(groupPosition);
        }

        @Override
        public Object getChild(int groupPosition, int childPosition) {
            return childList.get(groupPosition).get(childPosition);
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        @Override
        public boolean hasStableIds() {

            return true;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded,
                                 View convertView, ViewGroup parent) {
            GroupHolder groupHolder = null;
            if (convertView == null) {
                groupHolder = new GroupHolder();
                convertView = inflater.inflate(R.layout.group, null);
                groupHolder.textView = (TextView) convertView
                        .findViewById(R.id.group);
                groupHolder.imageView = (ImageView) convertView
                        .findViewById(R.id.image);
                convertView.setTag(groupHolder);
            } else {
                groupHolder = (GroupHolder) convertView.getTag();
            }

            groupHolder.textView.setText(((Group) getGroup(groupPosition))
                    .getTitle());
            if (isExpanded)// ture is Expanded or false is not isExpanded
                groupHolder.imageView.setImageResource(R.drawable.expanded);
            else
                groupHolder.imageView.setImageResource(R.drawable.collapse);
            return convertView;
        }

        @Override
        public View getChildView(int groupPosition, int childPosition,
                                 boolean isLastChild, View convertView, ViewGroup parent) {
            ChildHolder childHolder = null;
            if (convertView == null) {
                childHolder = new ChildHolder();
                convertView = inflater.inflate(R.layout.child, null);

                childHolder.textName = (TextView) convertView
                        .findViewById(R.id.name);
                childHolder.textDuty = (TextView) convertView
                        .findViewById(R.id.duty);
                childHolder.imageView = (ImageView) convertView
                        .findViewById(R.id.image);


                convertView.setTag(childHolder);
            } else {
                childHolder = (ChildHolder) convertView.getTag();
            }

            childHolder.textName.setText(((People) getChild(groupPosition,
                    childPosition)).getName());
            childHolder.textDuty.setText(((People) getChild(groupPosition,
                    childPosition)).getDuty());

            return convertView;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }
    }

    @Override
    public boolean onGroupClick(final ExpandableListView parent, final View v,
                                int groupPosition, final long id) {

        return false;
    }

    @Override
    public boolean onChildClick(ExpandableListView parent, View v,
                                int groupPosition, int childPosition, long id) {
       
        People p = groupList.get(groupPosition).child.get(childPosition);
        Intent myIntent =new Intent(getActivity(), Info4list.class);
        Bundle mybundle = new Bundle();
        mybundle.putString("name",p.getName());
        mybundle.putString("duty",p.getDuty());
        mybundle.putString("gender",p.getGender());
        mybundle.putString("phone",p.getPhone());
        mybundle.putString("weixin",p.getWeixin());
        mybundle.putString("dan",p.getDan());
        mybundle.putString("jiebie",p.getJiebie());
        mybundle.putString("school",p.getSchool());
        mybundle.putString("grade",p.getGrade());
        myIntent.putExtras(mybundle);
        startActivity(myIntent);
        return false;
    }

    class GroupHolder {
        TextView textView;
        ImageView imageView;
    }

    class ChildHolder {
        TextView textName;
        TextView textDuty;
        ImageView imageView;
    }

    @Override
    public View getPinnedHeader() {
        View headerView = (ViewGroup) getActivity().getLayoutInflater().inflate(R.layout.group, null);
        headerView.setLayoutParams(new LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

        return headerView;
    }

    @Override
    public void updatePinnedHeader(View headerView, int firstVisibleGroupPos) {
        Group firstVisibleGroup = (Group) adapter.getGroup(firstVisibleGroupPos);
        TextView textView = (TextView) headerView.findViewById(R.id.group);
        textView.setText(firstVisibleGroup.getTitle());
    }

    @Override
    public boolean giveUpTouchEvent(MotionEvent event) {
        if (expandableListView.getFirstVisiblePosition() == 0) {
            View view = expandableListView.getChildAt(0);
            if (view != null && view.getTop() >= 0) {
                return true;
            }
        }
        return false;
    }

}



class Group {

    private String title;
    ArrayList<People> child = new ArrayList<People>();

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}

class People {

    AVObject people;

    public String getName(){ return people.getString("name");}
    public String getDuty(){ return people.getString("position");}
    public String getGender(){ return people.getString("gender");}
    public String getPhone(){ return people.getString("phone");}
    public String getWeixin(){ return people.getString("weixin");}
    public String getJiebie(){ return people.getString("jiebie");}
    public String getDan(){ return people.getString("duan");}
    public String getSchool(){ return people.getString("yuanxi");}
    public String getGrade(){ return people.getString("nianji");}


}

