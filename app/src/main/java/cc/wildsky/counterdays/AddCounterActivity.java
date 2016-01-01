package cc.wildsky.counterdays;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AddCounterActivity extends AppCompatActivity implements View.OnClickListener {

    String eventDate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_counter);
        Button b = (Button) findViewById(R.id.button);
        b.setOnClickListener(this);

        CalendarView ct = (CalendarView) findViewById(R.id.calView);
        ct.setOnDateChangeListener(new CalendarView.OnDateChangeListener() { //監聽當日期改變

            public void onSelectedDayChange(CalendarView view, int year, int month, int date) { //Month從0算起
                eventDate = year + "/" + (month > 8 ? "" : "0") + (month + 1) + "/" + (date > 10 ? "" : "0") + date;
                Toast.makeText(getApplicationContext(), eventDate, Toast.LENGTH_SHORT).show();
            }
        });
    }

    long between(String small, String big, SimpleDateFormat sdf) {
        long fdate = 0, ndate = 0;
        try {
            try {
                fdate = sdf.parse(small).getTime();
                ndate = sdf.parse(big).getTime();
            } catch (java.lang.NullPointerException e) {
                Toast.makeText(getApplicationContext(), "Something Wrong!!", Toast.LENGTH_LONG).show();
            }
        } catch (ParseException e) {
            Toast.makeText(getApplicationContext(), "Something Wrong!!", Toast.LENGTH_LONG).show();
        }

        return (fdate - ndate) / (24 * 60 * 60 * 1000);
    }

    @Override
    public void onClick(View v) {

        TextView tv = (TextView) findViewById(R.id.eventName);
        String evenName = tv.getText().toString();

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
        String today = formatter.format(new Date());

        Intent resultData = new Intent();
        resultData.putExtra("valueName", "距離" + evenName + "還剩下" + between(eventDate, today, formatter) + "天");
        setResult(Activity.RESULT_OK, resultData);
        finish();
    }

    public void cancel(View v) {
        Toast.makeText(getApplicationContext(), "取消囉～", Toast.LENGTH_SHORT).show();
        Intent resultData = new Intent();
        resultData.putExtra("valueName", "cancel");
        setResult(Activity.RESULT_OK, resultData);
        finish();
    }
}
