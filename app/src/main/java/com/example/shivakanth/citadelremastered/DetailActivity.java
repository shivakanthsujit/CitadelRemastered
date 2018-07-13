package com.example.shivakanth.citadelremastered;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import data.ApiUtils;
import data.Character;
import data.Data;
import data.GOTService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailActivity extends AppCompatActivity {

    private GOTService mService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Intent intent = getIntent();
        String message = intent.getStringExtra("name");
        mService = ApiUtils.getGOTService();
        Call<Character> call=ApiUtils.getGOTService().getCharacter(message);
        call.enqueue(new Callback<Character>() {
            @Override
            public void onResponse(Call<Character> call, Response<Character> response) {
                Data c = response.body().getData();
                Toast.makeText(DetailActivity.this, c.getName(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<Character> call, Throwable t) {

            }
        });

    }

}
