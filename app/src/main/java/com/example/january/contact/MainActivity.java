package com.example.january.contact;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.provider.ContactsContract;
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
import android.widget.SearchView;
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

        Intent intent = getIntent();
        if(intent != null){
            handleIntent(intent);
        }


        initialToolbar();
        initialBottomNav();
        initialContactsData();
        initialContactsListView();

    }

    @Override
    protected void onNewIntent(Intent intent){
        handleIntent(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater menu_inflater = getMenuInflater();
        menu_inflater.inflate(R.menu.main_activity_menu, menu);

        // Get the SearchView and set the searchable configuration
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.contact_search).getActionView();
        // Assumes current activity is the searchable activity
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconifiedByDefault(true   ); // Do not iconify the widget; expand it by default
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
            case R.id.setting:
                Toast.makeText(this, "你点击了设置按钮", Toast.LENGTH_SHORT).show();
                break;
        }
        return true;
    }

    private void initialToolbar(){
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setElevation(16);
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


            //Log.d(TAG, "onScrolled: " + String.valueOf(hover_first_char_view_top));

        }

        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState){
            //Log.d(TAG, "onScrollStateChanged");
        }

    }

    private void initialContactsData() {
        Cursor cursor = getContentResolver().query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null,
                null, "sort_key");
        if(cursor == null){
            return;
        }
        while (cursor.moveToNext()) {
            String name = cursor
                    .getString(cursor
                            .getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            String number = cursor
                    .getString(cursor
                            .getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            int num = contacts.size() - 1;
            if(num >= 0) {
                String last_name = contacts.get(num).getName();
                if (!name.equals(last_name))
                    contacts.add(new ContactDetails(name, name.substring(0, 1)));
            }else{
                contacts.add(new ContactDetails(name, name.substring(0, 1)));
            }
        }
        if (cursor != null) {
            cursor.close();
        }
    }
    private void handleIntent(Intent intent){
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            Toast.makeText(this, query, Toast.LENGTH_SHORT).show();
        }
    }
}
