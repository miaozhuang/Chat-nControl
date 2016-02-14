package com.example.zhuangmi.chatncontrol;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import io.yunba.android.manager.YunBaManager;

/**
 * Created by Y510P on 12/24/2015.
 */
public class ChatContentActivity extends Activity {

    private static final String TAG = ChatContentActivity.class.getSimpleName();
    private ListView    talkView;
    private Button      messageButton;
    private EditText    messageText;
    private String      strID;
    private ArrayList<ChatMsgEntity> list = new ArrayList();

    private ChatDBAdapter dbTest;

    public ChatContentActivity() {
    }

    public void onCreate(Bundle savedInstanceState) {
        Log.v(TAG, "onCreate >>>>>>");
        try {
            super.onCreate(savedInstanceState);
            this.setContentView(R.layout.chatcontent);
            this.talkView = (ListView) this.findViewById(R.id.list_content);
            this.messageButton = (Button) this.findViewById(R.id.MessageButton);
            this.messageText = (EditText) this.findViewById(R.id.MessageText);

            dbTest = new ChatDBAdapter(this);
            dbTest.open();
            final ChatDBAdapter dbTestTemp = dbTest;


            final ListView finalTalkView = this.talkView;

            Intent intent = getIntent();

            strID = intent.getStringExtra("id_number");

            messageText.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    messageText.setHint(null);
                }
            });

            this.findViewById(R.id.dbutton).setOnClickListener(new View.OnClickListener() {


                @Override
                public void onClick(View v) {
                    Cursor cTest = dbTestTemp.getAllName();
                    int nCount = cTest.getCount();
                    Toast.makeText(getApplicationContext(),
                            "Count :" + nCount, Toast.LENGTH_LONG)
                            .show();
                    while(cTest.moveToNext())
                    {
                        String strValue= cTest.getString(2);
                        Toast.makeText(getApplicationContext(), "Content :" + strValue, Toast.LENGTH_LONG).show();
                    }

                }
            });


            View.OnClickListener messageButtonListener = new View.OnClickListener() {
                public void onClick(View arg0) {
                    Log.v(ChatContentActivity.TAG, "onclick >>>>>>>>");
                    String name = ChatContentActivity.this.getName();
                    String date = ChatContentActivity.this.getDate();
                    String msgText = ChatContentActivity.this.getText();
                    if (msgText.trim().isEmpty()) {
                        return;
                    }
                    int RId_other = R.layout.other_msg;
                    ChatMsgEntity newMessage_other = new ChatMsgEntity(name, date, msgText, RId_other, false);
                    ChatContentActivity.this.list.add(newMessage_other);
                    ChatContentActivity.this.talkView.setAdapter(new ChatMsgViewAdapter(ChatContentActivity.this, ChatContentActivity.this.list));
                    //ChatContentActivity.this.messageText.setText("");


                    int RId_my = R.layout.my_msg;
                    date = ChatContentActivity.this.getDate();
                    ChatMsgEntity newMessage_my = new ChatMsgEntity(name, date, msgText, RId_my, true);
                    ChatContentActivity.this.list.add(newMessage_my);
                    ChatContentActivity.this.talkView.setAdapter(new ChatMsgViewAdapter(ChatContentActivity.this, ChatContentActivity.this.list));
                    ChatContentActivity.this.messageText.setText("");
                    finalTalkView.setSelection(finalTalkView.getBottom());
                    //dbTestTemp.addNameList(msgText, name, "");
                    long lRtn= dbTestTemp.addChatContent(strID, date,"me", msgText);
                    publish("1", msgText);
                }
            };
            this.messageButton.setOnClickListener(messageButtonListener);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        registerMessageReceiver();
        ChatnControlApplication currentApp = (ChatnControlApplication)this.getApplication();
        currentApp.setCCA(this);
    }

    private String getName() {
        //return this.getResources().getString(2130968578);
        return "me";
    }

    private String getDate() {
        Calendar c = Calendar.getInstance();

        String date = String.valueOf(c.get(Calendar.YEAR)) + "-" + c.get(Calendar.MONTH) + "-" + c.get(Calendar.DAY_OF_MONTH)+" "+c.get(Calendar.HOUR_OF_DAY)+":"+c.get(Calendar.MINUTE)+":"+c.get(Calendar.SECOND);
        return date;
    }

    private String getText() {
        return this.messageText.getText().toString();
    }

    public void onDestroy() {
        Log.v(TAG, "onDestroy>>>>>>");
        super.onDestroy();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus)
    {
        if(hasFocus)
        {
            if(dbTest!=null) {
                Cursor data = dbTest.getAllChats(strID);
                data.moveToFirst();
                do{
                    String strTime = data.getString(1);
                    String strPerson = data.getString(2);
                    String strContent = data.getString(3);
                    if(strPerson.equals("me"))
                    {
                        int RId_my = R.layout.my_msg;
                        ChatMsgEntity newMessage_my = new ChatMsgEntity(strPerson, strTime, strContent, RId_my, true);
                        ChatContentActivity.this.list.add(newMessage_my);
                        ChatContentActivity.this.talkView.setAdapter(new ChatMsgViewAdapter(ChatContentActivity.this, ChatContentActivity.this.list));
                    }
                    else
                    {
                        int RId_other = R.layout.other_msg;
                        ChatMsgEntity newMessage_other = new ChatMsgEntity(strPerson, strTime, strContent, RId_other, false);
                        ChatContentActivity.this.list.add(newMessage_other);
                        ChatContentActivity.this.talkView.setAdapter(new ChatMsgViewAdapter(ChatContentActivity.this, ChatContentActivity.this.list));
                    }
                }
                while(data.moveToNext());
                this.talkView.setSelection(this.talkView.getBottom());
            }
        }
    }

    public void receiveMsg(String msg)
    {
        String name = ChatContentActivity.this.getName();
        String date = ChatContentActivity.this.getDate();
        String msgText = msg;
        if (msgText.trim().isEmpty()) {
            return;
        }
        int RId_other = R.layout.other_msg;
        ChatMsgEntity newMessage_other = new ChatMsgEntity(name, date, msgText, RId_other, false);
        ChatContentActivity.this.list.add(newMessage_other);
        ChatContentActivity.this.talkView.setAdapter(new ChatMsgViewAdapter(ChatContentActivity.this, ChatContentActivity.this.list));
    }

    private void publish(String topic, String msg) {
        if (TextUtils.isEmpty(topic) || TextUtils.isEmpty(msg)) {
            Toast.makeText(ChatContentActivity.this, "String should not be null", Toast.LENGTH_SHORT).show();
            return;
        }
        addTopic(topic);
        setCostomMsg("Publish msg = " + msg + " to topic = " + topic);
        final String fTopic = topic;
        final String fMsg   = msg;
        YunBaManager.publish(getApplicationContext(), topic, msg, new IMqttActionListener() {
            public void onSuccess(IMqttToken asyncActionToken) {

                String msgLog = "Publish succeed : " + fTopic;
                StringBuilder showMsg = new StringBuilder();
                showMsg.append("[Demo] publish msg")
                        .append(" = ").append(fMsg).append(" to ")
                        .append(YunBaManager.MQTT_TOPIC).append(" = ").append(fTopic).append(" succeed");
                setCostomMsg(showMsg.toString());
                MessageUtil.showToast(msgLog, getApplicationContext());
            }

            @Override
            public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                String msg = "[Demo] Publish topic = " + fTopic + " failed : " + exception.getMessage();
                setCostomMsg(msg);
                MessageUtil.showToast(msg, getApplicationContext());

            }
        });
        SharePrefsHelper.setString(getApplicationContext(), YunBaManager.LAST_PUB, topic);
    }



    public static void setCostomMsg(String strMsg)
    {
        Log.v(TAG, "Send :" + strMsg);
        //Toast.makeText(ChatContentActivity.this.getApplicationContext(), "Content : " + strMsg, Toast.LENGTH_LONG).show();
    }
    private List<String> topics = new ArrayList<String>();

    private void addTopic(String topic)  {
        if(MessageUtil.isEmpty(topic)) return;
        for (int i = 0; i < topics.size(); i++) {
            if (topic.equals(topics.get(i))) return;
        }
        topics.add(topic);
        Log.i(TAG, "Topic size = " + topics.size());
    }


    private BroadcastReceiver mMessageReceiver;
    public void registerMessageReceiver() {
        mMessageReceiver = new MessageReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(YunBaManager.MESSAGE_RECEIVED_ACTION);
        filter.addCategory(getPackageName());
        registerReceiver(mMessageReceiver, filter);

        IntentFilter filterCon = new IntentFilter();
        filterCon.addAction(YunBaManager.MESSAGE_CONNECTED_ACTION);
        filterCon.addCategory(getPackageName());
        registerReceiver(mMessageReceiver, filterCon);

        IntentFilter filterDis = new IntentFilter();
        filterDis.addAction(YunBaManager.MESSAGE_DISCONNECTED_ACTION);
        filterDis.addCategory(getPackageName());
        registerReceiver(mMessageReceiver, filterDis);

        IntentFilter pres = new IntentFilter();
        pres.addAction(YunBaManager.PRESENCE_RECEIVED_ACTION);
        pres.addCategory(getPackageName());
        registerReceiver(mMessageReceiver, pres);

    }

    public static void  setTitleOfApp(final String status) {
        /*
        Activity parent = this.getParent();
        if(!MessageUtil.isEmpty(status) && null != parent) {
            this.getParent().setTitle(status);
        }
        */
    }

    static public class MessageReceiver extends BroadcastReceiver {
        public MessageReceiver()
        {
            super();
        }
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.i(TAG, "Action - " + intent.getAction());
            if (YunBaManager.MESSAGE_RECEIVED_ACTION.equals(intent.getAction())) {
                String status = "YunBa - Connected";
                ChatContentActivity.setTitleOfApp(status);
                String topic = intent.getStringExtra(YunBaManager.MQTT_TOPIC);
                String msg = intent.getStringExtra(YunBaManager.MQTT_MSG);
                StringBuilder showMsg = new StringBuilder();
                showMsg.append("[Message] ").append(YunBaManager.MQTT_TOPIC)
                        .append(" = ").append(topic).append(" ,")
                        .append(YunBaManager.MQTT_MSG).append(" = ").append(msg);
                ChatContentActivity.setCostomMsg(showMsg.toString());
                ChatnControlApplication application = (ChatnControlApplication) context.getApplicationContext();
                application.getCCA().receiveMsg(showMsg.toString());
                //ChatContentActivity.this.receiveMsg(showMsg);
            } else if(YunBaManager.MESSAGE_CONNECTED_ACTION.equals(intent.getAction())) {
                ChatContentActivity.setCostomMsg("[YunBa] Connected");
                String status = "YunBa - Connected";
                ChatContentActivity.setTitleOfApp(status);
                //SharePrefsHelper.setString(ChatContentActivity.getApplicationContext(), CONNECT_STATUS, status);
            } else if(YunBaManager.MESSAGE_DISCONNECTED_ACTION.equals(intent.getAction())) {
                ChatContentActivity.setCostomMsg("[YunBa] DisConnected");
                String status = "YunBa - DisConnected";
                ChatContentActivity.setTitleOfApp(status);
                //SharePrefsHelper.setString(getApplicationContext(), CONNECT_STATUS, status);
            } else if (YunBaManager.PRESENCE_RECEIVED_ACTION.equals(intent.getAction())) {

                String status = "YunBa - Connected";
                ChatContentActivity.setTitleOfApp(status);
                String topic = intent.getStringExtra(YunBaManager.MQTT_TOPIC);
                String msg = intent.getStringExtra(YunBaManager.MQTT_MSG);
                StringBuilder showMsg = new StringBuilder();
                showMsg.append("[Message from prensence] ").append(YunBaManager.MQTT_TOPIC)
                        .append(" = ").append(topic).append(" ,")
                        .append(YunBaManager.MQTT_MSG).append(" = ").append(msg);
                ChatContentActivity.setCostomMsg(showMsg.toString());

            }
            Log.i(TAG, "Action - " + intent.getAction()+" end");
        }
    }
}
