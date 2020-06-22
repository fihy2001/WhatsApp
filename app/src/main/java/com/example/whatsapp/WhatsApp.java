package com.example.whatsapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.LogOutCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.util.ArrayList;
import java.util.List;

public class WhatsApp extends AppCompatActivity implements AdapterView.OnItemClickListener{
    private ListView listViewUsers;
    private ArrayList<String> waUsers;
    private ArrayAdapter adapter;
    private SwipeRefreshLayout swipeRefresh;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_whats_app);
        setTitle("What's App Users");

        listViewUsers = findViewById(R.id.listViewUsers);
        listViewUsers.setOnItemClickListener(this);
        waUsers = new ArrayList<>();
        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, waUsers);

        swipeRefresh = findViewById(R.id.pullToRefresh);

        try {
            ParseQuery<ParseUser> parseQuery = ParseUser.getQuery();
            parseQuery.whereNotEqualTo("username", ParseUser.getCurrentUser().getUsername());
            parseQuery.findInBackground(new FindCallback<ParseUser>() {
                @Override
                public void done(List<ParseUser> users, ParseException e) {
                    if (e == null) {
                        if (users.size() > 0) {
                            for (ParseUser user : users) {
                                waUsers.add(user.getUsername());
                            }
                            listViewUsers.setAdapter(adapter);
                        }
                    }
                }
            });


        } catch (Exception e) {
            e.printStackTrace();
        }

        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                try {
                    ParseQuery<ParseUser> parseQuery = ParseUser.getQuery();
                    parseQuery.whereNotEqualTo("username", ParseUser.getCurrentUser().getUsername());
                    parseQuery.whereNotContainedIn("username", waUsers);
                    parseQuery.findInBackground(new FindCallback<ParseUser>() {
                        @Override
                        public void done(List<ParseUser> users, ParseException e) {
                            if (users.size() > 0) {
                                if (e == null) {
                                    for (ParseUser user : users) {
                                        waUsers.add(user.getUsername());
                                    }
                                    adapter.notifyDataSetChanged();
                                    if (swipeRefresh.isRefreshing()) {
                                        swipeRefresh.setRefreshing(false);
                                    }
                                }
                            } else {
                                if (swipeRefresh.isRefreshing()){
                                    swipeRefresh.setRefreshing(false);
                                }
                            }
                        }
                    });

                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.logout_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        ParseUser.getCurrentUser().logOutInBackground(new LogOutCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    Intent intent = new Intent(WhatsApp.this, Login.class);
                    startActivity(intent);
                    finish();
                } else {
                    FancyToast.makeText(WhatsApp.this, e.getMessage(), Toast.LENGTH_LONG,
                            FancyToast.ERROR, false);
                }

            }
        });
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        Intent intent = new Intent(WhatsApp.this, Chat.class);
        intent.putExtra("selectedUser", waUsers.get(position));
        startActivity(intent);
    }
}