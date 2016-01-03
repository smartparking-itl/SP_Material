package com.tim.smartparking;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;


public class ServerTest extends Activity {

    private static final String LOG_TAG = "SP";
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
    private static Context context;
    private static String android_id;


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

        // Тут создаются AlertDialog'и
        ad.setPositiveButton("", null);
        ad.setNegativeButton("", null);
        ad.setView(getLayoutInflater().inflate(R.layout.alert_wait, null));
        ald2 = ad.create();
        Log.d(LOG_TAG, "refresh alert dialog is ready");
        //узнаём android ID
        android_id = Secure.getString(this.getContentResolver(),
                Secure.ANDROID_ID);
        Log.d("Server", "Android ID : " + android_id);

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
                          Toast.makeText(ServerTest.this, "Saved", Toast.LENGTH_SHORT).show();
                          Notification.Builder nb = new Notification.Builder(getApplicationContext());
                          NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                          SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
                          parkPlace = Integer.parseInt(v.getTag().toString());
                          String push = "Вы припарковались на месте " + parkPlace + ". Время парковки: " + sdf.format(System.currentTimeMillis());
                          Log.d("Server", "parkPlace : " + parkPlace);
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
                          new CreateNewProduct().execute();
                          Log.d("Server","parkTime: "+parkTime);
                          refresh();
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
            pDialog.dismiss();
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

            // получаем JSON объект
            JSONObject json = jsonParser.makeHttpRequest(url_create_space, "POST", params);

            try {
                Log.d("Create Response", json.toString());
            } catch(NullPointerException e) {
                Log.d("Create Response", "Null pointer!");
            }

            try {
                int success = json.getInt(TAG_SUCCESS);

                if (success == 1) {
                    // успех ёпт
                    Toast toast = Toast.makeText(getApplicationContext(),
                            "Успешно припорковались!", Toast.LENGTH_SHORT);
                    toast.show();
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
}