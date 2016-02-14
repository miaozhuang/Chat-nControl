package com.example.zhuangmi.chatncontrol;

import android.content.ContentResolver;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends ActionBarActivity {

    //Yunba Application start
    public final static String MESSAGE_RECEIVED_ACTION = "io.yunba.example.msg_received_action";
    public final static String CONNECT_STATUS = "connect_status";
    //Yunba Application end

    private final static String TAG = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ImageView MainPagePicture =  (ImageView) findViewById(R.id.imageView_MainPage);
/*
        Resources r =  this.getApplication().getApplicationContext().getResources();
        Uri uri =  Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://"
                + r.getResourcePackageName(R.drawable.main_page) + "/"
                + r.getResourceTypeName(R.drawable.main_page) + "/"
                + r.getResourceEntryName(R.drawable.main_page));
        //Bitmap bitmap = getHttpBitmap("http://blog.3gstdy.com/wp-content/themes/twentyten/images/headers/path.jpg");//从网上取图片
        //Bitmap bitmap = getLoacalBitmap(uri.toString());//从网上取图片
        //MainPagePicture .setImageBitmap(bitmap);	//设置Bitmap
*/
        int resId=this.getResources().getIdentifier("main_page", "drawable", getPackageName());
        MainPagePicture.setImageResource(resId);
        MainPagePicture.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        MainPagePicture.setScaleType(ImageView.ScaleType.FIT_XY);
        //MainPagePicture.setScaleType(ImageView.ScaleType.CENTER_CROP);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        final Intent intentList = new Intent();
        intentList.setClass(MainActivity.this, ChatListActivity.class);
        //Toast.makeText(this, "这是一个提示", Toast.LENGTH_SHORT).show();
        Timer timer5s = new Timer();
        final MainActivity mainThis = this;
        TimerTask task5s = new TimerTask(){
            @Override
            public void run() {
                //Toast.makeText(mainThis, "这是一个提示", Toast.LENGTH_SHORT).show();
                MainActivity.this.startActivity(intentList);
            }
        };
        timer5s.schedule(task5s, 1000*5);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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



    /**
     * 加载本地图片
     * http://bbs.3gstdy.com
     * @param url
     * @return
     */
    public static Bitmap getLoacalBitmap(String url) {
        try {
            FileInputStream fis = new FileInputStream(url);
            return BitmapFactory.decodeStream(fis);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }
    /**
     * 从服务器取图片
     *http://bbs.3gstdy.com
     * @param url
     * @return
     */
    public static Bitmap getHttpBitmap(String url) {
        URL myFileUrl = null;
        Bitmap bitmap = null;
        try {
            myFileUrl = new URL(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        try {
            HttpURLConnection conn = (HttpURLConnection) myFileUrl.openConnection();
            conn.setConnectTimeout(0);
            conn.setDoInput(true);
            conn.connect();
            InputStream is = conn.getInputStream();
            bitmap = BitmapFactory.decodeStream(is);
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
            Log.d(TAG, url);
        return bitmap;
    }
}
