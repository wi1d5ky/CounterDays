package cc.wildsky.counterdays;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class MainActivity extends AppCompatActivity implements View.OnLongClickListener, View.OnClickListener {

    final static int add_code = 123;   // add a new counter
    final static int edit_code = 333;  // edit a counter
    static int counterNum = 0;
    int num_of_counter = 0;
    TextView selectedView = null;
    private String SAVE_FILE_NAME = "counter_savefile2.json";

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

        JSONArray jarray = new JSONArray();

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
                jarray = new JSONArray(before_conv);
            }
        } catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jarray;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        JSONArray data = readFromFile();

        for (int i = 0; i < data.length(); i++) {
            try {
                addCounter("距離" + data.getJSONObject(i).getString("Name") +
                        "還剩下" + data.getJSONObject(i).getInt("Val") + "天");
                num_of_counter += 1;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        if (num_of_counter == 0) {
            LinearLayout LL = (LinearLayout) findViewById(R.id.LL);
            TextView intro = new TextView(this);
            intro.setText("右下角的按鈕可以新增，單點可編輯，長按可移除。");
            intro.setId(R.id.intro);
            LL.addView(intro);
        }

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
        startActivityForResult(it, edit_code);
    }

    private void removeEleFromFile(String s) {
        JSONArray origin_data = readFromFile();
        JSONArray new_data = new JSONArray();
        boolean deleted = false;

        if (origin_data != null) {
            try {
                for (int i = 0; i < origin_data.length(); i++) {
                    String str = "距離" + origin_data.getJSONObject(i).getString("Name") +
                            "還剩下" + origin_data.getJSONObject(i).getInt("Val") + "天";
                    if (!str.equals(s) && !deleted)
                        new_data.put(origin_data.get(i));
                    else
                        deleted = true;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        writeToFile(new_data.toString());
    }

    @Override
    public boolean onLongClick(View v) {
        LinearLayout LL = (LinearLayout) findViewById(R.id.LL);
        TextView tv = (TextView) v;
        removeEleFromFile(tv.getText().toString());
        Log.i("num_of_counter ", String.valueOf(num_of_counter));
        num_of_counter -= 1;


        if (num_of_counter == 0) {
            TextView intro = new TextView(this);
            intro.setText("右下角的按鈕可以新增，單點可編輯，長按可移除。");
            intro.setId(R.id.intro);
            LL.addView(intro);
        }

        LL.removeView(v);
        Toast.makeText(getApplicationContext(), "已移除", Toast.LENGTH_SHORT).show();
        return true;
    }

    public void go(View v) {
        Intent it = new Intent(this, AddCounterActivity.class);
        startActivityForResult(it, add_code);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {
        if (resultCode == RESULT_OK) {
            String myValue = data.getStringExtra("response");
            switch (requestCode) {
                case edit_code:
                    if (!myValue.equals("cancel")) {
                        LinearLayout LL = (LinearLayout) findViewById(R.id.LL);
                        LL.removeView(selectedView);
                        removeEleFromFile(selectedView.getText().toString());
                        num_of_counter -= 1;
                        Toast.makeText(getApplicationContext(), "已更新", Toast.LENGTH_SHORT).show();
                    }
                case add_code:
                    if (!myValue.equals("cancel")) {
                        addCounter(myValue);
                        num_of_counter += 1;
                    }
                    LinearLayout LL = (LinearLayout) findViewById(R.id.LL);
                    TextView intro = (TextView) findViewById(R.id.intro);
                    if (intro != null)
                        LL.removeView(intro);
                    break;
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
