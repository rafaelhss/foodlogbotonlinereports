package com.foodlog;

import okhttp3.*;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class Sender {

    private String botId;
    private String UrlTemplate      = "https://api.telegram.org/bot@@BOTID@@/sendmessage?chat_id=@@CHATID@@&text=@@TEXT@@";
    private String UrlImageTemplate = "http://api.telegram.org/bot@@BOTID@@/sendPhoto?chat_id=@@CHATID@@";


    public Sender(String botId){
        this.botId = botId;
        this.UrlTemplate = UrlTemplate.replace("@@BOTID@@", botId);
    }

    public void sendResponse(Integer id, String text_response) throws IOException {
        text_response = URLEncoder.encode(text_response, "UTF-8");
        URL url = new URL(UrlTemplate.replace("@@CHATID@@", id.toString()).replace("@@TEXT@@", text_response));

        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
    }

    public void sendImage(Integer id, String image){
        URL url = null;
        try {
            url = new URL(UrlTemplate.replace("@@CHATID@@", id.toString()));

            OkHttpClient client = new OkHttpClient();
            File file = new File(image);

            RequestBody formBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("file", "image.gif",
                            RequestBody.create(MediaType.parse("image/gif"), file))

                    .build();

            Request request = new Request.Builder().url(url).post(formBody).build();

            Response response = client.newCall(request).execute();
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
