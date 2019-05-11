package com.boollean.fun2048.Utils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.boollean.fun2048.Entity.MessageEntity;
import com.boollean.fun2048.Entity.User;
import com.boollean.fun2048.R;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * 网络请求工具类。
 *
 * @author Boollean
 */
public class HttpUtils {
    public static final String GET_BEST_100_USERS_4 = "getBest100Users4";
    public static final String GET_BEST_100_USERS_5 = "getBest100Users5";
    public static final String GET_BEST_100_USERS_6 = "getBest100Users6";
    public static final String GET_MESSAGES = "getLatest100Messages";
    private static final String BASE_URL = "http://10.14.150.201:8080/appservice/";
    private static final String ADD_USER = "addUser";
    private static final String UPDATE_USER = "updateUser";
    private static final String UPDATE_USER_DATA = "updateUserData";
    private static final String DELETE_USER = "deleteUser";
    private static final String UPLOAD_IMAGE = "uploadImage";
    private static final String UPDATE_SCORE_4 = "updateScore4";
    private static final String UPDATE_SCORE_5 = "updateScore5";
    private static final String UPDATE_SCORE_6 = "updateScore6";
    private static final String GET_IMAGE = "getImage";
    private static final String ADD_MESSAGE = "addMessage";
    private static final int TIME_OUT = 5 * 1000;   //超时时间
    private static final String CHARSET = "UTF-8"; //设置编码

    /**
     * 判断手机网络是否可用
     * @param context 上下文
     * @return 判断结果可用与否
     */
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager manager = (ConnectivityManager) context
                .getApplicationContext().getSystemService(
                        Context.CONNECTIVITY_SERVICE);
        if (manager == null) {
            return false;
        }

        NetworkInfo networkinfo = manager.getActiveNetworkInfo();
        return networkinfo != null && networkinfo.isAvailable();
    }

    /**
     * 获取response中的Json字段
     * @param path 请求路径尾部字段
     * @return Json字段
     */
    public static String getJsonContent(String path) {
        try {
            URL url = new URL(BASE_URL + path);
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

    /**
     * 下载头像图片
     * @param name 用户名
     * @return 用户的头像
     */
    public static Bitmap getImage(String name) {
        Bitmap bitmap;
        Response response = null;
        InputStream inputStream = null;
        try {
            URL url = new URL(BASE_URL + GET_IMAGE + "?name=" + name);
            OkHttpClient mOkHttpClient = new OkHttpClient();
            Request.Builder reqBuilder = new Request.Builder();
            Request request = reqBuilder
                    .url(url)
                    .build();

            response = mOkHttpClient.newCall(request).execute();

            if (response.isSuccessful()) {
                assert response.body() != null;
                inputStream = response.body().byteStream();
                bitmap = BitmapFactory.decodeStream(inputStream);
                return bitmap;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return BitmapFactory.decodeResource(Resources.getSystem(), R.mipmap.ic_avatar);
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.reset();
                    inputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (response != null) {
                response.close();
            }
        }
        return BitmapFactory.decodeResource(Resources.getSystem(), R.mipmap.ic_avatar);
    }

    /**
     * 从输入流中获取Json字段
     * @param inputStream 输入流
     * @return Json字段
     */
    private static String changeInputString(InputStream inputStream) {
        String jsonString = "";
        ByteArrayOutputStream outPutStream = new ByteArrayOutputStream();
        byte[] data = new byte[1024];
        int len;
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

    /**
     * 将参数组合形成URL
     * @param name     用户名
     * @param password 密码
     * @return 组合后的RUL字符串
     */
    public static String getURLWithParams(String name, String password) {
        return BASE_URL + DELETE_USER + "?" +
                "name=" +
                name +
                "&" +
                "password=" +
                password;
    }

    /**
     * 将参数组合形成URL
     * @param whichGame 游戏模式
     * @param name      用户名
     * @param score     分数
     * @return 组合后的RUL字符串
     */
    public static String getURLWithParams(int whichGame, String name, int score) {
        StringBuilder url = new StringBuilder(BASE_URL);
        if (whichGame == 4) {
            url.append(UPDATE_SCORE_4);
        }
        if (whichGame == 5) {
            url.append(UPDATE_SCORE_5);
        }
        if (whichGame == 6) {
            url.append(UPDATE_SCORE_6);
        }
        url.append("?");
        url.append("name=");
        url.append(name);
        url.append("&");
        url.append("score=");
        url.append(score);
        return url.toString();
    }

    /**
     * 向指定地址发送Http请求
     * @param address  Http请求的地址
     * @param listener 回调对象
     * @return response中的内容或者“fail”
     */
    public static String sendHttpRequest(String address, HttpCallbackListener listener) {
        OkHttpClient mOkHttpClient = new OkHttpClient();

        Request.Builder reqBuilder = new Request.Builder();
        Request request = reqBuilder
                .url(address)
                .build();

        try {
            Response response = mOkHttpClient.newCall(request).execute();
            if (response.isSuccessful() && response.body() != null) {
                String resultValue = response.body().string();
                listener.onFinish(resultValue);
                return resultValue;
            }
        } catch (Exception e) {
            e.printStackTrace();
            listener.onError(e);
            return "fail";
        }
        return "fail";
    }

    /**
     * 新增一个User对象的网络请求
     * @param user 新增的用户对象
     * @return response中的内容
     */
    public static String addUser(User user) {
        InputStream inputStream = null;
        InputStreamReader inputStreamReader = null;
        BufferedReader reader = null;
        StringBuffer resultBuffer = new StringBuffer();

        Map<String, Object> subject = new HashMap<>();
        subject.put("name", user.getName());
        subject.put("password", user.getPassword());
        subject.put("gender", user.getGender());
        subject.put("avatar", "avatarPath");
        subject.put("bestscore4", user.getBestScore4());
        subject.put("bestscore5", user.getBestScore5());
        subject.put("bestscore6", user.getBestScore6());

        Map<String, Object> request = new HashMap<>();
        request.put("code", 200);
        request.put("msg", "success");
        request.put("subject", subject);

        Gson gson = new Gson();
        String jsonData = gson.toJson(request);

        HttpURLConnection connection;
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
            connection.setRequestProperty("Content-Type", "application/json");
            // 调用HttpURLConnection对象的connect()方法，建立与服务器的真实连接；
            connection.connect();

            OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
            //将Json写入输出流中
            writer.write(jsonData);
            writer.flush();

            if (connection.getResponseCode() >= 300) {
                throw new Exception("HTTP Request is not success, Response code is " + connection.getResponseCode());
            }

            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                String tempLine;
                inputStream = connection.getInputStream();
                inputStreamReader = new InputStreamReader(inputStream, CHARSET);
                reader = new BufferedReader(inputStreamReader);
                resultBuffer = new StringBuffer();
                while ((tempLine = reader.readLine()) != null) {
                    resultBuffer.append(tempLine);
                    resultBuffer.append("\n");
                }
            }
        } catch (Exception e) {
            resultBuffer = new StringBuffer("fail");
            e.printStackTrace();
        } finally {
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

    /**
     * 更新用户信息
     * @param oldName 旧的用户名
     * @param user    新的用户名
     * @return 返回的response中的内容
     */
    public static String updateUser(String oldName, User user) {
        InputStream inputStream = null;
        InputStreamReader inputStreamReader = null;
        BufferedReader reader = null;
        StringBuffer resultBuffer = new StringBuffer();

        Map<String, Object> subject = new HashMap<>();
        subject.put("name", user.getName());
        subject.put("password", user.getPassword());
        subject.put("gender", user.getGender());
        subject.put("avatar", "avatarPath");
        subject.put("bestscore4", user.getBestScore4());
        subject.put("bestscore5", user.getBestScore5());
        subject.put("bestscore6", user.getBestScore6());

        Map<String, Object> request = new HashMap<>();
        request.put("code", 200);
        request.put("msg", "success");
        request.put("oldName", oldName);
        request.put("subject", subject);
        Gson gson = new Gson();
        String jsonData = gson.toJson(request);

        HttpURLConnection connection;
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
            connection.setRequestProperty("Content-Type", "application/json");
            // 调用HttpURLConnection对象的connect()方法，建立与服务器的真实连接；
            connection.connect();

            OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
            writer.write(jsonData);
            writer.flush();

            if (connection.getResponseCode() >= 300) {
                throw new Exception("HTTP Request is not success, Response code is " + connection.getResponseCode());
            }

            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                String tempLine;
                inputStream = connection.getInputStream();
                inputStreamReader = new InputStreamReader(inputStream, CHARSET);
                reader = new BufferedReader(inputStreamReader);
                resultBuffer = new StringBuffer();
                while ((tempLine = reader.readLine()) != null) {
                    resultBuffer.append(tempLine);
                    resultBuffer.append("\n");
                }
            }
        } catch (Exception e) {
            resultBuffer = new StringBuffer("fail");
            e.printStackTrace();
        } finally {
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

    /**
     * 更新用户对象的信息
     * @param user 更新了信息都的用户对象
     * @return 更新结果返回的response中的内容
     */
    public static String updateUserData(User user) {
        InputStream inputStream = null;
        InputStreamReader inputStreamReader = null;
        BufferedReader reader = null;
        StringBuffer resultBuffer = new StringBuffer();

        Map<String, Object> subject = new HashMap<>();
        subject.put("name", user.getName());
        subject.put("password", user.getPassword());
        subject.put("gender", user.getGender());
        subject.put("avatar", "avatarPath");
        subject.put("bestscore4", user.getBestScore4());
        subject.put("bestscore5", user.getBestScore5());
        subject.put("bestscore6", user.getBestScore6());

        Map<String, Object> request = new HashMap<>();
        request.put("code", 200);
        request.put("msg", "success");
        request.put("subject", subject);
        Gson gson = new Gson();
        String jsonData = gson.toJson(request);

        HttpURLConnection connection;
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
            connection.setRequestProperty("Content-Type", "application/json");
            // 调用HttpURLConnection对象的connect()方法，建立与服务器的真实连接；
            connection.connect();

            OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
            //将Json数据写入输出流中
            writer.write(jsonData);
            writer.flush();

            if (connection.getResponseCode() >= 300) {
                throw new Exception("HTTP Request is not success, Response code is " + connection.getResponseCode());
            }

            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                String tempLine;
                inputStream = connection.getInputStream();
                inputStreamReader = new InputStreamReader(inputStream, CHARSET);
                reader = new BufferedReader(inputStreamReader);
                resultBuffer = new StringBuffer();
                while ((tempLine = reader.readLine()) != null) {
                    resultBuffer.append(tempLine);
                    resultBuffer.append("\n");
                }
            }
        } catch (Exception e) {
            resultBuffer = new StringBuffer("fail");
            e.printStackTrace();
        } finally {
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

    /**
     * 新增一条留言
     * @param messageEntity 新的留言对象实体
     * @return 请求结果返回的response中的内容
     */
    public static String addMessage(MessageEntity messageEntity) {
        InputStream inputStream = null;
        InputStreamReader inputStreamReader = null;
        BufferedReader reader = null;
        StringBuffer resultBuffer = new StringBuffer();

        Map<String, Object> subject = new HashMap<>();
        subject.put("name", messageEntity.getName());
        subject.put("date", messageEntity.getDate());
        subject.put("message", messageEntity.getMessage());

        Map<String, Object> request = new HashMap<>();
        request.put("code", 200);
        request.put("msg", "success");
        request.put("subject", subject);

        Gson gson = new Gson();
        String jsonData = gson.toJson(request);

        HttpURLConnection connection;
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
            connection.setRequestProperty("Content-Type", "application/json");
            // 调用HttpURLConnection对象的connect()方法，建立与服务器的真实连接；
            connection.connect();

            OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
            //将Json数据写入到输出流
            writer.write(jsonData);
            writer.flush();

            if (connection.getResponseCode() >= 300) {
                throw new Exception("HTTP Request is not success, Response code is " + connection.getResponseCode());
            }

            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                String tempLine;
                inputStream = connection.getInputStream();
                inputStreamReader = new InputStreamReader(inputStream, CHARSET);
                reader = new BufferedReader(inputStreamReader);
                resultBuffer = new StringBuffer();
                while ((tempLine = reader.readLine()) != null) {
                    resultBuffer.append(tempLine);
                    resultBuffer.append("\n");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            resultBuffer = new StringBuffer("fail");
        } finally {
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

    /**
     * 上传头像图片到服务器
     * @param name 用户名
     * @param imagePath 头像的本地路径
     * @return 返回的信息
     */
    public static String upLoadImage(String name, String imagePath) {
        OkHttpClient mOkHttpClient = new OkHttpClient();

        String result = "fail";
        MultipartBody.Builder builder = new MultipartBody.Builder();
        builder.addFormDataPart("image", imagePath, RequestBody.create(MediaType.parse("image/jpg"), new File(imagePath)));
        RequestBody requestBody = builder.build();
        Request.Builder reqBuilder = new Request.Builder();
        Request request = reqBuilder
                .url(BASE_URL + UPLOAD_IMAGE + "?name=" + name)
                .post(requestBody)
                .build();

        try {
            Response response = mOkHttpClient.newCall(request).execute();
            if (response.isSuccessful()) {
                return response.body().string();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public interface HttpCallbackListener {
        void onFinish(String s);
        void onError(Exception e);
    }
}


