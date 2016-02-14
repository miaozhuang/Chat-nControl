package com.example.zhuangmi.chatncontrol;

import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.*;

import io.yunba.android.manager.YunBaManager;

/**
 * Created by Y510P on 12/20/2015.
 */
public class ChatListActivity  extends Activity {

    private final static String TAG = "ChatListActivity";

    private ListView lv;

    private List<HashMap<String, Object>> data;

    private ChatDBAdapter dbTest4ChatList;

    private PopupWindow pop;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chatlist);
        lv = (ListView)findViewById(R.id.lv);
        //获取将要绑定的数据设置到data中
        // init the database
        dbTest4ChatList = new ChatDBAdapter(this);
        dbTest4ChatList.open();
        data = getData();
        MyAdapter adapter = new MyAdapter(this);
        lv.setAdapter(adapter);
        this.findViewById(R.id.imageButton_add_new_person).setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Add new person", Toast.LENGTH_LONG).show();
                /* its popupwindow method but it is hard to control the button and gridview
                LayoutInflater inflater = (LayoutInflater) ChatListActivity.this
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View layout = inflater.inflate(R.layout.adduser,
                        (ViewGroup) findViewById(R.id.adduser_popup));

                pop = new PopupWindow(layout, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
                pop.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
                pop.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

                pop.setFocusable(true);
                pop.setBackgroundDrawable(new BitmapDrawable());
                backgroundAlpha(0.3f);
                pop.showAtLocation(layout, Gravity.CENTER, 0, 0);
                //pop.setContentView(layoutOfPopup);
                pop.update();
                pop.setOnDismissListener(new PopupWindow.OnDismissListener() {

                    @Override
                    public void onDismiss() {
                        backgroundAlpha(1f);
                    }
                });

                */
                // the effect of dialog is much better than the popupwindow
                /* add new user
                Intent i = new Intent(ChatListActivity.this, AddUserActivity.class);
                */
                Intent i = new Intent(ChatListActivity.this, AddFriendActivity.class);
                startActivityForResult(i, 0);

            }
        });


        // ListView Item Click Listener
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                // ListView Clicked item index
                int itemPosition = position;

                // ListView Clicked item value
                //HashMap<String, Object> person = (HashMap<String, Object>) parent.getItemAtPosition(itemPosition);
                String strPerson = parent.getItemAtPosition(itemPosition).toString();
                // String itemValue = (String) parent.getItemAtPosition(position);
                String strTemp = data.get(itemPosition).toString();
                // Show Alert
                //Toast.makeText(getApplicationContext(), "Content :" + strTemp/*person.get("info")*/, Toast.LENGTH_LONG).show();
                strTemp = strTemp.substring(strTemp.indexOf("id=") + 3, strTemp.indexOf(", img="));
                Toast.makeText(getApplicationContext(),"ID Number :" + strTemp/*person.get("info")*/, Toast.LENGTH_LONG).show();
                final Intent intentList = new Intent();
                intentList.putExtra("id_number", strTemp);
                intentList.setClass(ChatListActivity.this, ChatContentActivity.class);
                ChatListActivity.this.startActivity(intentList);
            }

        });

    }



    private List<HashMap<String, Object>> getData()
    {
        Resources r =  this.getApplication().getApplicationContext().getResources();
        Uri uri =  Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://"
                + r.getResourcePackageName(R.drawable.test) + "/"
                + r.getResourceTypeName(R.drawable.test) + "/"
                + r.getResourceEntryName(R.drawable.test));
        List<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
        HashMap<String, Object> map;


        Cursor cNameList = dbTest4ChatList.getAllName();
        //get the attribute number of the NameList
        int nNameListCount = cNameList.getCount();
        int nNameListAreaCount = cNameList.getColumnCount();
        ArrayList<String> testList = new ArrayList<String>();
        for(int nCnt = 0; nCnt < nNameListAreaCount; nCnt++)
        {
            testList.add(cNameList.getColumnName(nCnt));
        }

        if(nNameListCount > 0) {
            cNameList.moveToFirst();
             do{
                map = new HashMap<String, Object>();
                map.put("img", R.drawable.test);
                map.put("title", cNameList.getString(2));
                map.put("info", cNameList.getString(4));
                map.put("id",cNameList.getString(1));
                list.add(map);
            }while (cNameList.moveToNext());
        }
        cNameList.close();
        if(list.isEmpty())
        {
            for (int i = 0; i < 10; i++) {
                map = new HashMap<String, Object>();
                String newPicPath = "/drawable/h0";
                //getimage(uri.getPath());
                //transImage(uri.getPath(),newPicPath , 80, 48,100);
                map.put("img", R.drawable.test);
                map.put("title", "跆拳道" + i);
                map.put("info", "快乐源于生活..." + i + i);
                list.add(map);
            }
        }
        return list;
    }

    //ViewHolder静态类
    static class ViewHolder
    {
        public ImageView img;
        public TextView title;
        public TextView info;
        public TextView id;
    }

    public class MyAdapter extends BaseAdapter
    {
        private LayoutInflater mInflater = null;
        private MyAdapter(Context context)
        {
            //根据context上下文加载布局，这里的是Demo17Activity本身，即this
            this.mInflater = LayoutInflater.from(context);
        }
        @Override
        public int getCount() {
            //How many items are in the data set represented by this Adapter.
            //在此适配器中所代表的数据集中的条目数
            return data.size();
        }
        @Override
        public Object getItem(int position) {
            // Get the data item associated with the specified position in the data set.
            //获取数据集中与指定索引对应的数据项
            return position;
        }
        @Override
        public long getItemId(int position) {
            //Get the row id associated with the specified position in the list.
            //获取在列表中与指定索引对应的行id
            return position;
        }

        //Get a View that displays the data at the specified position in the data set.
        //获取一个在数据集中指定索引的视图来显示数据
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            //如果缓存convertView为空，则需要创建View
            if(convertView == null)
            {
                holder = new ViewHolder();
                //根据自定义的Item布局加载布局
                convertView = mInflater.inflate(R.layout.chatlist_item, null);
                holder.img      =   (ImageView)convertView.findViewById(R.id.img);
                holder.title    =   (TextView)convertView.findViewById(R.id.name);
                holder.info     =   (TextView)convertView.findViewById(R.id.info);
                holder.id     =   (TextView)convertView.findViewById(R.id.id);
                //将设置好的布局保存到缓存中，并将其设置在Tag里，以便后面方便取出Tag
                convertView.setTag(holder);
            }else
            {
                holder = (ViewHolder)convertView.getTag();
            }
            holder.img.setBackgroundResource((Integer)data.get(position).get("img"));
            holder.title.setText((String) data.get(position).get("title"));
            holder.info.setText((String)data.get(position).get("info"));
            holder.id.setText((String)data.get(position).get("id"));
            return convertView;
        }

    }

    private Bitmap getimage(String srcPath) {
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        //开始读入图片，此时把options.inJustDecodeBounds 设回true了
        newOpts.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeFile(srcPath,newOpts);//此时返回bm为空

        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        //现在主流手机比较多是800*480分辨率，所以高和宽我们设置为
        float hh = 800f;//这里设置高度为800f
        float ww = 480f;//这里设置宽度为480f
        //缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;//be=1表示不缩放
        if (w > h && w > ww) {//如果宽度大的话根据宽度固定大小缩放
            be = (int) (newOpts.outWidth / ww);
        } else if (w < h && h > hh) {//如果高度高的话根据宽度固定大小缩放
            be = (int) (newOpts.outHeight / hh);
        }
        if (be <= 0)
            be = 1;
        newOpts.inSampleSize = be;//设置缩放比例
        //重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
        return compressImage(bitmap);//压缩好比例大小后再进行质量压缩
    }

    private Bitmap compressImage(Bitmap image) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        while ( baos.toByteArray().length / 1024>100) {	//循环判断如果压缩后图片是否大于100kb,大于继续压缩
            baos.reset();//重置baos即清空baos
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中
            options -= 10;//每次都减少10
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//把压缩后的数据baos存放到ByteArrayInputStream中
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);//把ByteArrayInputStream数据生成图片
        return bitmap;
    }

    // change the window alpha
    public void backgroundAlpha(float bgAlpha)
    {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        getWindow().setAttributes(lp);
    }



}
