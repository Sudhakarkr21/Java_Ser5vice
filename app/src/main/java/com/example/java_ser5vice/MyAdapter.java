package com.example.java_ser5vice;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
    private List<Student> students;
    private RecevievData recevievData = new RecevievData();
    private android.os.Handler handler;
    private Context context;
    int lastPosition = -1;
    MyAdapter(List<Student> students, Handler handler, Context context){
        this.students = students;
        this.handler = handler;
        this.context = context;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        View view = layoutInflater.inflate(R.layout.listirem,viewGroup,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Student student = students.get(i);
        viewHolder.name.setText(student.name);
        viewHolder.age.setText(student.age);
        viewHolder.loc.setText(student.location);
        viewHolder.id.setText(student.id);

//        if(i >lastPosition) {
//
//            Animation animation = AnimationUtils.loadAnimation(context,
//                    R.anim.up_from_bottom);
//            viewHolder.itemView.startAnimation(animation);
//            lastPosition = i;
//        }

    }

    @Override
    public int getItemCount() {
        return students.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView name,age,loc,id,delete,update;
        ViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name1);
            age = itemView.findViewById(R.id.age1);
            loc = itemView.findViewById(R.id.location1);
            id = itemView.findViewById(R.id.id1);
            delete = itemView.findViewById(R.id.delete);
            delete.setOnClickListener(this);
            update = itemView.findViewById(R.id.alert_update);
            update.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.delete:
                    int pos = getAdapterPosition();
                     recevievData.new Delete(handler,students.get(pos).getId()).execute();
                break;
                case R.id.alert_update:
                    int p = getAdapterPosition();
                    AlertDialog builder = new AlertDialog.Builder(view.getContext()).create();
                    LayoutInflater layoutInflater = LayoutInflater.from(view.getContext());
                    @SuppressLint("InflateParams") View v = layoutInflater.inflate(R.layout.alert_update, null);
                    final EditText name = v.findViewById(R.id.name2);
                    final EditText age = v.findViewById(R.id.age2);
                    final EditText loc = v.findViewById(R.id.loc2);
                    final EditText id = v.findViewById(R.id.id2);
                    name.setText(students.get(p).getName());
                    age.setText(students.get(p).getAge());
                    loc.setText(students.get(p).getLocation());
                    id.setText(students.get(p).getId());

                    id.setEnabled(false);
                    Button update = v.findViewById(R.id.bt_update);
                    update.setVisibility(View.VISIBLE);
                    update.setText(R.string.update);

                    update.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            try {
                                Student student = new Student();
                                student.setName(name.getText().toString());
                                student.setAge(age.getText().toString());
                                student.setLocation(loc.getText().toString());
                                student.setId((id.getText().toString()));
                                Gson gson = new Gson();
                                final String json = gson.toJson(student);

                                JSONObject jsonObject = new JSONObject(json);
                                RecevievData.UpdateData updateData = recevievData.new UpdateData(handler,jsonObject);
                                updateData.execute();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                    builder.setView(v);
                    builder.show();
                    break;
            }
        }
    }
}
