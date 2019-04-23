package com.example.java_ser5vice;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;

import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

import static com.example.java_ser5vice.Constant.DELETE;
import static com.example.java_ser5vice.Constant.LOGIN;
import static com.example.java_ser5vice.Constant.LOGIN_FAILURE;
import static com.example.java_ser5vice.Constant.NOT_DELETED;

public class RecevievData {
    private String UrlGetConnection(String Get_Url) throws IOException {
        String response;
        URL url = new URL(Get_Url);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setReadTimeout(4 * 60 * 1000);
        conn.setConnectTimeout(4 * 60 * 1000);
        int responseCode = conn.getResponseCode();
        if (responseCode == HttpsURLConnection.HTTP_OK) {
            String line;
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder responseBuilder = new StringBuilder();
            while ((line = br.readLine()) != null) {
                responseBuilder.append(line);
            }
            response = responseBuilder.toString();
        } else response = "";
        return response;
    }

    private String UrlPostConnection(String Post_Url, JSONObject jsonObject) throws IOException {
        String response;
        URL url = new URL(Post_Url);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setReadTimeout(4 * 60 * 1000);
        conn.setConnectTimeout(4 * 60 * 1000);
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setDoInput(true);
        conn.setDoOutput(true);

        OutputStream os = conn.getOutputStream();
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, StandardCharsets.UTF_8));
        String jsonResult = jsonObject.toString();
        writer.write(jsonResult);
        writer.flush();
        writer.close();
        os.close();
        int responseCode = conn.getResponseCode();
        if (responseCode == HttpsURLConnection.HTTP_OK) {
            String line;
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder responseBuilder = new StringBuilder();
            while ((line = br.readLine()) != null) {
                responseBuilder.append(line).append("\n");
            }
            response = responseBuilder.toString();
        } else response = "";
        return response;
    }

    private String UrlPostConnection(String Post_Url, HashMap<String, String> datamap) throws IOException {
        String response;
        URL url = new URL(Post_Url);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setReadTimeout(4 * 60 * 1000);
        conn.setConnectTimeout(4 * 60 * 1000);
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        conn.setDoInput(true);
        conn.setDoOutput(true);

        OutputStream os = conn.getOutputStream();
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
        writer.write(getPostDataString(datamap));
        writer.flush();
        writer.close();
        os.close();
        int responseCode = conn.getResponseCode();
        if (responseCode == HttpsURLConnection.HTTP_OK) {
            String line;
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder responseBuilder = new StringBuilder();
            while ((line = br.readLine()) != null) {
                responseBuilder.append(line).append("\n");
            }
            response = responseBuilder.toString();
        } else response = "";
        return response;
    }

    private String getPostDataString(HashMap<String, String> params) throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        boolean first = true;
        for (Map.Entry<String, String> entry : params.entrySet()) {
            if (first)
                first = false;
            else result.append("&");
            result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
        }
        return result.toString();
    }
    @SuppressLint("StaticFieldLeak")
    public class Insert_Record extends AsyncTask<String,String,String>{
        Handler handler;
        JSONObject jsonObject;
        String result = "";

        public Insert_Record(Handler handler, JSONObject jsonObject) {
            this.handler = handler;
            this.jsonObject = jsonObject;
        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                result = UrlPostConnection("http://192.168.100.48:8081/WebServiceExample/saveStudent",jsonObject);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            getInsert_Record(handler,s);
        }
    }

    @SuppressLint("StaticFieldLeak")
    public class UpdateData extends AsyncTask<String,String,String>{
        Handler handler;
        JSONObject jsonObject;
        String result="";

        public UpdateData(Handler handler, JSONObject jsonObject) {
            this.handler = handler;
            this.jsonObject = jsonObject;
        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                result = UrlPostConnection("http://192.168.100.48:8081/WebServiceExample/updateStudent",jsonObject);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            getUpdate_Record(handler,s);
        }
    }
    @SuppressLint("StaticFieldLeak")
    public class FetchData extends AsyncTask<String, String, String> {

        String response = "";
        Handler handler;
        Context context;
        MyAdapter myAdapter;
        RecyclerView recyclerView;


        public FetchData( RecyclerView recyclerView,MyAdapter myAdapter,Context context,Handler handler) {
            this.recyclerView = recyclerView;
            this.myAdapter = myAdapter;
            this.context = context;
            this.handler = handler;
        }

        @Override
        protected String doInBackground(String... strings) {
            try {

                response = UrlGetConnection("http://192.168.100.48:8081/WebServiceExample/all");
            } catch (Exception e) {
                e.printStackTrace();
            }
            return response;
        }

        @Override
        protected void onPostExecute(String result) {
            getStudent(result,recyclerView,myAdapter,context,handler);
            super.onPostExecute(result);
        }
    }

    @SuppressLint("StaticFieldLeak")
    public class Delete extends AsyncTask<String, String, String> {
        String response = "";
        Handler handler;
        JSONObject jsonObject;
        String id;

        public Delete(Handler handler,String id) {
            this.handler = handler;
            this.id = id;

        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                HashMap<String,String> map = new HashMap<>();
                map.put("id",id);
                response = UrlPostConnection("http://192.168.100.48:8081/WebServiceExample/delete",map);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return response;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            deletebyid(s,handler);
        }
    }

    public void getInsert_Record(Handler handler,String result){
        if(!TextUtils.isEmpty(result)){
            handler.sendEmptyMessage(LOGIN);
        }else handler.sendEmptyMessage(LOGIN_FAILURE);
    }

    public void getUpdate_Record(Handler handler,String result){
        if(!TextUtils.isEmpty(result)){
            handler.sendEmptyMessage(LOGIN);
        }else handler.sendEmptyMessage(LOGIN_FAILURE);

    }

    public void deletebyid(String s,Handler handler) {
        if (!TextUtils.isEmpty(s)) {
            handler.sendEmptyMessage(DELETE);
        } else {
            handler.sendEmptyMessage(NOT_DELETED);}
    }

    public void getStudent(String string, RecyclerView recyclerView,MyAdapter myAdapter,Context context,Handler handler) {

        if (!TextUtils.isEmpty(string)) {
            List<Student> getSetvalues = getResponses(string);
            if (getSetvalues.size() > 0) {
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
                myAdapter = new MyAdapter(getSetvalues,handler,context);
                recyclerView.setAdapter(myAdapter);

                final LayoutAnimationController controller =
                        AnimationUtils.loadLayoutAnimation(context, R.anim.layout_animation_up_to_down);
                recyclerView.setLayoutAnimation(controller);
                myAdapter.notifyDataSetChanged();
                recyclerView.scheduleLayoutAnimation();
//                handler.sendEmptyMessage(LOGIN);
            }
//            else handler.sendEmptyMessage(LOGIN_FAILURE);
        }
    }

    public List<Student> getResponses(String data) {
        return Arrays.asList(new Gson().fromJson(data, Student[].class));
    }

}
