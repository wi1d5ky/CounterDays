package cc.wildsky.counterdays;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import static android.widget.PopupMenu.*;

public class MainActivity extends AppCompatActivity implements View.OnLongClickListener, View.OnClickListener {

    static int request_code = 123;
    static int counterNum = 0;
    int a = 0;
    TextView selectedView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new FloatingActionButton.OnClickListener() {

            @Override
            public void onClick(View v) {
                go(v);
            }
        });
    }

    @Override
    public void onClick(View v) {
        selectedView = (TextView) v;
        Intent it = new Intent(this, AddCounterActivity.class);
        startActivityForResult(it, 333);
    }

    @Override
    public boolean onLongClick(View v) {
        LinearLayout LL = (LinearLayout) findViewById(R.id.LL);
        LL.removeView(v);
        Toast.makeText(getApplicationContext(), "已移除", Toast.LENGTH_SHORT).show();
        return true;
    }

    public void go(View v) {
        Intent it = new Intent(this, AddCounterActivity.class);
        startActivityForResult(it, request_code);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {
        if (requestCode == request_code && resultCode == RESULT_OK) {
            String myValue = data.getStringExtra("valueName");
            if (!myValue.equals("cancel"))
                addCounter(myValue);
        }

        if (requestCode == 333 && resultCode == RESULT_OK) {
            String myValue = data.getStringExtra("valueName");
            if (!myValue.equals("cancel")) {
                LinearLayout LL = (LinearLayout) findViewById(R.id.LL);
                LL.removeView(selectedView);
                Toast.makeText(getApplicationContext(), "已更新", Toast.LENGTH_SHORT).show();
                addCounter(myValue);
            }
        }
    }

    private void addCounter(String val) {
        TextView newt = new TextView(this);
        newt.setId(counterNum + 500);
        LinearLayout LL = (LinearLayout) findViewById(R.id.LL);
        newt.setTextSize(36);
        newt.setText(val);
        LL.addView(newt);
        newt.setOnClickListener(this);
        newt.setOnLongClickListener(this);
    }
}
