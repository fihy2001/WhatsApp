package com.example.whatsapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Chat extends AppCompatActivity implements View.OnClickListener {
    private ListView chatListView;
    private ArrayList<String> chatsList;
    private ArrayAdapter adapter;
    private String selectedUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        chatListView = findViewById(R.id.listConversation);
        chatsList = new ArrayList<>();
        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, chatsList);
        chatListView.setAdapter(adapter);


        findViewById(R.id.btnSend).setOnClickListener(this);

        selectedUser = getIntent().getStringExtra("selectedUser");

        try {
            ParseQuery<ParseObject> firstUserChatQuery = ParseQuery.getQuery("Chat");
            ParseQuery<ParseObject> secondUserChatQuery = ParseQuery.getQuery("Chat");

            firstUserChatQuery.whereEqualTo("waSender", ParseUser.getCurrentUser().getUsername());
            firstUserChatQuery.whereEqualTo("waTargetRecipient", selectedUser);

            secondUserChatQuery.whereEqualTo("waSender", selectedUser);
            secondUserChatQuery.whereEqualTo("waTargetRecipient", ParseUser.getCurrentUser().getUsername());

            ArrayList<ParseQuery<ParseObject>> allQueries = new ArrayList<>();
            allQueries.add(firstUserChatQuery);
            allQueries.add(secondUserChatQuery);

            ParseQuery<ParseObject> myQuery = ParseQuery.or(allQueries);
            myQuery.orderByAscending("createdAt");

            myQuery.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> objects, ParseException e) {
                    if (objects.size() > 0 && e == null) {
                        for (ParseObject chatObject : objects) {
                            String waMessage = chatObject.get("waMessage") + "";
                            if (chatObject.get("waSender").equals(ParseUser.getCurrentUser().getUsername())) {
                                waMessage = ParseUser.getCurrentUser().getUsername() + ": " + waMessage;
                            }
                            if (chatObject.get("waSender").equals(selectedUser)) {
                                waMessage = selectedUser + ": " + waMessage;
                            }
                            chatsList.add(waMessage);

                        }
                        adapter.notifyDataSetChanged();

                    }
                }
            });
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View view) {
        EditText edtMessage = findViewById(R.id.edtMessage);

        if (edtMessage.getText().toString().equals("")){
            FancyToast.makeText(Chat.this, "Message is required", Toast.LENGTH_LONG,
                    FancyToast.INFO, false).show();
        }
        else {

            edtMessage = findViewById(R.id.edtMessage);

            ParseObject chat = new ParseObject("Chat");
            chat.put("waSender", ParseUser.getCurrentUser().getUsername());
            chat.put("waTargetRecipient", selectedUser);
            chat.put("waMessage", edtMessage.getText().toString());

            final EditText finalEdtMessage = edtMessage;
            final EditText finalEdtMessage1 = edtMessage;
            chat.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    if (e== null){
                        FancyToast.makeText(Chat.this, "Message saved", Toast.LENGTH_LONG,
                                FancyToast.SUCCESS, false).show();
                        chatsList.add(ParseUser.getCurrentUser().getUsername() + ": " + finalEdtMessage.getText().toString());
                        adapter.notifyDataSetChanged();
                        finalEdtMessage1.setText("");
                    } else {
                        FancyToast.makeText(Chat.this, e.getMessage(), Toast.LENGTH_LONG,
                                FancyToast.ERROR, false).show();
                    }
                }
            });

        }
    }

}