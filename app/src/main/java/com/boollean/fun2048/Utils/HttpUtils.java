package com.boollean.fun2048.Utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.boollean.fun2048.Entity.MessageEntity;
import com.boollean.fun2048.Entity.User;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * 网络请求工具类
 *
 * @author Boollean
 */
public class HttpUtils {
    public static final String BASE_URL = "http://10.15.68.54:8080/appservice/";
    public static final String ADD_USER="addUser";
    public static final String UPDATE_USER="updateUser";
    public static final String UPDATE_USER_DATA="updateUserData";
    public static final String DELETE_USER = "deleteUser";
    public static final String UPLOAD_IMAGE="uploadImage";
    public static final String GET_BEST_100_USERS_4 = "getBest100Users4";
    public static final String GET_BEST_100_USERS_5 = "getBest100Users5";
    public static final String GET_BEST_100_USERS_6 = "getBest100Users6";
    public static final String UPDATE_SCORE_4 = "updateScore4";
    public static final String UPDATE_SCORE_5 = "updateScore5";
    public static final String UPDATE_SCORE_6 = "updateScore6";
    public static final String GET_MESSAGES = "getLatest100Messages";
    public static final String GET_IMAGE = "getImage";
    public static final String ADD_MESSAGE = "addMessage";
    private static final int TIME_OUT = 5 * 1000;   //超时时间
    private static final String CHARSET = "UTF-8"; //设置编码

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager manager = (ConnectivityManager) context
                .getApplicationContext().getSystemService(
                        Context.CONNECTIVITY_SERVICE);
        if (manager == null) {
            return false;
        }

        NetworkInfo networkinfo = manager.getActiveNetworkInfo();
        if (networkinfo == null || !networkinfo.isAvailable()) {
            return false;
        }
        return true;
    }

    public static String getJsonContent(String path) {
        try {
            URL url = new URL(BASE_URL + path);
            Log.i("Json",url.toString());
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(TIME_OUT);
            connection.setRequestMethod("GET");
            connection.setDoInput(true);
            int code = connection.getResponseCode();
            if (code == 200) {
                return changeInputString(connection.getInputStream());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "fail";
    }

    public static Bitmap getImage(String name) {
        Bitmap bitmap = null;
        Response response = null;
        InputStream inputStream = null;
        try {
            URL url = new URL(BASE_URL + GET_IMAGE+"?name="+name);
            OkHttpClient mOkHttpClient = new OkHttpClient();
            Request.Builder reqBuilder = new Request.Builder();
            Request request = reqBuilder
                    .url(url)
                    .build();

            Log.i("message", url.toString());

            response = mOkHttpClient.newCall(request).execute();

            if (response.isSuccessful()) {
                inputStream = response.body().byteStream();
                bitmap = BitmapFactory.decodeStream(inputStream);
                return bitmap;
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try {
                inputStream.reset();
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            response.close();
        }
        return bitmap;
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

    public static String getURLWithParams(String name) {
        StringBuilder url = new StringBuilder();
        url = new StringBuilder(BASE_URL + UPLOAD_IMAGE);
        url.append("?");
        url.append("name=");
        url.append(name);

        return url.toString();
    }

    public static String getURLWithParams(String name,String password){
        StringBuilder url = new StringBuilder(BASE_URL + DELETE_USER);
        url.append("?");
        url.append("name=");
        url.append(name);
        url.append("&");
        url.append("password=");
        url.append(password);

        return url.toString();
    }

    public static String getURLWithParams(int whichGame, String name, int score) {
        StringBuilder url = new StringBuilder();
        if (whichGame == 4) {
            url = new StringBuilder(BASE_URL + UPDATE_SCORE_4);
        }
        if (whichGame == 5) {
            url = new StringBuilder(BASE_URL + UPDATE_SCORE_5);
        }
        if (whichGame == 6) {
            url = new StringBuilder(BASE_URL + UPDATE_SCORE_6);
        }
        url.append("?");
        url.append("name=");
        url.append(name);
        url.append("&");
        url.append("score=");
        url.append(score);

        return url.toString();
    }

    public static String sendHttpRequest(String address, HttpCallbackListener listener) {
        String result = "fail";
        OkHttpClient mOkHttpClient = new OkHttpClient();

        Request.Builder reqBuilder = new Request.Builder();
        Request request = reqBuilder
                .url(address)
                .build();

        try{
            Response response = mOkHttpClient.newCall(request).execute();
            if (response.isSuccessful()) {
                String resultValue = response.body().string();
                listener.onFinish(resultValue);
                return resultValue;
            }
        } catch (Exception e) {
            e.printStackTrace();
            listener.onError(e);
        }
        return result;
    }

    public static String addUser(User user) {
        DataOutputStream ds = null;
        InputStream inputStream = null;
        InputStreamReader inputStreamReader = null;
        BufferedReader reader = null;
        StringBuffer resultBuffer = new StringBuffer();

        Map subject = new HashMap<String,Object>();
        subject.put("name",user.getName());
        subject.put("password",user.getPassword());
        subject.put("gender",user.getGender());
        subject.put("avatar","avatarPath");
        subject.put("bestscore4",user.getBestScore4());
        subject.put("bestscore5",user.getBestScore5());
        subject.put("bestscore6",user.getBestScore6());

        Map request = new HashMap<String,Object>();
        request.put("code",200);
        request.put("msg","success");
        request.put("subject",subject);

        Gson gson = new Gson();
        String jsonData = gson.toJson(request);
        Log.i("message",jsonData);
        HttpURLConnection connection = null;
        try {
            // 实例化URL对象。调用URL有参构造方法，参数是一个url地址；
            URL url = new URL(BASE_URL + ADD_USER);
            // 调用URL对象的openConnection()方法，创建HttpURLConnection对象；
            connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(TIME_OUT);
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            // 设置Http请求头信息
            connection.setUseCaches(false);
            connection.setRequestProperty("Connection", "Keep-Alive");
            connection.setRequestProperty("Accept", "*/*");
            connection.setRequestProperty("Charset", CHARSET);
            connection.setRequestProperty("Accept-Encoding", "gzip, deflate");
            connection.setRequestProperty("Cache-Control", "no-cache");
            // 这个比较重要，按照上面分析的拼装出Content-Type头的内容
            connection.setRequestProperty("Content-Type",
                    "application/json");
            // 调用HttpURLConnection对象的connect()方法，建立与服务器的真实连接；
            connection.connect();

            OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
            writer.write(jsonData);
            writer.flush();

            if (connection.getResponseCode() >= 300) {
                throw new Exception(
                        "HTTP Request is not success, Response code is " + connection.getResponseCode());
            }

            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                String tempLine = null;
                Log.i("message", connection.getResponseCode() + "");
                inputStream = connection.getInputStream();
                inputStreamReader = new InputStreamReader(inputStream,CHARSET);
                reader = new BufferedReader(inputStreamReader);
                tempLine = null;
                resultBuffer = new StringBuffer();
                while ((tempLine = reader.readLine()) != null) {
                    resultBuffer.append(tempLine);
                    resultBuffer.append("\n");
                    Log.i("message", resultBuffer.toString());
                }
            }
        } catch (Exception e) {
            resultBuffer = new StringBuffer("fail");
            e.printStackTrace();
        } finally {
            if (ds != null) {
                try {
                    ds.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (inputStreamReader != null) {
                try {
                    inputStreamReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            return resultBuffer.toString();
        }
    }


    public static String updateUser(String oldName,User user) {
        DataOutputStream ds = null;
        InputStream inputStream = null;
        InputStreamReader inputStreamReader = null;
        BufferedReader reader = null;
        StringBuffer resultBuffer = new StringBuffer();

        Map subject = new HashMap<String,Object>();
        subject.put("name",user.getName());
        subject.put("password",user.getPassword());
        subject.put("gender",user.getGender());
        subject.put("avatar","avatarPath");
        subject.put("bestscore4",user.getBestScore4());
        subject.put("bestscore5",user.getBestScore5());
        subject.put("bestscore6",user.getBestScore6());

        Map request = new HashMap<String,Object>();
        request.put("code",200);
        request.put("msg","success");
        request.put("oldName",oldName);
        request.put("subject",subject);
        Gson gson = new Gson();
        String jsonData = gson.toJson(request);
        Log.i("message",jsonData);
        HttpURLConnection connection = null;
        try {
            // 实例化URL对象。调用URL有参构造方法，参数是一个url地址；
            URL url = new URL(BASE_URL + UPDATE_USER);
            // 调用URL对象的openConnection()方法，创建HttpURLConnection对象；
            connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(TIME_OUT);
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            // 设置Http请求头信息
            connection.setUseCaches(false);
            connection.setRequestProperty("Connection", "Keep-Alive");
            connection.setRequestProperty("Accept", "*/*");
            connection.setRequestProperty("Charset", CHARSET);
            connection.setRequestProperty("Accept-Encoding", "gzip, deflate");
            connection.setRequestProperty("Cache-Control", "no-cache");
            // 这个比较重要，按照上面分析的拼装出Content-Type头的内容
            connection.setRequestProperty("Content-Type",
                    "application/json");
            // 调用HttpURLConnection对象的connect()方法，建立与服务器的真实连接；
            connection.connect();

            OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
            writer.write(jsonData);
            writer.flush();

            if (connection.getResponseCode() >= 300) {
                throw new Exception(
                        "HTTP Request is not success, Response code is " + connection.getResponseCode());
            }

            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                String tempLine = null;
                Log.i("message", connection.getResponseCode() + "");
                inputStream = connection.getInputStream();
                inputStreamReader = new InputStreamReader(inputStream,CHARSET);
                reader = new BufferedReader(inputStreamReader);
                tempLine = null;
                resultBuffer = new StringBuffer();
                while ((tempLine = reader.readLine()) != null) {
                    resultBuffer.append(tempLine);
                    resultBuffer.append("\n");
                    Log.i("message", resultBuffer.toString());
                }
            }
        } catch (Exception e) {
            resultBuffer = new StringBuffer("fail");
            e.printStackTrace();
        } finally {
            if (ds != null) {
                try {
                    ds.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (inputStreamReader != null) {
                try {
                    inputStreamReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return resultBuffer.toString();
        }
    }

    public static String updateUserData(User user) {
        DataOutputStream ds = null;
        InputStream inputStream = null;
        InputStreamReader inputStreamReader = null;
        BufferedReader reader = null;
        StringBuffer resultBuffer = new StringBuffer();

        Map subject = new HashMap<String,Object>();
        subject.put("name",user.getName());
        subject.put("password",user.getPassword());
        subject.put("gender",user.getGender());
        subject.put("avatar","avatarPath");
        subject.put("bestscore4",user.getBestScore4());
        subject.put("bestscore5",user.getBestScore5());
        subject.put("bestscore6",user.getBestScore6());

        Map request = new HashMap<String,Object>();
        request.put("code",200);
        request.put("msg","success");
        request.put("subject",subject);
        Gson gson = new Gson();
        String jsonData = gson.toJson(request);
        Log.i("message",jsonData);
        HttpURLConnection connection = null;
        try {
            // 实例化URL对象。调用URL有参构造方法，参数是一个url地址；
            URL url = new URL(BASE_URL + UPDATE_USER_DATA);
            // 调用URL对象的openConnection()方法，创建HttpURLConnection对象；
            connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(TIME_OUT);
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            // 设置Http请求头信息
            connection.setUseCaches(false);
            connection.setRequestProperty("Connection", "Keep-Alive");
            connection.setRequestProperty("Accept", "*/*");
            connection.setRequestProperty("Charset", CHARSET);
            connection.setRequestProperty("Accept-Encoding", "gzip, deflate");
            connection.setRequestProperty("Cache-Control", "no-cache");
            // 这个比较重要，按照上面分析的拼装出Content-Type头的内容
            connection.setRequestProperty("Content-Type",
                    "application/json");
            // 调用HttpURLConnection对象的connect()方法，建立与服务器的真实连接；
            connection.connect();

            OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
            writer.write(jsonData);
            writer.flush();

            if (connection.getResponseCode() >= 300) {
                throw new Exception(
                        "HTTP Request is not success, Response code is " + connection.getResponseCode());
            }

            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                String tempLine = null;
                Log.i("message", connection.getResponseCode() + "");
                inputStream = connection.getInputStream();
                inputStreamReader = new InputStreamReader(inputStream,CHARSET);
                reader = new BufferedReader(inputStreamReader);
                tempLine = null;
                resultBuffer = new StringBuffer();
                while ((tempLine = reader.readLine()) != null) {
                    resultBuffer.append(tempLine);
                    resultBuffer.append("\n");
                    Log.i("message", resultBuffer.toString());
                }
            }
        } catch (Exception e) {
            resultBuffer = new StringBuffer("fail");
            e.printStackTrace();
        } finally {
            if (ds != null) {
                try {
                    ds.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (inputStreamReader != null) {
                try {
                    inputStreamReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            return resultBuffer.toString();
        }
    }

    public static String addMessage(MessageEntity messageEntity) {
        DataOutputStream ds = null;
        InputStream inputStream = null;
        InputStreamReader inputStreamReader = null;
        BufferedReader reader = null;
        StringBuffer resultBuffer = new StringBuffer();

        Map subject = new HashMap<String,Object>();
        subject.put("name",messageEntity.getName());
        subject.put("date",messageEntity.getDate());
        subject.put("message",messageEntity.getMessage());

        Map request = new HashMap<String,Object>();
        request.put("code",200);
        request.put("msg","success");
        request.put("subject",subject);

        Gson gson = new Gson();
        String jsonData = gson.toJson(request);
        Log.i("Message",jsonData);
        HttpURLConnection connection = null;
        try {
            // 实例化URL对象。调用URL有参构造方法，参数是一个url地址；
            URL url = new URL(BASE_URL + ADD_MESSAGE);
            // 调用URL对象的openConnection()方法，创建HttpURLConnection对象；
            connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(TIME_OUT);
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            // 设置Http请求头信息
            connection.setUseCaches(false);
            connection.setRequestProperty("Connection", "Keep-Alive");
            connection.setRequestProperty("Accept", "*/*");
            connection.setRequestProperty("Charset", CHARSET);
            connection.setRequestProperty("Accept-Encoding", "gzip, deflate");
            connection.setRequestProperty("Cache-Control", "no-cache");
            // 这个比较重要，按照上面分析的拼装出Content-Type头的内容
            connection.setRequestProperty("Content-Type",
                    "application/json");
            // 调用HttpURLConnection对象的connect()方法，建立与服务器的真实连接；
            connection.connect();

            OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
            writer.write(jsonData);
            writer.flush();
            Log.i("Message","ResponseCode= "+String.valueOf(connection.getResponseCode()));
            if (connection.getResponseCode() >= 300) {
                throw new Exception(
                        "HTTP Request is not success, Response code is " + connection.getResponseCode());
            }

            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                String tempLine = null;
                Log.i("Message", connection.getResponseCode() + "");
                inputStream = connection.getInputStream();
                inputStreamReader = new InputStreamReader(inputStream,CHARSET);
                reader = new BufferedReader(inputStreamReader);
                resultBuffer = new StringBuffer();
                while ((tempLine = reader.readLine()) != null) {
                    resultBuffer.append(tempLine);
                    resultBuffer.append("\n");
                    Log.i("message", resultBuffer.toString());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            resultBuffer = new StringBuffer("fail");
        } finally {
            if (ds != null) {
                try {
                    ds.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (inputStreamReader != null) {
                try {
                    inputStreamReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            return resultBuffer.toString();
        }
    }

    public static String upLoadImage(String name,String imagePath){
        OkHttpClient mOkHttpClient = new OkHttpClient();

        String result = "fail";
        MultipartBody.Builder builder = new MultipartBody.Builder();
        builder.addFormDataPart("image", imagePath, RequestBody.create(MediaType.parse("image/jpg"), new File(imagePath)));
        RequestBody requestBody = builder.build();
        Request.Builder reqBuilder = new Request.Builder();
        Request request = reqBuilder
                .url(BASE_URL + UPLOAD_IMAGE+"?name="+name)
                .post(requestBody)
                .build();

        try{
            Response response = mOkHttpClient.newCall(request).execute();
            if (response.isSuccessful()) {
                String resultValue = response.body().string();
                return resultValue;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public interface HttpCallbackListener{
        void onFinish(String s);
        void onError(Exception e);
    }
}


