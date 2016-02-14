package com.example.zhuangmi.chatncontrol;

import android.app.Application;
import android.util.Log;

import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;

import io.yunba.android.manager.YunBaManager;

/**
 * Created by Y510P on 12/28/2015.
 */
public class ChatnControlApplication extends Application {
    private final static String TAG = "ChatnControlApplication";
    @Override
    public void onCreate() {
        super.onCreate();
        initConnectStatus();
        startBlackService();
    }

    private void initConnectStatus() {
        //set MainActivity title status
        SharePrefsHelper.setString(getApplicationContext(), MainActivity.CONNECT_STATUS, "");
    }

    private void startBlackService() {
        YunBaManager.start(getApplicationContext());

        IMqttActionListener listener = new IMqttActionListener() {

            @Override
            public void onSuccess(IMqttToken asyncActionToken) {
                String topic = MessageUtil.join(asyncActionToken.getTopics(), ",");
                Log.d(TAG, "Subscribe succeed : " + topic);
//				DemoUtil.showToast( "Subscribe succeed : " + topic, getApplicationContext());
                StringBuilder showMsg = new StringBuilder();
                showMsg.append("subscribe succ：").append(YunBaManager.MQTT_TOPIC)
                        .append(" = ").append(topic);
            }

            @Override
            public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                String msg =  "Subscribe failed : " + exception.getMessage();
                Log.d(TAG, msg);
//				DemoUtil.showToast(msg, getApplicationContext());
//

            }
        };

        //for test
        YunBaManager.subscribe(getApplicationContext(), new String[]{"1", "t2", "t3"}, listener);
    }


    @Override
    public void onTerminate() {
        super.onTerminate();
    }


    private ChatContentActivity cca;

    public void setCCA(ChatContentActivity cca)
    {
        this.cca = cca;
    }

    public ChatContentActivity getCCA()
    {
        return this.cca;
    }
}
