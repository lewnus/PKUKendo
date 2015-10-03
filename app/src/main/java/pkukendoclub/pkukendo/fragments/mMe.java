package pkukendoclub.pkukendo.fragments;

import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.GetDataCallback;
import com.avos.avoscloud.SaveCallback;
import com.avos.avoscloud.SignUpCallback;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.StringTokenizer;

import pkukendoclub.pkukendo.*;

import static android.app.Activity.RESULT_CANCELED;

public class mMe extends Fragment {

    private View mName;
    private View mPassword;
    private TableRow mNote;
    private TableRow AboutUs;
    public  ImageView  cImageView;
    private String   IMAGE_FILE_NAME= "PKUkendo_tempimg.jpg";
    private String[] items = new String[] { "选择本地图片", "拍照" };

    public static int clipflag = 0;
    public static Bitmap bitmap_from_clip;

    private TextView text_name;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_m_me, container, false);

    }



    @Override
    public void onStart() {
        super.onStart();
        init();
    }

    private void init(){

        mName = getActivity().findViewById(R.id.mUser);
        mPassword = getActivity().findViewById(R.id.password);
        mNote = (TableRow) getActivity().findViewById(R.id.mynote);
        AboutUs = (TableRow) getActivity().findViewById(R.id.aboutus);
        cImageView = (ImageView) getActivity().findViewById(R.id.cImageView);
        text_name = (TextView) getActivity().findViewById(R.id.uname);

        AVUser currentUser = AVUser.getCurrentUser();
        text_name.setText(currentUser.getString("NickName"));
        AVFile avFile = currentUser.getAVFile("Avartar");

        avFile.getDataInBackground(new GetDataCallback(){
            public void done(byte[] data, AVException e){
                if (e==null)
                cImageView.setImageBitmap(BitmapFactory.decodeByteArray(data, 0, data.length));
                else {
                    int x =1;
                    x++;
                }
            }

        });



        cImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                set_head_image();
            }
        });


        mName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), mName.class);
                startActivity(intent);
            }
        });

        mNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), mNote.class);
                startActivity(intent);
            }
        });

        mPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), mPassword.class);
                startActivity(intent);
            }
        });

        AboutUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Aboutus.class);
                startActivity(intent);
            }
        });

    }



    /* 设置头像
     *  弹出一个消息框，有通过相机和相册两种方式
     *  图像的大小裁剪暂时没做的很好
     */
    public void set_head_image(){
        new AlertDialog.Builder(getActivity())
                .setTitle("设置头像")
                .setItems(items, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                // 相册
                                Intent intent;
                                intent = new Intent(
                                        Intent.ACTION_PICK,
                                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                startActivityForResult(intent, 0);
                                break;

                            case 1:
                                // 相机
                                Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                Uri imageUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(),IMAGE_FILE_NAME));
                                //指定照片保存路径（SD卡），一个临时文件，每次拍照后这个图片都会被替换
                                openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                                startActivityForResult(openCameraIntent, 1);
                                break;
                        }
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
    }

    //  响应之前的调用
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode!=2 && resultCode != getActivity().RESULT_OK) {

            return;
        }
        switch (requestCode){
            case 0:
                Uri uri = data.getData();
                String[] proj = { MediaStore.Images.Media.DATA };
                // Cursor actualimagecursor = managedQuery(uri, proj, null, null,
                // null);
                Cursor actualimagecursor = getActivity().getContentResolver().query(uri, proj,
                        null, null, null);
                int actual_image_column_index = actualimagecursor
                        .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                actualimagecursor.moveToFirst();
                String img_path = actualimagecursor
                        .getString(actual_image_column_index);

                // 把结果显示在album活动中
                Intent myIntent =new Intent(getActivity(), Clip.class);
                Bundle mybundle = new Bundle();
                mybundle.putString("message",img_path);
                myIntent.putExtras(mybundle);
                startActivityForResult(myIntent, 2);

                break;

            case 1:

                String img_path2 = Environment.getExternalStorageDirectory() +"/"+IMAGE_FILE_NAME;



                Intent myIntent2 =new Intent(getActivity(), Clip.class);
                Bundle mybundle2 = new Bundle();
                mybundle2.putString("message",img_path2);
                myIntent2.putExtras(mybundle2);
                startActivityForResult(myIntent2, 2);

                break;

            case 2://from clip

                if (mMe.clipflag==1){
                    mMe.clipflag = 0;
                    String img_path3 = savePhotoToSDCard(bitmap_from_clip,Environment.getExternalStorageDirectory().toString(),IMAGE_FILE_NAME );
                    try{
                        AVFile file = AVFile.withAbsoluteLocalPath(IMAGE_FILE_NAME , Environment.getExternalStorageDirectory()+"/" +IMAGE_FILE_NAME );
                        AVUser currentUser = AVUser.getCurrentUser();
                        currentUser.put("Avartar", file);
                        currentUser.saveInBackground(new SaveCallback() {

                            public void done(AVException e) {
                                if (e == null) {
                                    setDig("上传成功");
                                    cImageView.setImageBitmap(bitmap_from_clip);
                                } else {
                                    setDig("上传失败");
                                }
                            }
                        });
                    }catch (FileNotFoundException e){
                        //TODO
                        setDig("上传失败file");
                    }catch (IOException e){
                        setDig("上传失败io");
                        //TODO
                    }


                    //

                }

                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }




    private String savePhotoToSDCard(Bitmap photoBitmap,String path,String photoName){

        File dir = new File(path);
        if (!dir.exists()){
            dir.mkdirs();
        }
        File photoFile = new File(path , photoName);
        String filepath = photoFile.getAbsolutePath();

        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(photoFile);
            if (photoBitmap != null) {
                if (photoBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream)) {
                    fileOutputStream.flush();
//						fileOutputStream.close();
                }
            }
        } catch (FileNotFoundException e) {
            photoFile.delete();
            e.printStackTrace();
        } catch (IOException e) {
            photoFile.delete();
            e.printStackTrace();
        } finally{
            try {
                fileOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return  filepath;
    }


    private void setDig(String message){
        new AlertDialog.Builder(getActivity())
                .setTitle(message)
                .setNegativeButton("确认", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();

    }

}


