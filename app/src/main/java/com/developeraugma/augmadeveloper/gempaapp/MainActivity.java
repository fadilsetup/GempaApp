package com.developeraugma.augmadeveloper.gempaapp;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.developeraugma.augmadeveloper.gempaapp.adapter.DataGempaHolder;
import com.developeraugma.augmadeveloper.gempaapp.adapter.RecycleViewAdapater;
import com.developeraugma.augmadeveloper.gempaapp.api.ApiInterface;
import com.developeraugma.augmadeveloper.gempaapp.model.Data;
import com.developeraugma.augmadeveloper.gempaapp.model.Gempa;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    ProgressBar progressBar;
    Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView=(RecyclerView)findViewById(R.id.recycleView);
        progressBar=(ProgressBar)findViewById(R.id.progressBar);
        context=this;

        recyclerView.setLayoutManager(new LinearLayoutManager(context));

        getDataGempa();
    }
    private void loading(boolean status){
        progressBar.setVisibility(status ? View.VISIBLE : View.GONE);
        recyclerView.setVisibility(status ? View.GONE : View.VISIBLE);
    }
    private void getDataGempa() {
        loading(true);
        ApiInterface apiInterface= ApiInterface.Factory.create(context);
        Call<Data> getData=apiInterface.getDataGempa();
        getData.enqueue(new Callback<Data>() {
            @Override
            public void onResponse(Call<Data> call, Response<Data> response) {
                Data ressData= response.body();
                if(ressData.getStatus().equals("success"));
                {
                    RecycleViewAdapater adapater=new RecycleViewAdapater<Gempa, DataGempaHolder>(R.layout.item_list,
                            DataGempaHolder.class,Gempa.class,ressData.getData()) {
                        @Override
                        protected void bindView(DataGempaHolder holder, Gempa model, final int position) {
                            holder.bind(context,model);
                            holder.itemView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Toast.makeText(context,"Data Ke "+position+ "clicked!",Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                    };
                    recyclerView.setAdapter(adapater);
                }

                loading(false);

            }

            @Override
            public void onFailure(Call<Data> call, Throwable t) {
                Toast.makeText(context,t.getMessage(),Toast.LENGTH_LONG).show();
                loading(false);
            }
        });
    }
}
