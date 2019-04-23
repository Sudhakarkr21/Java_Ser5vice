package com.example.java_ser5vice;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.Toast;

import static com.example.java_ser5vice.Constant.DELETE;
import static com.example.java_ser5vice.Constant.LOGIN;
import static com.example.java_ser5vice.Constant.LOGIN_FAILURE;
import static com.example.java_ser5vice.Constant.NOT_DELETED;

public class Data extends AppCompatActivity {
    MyAdapter myAdapter;
    RecyclerView recyclerView;
    ProgressDialog progressDialog;
    RecevievData recevievData;
    Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            switch (message.what){
                case DELETE:
                    Toast.makeText(getApplicationContext(),"Deleted..",Toast.LENGTH_SHORT).show();
                    fetchdata();
                    break;
                case NOT_DELETED:
                    Toast.makeText(getApplicationContext(),"Deleted..",Toast.LENGTH_SHORT).show();
                    break;
                case LOGIN:
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(),"Success..",Toast.LENGTH_SHORT).show();
                    break;
                case LOGIN_FAILURE:
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(),"Failure..",Toast.LENGTH_SHORT).show();
                    break;
            }
            return false;
        }
    });
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.data);
        recevievData = new RecevievData();
        recyclerView = findViewById(R.id.recyclerView);

        fetchdata();
    }

    public void fetchdata(){
//        setProgressDialog("Fetching the Data","Plz wait");
        RecevievData.FetchData fetchData = recevievData.new FetchData(recyclerView,myAdapter,Data.this,handler);
        fetchData.execute();
    }

    public void setProgressDialog(String title,String message){
        progressDialog = new ProgressDialog(Data.this);
        progressDialog.setTitle(title);
        progressDialog.setMessage(message);
        progressDialog.show();
    }


}