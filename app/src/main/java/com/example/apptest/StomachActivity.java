package com.example.apptest;

import android.content.Context;
import android.os.Bundle;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.GridView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class StomachActivity extends AppCompatActivity {
    private final  String TAG = getClass().getSimpleName();
    private final String BASE_URL = "http://8edbcb6006ca.ngrok.io";
    private MyAPI mMyAPI;

    GridView gridView;
    private MedicineAdapter adapter;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stomach);
        initMyAPI(BASE_URL);
        gridView = (GridView) findViewById(R.id.gridView);
        adapter = new MedicineAdapter(this);
        ArrayList<MedicineItem> list = new ArrayList<MedicineItem>();
        Call<List<MedicineItem>> getCall = mMyAPI.get_medicines();

        getCall.enqueue(new Callback<List<MedicineItem>>() {
            @Override
            public void onResponse(Call<List<MedicineItem>> call, Response<List<MedicineItem>> response) {
                if(response.isSuccessful()) {
                    List<MedicineItem> mList = response.body();
                    for( MedicineItem item : mList){
                        String name = item.getName();
                        String explanation = item.getExplanation();
                        Float rate = item.getRate();
                        adapter.addItem(new MedicineItem(name, explanation, rate, R.drawable.test));
                    }
                    gridView.setAdapter(adapter);
                } else {
                    Log.d(TAG, "Status Code : " + response.code());
                }
            }
            @Override
            public void onFailure(Call<List<MedicineItem>> call, Throwable t) {
                Log.d(TAG,"Fail msg : " + t.getMessage());
            }
        });
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int index, long id) {
                MedicineItem item = (MedicineItem) adapter.getItem(index);
                Toast.makeText(getApplicationContext(), "??????: " + item.getName(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void initMyAPI(String baseUrl){

        Log.d(TAG,"initMyAPI : " + baseUrl);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        mMyAPI = retrofit.create(MyAPI.class);
    }
}
