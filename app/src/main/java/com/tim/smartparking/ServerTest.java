package com.tim.smartparking;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings.Secure;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;


public class ServerTest extends Activity {

    private static final String LOG_TAG = "SP";
    private static final String LOG_TAG_CHECK = "check";
    public static String s = "";
    public static int parkPlace;
    public static long parkTime;
    public static long unparkTime;
    private static int count_of_cars = 10;
    private static AlertDialog.Builder ad;
    private static AlertDialog ald2;
    String web_site = "http://www.testing44.rurs.net/"; // then we will change it
    private int id = -1;
    private ProgressDialog pDialog;
    JSONParser jsonParser = new JSONParser();
    private static String url_create_space = "http://testing44.rurs.net/create_space.php";
    private static final String TAG_SUCCESS = "success";
    private static String android_id;
    private static String session_id;
    private static final String TAG_ANDROID_ID = "android_id";
    private static final String TAG_SESSION_ID = "session_id";
    public static final String APP_PREFERENCES = "mydata";
    public static final String SAVED_SESSION_ID = "DataSession";
    public static final String SAVED_ANDROID_ID = "DataAndroid";
    public static final String SAVED_PLACE = "DataPlace";
    private static final String TAG_PLACE = "place";
    SharedPreferences mSettings;
    JSONParser jParser = new JSONParser();
    private static String url_find_session = "http://testing44.rurs.net/find_session.php";
    JSONArray session_id_load = null;
    ArrayList<HashMap<String, String>> productsList;




    private void setColorCars(String s) {

		  RelativeLayout rl = (RelativeLayout)findViewById(R.id.batya);
		  count_of_cars = rl.getChildCount();

		  while(count_of_cars<s.length())
			  s += "0";
		  for(int i = 0, v = 0; i < Math.min(count_of_cars, s.length()) && v<count_of_cars; v++) {

			  if(!(rl.getChildAt(v) instanceof TextView))
			  {
				  continue;
			  }

			  if(rl.getChildAt(v) instanceof Button)
			  {
				  continue;
			  }

			  if(s.charAt(i) == '1') {
                  try {
                      rl.getChildAt(v).setBackgroundResource(R.drawable.redcar);
                  } catch (NullPointerException e) {
                  }
              } else{
                  try {
                      rl.getChildAt(v).setBackgroundResource(R.drawable.greencar);
                  } catch (NullPointerException e) {
                  }
              }
			  i++;
		  }
          try {
              findViewById(R.id.hel).setBackgroundResource(R.drawable.redcar);
          } catch (Exception e) {
              e.printStackTrace();
          }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.kolco_map1);

        ad = new AlertDialog.Builder(this);
        mSettings = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);

        if(mSettings.contains(SAVED_ANDROID_ID)) {
            String savedDataAndroid = mSettings.getString(SAVED_ANDROID_ID, "");
            android_id = savedDataAndroid;
            Toast.makeText(this, "Android ID loaded", Toast.LENGTH_SHORT).show();
        }

        if(mSettings.contains(SAVED_SESSION_ID)) {
            String savedDataSession =  mSettings.getString(SAVED_SESSION_ID, "");
            session_id = savedDataSession;
            Toast.makeText(this, "Session ID loaded", Toast.LENGTH_SHORT).show();
        } else {
            new LoadSession().execute();
        }

        if(mSettings.contains(SAVED_PLACE)){
            String savedDataPlace = mSettings.getString(SAVED_PLACE, "");
            Integer savedDataInt = new Integer(savedDataPlace);
            parkPlace = savedDataInt;
            Toast.makeText(this, "Place loaded", Toast.LENGTH_SHORT).show();
        }

        // Тут создаются AlertDialog'и
        ad.setPositiveButton("", null);
        ad.setNegativeButton("", null);
        ad.setView(getLayoutInflater().inflate(R.layout.alert_wait, null));
        ald2 = ad.create();
        Log.d(LOG_TAG, "refresh alert dialog is ready");
        //узнаём android ID
        android_id = Secure.getString(this.getContentResolver(),
                Secure.ANDROID_ID);

        CheckLog();

        SharedPreferences storage = this.getSharedPreferences("Configuration", MODE_MULTI_PROCESS);
		final String name = storage.getString("name", "Malik");
		id = storage.getInt("id", -1);
		Log.e("eclipse", "eclispe");

		 RelativeLayout rl = (RelativeLayout)findViewById(R.id.batya);
		 count_of_cars = rl.getChildCount();

		  for(int i = 0; i <count_of_cars; i++) {

			  if(!(rl.getChildAt(i) instanceof TextView))
				  continue;
              rl.getChildAt(i).setOnLongClickListener(new OnLongClickListener() {

                  @Override
                  public boolean onLongClick(View v) {
                      // TODO Auto-generated method stub
                      if (id != v.getId()) {
                          SharedPreferences storage = ServerTest.this.getSharedPreferences("Configuration", MODE_MULTI_PROCESS);
                          SharedPreferences.Editor editor = storage.edit();
                          int hd = storage.getInt("id", -1);
                          if (hd != -1) {

                              try {
                                  ((TextView) findViewById(hd)).setText("");
                              } catch (NullPointerException e) {
                              }
                          }
                          editor.putInt("id", v.getId());
                          id = v.getId();
                          editor.commit();
                          try {
                              ((TextView) v).setText(name);
                          } catch (NullPointerException e) {
                          }
                          Notification.Builder nb = new Notification.Builder(getApplicationContext());
                          NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                          SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
                          parkPlace = Integer.parseInt(v.getTag().toString());
                          String push = "Вы припарковались на месте " + parkPlace + ". Время парковки: " + sdf.format(System.currentTimeMillis());
                          nm.cancel(5);
                          nb.setOngoing(true)
                                  .setSmallIcon(R.drawable.ic_launcher)
                                  .setContentText(push)
                                  .setContentTitle("Smart Parking")
                                  .setWhen(System.currentTimeMillis())
                                  .setContentIntent(PendingIntent.getActivity(getApplicationContext(), 0, new Intent(getApplicationContext(), ServerTest.class), PendingIntent.FLAG_UPDATE_CURRENT));
                          Notification notif = new Notification.BigTextStyle(nb).bigText(push).build();
                          nm.notify(5, notif);
                          parkTime = System.currentTimeMillis();
                          char[] chars = "abcdefghijklmnopqrstuvwxyz123456789".toCharArray();
                          StringBuilder sb = new StringBuilder();
                          Random random = new Random();
                          for (int i = 0; i < 20; i++) {
                              char c = chars[random.nextInt(chars.length)];
                              sb.append(c);
                          }
                          session_id = sb.toString();
                          CheckLog();
                          SaveData();
                          refresh();
                          new CreateNewProduct().execute();
                          return false;
                      } else {
                          SharedPreferences storage = ServerTest.this.getSharedPreferences("Configuration", MODE_MULTI_PROCESS);
                          SharedPreferences.Editor editor = storage.edit();
                          int hd = storage.getInt("id", -1);
                          if (hd != -1) {

                              try {
                                  ((TextView) findViewById(hd)).setText("");
                              } catch (NullPointerException e) {
                              }
                          }
                          editor.commit();
                          driveAway(parkPlace);
                          refresh();
                          return false;
                      }
                  }
              });
          }


        if (id != -1) {
            try {
				((TextView)findViewById(id)).setText(name);
			} catch (NullPointerException e) {
				e.printStackTrace();
			}
        }

        // Обновить статус всех мест
        Button refresh = (Button) findViewById(R.id.refresh);
        refresh.setOnClickListener(new OnClickListener() {

            @Override
			public void onClick(View v) {
                refresh();
                // TODO Auto-generated method stub

            }
		});
    }

    void SaveData(){
        Editor editor = mSettings.edit();
        editor.putString(SAVED_ANDROID_ID, android_id);
        editor.putString(SAVED_SESSION_ID, session_id);
        String parkPlaceStr = Integer.toString(parkPlace);
        editor.putString(SAVED_PLACE, parkPlaceStr);
        editor.apply();
        Log.d(LOG_TAG_CHECK, "Данные тип сохранились");
    }


    //Засунем сюда все логи, и будем вызывать где нужно (бордак в коде с логами уже )
    void CheckLog(){
        Log.d(LOG_TAG_CHECK, "Place: " + parkPlace);
        Log.d(LOG_TAG_CHECK, "Park Time: " + parkTime);
        Log.d(LOG_TAG_CHECK, "Android ID: " + android_id);
        Log.d(LOG_TAG_CHECK, "Session ID: " + session_id);
    }

    protected void driveAway(int pos) {
        Log.d(LOG_TAG, "Driving away");
        String thank = "Вы уехали с места " + parkPlace +". Нажмите сюда, чтобы перейти к оплате.";
        Notification.Builder nb = new Notification.Builder(getApplicationContext());
        NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        nm.cancel(5);
        nb.setOngoing(true)
                .setSmallIcon(R.drawable.ic_launcher)
                .setContentText(thank)
                .setContentTitle("Smart Parking")
                .setWhen(System.currentTimeMillis())
                .setContentIntent(PendingIntent.getActivity(getApplicationContext(), 0, new Intent(getApplicationContext(), OplataActivity.class), PendingIntent.FLAG_UPDATE_CURRENT));
        Notification notif = new Notification.BigTextStyle(nb).bigText(thank).build();
        unparkTime = System.currentTimeMillis();
        nm.notify(5, notif);
        AlertDialog.Builder builder = new AlertDialog.Builder(ServerTest.this);
        builder.setTitle("Спасибо за парковку!")
                .setMessage("Осталось всего лишь оплатить..")
                .setIcon(R.drawable.ic_android_pay)
                .setCancelable(false)
                .setNegativeButton("Отдать деньги",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                launchIntent();
                            }
                        });
        AlertDialog alert = builder.create();
        alert.show();
    }


    private void launchIntent() {
        Intent it = new Intent(ServerTest.this, OplataActivity.class);
        it.putExtra(TAG_ANDROID_ID, android_id);
        it.putExtra(TAG_SESSION_ID, session_id);
        it.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(it);
    }

	protected void refresh() {
        Log.d(LOG_TAG, "refresh started");
        ald2.show();
        get_place();
        ald2.dismiss();
    }

    // Узнать про все места
    private void get_place() {
        GettingInfo info = new GettingInfo(getApplicationContext());
        String ginfo;
        String scolor = "";

        try {
            ginfo = info.execute(web_site).get(7000, TimeUnit.MILLISECONDS);
            if (ginfo.equals("Error")) {
                Toast.makeText(getApplicationContext(), "Error getting info", Toast.LENGTH_SHORT).show();
                return;
            }

            JSONWorking jw = new JSONWorking(getApplicationContext());

            try {
                ArrayList<HashMap<String, String>> res = jw.execute(ginfo).get();

                if (res == null) {
                    Toast.makeText(getApplicationContext(), "Error Array equals to null", Toast.LENGTH_SHORT).show();
                    return;
                }
                for (int i = 0; i < res.size(); i++) {
                    HashMap<String, String> item = res.get(i);
                    String used = item.get("Used");

                    scolor = scolor + used;
                }
            } catch (InterruptedException e) {
                Toast.makeText(getApplicationContext(), "Error in using JSON", Toast.LENGTH_SHORT).show();
                // TODO Auto-generated catch block
            }
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            Toast.makeText(getApplicationContext(), "Error 1", Toast.LENGTH_SHORT).show();
        } catch (ExecutionException e) {
            // TODO Auto-generated catch block
            Toast.makeText(getApplicationContext(), "Error 2", Toast.LENGTH_SHORT).show();
        } catch (TimeoutException e) {
            // TODO Auto-generated catch block
            Toast.makeText(getApplicationContext(), "Error Time out", Toast.LENGTH_SHORT).show();
        }

        setColorCars(scolor);
    }

    class GettingInfo extends AsyncTask<String, Void, String> {



        public GettingInfo(Context applicationContext) {
            // TODO Auto-generated constructor stub
            super();
        }


        @Override
        protected String doInBackground(String... params) {
            String info = "";
            URL url;
            try {
                url = new URL(params[0]);
                HttpURLConnection httpURLConnection;
                try {
                    httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setDoInput(true);
                    InputStream in;
                    try {
                        in = new BufferedInputStream(httpURLConnection.getInputStream());
                        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                        try {
                            String inf;
                            while ((inf = reader.readLine()) != null)
                                info += inf;

                            httpURLConnection.disconnect();
                            return info;

                        } catch (IOException e) {
                            Log.e("m", "I m here3");
                        }

                    } catch (IOException e) {
                        Log.e("m", "I m here2");
                    }


                } catch (IOException e) {
                    Log.e("m", "I m here");
                }

            } catch (MalformedURLException e) {
            }
            return "Error";
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
        }


    }

    class CreateNewProduct extends AsyncTask<String, String, String> {

        /**
         * Перед согданием в фоновом потоке показываем прогресс диалог
         **/
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(ServerTest.this);
            pDialog.setMessage("Создание места...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        /**
         * Создание продукта
         **/
        protected String doInBackground(String[] args) {

            // Заполняем параметры
            String parkPlaceStr = Integer.toString(parkPlace); //переводим из int в string
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("android_id", android_id));
            params.add(new BasicNameValuePair("parkPlace", parkPlaceStr));
            params.add(new BasicNameValuePair("session_id", session_id));


            // получаем JSON объект
            JSONObject json = jsonParser.makeHttpRequest(url_create_space, "POST", params);

            Log.d("Create Response", json.toString());

            try {
                int success = json.getInt(TAG_SUCCESS);

                if (success == 1) {
                    // успех ёпт
                    Log.d(LOG_TAG, "Parking Success");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        /**
         * После оконачния скрываем прогресс диалог
         **/
        protected void onPostExecute(String file_url) {
            pDialog.dismiss();
        }

    }

    class LoadSession extends AsyncTask<String, String, String> {

        /**
         * Перед началом фонового потока Show Progress Dialog
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(ServerTest.this);
            pDialog.setMessage("Загрузка сессии, подождите...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        /**
         * Получаем все продукт из url
         */
        protected String doInBackground(String... args) {
            // Будет хранить параметры
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("android_id", android_id));
            // получаем JSON строк с URL
            JSONObject json_post = jsonParser.makeHttpRequest(url_find_session, "POST", params);
            JSONObject json = jParser.makeHttpRequest(url_find_session, "GET", params);

            Log.d("Create Response", json_post.toString());
            Log.d("Session ID: ", json.toString());

            try {
                int success = json_post.getInt(TAG_SUCCESS);

                if (success == 1) {
                    // успех ёпт
                    Log.d(LOG_TAG_CHECK, "Android_id send");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            try {
                // Получаем SUCCESS тег для проверки статуса ответа сервера
                int success = json.getInt(TAG_SUCCESS);

                if (success == 1) {
                    // продукт найден
                    // Получаем масив из Продуктов
                    session_id_load = json.getJSONArray(TAG_PLACE);

                    // перебор всех продуктов
                    for (int i = 0; i < session_id_load.length(); i++) {
                        JSONObject c = session_id_load.getJSONObject(i);

                        // Сохраняем каждый json елемент в переменную
                        String session_id_ld = c.getString(TAG_SESSION_ID);
                        Log.d(LOG_TAG_CHECK, "Загруженная сессия: " + session_id_ld);

                        // Создаем новый HashMap
                        HashMap<String, String> map = new HashMap<String, String>();

                        // добавляем каждый елемент в HashMap ключ => значение
                        map.put(TAG_SESSION_ID, session_id_ld);
                        // добавляем HashList в ArrayList
                        productsList.add(map);
                    }
                } else {
                    // продукт не найден
                    // Пишем в лог
                    Log.d(LOG_TAG_CHECK, "Воробушки! Сессии нет!");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        /**
         * После завершения фоновой задачи закрываем прогрес диалог
         **/
        protected void onPostExecute(String file_url) {
            // закрываем прогресс диалог после получение сессии
            pDialog.dismiss();
        }

    }

}