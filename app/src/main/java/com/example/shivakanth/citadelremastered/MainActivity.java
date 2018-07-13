package com.example.shivakanth.citadelremastered;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    Button search;
    EditText txt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        search = findViewById(R.id.button);
        txt = findViewById(R.id.name);
        search.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v)
            {
                String t= txt.getText().toString();
                Intent intent = new Intent(MainActivity.this,DetailActivity.class );
                intent.putExtra("name",t);
                startActivity(intent);
            }
        });


        }
    }




