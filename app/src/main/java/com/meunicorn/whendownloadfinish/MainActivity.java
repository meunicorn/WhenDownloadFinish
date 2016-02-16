package com.meunicorn.whendownloadfinish;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private EditText filesize;
    private EditText downloadspeed;
    private Button calculateBtn;
    private Spinner fileSpinner;
    private Spinner ntwSpinner;
    private TextView timeResult;
    TextWatcher textWatcher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        filesize = (EditText) findViewById(R.id.filesize);
        downloadspeed = (EditText) findViewById(R.id.network);
        calculateBtn = (Button) findViewById(R.id.calculateBtn);
        fileSpinner = (Spinner) findViewById(R.id.sizetype);
        ntwSpinner = (Spinner) findViewById(R.id.downloadtype);
        timeResult = (TextView) findViewById(R.id.time);

        fileSpinnerMethod();
        ntwSpinnerMethod();
        calculateBtnMethod();

        filesize.addTextChangedListener(getTextWatcher());
        downloadspeed.addTextChangedListener(getTextWatcher());


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filesize.setText("");
                downloadspeed.setText("");
                timeResult.setText("");
            }
        });
    }

    private void calculateBtnMethod() {
        calculateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    calculate();
                } catch (Exception e) {

                }
            }
        });
    }

    private void calculate() {
        float filesize = fileTrans();
        float ntwSpeed = ntwTrans();
        Log.i("speed", "filesize:" + filesize + "  ntwspped:" + ntwSpeed);
        if (ntwSpeed == 0) {
            timeResult.setText("下到下辈子吧 ´_>`");
        } else {
            float downloadsecond = filesize / ntwSpeed;
            timeResult.setText("理想时间：" + transTime(downloadsecond));
        }
    }

    private String transTime(float downloadsecond) {
        int hours = (int) (downloadsecond / 3600);
        int minutes = (int) ((downloadsecond % 3600) / 60);
        int seconds = (int) (downloadsecond % 60);
        String transTimeStr = String.format("%02d:%02d:%02d", hours, minutes, seconds);
        return transTimeStr;
    }


    private float ntwTrans() {
        float speed = Float.parseFloat(downloadspeed.getText().toString());
        switch ((String) ntwSpinner.getSelectedItem()) {
            case "KB/S":
                speed = speed / 1024;
                break;
            case "MB/S":
                break;
            case "GB/S":
                speed = speed * 1024;
                break;
        }
        return speed;
    }

    private float fileTrans() {
        float size = Float.parseFloat(filesize.getText().toString());
        switch ((String) fileSpinner.getSelectedItem()) {
            case "KB":
                size = size / 1024;
                break;
            case "MB":
                break;
            case "GB":
                size = size * 1024;
                break;
        }
        return size;
    }

    private void ntwSpinnerMethod() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.downloadspeed, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ntwSpinner.setAdapter(adapter);
        ntwSpinner.setSelection(1);
    }

    private void fileSpinnerMethod() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.filesize, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        fileSpinner.setAdapter(adapter);
        fileSpinner.setSelection(1);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so float
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public TextWatcher getTextWatcher() {
        textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!filesize.getText().toString().isEmpty() && !downloadspeed.getText().toString().isEmpty()) {
                    calculate();
                }else {
                    timeResult.setText("");
                }
            }
        };
        return textWatcher;
    }
}
