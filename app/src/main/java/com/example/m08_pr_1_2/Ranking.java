package com.example.m08_pr_1_2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

public class Ranking extends AppCompatActivity {

    static ArrayList<Result> results = new ArrayList<Result>();

    public void backToMain(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranking);

        // Getting intent extra message info
        Intent intent = getIntent();
        String message = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);

        String[] splittedMsg = message.split(",");
        results.add(new Result(splittedMsg[0], Integer.parseInt(splittedMsg[1]), Integer.parseInt(splittedMsg[2])));


        // Setting Headers TextViews
        TextView txtHeader1 = findViewById(R.id.txtHeader1);
        txtHeader1.setText("USUARI");
        txtHeader1.setTextColor(Color.BLACK);

        TextView txtHeader2 = findViewById(R.id.txtHeader2);
        txtHeader2.setText("INTENTS");
        txtHeader2.setTextColor(Color.BLACK);

        TextView txtHeader3 = findViewById(R.id.txtHeader3);
        txtHeader3.setText("TEMPS");
        txtHeader3.setTextColor(Color.BLACK);

        // Setting Data TextViews
        TextView txtData1 = findViewById(R.id.txtData1);
        txtData1.setText("");
        txtData1.setTextColor(Color.BLACK);

        TextView txtData2 = findViewById(R.id.txtData2);
        txtData2.setText("");
        txtData2.setTextColor(Color.BLACK);

        TextView txtData3 = findViewById(R.id.txtData3);
        txtData3.setText("");
        txtData3.setTextColor(Color.BLACK);





        for (Result r : results)
        {
            txtData1.append(r.nick + "\n");
            txtData2.append(r.tries + "\n");
            txtData3.append(r.time + "\n");
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