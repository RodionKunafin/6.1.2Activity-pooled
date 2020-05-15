package com.example.a612activity_pooled;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    private BaseAdapter listContentAdapter;
    public SharedPreferences sharedPref;
    public static String NOTE_TEXT = "note_text";
    private List<Map<String, String>> values = new ArrayList<>();
    private ArrayList<Integer> index = new ArrayList<>();



    public List<Map<String, String>> prepareContent() {
        sharedPref = getSharedPreferences("MyNote", MODE_PRIVATE);
        String[] strings = sharedPref.getString(NOTE_TEXT, null).split("\n");

        for (String string : strings) {
            Map<String, String> firstMap = new HashMap<>();
            firstMap.put("left", String.valueOf(string.length()));
            firstMap.put("right", string);
            values.add(firstMap);
        }
        return values;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ListView listView = findViewById(R.id.list);
        final SwipeRefreshLayout swipeLayout = findViewById(R.id.swipeRefresh);
        savedInstanceState.putIntegerArrayList(TAG,index);
        index = new ArrayList<>();


        sharedPref = getSharedPreferences("MyNote", MODE_PRIVATE);
        if (!sharedPref.contains(NOTE_TEXT)) {
            sharedPref.edit().putString(NOTE_TEXT, getString(R.string.large_text)).apply();
        }

        String[] from = {"left", "right"};
        int[] to = {R.id.left_text, R.id.right_text};
        listContentAdapter = new SimpleAdapter(this, prepareContent(), R.layout.item_simple, from, to);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                values.remove(0);
                index.add(values.indexOf(0));
                //values.remove(index.intValue());
                listContentAdapter.notifyDataSetChanged();
            }
        });
        listView.setAdapter(listContentAdapter);


        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                values.clear();
                prepareContent();
                listContentAdapter.notifyDataSetChanged();
                swipeLayout.setRefreshing(false);

            }
        });
    }

   /* @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putIntegerArrayList(TAG,index);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        savedInstanceState.getIntegerArrayList(TAG);
    }*/
}