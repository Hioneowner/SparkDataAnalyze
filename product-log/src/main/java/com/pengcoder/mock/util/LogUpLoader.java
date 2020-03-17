package com.pengcoder.mock.util;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class LogUpLoader {


    public static final void uplog(String json) {
        try {

            System.out.println("=========start=======");
            URL url = new URL("http://localhost:8080/log");
            HttpURLConnection connection =  (HttpURLConnection)url.openConnection();

            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

            connection.setDoOutput(true);



            connection.setRequestProperty("clientTime",System.currentTimeMillis() + "");

            connection.setRequestMethod("POST");


            OutputStream outputStream = connection.getOutputStream();
            outputStream.write(("log=" + json).getBytes());

            outputStream.flush();
            outputStream.close();

            System.out.println(json);
            System.out.println("================");
            System.out.println(connection.getResponseCode());

        } catch (Exception e) {
            e.printStackTrace();
        }


    }



}
