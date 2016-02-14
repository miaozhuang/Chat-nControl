package com.example.zhuangmi.chatncontrol;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

/**
 * Created by Y510P on 12/26/2015.
 */
public class AddUserActivity extends Activity {

    private Integer[] imageId={

            R.drawable.h0,R.drawable.h1,
            R.drawable.h2,R.drawable.h3,
            R.drawable.h4,R.drawable.h5,
            R.drawable.h6,R.drawable.h7,
            R.drawable.h8,R.drawable.h9,
            R.drawable.h10,R.drawable.h11,
            R.drawable.h12,R.drawable.h13,
            R.drawable.h14,R.drawable.h15,
            R.drawable.h16,R.drawable.h17,
            R.drawable.h18,
    };

    private EditText user_Name;

    private EditText password;

    private EditText password_confirm;

    private EditText email;

    private Button  confirm;

    private Button  cancel;

    private GridView head_view;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.adduser);
        user_Name   =   (EditText)this.findViewById(R.id.adduser_user_name);
        password    =   (EditText)this.findViewById(R.id.adduser_pwd);
        password_confirm = (EditText)this.findViewById(R.id.adduser_pwd_confirm);
        email        =   (EditText)this.findViewById(R.id.adduser_email);
        head_view   =   (GridView)this.findViewById(R.id.add_user_gridView_head_choose);
        confirm     =   (Button)this.findViewById(R.id.adduser_button_new_user_ok);
        cancel     =   (Button)this.findViewById(R.id.adduser_button_new_user_cancel);

        confirm.setOnClickListener(new View.OnClickListener() {
                                       @Override
                                       public void onClick(View v) {
                                           Toast.makeText(AddUserActivity.this, "ahoy", Toast.LENGTH_SHORT).show();
                                       }
                                   }
            );

        cancel.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                user_Name.setText("");
                password.setText("");
                password_confirm.setText("");
                email.setText("");
            }
        });

            final AddUserActivity addUser = this;
            BaseAdapter adapter = new BaseAdapter() {

                @Override
                public int getCount() {
                    return imageId.length;
                }

                @Override
                public Object getItem(int position) {
                    return position;
                }

                @Override
                public long getItemId(int position) {
                    return position;
                }

                @Override
                public View getView(int position, View convertView, ViewGroup parent) {
                    ImageView imageView;
                    if (convertView == null) {
                        imageView = new ImageView(addUser);
                        imageView.setAdjustViewBounds(true);
                        imageView.setMaxHeight(58);
                        imageView.setMaxWidth(50);
                        imageView.setPadding(0, 0, 0, 0);
                    } else {
                        imageView = (ImageView) convertView;
                    }
                    imageView.setImageResource(imageId[position]);
                    return imageView;
                }
            };
            head_view.setAdapter(adapter);
            head_view.setOnItemClickListener(new AdapterView.OnItemClickListener()

                                             {
                                                 @Override
                                                 public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                /*
                Intent intent=getIntent();
                Bundle bundle=new Bundle();
                bundle.putInt("imageId",imageId[position]);
                intent.putExtras(bundle);
                setResult(0x11,intent);
                finish();
                */
                                                     Toast.makeText(AddUserActivity.this, "" + position, Toast.LENGTH_SHORT).show();
                                                     head_view.getAnimation();
                                                     for (int i = 0; i < parent.getCount(); i++) {
                                                         View v = parent.getChildAt(i);
                                                         if (position == i) {//当前选中的Item改变背景颜色
                                                             view.setBackgroundColor(Color.YELLOW);
                                                         } else {
                                                             v.setBackgroundColor(Color.TRANSPARENT);
                                                         }
                                                     }
                                                 }
                                             }

            );

        }

    }
