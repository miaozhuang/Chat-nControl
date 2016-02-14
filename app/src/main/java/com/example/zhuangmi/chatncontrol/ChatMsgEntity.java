package com.example.zhuangmi.chatncontrol;

/**
 * Created by Y510P on 12/24/2015.
 */
public class ChatMsgEntity {
    private static final String TAG = ChatMsgEntity.class.getSimpleName();
    private String name;
    private String date;
    private String text;
    private int layoutID;



    private boolean isMe;


    public boolean isMe() {
        return isMe;
    }

    public void setIsMe(boolean isMe) {
        this.isMe = isMe;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return this.date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getText() {
        return this.text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getLayoutID() {
        return this.layoutID;
    }

    public void setLayoutID(int layoutID) {
        this.layoutID = layoutID;
    }

    public ChatMsgEntity() {
    }

    public ChatMsgEntity(String name, String date, String text, int layoutID) {
        this.name = name;
        this.date = date;
        this.text = text;
        this.layoutID = layoutID;
    }

    public ChatMsgEntity(String name, String date, String text, int layoutID, boolean isMe) {
        this.name = name;
        this.date = date;
        this.text = text;
        this.layoutID = layoutID;
        this.isMe = isMe;
    }
}