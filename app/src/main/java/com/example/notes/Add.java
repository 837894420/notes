package com.example.notes;


import android.content.ContentResolver;
import android.content.ContentValues;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.lang.String;
import android.content.SharedPreferences;

public class Add extends AppCompatActivity implements OnClickListener{
        String Title,Content,simpleDate;
    Button ButtonAddCancel,ButtonAddSave;
    EditText EditTextAddTitle,EditTextAddContent,EditTextAddAuthor;
    String Author;


    Bitmap bm = null;
    Button btnPhone;
    ImageView imageView;
    private static final String IMAGE_UNSPECIFIED = "image/*";
    private final int IMAGE_CODE = 0; // 这里的IMAGE_CODE是自己任意定义的

    private TextView tv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        ButtonAddCancel = (Button)findViewById(R.id.ButtonAddCancel);
        ButtonAddSave = (Button)findViewById(R.id.ButtonAddSave);
        EditTextAddContent = findViewById(R.id.EditTextAddContent);
        EditTextAddTitle = findViewById(R.id.EditTextAddTitle);
        //EditTextAddAuthor = findViewById(R.id.EditTextAddAuthor);
        ButtonAddCancel.setOnClickListener(this);
        ButtonAddSave.setOnClickListener(this);



        btnPhone = (Button) findViewById(R.id.btnPhone);
        imageView = (ImageView) findViewById(R.id.imageView);
        tv = (TextView) findViewById(R.id.img_path);

        btnPhone.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {


                setImage1(); // 魅族显示风格：最新，照片，图库；华为：包含有相片的一组目录，
                // 小米：选择要使用的应用，最后没有结果

                // setImage(); //魅族显示风格：图库，文件选择(图片文件) ；华为：最近的照片 小米：选择要使用的应用，最后没有结果

            }

        });

    }
    private void setImage1() {
        Intent intent = new Intent(Intent.ACTION_PICK, null);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, IMAGE_UNSPECIFIED);
        startActivityForResult(intent, IMAGE_CODE);
    }

//    private void setImage() {
//
//        // 使用intent调用系统提供的相册功能，使用startActivityForResult是为了获取用户选择的图片
//
//        Intent getAlbum = new Intent(Intent.ACTION_GET_CONTENT);
//
//        getAlbum.setType(IMAGE_UNSPECIFIED);
//
//        startActivityForResult(getAlbum, IMAGE_CODE);
//
//    }





    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {



        //Bitmap bm = null;

        // 外界的程序访问ContentProvider所提供数据 可以通过ContentResolver接口

        ContentResolver resolver = getContentResolver();

        if (requestCode == IMAGE_CODE) {

            try {

                Uri originalUri = data.getData(); // 获得图片的uri


                bm = MediaStore.Images.Media.getBitmap(resolver, originalUri);

                imageView.setImageBitmap(ThumbnailUtils.extractThumbnail(bm, 100, 100));  //使用系统的一个工具类，参数列表为 Bitmap Width,Height  这里使用压缩后显示，否则在华为手机上ImageView 没有显示
                // 显得到bitmap图片
                // imageView.setImageBitmap(bm);

                String[] proj = { MediaStore.Images.Media.DATA };

                // 好像是android多媒体数据库的封装接口，具体的看Android文档
                Cursor cursor = managedQuery(originalUri, proj, null, null, null);

                // 按我个人理解 这个是获得用户选择的图片的索引值
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                // 将光标移至开头 ，这个很重要，不小心很容易引起越界
                cursor.moveToFirst();
                // 最后根据索引值获取图片路径
                String path = cursor.getString(column_index);
                tv.setText(path);
            } catch (IOException e) {
                Log.e("TAG-->Error", e.toString());

            }

            finally {
                return;
            }
        }

        super.onActivityResult(requestCode, resultCode, data);

    }

        @Override
        public void onClick (View v) {
            // 创建助手类的实例
            // CursorFactory的值为null,表示采用默认的工厂类
            MyDataBaseHelper dbHelper = new MyDataBaseHelper(this, "Note.db", null, 1);
            // 创建一个可读写的数据库
            SQLiteDatabase db = dbHelper.getWritableDatabase();


            switch (v.getId()) {
                case R.id.ButtonAddSave:
                    Date date = new Date();
                    DateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");        //配置时间格式
                    simpleDate = simpleDateFormat.format(date);
                    ContentValues values = new ContentValues();
                    Title = String.valueOf(EditTextAddTitle.getText());         //获取需要储存的值
                    Content = String.valueOf(EditTextAddContent.getText());
                    Author = String.valueOf(EditTextAddAuthor.getText());

                    if (imageView.getDrawable() == null) {

                        Toast.makeText(this, "请放入一个图片", Toast.LENGTH_LONG).show();

                    }
                    //获取 ImageView 的 Bitmap
                    else {
                        Bitmap bitmap1 = ((BitmapDrawable) ((ImageView) imageView).getDrawable()).getBitmap();
                        //创建一个字节数组输出流,流的大小为size
                        int size = bitmap1.getWidth() * bitmap1.getHeight() * 4;
                        ByteArrayOutputStream baos = new ByteArrayOutputStream(size);
                        //设置位图的压缩格式，质量为100%，并放入字节数组输出流中
                        bitmap1.compress(Bitmap.CompressFormat.PNG, 100, baos);
                        //将字节数组输出流转化为字节数组byte[]
                        byte[] imagedata1 = baos.toByteArray();
                        //Log.d("a","1:"+imagedata1);

                        Log.d("Title", Title);


                        if (Title.length() == 0) {               //标题为空给出提示
                            Toast.makeText(this, "请输入一个标题", Toast.LENGTH_LONG).show();
                        } else {

                           values.put("author", Author);
                            values.put("title", Title);
                            values.put("content", Content);
                            values.put("date", simpleDate);

                            values.put("image", imagedata1);

                            db.insert("Note", null, values);                 //将值传入数据库中

                            Add.this.setResult(RESULT_OK, getIntent());
                            Add.this.finish();


                        }

                        try {
                            //关闭字节数组输出流
                            baos.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }


//                        SharedPreferences.Editor editor = getSharedPreferences("data", MODE_PRIVATE).edit();
//                        editor.putString("author", Author);      //使用sharedperferences设置默认作者
//                       // editor.apply();
//                        editor.commit();
                        break;
                    }
                        case R.id.ButtonAddCancel:
                            Add.this.setResult(RESULT_OK, getIntent());
                            Add.this.finish();

                            break;
                    }

            }

}