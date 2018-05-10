package com.example.january.contact;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    private List<ContactDetails> contacts = new ArrayList<>();
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initialToolbar();
        initialBottomNav();
        initialContactsData();
        initialContactsListView();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater menu_inflater = getMenuInflater();
        menu_inflater.inflate(R.menu.main_activity_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
            case R.id.record:
                Toast.makeText(this, "你点击了通话记录", Toast.LENGTH_SHORT).show();
                break;
        }
        return true;
    }

    private void initialToolbar(){
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    private void initialContactsListView(){
        RecyclerView contacts_view = (RecyclerView)findViewById(R.id.contacts_list);
        LinearLayoutManager layout_manager = new LinearLayoutManager(this);
        contacts_view.setLayoutManager(layout_manager);

        ContactAdapter contact_adpater = new ContactAdapter(contacts);
        contacts_view.setAdapter(contact_adpater);

        contacts_view.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));

        contacts_view.addOnScrollListener(new ContactsListScrollListener((TextView)findViewById(R.id.hover_first_char)));
    }


    private void initialBottomNav(){
        TextView contacts_button = (TextView)findViewById(R.id.contacts_button);
        TextView recents_button = (TextView)findViewById(R.id.recents_button);

        contacts_button.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "你点击了联系人按钮", Toast.LENGTH_SHORT).show();
            }
        });

        recents_button.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "你点击了通话记录按钮", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private class ContactsListScrollListener extends RecyclerView.OnScrollListener{

        private int hover_first_char_view_top;
        private int hover_first_char_view_height;
        TextView hover_first_char_view;

        public ContactsListScrollListener(TextView _hover_first_char_view){
            Timer timer = new Timer(true);
            hover_first_char_view = _hover_first_char_view;

            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    hover_first_char_view_top = hover_first_char_view.getTop();
                    hover_first_char_view_height = hover_first_char_view.getMeasuredHeight();
                }
            }, 1000);


        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy){
            LinearLayoutManager layout_manager = (LinearLayoutManager)recyclerView.getLayoutManager();
            int f_item_num = layout_manager.findFirstVisibleItemPosition();
            int n_item_num = f_item_num + 1;
            View next_view = layout_manager.findViewByPosition(n_item_num);

            TextView hover_first_char_view = (TextView)findViewById(R.id.hover_first_char);
            String d_char = hover_first_char_view.getText().toString();
            String c_char = contacts.get(f_item_num).getFirstChar();
            String n_char = contacts.get(n_item_num).getFirstChar();

            if(!d_char.equals(c_char)){
                hover_first_char_view.setText(c_char);
            }

            int next_top = next_view.getTop();
            if(!c_char.equals(n_char) && (next_top < hover_first_char_view_height)){
                hover_first_char_view.setY(next_view.getTop() - hover_first_char_view_height + hover_first_char_view_top);
            }else{
                hover_first_char_view.setY(hover_first_char_view_top);
            }


            Log.d(TAG, "onScrolled: " + String.valueOf(hover_first_char_view_top));

        }

        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState){
            //Log.d(TAG, "onScrollStateChanged");
        }

    }

    private void initialContactsData() {
        for(int i=0; i<5; i++)
            contacts.add(new ContactDetails("Bob", "B"));
        for(int i=0; i<500; i++)
            contacts.add(new ContactDetails("Jack", "J"));
        for(int i=0; i<5; i++)
            contacts.add(new ContactDetails("小明", "X"));
    }
}
