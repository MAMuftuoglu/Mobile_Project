package edu.marda.cs310news;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutorService;

public class NewsRepository {

    public void getAllCategories(ExecutorService srv, Handler uiHandler) {
        srv.execute(() -> {
            try {
                URL url = new URL("http://10.3.0.14:8080/newsapp/getallnewscategories");

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));

                StringBuilder buffer = new StringBuilder();
                String line;

                while((line = reader.readLine()) != null) {
                    buffer.append(line);
                }

                Message msg = new Message();
                msg.obj = buffer.toString();
                uiHandler.sendMessage(msg);

                conn.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public void getAllNews(ExecutorService srv, Handler uiHandler) {
        srv.execute(() -> {
            try {
                URL url = new URL("http://10.3.0.14:8080/newsapp/getall");

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));

                StringBuilder buffer = new StringBuilder();
                String line;

                while((line = reader.readLine()) != null) {
                    buffer.append(line);
                }

                Message msg = new Message();
                msg.obj = buffer.toString();
                uiHandler.sendMessage(msg);

                conn.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public void getNewsByCategoryID(ExecutorService srv, Handler uiHandler, int id) {
        srv.execute(() -> {
            try {
                URL url = new URL("http://10.3.0.14:8080/newsapp/getbycategoryid/"+id);

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));

                StringBuilder buffer = new StringBuilder();
                String line;

                while((line = reader.readLine()) != null) {
                    buffer.append(line);
                }

                Message msg = new Message();
                msg.obj = buffer.toString();
                uiHandler.sendMessage(msg);


                conn.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public void getNewsByID(ExecutorService srv, Handler uiHandler, int id) {
        srv.execute(() -> {
            try {
                URL url = new URL("http://10.3.0.14:8080/newsapp/getnewsbyid/"+id);

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));

                StringBuilder buffer = new StringBuilder();
                String line;

                while((line = reader.readLine()) != null) {
                    buffer.append(line);
                }


                Message msg = new Message();
                msg.obj = buffer.toString();
                uiHandler.sendMessage(msg);

                conn.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public void getCommentsByNewsID(ExecutorService srv, Handler uiHandler, int id) {
        srv.execute(() -> {
            try {
                URL url = new URL("http://10.3.0.14:8080/newsapp/getcommentsbynewsid/"+id);

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));

                StringBuilder buffer = new StringBuilder();
                String line;


                while((line = reader.readLine()) != null) {
                    buffer.append(line);
                }

                Message msg = new Message();
                msg.obj = buffer.toString();
                uiHandler.sendMessage(msg);

                conn.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public void downloadImage(ExecutorService srv, Handler uiHandler, String path) {
        srv.execute(() -> {
            try {
                URL url = new URL(path);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                Bitmap bitmap = BitmapFactory.decodeStream(conn.getInputStream());
                Message msg = new Message();
                msg.obj = bitmap;
                uiHandler.sendMessage(msg);

            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public void postComment(ExecutorService srv, Handler uiHandler, CommentModel comment) {
        srv.execute(() -> {
            try {
                URL url = new URL("http://10.3.0.14:8080/newsapp/savecomment");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
                conn.setRequestProperty("Accept", "application/json");
                conn.setDoOutput(true);
                conn.setDoInput(true);

                JSONObject jsonObject = new JSONObject();
                jsonObject.put("name", comment.getName());
                jsonObject.put("text", comment.getText());
                jsonObject.put("news_id", comment.getNews_id());

                OutputStream os = conn.getOutputStream();
                os.write(jsonObject.toString().getBytes(StandardCharsets.UTF_8));
                os.close();

                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder stringBuilder = new StringBuilder();
                String line;

                while ((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line);
                }
                bufferedReader.close();

                Message msg = new Message();
                msg.obj = stringBuilder.toString();
                Log.d("PostCheck", stringBuilder.toString());
                uiHandler.sendMessage(msg);

            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
        });
    }
}
