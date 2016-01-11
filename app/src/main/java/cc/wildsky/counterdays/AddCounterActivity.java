package cc.wildsky.counterdays;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AddCounterActivity extends AppCompatActivity implements View.OnClickListener {

    String eventDate;
    private String SAVE_FILE_NAME = "counter_savefile2.json";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_counter);

        Button b = (Button) findViewById(R.id.button);
        b.setOnClickListener(this);

        CalendarView ct = (CalendarView) findViewById(R.id.calView);
        ct.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            //監聽日期改變
            public void onSelectedDayChange(CalendarView view, int year, int month, int date) { //Month從0算起
                eventDate = year + "/" + (month > 8 ? "" : "0") + (month + 1) + "/" + (date >= 10 ? "" : "0") + date;
                Toast.makeText(getApplicationContext(), eventDate, Toast.LENGTH_SHORT).show();
            }
        });
    }

    long between(String small, String big, SimpleDateFormat sdf) {
        long fdate = 0, ndate = 0;
        try {
            fdate = sdf.parse(small).getTime();
            ndate = sdf.parse(big).getTime();
        } catch (java.lang.NullPointerException e) {
            Log.e("NullPtr", e.toString());
        } catch (ParseException e) {
            Log.e("ParseEcp", e.toString());
        }
        return (fdate - ndate) / (24 * 60 * 60 * 1000);
    }

    private void writeToFile(String data) {
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(openFileOutput(SAVE_FILE_NAME, Context.MODE_PRIVATE));
            outputStreamWriter.write(data);
            outputStreamWriter.close();
        } catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }

    private JSONArray readFromFile() {
        JSONArray counterArray = new JSONArray();

        try {
            InputStream inputStream = openFileInput(SAVE_FILE_NAME);

            if (inputStream != null) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString;
                StringBuilder stringBuilder = new StringBuilder();

                while ((receiveString = bufferedReader.readLine()) != null) {
                    stringBuilder.append(receiveString);
                }

                inputStream.close();
                String before_conv = stringBuilder.toString();
                counterArray = new JSONArray(before_conv);
            }
        } catch (FileNotFoundException e) {
            Log.e("File not found", e.toString());
        } catch (IOException e) {
            Log.e("Can not read file", e.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return counterArray;
    }

    @Override
    public void onClick(View v) {

        TextView tv = (TextView) findViewById(R.id.eventName);
        String evenName = tv.getText().toString();

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
        String today = formatter.format(new Date());

        String string = "距離" + evenName + "還剩下" + between(eventDate, today, formatter) + "天";

        JSONArray dataArray = readFromFile();

        try {
            JSONObject newEvent = new JSONObject();
            newEvent.put("Name", evenName);
            newEvent.put("Val", between(eventDate, today, formatter));
            dataArray.put(newEvent);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        writeToFile(dataArray.toString());

        Intent resultData = new Intent();
        resultData.putExtra("response", string);
        setResult(Activity.RESULT_OK, resultData);
        finish();
    }

    public void cancel(View v) {
        Toast.makeText(getApplicationContext(), "已取消！", Toast.LENGTH_SHORT).show();
        Intent resultData = new Intent();
        resultData.putExtra("response", "cancel");
        setResult(Activity.RESULT_OK, resultData);
        finish();
    }
}
