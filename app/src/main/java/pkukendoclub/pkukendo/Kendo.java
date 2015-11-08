package pkukendoclub.pkukendo;

import android.app.Activity;
import android.graphics.Color;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tencent.plus.ImageActivity;

import pkukendoclub.pkukendo.fragments.*;


/*
*   主要活动界面，包含五个fragment
*
*   主要程序是五个fragment的交互
* */

public class Kendo  extends Activity implements View.OnClickListener{


    private mMessage2 mMessage2;          //我的日志  编号0
    private mList mList;                //通讯录    编号1
    private mYear mYear;                //年表      编号2
    private mMe mMe;                    //我        编号3
    private mNotice mNotice;            //通知      编号4    注意“通知”界面按照顺序在第三个，但是由于历史原因 编号为4。


    private View mMessage2Layout;
    private View mListLayout;
    private View mYearLayout;
    private View mMeLayout;
    private View mNoticeLayout;

    private ImageView mMessage2Image;
    private ImageView mListImage;
    private ImageView mYearImage;
    private ImageView mMeImage;
    private ImageView mNoticeImage;

    private TextView mMessage2Text;
    private TextView mListText;
    private TextView mYearText;
    private TextView mMeText;
    private TextView mNoticeText;

    private FragmentManager mFragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kendo);

        initViews();

        mFragmentManager = getFragmentManager();
        setTabSelection(4);         //  初始化在通知界面

    }


    private void initViews(){

        //fragments
        mMessage2Layout = findViewById(R.id.firstpage_tab1);
        mListLayout = findViewById(R.id.firstpage_tab2);
        mYearLayout = findViewById(R.id.firstpage_tab3);
        mMeLayout = findViewById(R.id.firstpage_tab4);
        mNoticeLayout = findViewById(R.id.firstpage_tab5);


        //tab buttons
        mMessage2Image = (ImageView) findViewById(R.id.message_image);
        mListImage = (ImageView) findViewById(R.id.list_image);
        mYearImage = (ImageView) findViewById(R.id.year_image);
        mMeImage = (ImageView) findViewById(R.id.me_image);
        mNoticeImage = (ImageView) findViewById(R.id.notice_image);

        mMessage2Text = (TextView) findViewById(R.id.messgae_text);
        mListText = (TextView) findViewById(R.id.list_text);
        mYearText = (TextView) findViewById(R.id.year_text);
        mMeText = (TextView) findViewById(R.id.me_text);
        mNoticeText = (TextView) findViewById(R.id.notice_text);


        // button
        mMessage2Layout.setOnClickListener(this);
        mListLayout.setOnClickListener(this);
        mYearLayout.setOnClickListener(this);
        mMeLayout.setOnClickListener(this);
        mNoticeLayout.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.firstpage_tab1:
                // 当点击了消息tab时，选中第1个tab
                setTabSelection(0);
                break;
            case R.id.firstpage_tab2:
                // 当点击了联系人tab时，选中第2个tab
                setTabSelection(1);
                break;
            case R.id.firstpage_tab3:
                // 当点击了动态tab时，选中第3个tab
                setTabSelection(2);
                break;
            case R.id.firstpage_tab4:
                // 当点击了动态tab时，选中第4个tab
                setTabSelection(3);
                break;
            case R.id.firstpage_tab5:
                setTabSelection(4);
            default:
                break;
        }
    }


    /**
     * 根据传入的index参数来设置选中的tab页。
     *
     * @param index
     *            每个tab页对应的下标。0表示message，1表示list，2表示year，3表示me,4 notice。
     */

    private void setTabSelection(int index) {
        // 每次选中之前先清楚掉上次的选中状态
        clearSelection();
        // 开启一个Fragment事务
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        // 先隐藏掉所有的Fragment，以防止有多个Fragment显示在界面上的情况
        hideFragments(transaction);
        switch (index) {
            case 0:
                mMessage2Text.setTextColor(Color.parseColor("#268bfd"));
                mMessage2Image.setImageResource(R.drawable.tag1_1);
                if (mMessage2 == null) {
                    mMessage2 = new mMessage2();
                    transaction.add(R.id.firstpage_content, mMessage2);
                } else {
                    transaction.show(mMessage2);
                }
                break;
            case 1:
                mListText.setTextColor(Color.parseColor("#268bfd"));
                mListImage.setImageResource(R.drawable.tag2_1);
                if (mList == null) {
                    mList = new mList();
                    transaction.add(R.id.firstpage_content, mList);
                } else {
                    transaction.show(mList);
                }
                break;

            case 2:
                mYearText.setTextColor(Color.parseColor("#268bfd"));
                mYearImage.setImageResource(R.drawable.tag4_1);
                if (mYear == null) {
                    mYear = new mYear();
                    transaction.add(R.id.firstpage_content, mYear);
                } else {
                    transaction.show(mYear);
                }
                break;
            case 3:

                mMeText.setTextColor(Color.parseColor("#268bfd"));
                mMeImage.setImageResource(R.drawable.tag5_1);
                if (mMe == null) {
                    mMe = new mMe();
                    transaction.add(R.id.firstpage_content, mMe);
                } else {
                    transaction.show(mMe);
                }
                break;
            case 4:

                mNoticeText.setTextColor(Color.parseColor("#268bfd"));
                mNoticeImage.setImageResource(R.drawable.tag3_1);
                if (mNotice == null) {
                    mNotice = new mNotice();
                    transaction.add(R.id.firstpage_content, mNotice);
                } else {
                    transaction.show(mNotice);
                }
                break;
            default:
        }
        transaction.commit();
    }
    private void clearSelection() {
        mMessage2Image.setImageResource(R.drawable.tag1_0);
        mMessage2Text.setTextColor(Color.BLACK);
        mListImage.setImageResource(R.drawable.tag2_0);
        mListText.setTextColor(Color.BLACK);
        mNoticeImage.setImageResource(R.drawable.tag3_0);
        mNoticeText.setTextColor(Color.BLACK);
        mYearImage.setImageResource(R.drawable.tag4_0);
        mYearText.setTextColor(Color.BLACK);
        mMeImage.setImageResource(R.drawable.tag5_0);
        mMeText.setTextColor(Color.BLACK);

    }

    private void hideFragments(FragmentTransaction transaction) {
        if (mMessage2 != null) {
            transaction.hide(mMessage2);
        }
        if (mList != null) {
            transaction.hide(mList);
        }
        if (mYear != null) {
            transaction.hide(mYear);
        }
        if (mMe != null) {
            transaction.hide(mMe);
        }
        if (mNotice != null){
            transaction.hide(mNotice);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_kendo, menu);
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
