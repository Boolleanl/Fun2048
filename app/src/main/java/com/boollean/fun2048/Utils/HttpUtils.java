package com.boollean.fun2048.Utils;

import com.boollean.fun2048.User.User;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpUtils {

    public static final String BASE_URL = "http://api.douban.com/v2/movie/top250?count=1";
    //用于接收Http请求的servlet的URL地址，请自己定义
    public static final String originAddress = "http://localhost:8080/Server/servlet/LoginServlet";

    public static String getJsonContent(String path) {
        try {
            URL url = new URL(path);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(3000);
            connection.setRequestMethod("GET");
            connection.setDoInput(true);
            int code = connection.getResponseCode();
            if (code == 200) {
                return changeInputString(connection.getInputStream());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "获取数据失败";
    }

    private static String changeInputString(InputStream inputStream) {

        String jsonString = "";
        ByteArrayOutputStream outPutStream = new ByteArrayOutputStream();
        byte[] data = new byte[1024];
        int len = 0;
        try {
            while ((len = inputStream.read(data)) != -1) {
                outPutStream.write(data, 0, len);
            }
            jsonString = new String(outPutStream.toByteArray());

        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonString;
    }

    public static String getURLWithParams(String oldName, User user) {
        StringBuilder url = new StringBuilder(originAddress);
        url.append("?");
        url.append("oldName=");
        url.append(oldName);
        url.append("&");
        url.append("name=");
        url.append(user.getName());
        url.append("&");
        url.append("password=");
        url.append(user.getPassword());
        url.append("&");
        url.append("gender=");
        url.append(user.getGender());

        return url.toString();
    }

    public static String getURLWithParams(int whichGame,String name,int score){
        StringBuilder url = new StringBuilder();
        if (whichGame == 4){
           url = new StringBuilder(originAddress);
        }if (whichGame == 5){
            url = new StringBuilder(originAddress);
        }if (whichGame == 6){
            url = new StringBuilder(originAddress);
        }
        url.append("?");
        url.append("name=");
        url.append(name);
        url.append("&");
        url.append("score=");
        url.append(score);

        return url.toString();
    }

    public static void sendHttpRequest(String address,HttpCallbackListener listener) {
        HttpURLConnection connection = null;
        try{
            URL url = new URL(address);
            //使用HttpURLConnection
            connection = (HttpURLConnection) url.openConnection();
            //设置方法和参数
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(3000);
            connection.setReadTimeout(3000);
            connection.setDoInput(true);
            connection.setDoOutput(true);
            //获取返回结果
            InputStream inputStream = connection.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null){
                response.append(line);
            }
            //成功则回调onFinish
            if (listener != null){
                listener.onFinish(response.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
            //出现异常则回调onError
            if (listener != null){
                listener.onError(e);
            }
        }finally {
            if (connection != null){
                connection.disconnect();
            }
        }
    }

    public void upLoadFile(String originAddress,String fileName,String filePath){

    }

    public static interface HttpCallbackListener {
        void onFinish(String s);
        void onError(Exception e);
    }
}


