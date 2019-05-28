package com.example.asus_pc.trainer.until;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.example.asus_pc.trainer.bean.HealthBean;
import com.example.asus_pc.trainer.bean.JsoupBean;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class GetJsonFromTianAndParse {
    private int REQUEST_CODE = 200;
    public static final  int PARSESUCCESS  = 0x2019;
    public static final int FAILED =  0x2018;
    private Handler handler;
    private Message msg = new Message();
    //private final String url = "http://route.showapi.com/90-88?showapi_appid=93506&id=5cade137948498fff2bf1829&showapi_sign=04fb86a11ab644e285c260a126209ced";
    private final String url = "http://api.tianapi.com/health/?&key=91c18dac214b396a151a14d54251018b&num=10";

    public GetJsonFromTianAndParse(Handler handler){
        this.handler = handler;
    }

    public void getJsonFromTian(){
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
                        JsoupBean jsoupBean = null;
                        try {
                            healthBean = parseJson(in);
                            //jsoupBean = jsoupData(healthBean);
                            Log.e("in", String.valueOf(in));

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        if (healthBean!= null) {
                            msg.what = PARSESUCCESS;
                            msg.obj = healthBean;
                           // msg.obj = jsoupBean;
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
        JSONArray jsonArray = jsonObject.getJSONArray("newslist");
        HealthBean healthBean = new HealthBean();
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject_body = jsonArray.getJSONObject(i);
            String ctime = jsonObject_body.getString("ctime");
            String title = jsonObject_body.getString("title");
            String description = jsonObject_body.getString("description");
            String url = jsonObject_body.getString("url");
            healthBean.setCtime(ctime);
            healthBean.setTitle(title);
            healthBean.setDescription(description);
            healthBean.setUrl(url);
        }
        return healthBean;
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

    private JsoupBean jsoupData(final HealthBean healthBean){
        final JsoupBean jsoupBean = new JsoupBean();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Document document = Jsoup.connect(healthBean.getUrl()).get();
                    Log.e("heal.getUrl", healthBean.getUrl());
                    Elements elements = document.select("div.post_text>p.f_center").select("img");
                    List<String> imgList = new ArrayList();
                    for (Element element : elements) {
                        imgList.add(element.attr("src"));
                        Log.e("src", element.attr("src"));
                    }
                    jsoupBean.setPic(imgList);
                    Elements elements1 = document.select("div.post_text>p");
                    List<String> temList = new ArrayList<>();
                    for (Element element : elements1) {
                        temList.add(element.text());
                        Log.e("text", element.text());
                    }
                    jsoupBean.setContentList(temList);

                } catch (Exception e) {
                    Log.e("e", e.toString());
                }
            }
        }).start();
        return jsoupBean;
    }
}
