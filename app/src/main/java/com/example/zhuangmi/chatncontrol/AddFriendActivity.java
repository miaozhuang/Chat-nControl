package com.example.zhuangmi.chatncontrol;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by Y510P on 12/27/2015.
 */
public class AddFriendActivity extends Activity {

    private EditText addFriend_ID;
    private Button  addFriend_Confirm;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addfriend);
        addFriend_ID=(EditText)this.findViewById(R.id.addfriend_id);
        addFriend_Confirm=(Button)this.findViewById(R.id.addfriend_confirm);

        addFriend_Confirm.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String strFriendID = addFriend_ID.getText().toString();
                //deal with the friend ID
                Toast.makeText(AddFriendActivity.this, "Add new friend:" + strFriendID, Toast.LENGTH_SHORT).show();
            }
        });
    }
}

