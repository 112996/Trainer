package com.example.asus_pc.trainer.until;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.example.asus_pc.trainer.bean.HealthBean;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class GetJsonFromWanWeiAndParse {
    private int REQUEST_CODE = 200;
    public static final  int PARSESUCCESS  = 0x2019;
    public static final int FAILED =  0x2018;
    private Handler handler;
    private Message msg = new Message();
    private final String url = "http://route.showapi.com/90-88?showapi_appid=93506&id=5cade137948498fff2bf1829&showapi_sign=04fb86a11ab644e285c260a126209ced";

    public GetJsonFromWanWeiAndParse(Handler handler){
        this.handler = handler;
    }

    public void getJsonFromWanWei(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
                    conn.setConnectTimeout(2000);
                    conn.setRequestMethod("GET");
                    if (conn.getResponseCode() == REQUEST_CODE) {
                        InputStream in = conn.getInputStream();
                        HealthBean healthBean = null;
                        try {
                            healthBean = parseJson(in);
                            Log.e("in", String.valueOf(in));

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        if (healthBean!= null) {
                            msg.what = PARSESUCCESS;
                            msg.obj = healthBean;
                            handler.sendMessage(msg);
                        } else {
                            msg.what = FAILED;
                            handler.sendMessage(msg);
                        }
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    protected HealthBean parseJson(InputStream inputStream) throws Exception {
        byte[] jsonBytes = convertIsToByteArray(inputStream);
        String json = new String(jsonBytes);
        JSONObject jsonObject = new JSONObject(json);
        JSONObject jsonObject_body = jsonObject.getJSONObject("showapi_res_body");
        JSONObject jsonObject_item = jsonObject_body.getJSONObject("item");
        String tname = jsonObject_item.getString("tname");
        String media_name = jsonObject_item.getString("media_name");
        String content = jsonObject_item.getString("content");
        String title = jsonObject_item.getString("title");
        String ctime = jsonObject_item.getString("ctime");

        HealthBean healthBeanBean = new HealthBean();
        healthBeanBean.setTname(tname);
        healthBeanBean.setMedia_name(media_name);
        healthBeanBean.setContent(content);
        healthBeanBean.setTitle(title);
        healthBeanBean.setCtime(ctime);
        return healthBeanBean;
    }

    private byte[] convertIsToByteArray(InputStream inputStream) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte buffer[] = new byte[1024];
        int length = 0;
        while ((length = inputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, length);
        }
        inputStream.close();
        outputStream.flush();
        return outputStream.toByteArray();
    }
}
