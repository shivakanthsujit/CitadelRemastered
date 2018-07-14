package com.example.shivakanth.citadelremastered;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

import data.Data;

public class MainActivity extends AppCompatActivity {

    Button search;
    AutoCompleteTextView txt;
    ArrayList<String> history = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        search = findViewById(R.id.button);
        txt = findViewById(R.id.name);

        txt.setThreshold(0);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.select_dialog_item,history);
        txt.setAdapter(adapter);

        txt.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String t= (String)adapterView.getItemAtPosition(i);
                Toast.makeText(MainActivity.this, t, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this,DetailActivity.class );
                intent.putExtra("name",t);
                intent.putExtra("isHistory",1);
                startActivity(intent);
            }
        });

        search.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v)
            {
                String t= txt.getText().toString();
                Intent intent = new Intent(MainActivity.this,DetailActivity.class );
                intent.putExtra("name",t);
                intent.putExtra("isHistory",0);
                startActivityForResult(intent,1);
            }
        });


        }
        public void onActivityResult(int requestCode, int resultCode,Intent data)
        {
            super.onActivityResult(requestCode,resultCode,data);
            if (requestCode == 1)
            {
                if(resultCode == 1)
                {
                    history.add(data.getStringExtra("newC"));
                    Toast.makeText(this, data.getStringExtra("newC"), Toast.LENGTH_SHORT).show();
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.select_dialog_item,history);
                    txt.setAdapter(adapter);
                }


            }
        }

    public void saveDB(String save)
    {
        SQLiteDatabase DB = openOrCreateDatabase("history.db",MODE_PRIVATE,null);
        DB.execSQL("CREATE TABLE IF NOT EXISTS data (name VARCHAR(200))");


    }
    }




