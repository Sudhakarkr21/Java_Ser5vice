package com.example.java_ser5vice;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import static com.example.java_ser5vice.Constant.DELETE;
import static com.example.java_ser5vice.Constant.LOGIN;
import static com.example.java_ser5vice.Constant.LOGIN_FAILURE;
import static com.example.java_ser5vice.Constant.NOT_DELETED;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    EditText name, age, id, location;
    RecevievData recevievData;
    Button vew, delete,update,insert;
    List<Student> students;
    Dialog dialog;
    String msg = "";
    Student student1;
    ProgressDialog progressDialog;
    Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            switch (message.what) {
                case LOGIN:
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Success..", Toast.LENGTH_SHORT).show();
                    break;
                case LOGIN_FAILURE:
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
                    break;
                case DELETE:
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(),"Record deleted",Toast.LENGTH_SHORT).show();
                    break;
                case NOT_DELETED:
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(),"Record is not deleted",Toast.LENGTH_SHORT).show();
                    break;
            }
            return false;
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initialize();
    }

    void initialize() {
        insert = findViewById(R.id.insert);
        insert.setOnClickListener(this);
        student1 = new Student();
        update = findViewById(R.id.update);
        update.setOnClickListener(this);
        delete = findViewById(R.id.delete);
        delete.setOnClickListener(this);
        vew = findViewById(R.id.save);
        vew.setOnClickListener(this);
        recevievData = new RecevievData();
        name = findViewById(R.id.name);
        age = findViewById(R.id.age);
        id = findViewById(R.id.id);
        location = findViewById(R.id.location);



    }


    @Override
    public void onClick(View view) {
        int i = view.getId();

        switch (i) {
            case R.id.save:
                Intent intent = new Intent(MainActivity.this,Data.class);
                startActivity(intent);
                break;
            case R.id.update:
                final AlertDialog.Builder a = new AlertDialog.Builder(this);
                a.setTitle("Update the Data");
                a.setIcon(R.drawable.ic_attach_money);
                LinearLayout v = (LinearLayout) getLayoutInflater().inflate(R.layout.alert_update,null);
                final EditText name2 = v.findViewById(R.id.name2);
                final EditText age2 = v.findViewById(R.id.age2);
                final EditText loc2 = v.findViewById(R.id.loc2);
                final EditText id2 = v.findViewById(R.id.id2);

//                if(Patterns.EMAIL_ADDRESS.matcher(loc2.getText().toString()).matches())

                a.setPositiveButton("Update", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        try {
                            Student student = new Student();
                            student.setName(name2.getText().toString());
                            student.setAge(age2.getText().toString());
                            student.setLocation(loc2.getText().toString());
                            student.setId((id2.getText().toString()));
                            a.setCancelable(true);
                            Gson gson = new Gson();
                            progressdialog("Update the data");
                            final String json = gson.toJson(student);

                            JSONObject jsonObject = new JSONObject(json);
                            RecevievData.UpdateData updateData = recevievData.new UpdateData(handler,jsonObject);
                            updateData.execute();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });

                a.setView(v);
                dialog = a.create();
                dialog.show();
                break;

            case R.id.delete:
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Delete the Record");
                LinearLayout linearLayout = (LinearLayout) getLayoutInflater().inflate(R.layout.alert_update,null);
                final EditText name3 = linearLayout.findViewById(R.id.name2);
                final EditText age3 = linearLayout.findViewById(R.id.age2);
                final EditText loc3 = linearLayout.findViewById(R.id.loc2);
                final EditText id3 = linearLayout.findViewById(R.id.id2);
                name3.setVisibility(View.GONE);
                age3.setVisibility(View.GONE);
                loc3.setVisibility(View.GONE);

                builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        progressdialog("Deleting the Message");
                        Student student = new Student();
                        student.setId((id3.getText().toString()));
                        Gson gson = new Gson();
                        String json1 = gson.toJson(student);
                        try {
                            JSONObject g = new JSONObject(json1);
                            RecevievData.Delete delete = recevievData.new Delete(handler,id3.getText().toString());
                            delete.execute();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });
              builder.setView(linearLayout);
              dialog = builder.create();
              dialog.show();
                break;

            case R.id.insert:
                progressdialog("Inserting Plz wait");
                student1.setName(name.getText().toString());
                student1.setAge(age.getText().toString());
                student1.setLocation(location.getText().toString());
//                student.setId(id.getText().toString());
                Gson gson1 = new Gson();
                String json1 = gson1.toJson(student1);
                try {
                    JSONObject jsonObject = new JSONObject(json1);
                    RecevievData.Insert_Record insert_record = recevievData.new Insert_Record(handler,jsonObject);
                    insert_record.execute();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                break;

        }
    }

    public void progressdialog(String msg){
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Loading...");
        progressDialog.setMessage(msg);
        progressDialog.show();
    }
}
