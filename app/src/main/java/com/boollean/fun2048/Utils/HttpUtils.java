package com.boollean.fun2048.Utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.boollean.fun2048.Entity.MessageEntity;
import com.boollean.fun2048.Entity.User;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class HttpUtils {
    public static final String BASE_URL = "http://10.15.134.221:8080/appservice/";
    public static final String ADD_USER="addUser";
    public static final String UPDATE_USER="updateUser";
    public static final String UPDATE_USER_DATA="updateUserData";
    public static final String GET_BEST_100_USERS_4 = "getBest100Users4";
    public static final String GET_BEST_100_USERS_5 = "getBest100Users5";
    public static final String GET_BEST_100_USERS_6 = "getBest100Users6";
    public static final String UPDATE_SCORE_4 = "updateScore4";
    public static final String UPDATE_SCORE_5 = "updateScore5";
    public static final String UPDATE_SCORE_6 = "updateScore6";
    public static final String GET_MESSAGES = "getLatest100Messages";
    public static final String ADD_MESSAGE = "addMessage";
    private static final int TIME_OUT = 3 * 1000;   //超时时间
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
            Log.i("Json",String.valueOf(code));
            if (code == 200) {
                return changeInputString(connection.getInputStream());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "fail";
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
        url = new StringBuilder(BASE_URL);

        url.append("?");
        url.append("name=");
        url.append(name);

        return url.toString();
    }

    public static String getURLWithParams(String oldName, User user) {
        StringBuilder url = new StringBuilder(BASE_URL);
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

    public static void sendHttpRequest(String address, HttpCallbackListener listener) {
        HttpURLConnection connection = null;
        try {
            URL url = new URL(address);
            //使用HttpURLConnection
            connection = (HttpURLConnection) url.openConnection();
            //设置方法和参数
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(TIME_OUT);
            connection.setReadTimeout(TIME_OUT);
            connection.setDoInput(true);
            connection.setDoOutput(true);
            //获取返回结果
            InputStream inputStream = connection.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            //成功则回调onFinish
            if (listener != null) {
                listener.onFinish(response.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
            //出现异常则回调onError
            if (listener != null) {
                listener.onError(e);
            }
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    public static String addUser(User user, HttpCallbackListener listener) {
        DataOutputStream ds = null;
        InputStream inputStream = null;
        InputStreamReader inputStreamReader = null;
        BufferedReader reader = null;
        StringBuffer resultBuffer = new StringBuffer();

        Map subject = new HashMap<String,Object>();
        subject.put("name",user.getName());
        subject.put("password",user.getPassword());
        subject.put("gender",user.getGender());
        subject.put("avatar",user.getAvatar());
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
            listener.onFinish(resultBuffer.toString());
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            listener.onError(e);
        } finally {
            if (ds != null) {
                try {
                    ds.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            if (inputStreamReader != null) {
                try {
                    inputStreamReader.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

            return resultBuffer.toString();
        }
    }


    public static String updateUser(String oldName,User user, HttpCallbackListener listener) {
        DataOutputStream ds = null;
        InputStream inputStream = null;
        InputStreamReader inputStreamReader = null;
        BufferedReader reader = null;
        StringBuffer resultBuffer = new StringBuffer();

        Map subject = new HashMap<String,Object>();
        subject.put("name",user.getName());
        subject.put("password",user.getPassword());
        subject.put("gender",user.getGender());
        //subject.put("avatar",user.getAvatar());
        subject.put("bitmapPath",user.getBitmapPath());
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
            listener.onFinish(resultBuffer.toString());
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            listener.onError(e);
        } finally {
            if (ds != null) {
                try {
                    ds.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            if (inputStreamReader != null) {
                try {
                    inputStreamReader.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

            return resultBuffer.toString();
        }
    }

    public static String updateUserData(User user, HttpCallbackListener listener) {
        DataOutputStream ds = null;
        InputStream inputStream = null;
        InputStreamReader inputStreamReader = null;
        BufferedReader reader = null;
        StringBuffer resultBuffer = new StringBuffer();

        Map subject = new HashMap<String,Object>();
        subject.put("name",user.getName());
        subject.put("password",user.getPassword());
        subject.put("gender",user.getGender());
        //subject.put("avatar",user.getAvatar());
        subject.put("bitmapPath",user.getBitmapPath());
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
            listener.onFinish(resultBuffer.toString());
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            listener.onError(e);
        } finally {
            if (ds != null) {
                try {
                    ds.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            if (inputStreamReader != null) {
                try {
                    inputStreamReader.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

            return resultBuffer.toString();
        }
    }


    public static String addMessage(MessageEntity messageEntity, HttpCallbackListener listener) {
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
        Log.i("message",jsonData);
        HttpURLConnection connection = null;
        try {
            // 实例化URL对象。调用URL有参构造方法，参数是一个url地址；
            URL url = new URL(BASE_URL + ADD_MESSAGE);
            // 调用URL对象的openConnection()方法，创建HttpURLConnection对象；
            connection = (HttpURLConnection) url.openConnection();
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
            listener.onFinish(resultBuffer.toString());
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            listener.onError(e);
        } finally {
            if (ds != null) {
                try {
                    ds.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            if (inputStreamReader != null) {
                try {
                    inputStreamReader.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

            return resultBuffer.toString();
        }
    }

    public static String upLoadFile(String name, String filePath, HttpCallbackListener listener) {
        DataOutputStream ds = null;
        InputStream inputStream = null;
        InputStreamReader inputStreamReader = null;
        BufferedReader reader = null;
        StringBuffer resultBuffer = new StringBuffer();
        String tempLine = null;

        // 设置三个常用字符串常量：换行、前缀、分界线（NEWLINE、PREFIX、BOUNDARY）；
        final String NEWLINE = "\r\n"; // 换行，或者说是回车
        final String PREFIX = "--"; // 固定的前缀
        final String BOUNDARY = "######################"; // 分界线，就是上面提到的boundary，可以是任意字符串，建议写长一点，这里简单的写了一个#

        HttpURLConnection connection = null;
        DataOutputStream dos = null;
        try {
            // 实例化URL对象。调用URL有参构造方法，参数是一个url地址；
            URL url = new URL(BASE_URL);
            // 调用URL对象的openConnection()方法，创建HttpURLConnection对象；
            connection = (HttpURLConnection) url.openConnection();
            // 调用HttpURLConnection对象setDoOutput(true)、setDoInput(true)、setRequestMethod("POST")；
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            // 设置Http请求头信息；（Accept、Connection、Accept-Encoding、Cache-Control、Content-Type、User-Agent），不重要的就不解释了，直接参考抓包的结果设置即可
            connection.setUseCaches(false);
            connection.setRequestProperty("Connection", "Keep-Alive");
            connection.setRequestProperty("Accept", "*/*");
            connection.setRequestProperty("Charset", CHARSET);
            connection.setRequestProperty("Accept-Encoding", "gzip, deflate");
            connection.setRequestProperty("Cache-Control", "no-cache");
            // 这个比较重要，按照上面分析的拼装出Content-Type头的内容
            connection.setRequestProperty("Content-Type",
                    "multipart/form-data; boundary=" + BOUNDARY);
            // 调用HttpURLConnection对象的connect()方法，建立与服务器的真实连接；
            connection.connect();

            Log.i("upload: ", name + "   " + filePath);
            dos = new DataOutputStream(connection.getOutputStream());
            String fileName = filePath.substring(filePath.lastIndexOf("Fun2048_"));
            Log.i("upload ", fileName);
            dos.writeBytes(PREFIX + BOUNDARY + NEWLINE);
            dos.writeBytes("Content-Disposition: form-data; " + "name=\"" + name + "\";filename=\"" + fileName
                    + "\"" + NEWLINE);
            dos.writeBytes(NEWLINE);
            FileInputStream fileInputStream = new FileInputStream(filePath);
            int bufferSize = 1024;
            byte[] buffer = new byte[bufferSize];
            int length = -1;
            while ((length = fileInputStream.read(buffer)) != -1) {
                dos.write(buffer, 0, length);
            }
            dos.writeBytes(NEWLINE);
            dos.flush();

            if (connection.getResponseCode() >= 300) {
                throw new Exception(
                        "HTTP Request is not success, Response code is " + connection.getResponseCode());
            }

            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                Log.i("upload", connection.getResponseCode() + "");
                inputStream = connection.getInputStream();
                inputStreamReader = new InputStreamReader(inputStream);
                reader = new BufferedReader(inputStreamReader);
                tempLine = null;
                resultBuffer = new StringBuffer();
                while ((tempLine = reader.readLine()) != null) {
                    resultBuffer.append(tempLine);
                    resultBuffer.append("\n");
                    Log.i("upload", resultBuffer.toString());
                }
            }
            listener.onFinish(resultBuffer.toString());
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            listener.onError(e);
        } finally {
            if (ds != null) {
                try {
                    ds.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            if (inputStreamReader != null) {
                try {
                    inputStreamReader.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

            return resultBuffer.toString();
        }
    }

    public interface HttpCallbackListener {
        void onFinish(String s);

        void onError(Exception e);
    }
}


