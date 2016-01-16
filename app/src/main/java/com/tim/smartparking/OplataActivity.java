package com.tim.smartparking;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by aleksei on 14.12.15.
 */
public class OplataActivity extends Activity {

    public static String stayTime;
    String android_id;
    String session_id;
    private ProgressDialog pDialog;
    JSONParser jsonParser = new JSONParser();
    private static final String url_update_space = "http://testing44.rurs.net/update_space.php";
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_ANDROID_ID = "android_id";
    private static final String TAG_SESSION_ID = "session_id";
    private static final String TAG_PRICE = "price";
    public static int price;

    Button btnPay;

    static TextView tv3;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.oplata_info);
        btnPay = (Button) findViewById(R.id.pay);
        Intent i = getIntent();
        android_id = i.getStringExtra(TAG_ANDROID_ID);
        session_id = i.getStringExtra(TAG_SESSION_ID);
        Log.d(TAG_SESSION_ID, "Session ID Oplata: " + session_id);
        NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        nm.cancel(5);
        NfcAdapter adapter = NfcAdapter.getDefaultAdapter(this);
        TextView tv1 = (TextView) findViewById(R.id.textView);
        TextView tv2 = (TextView) findViewById(R.id.textView7);
        tv3 = (TextView) findViewById(R.id.textView8);
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        long time = System.currentTimeMillis() - ServerTest.parkTime - 3600 * 1000 * 3;
        stayTime = sdf.format(time);
        sdf = new SimpleDateFormat("HH");
        tv1.setText(stayTime);
        Log.d("Server", "stayTime: " + stayTime);
        if (Integer.parseInt(sdf.format(time)) < 0) {
            tv2.setText("Первый час на парковке бесплатен!");
            tv3.setText("");
            Log.d("Update", "Worked fine");
        } else {
            price = Integer.parseInt(sdf.format(time)) + 100;
            tv2.setText("Стоимость: " + price);
            Log.d("Server","Price: "+ price);
        }
        btnPay.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // запускаем выполнение задачи на обновление места
                new SaveProductDetails().execute();
            }
        });
    }

    class SaveProductDetails extends AsyncTask<String, String, String> {

        /**
         * Перед началом показываем в фоновом потоке прогрксс диалог
         **/
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(OplataActivity.this);
            pDialog.setMessage("Сохраняем инормацию...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        /**
         * Сохраняем продукт
         **/
        protected String doInBackground(String[] args) {


            // формируем параметры
            String priceStr = Integer.toString(price);
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair(TAG_ANDROID_ID, android_id));
            params.add(new BasicNameValuePair(TAG_PRICE, priceStr));
            params.add(new BasicNameValuePair(TAG_SESSION_ID, session_id));

            // отправляем измененные данные через http запрос
            JSONObject json = jsonParser.makeHttpRequest(url_update_space, "POST", params);

            // проверяем json success тег
            try {
                int success = json.getInt(TAG_SUCCESS);

                if (success == 1) {
                   Log.d("Server", "Success price");
                } else {
                    // продукт не обновлен
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        /**
         * После окончания закрываем прогресс диалог
         **/
        protected void onPostExecute(String file_url) {
            // закрываем прогресс диалог
            pDialog.dismiss();
        }
    }

}
