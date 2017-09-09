package com.foodlog;

import com.foodlog.util.MultipartUtility;



import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class Sender {

    private String botId;
    private String UrlTemplate      = "https://api.telegram.org/bot@@BOTID@@/sendmessage?chat_id=@@CHATID@@&text=@@TEXT@@";
    private String UrlDocumentTemplate = "https://api.telegram.org/bot@@BOTID@@/sendDocument?chat_id=@@CHATID@@";


    public Sender(String botId){
        this.botId = botId;
        this.UrlTemplate = UrlTemplate.replace("@@BOTID@@", botId);
        this.UrlDocumentTemplate = UrlDocumentTemplate.replace("@@BOTID@@", botId);

    }

    public void sendResponse(Integer id, String text_response) throws IOException {
        text_response = URLEncoder.encode(text_response, "UTF-8");
        URL url = new URL(UrlTemplate.replace("@@CHATID@@", id.toString()).replace("@@TEXT@@", text_response));

        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
    }

    public void sendDocument(Integer id, String image){
        URL url = null;
        try {
            url = new URL(UrlDocumentTemplate.replace("@@CHATID@@", id.toString()));

           /* OkHttpClient client = new OkHttpClient();
            File file = new File(image);

            RequestBody formBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("document", "document",
                            RequestBody.create(MediaType.parse("image/gif"), file))

                    .build();

            Request request = new Request.Builder().url(url).post(formBody).build();

            Response response = client.newCall(request).execute();
            System.out.println(response.body().string());
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);*/


            String charset = "UTF-8";
            String requestURL = url.toString();

            MultipartUtility multipart = new MultipartUtility(requestURL, charset);
            multipart.addFilePart("document", new File(image));
            String response = multipart.finish(); // response from server.
            System.out.println(response);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
