package com.example.m08_pr_1_2;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;

public class Ranking extends AppCompatActivity {

    static ArrayList<Result> results = new ArrayList<Result>();

    public void backToMain(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private TextView generateTV (String text, int color, int dpTextSize, TableRow.LayoutParams textViewParams)
    {
        TextView tv = new TextView (this);
        tv.setText(text);
        tv.setTextColor(color);
        tv.setLayoutParams(textViewParams);
        tv.setGravity(Gravity.CENTER);
        tv.setTextSize(TypedValue.COMPLEX_UNIT_SP,dpTextSize);
        return tv;
    }

    private TableRow generateRow (String[] items, int dpTextSize, TableRow.LayoutParams textViewParams, TableRow.LayoutParams tableRowParams) {

        TableRow tr = new TableRow(this);
        tr.setLayoutParams(tableRowParams);
        //tr.setGravity(Gravity.CENTER);

        for (int i = 0; i < items.length; i++)
        {
            TextView tv = generateTV(items[i], Color.BLACK, dpTextSize, textViewParams);
            tr.addView(tv);
        }
        return tr;
    }
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranking);

        // Getting intent extra message info and adding a new Result
        Intent intent = getIntent();
        String message = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);
        String[] splittedMsg = message.split(",");
        results.add(new Result(splittedMsg[0], Integer.parseInt(splittedMsg[1]), Integer.parseInt(splittedMsg[2])));

        // Creaint TableLayout and the row layout parameters
        TableLayout tbl = findViewById(R.id.tlRanking);

        TableRow.LayoutParams textViewParams = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT,1.0f);
        TableRow.LayoutParams tableRowParams = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT);

        // Setting Header Row
        String headers[] = new String[3];
        headers[0] = "USUARI";
        headers[1] = "INTENTS";
        headers[2] = "TEMPS (s)";
        tbl.addView(generateRow(headers, 25, textViewParams, tableRowParams));

        // Setting all results rows
        for (Result r : results)
        {
            String[] items = r.getData();
            tbl.addView(generateRow(items, 20,textViewParams, tableRowParams));
        }

        // Setting back button

        final Button bt = findViewById(R.id.btBack);
        bt.setText("Volver a jugar");
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backToMain();
            }
        });
    }
}