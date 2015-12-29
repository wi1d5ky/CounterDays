package cc.wildsky.counterdays;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.TextView;

import java.util.Date;

public class AddCounterActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_counter);
        Button b = (Button) findViewById(R.id.button);
        b.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        TextView tv = (TextView) findViewById(R.id.eventName);
        String evenName = tv.getText().toString();
        CalendarView ct = (CalendarView) findViewById(R.id.calView);
        long eventDate = ct.getDate();
        String s = "距離" + evenName + "還剩下" + eventDate;

        Intent resultData = new Intent();
        resultData.putExtra("valueName", s);
        setResult(Activity.RESULT_OK, resultData);
        finish();
    }
}
