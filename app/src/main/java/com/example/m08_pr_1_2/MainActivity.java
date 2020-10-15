package com.example.m08_pr_1_2;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    private int number;
    private int attempts;


    public static final String EXTRA_MESSAGE = "com.example.m08_pr_1_2.MESSAGE";

    private int time;
    private int recordTime;
    private int minutes;
    private int seconds;
    private TimerTask second;
    private Timer timer;
    private TextView txtTimer;
    private boolean timerActive;


    private AlertDialog.Builder adRanking;
    private EditText etRanking;



    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        reset();

        txtTimer = findViewById(R.id.txtTimer);
        txtTimer.setText("");
        txtTimer.setTextColor(Color.BLACK);

        setTimerTask();

        setRankingDialog();

        final TextView txtAttempts = findViewById(R.id.txtAttempts);
        txtAttempts.setText("Intentos: " + attempts);
        txtAttempts.setTextColor(Color.BLACK);

        final EditText editNumber = findViewById(R.id.editNumber);
        
        final Button btValidate = findViewById(R.id.btValidate);
        btValidate.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                String sIn = String.valueOf(editNumber.getText());
                String toastText = null;

                if (sIn.equals("") || sIn == null)
                {
                    toastText = "No has introducido ningun numero!!";
                }
                else
                {
                    int in = Integer.parseInt(sIn);

                    if (number > in)
                    {
                        toastText = "El numero que buscas es mas grande que " + in;
                        attempts++;
                    }
                    else if (number < in)
                    {
                        toastText = "El numero que buscas es mas pequeño que " + in;
                        attempts++;
                    }
                    else
                    {
                        timerActive = false;
                        recordTime = time;
                        toastText = "Has encontrado el numero " + number + " en " + attempts + " intentos y " + recordTime + " segundos!";
                        adRanking.show();

                    }
                }

                Toast t = Toast.makeText(getApplicationContext(), toastText, Toast.LENGTH_SHORT);
                t.show();

                txtAttempts.setText("Intentos: " + attempts);
                editNumber.setText("");
            }
        });
    }

    private void openRanking(String nick) {

        Intent intent = new Intent(this, Ranking.class);
        String message = nick + "," + attempts + "," + recordTime;
        intent.putExtra(EXTRA_MESSAGE,message);
        startActivity(intent);
    }


    private void reset()
    {
        timerActive = true;
        number = (int) (Math.random() * 100 + 1);
        number = 10;
        attempts = 0;
        time = 0;
    }

    private void setTimerTask() {
        second = new TimerTask() {

            public void run() {
                if (timerActive)
                {
                    time++;

                    if (time == 60) {
                        time = 0;
                        minutes++;
                    }
                    seconds = time;

                    runOnUiThread(new Runnable() {
                        public void run() {
                            txtTimer.setText(getTimerString());
                        }
                    });


                    if (minutes == 59 && time == 59)
                    {
                        timer.cancel();
                        second.cancel();
                    }
                }

            }
        };

        timer = new Timer();
        timer.schedule(second, 0, 1000);
    }


    private void setRankingDialog() {
        adRanking = new AlertDialog.Builder(this);
        adRanking.setTitle("Ranking");
        adRanking.setMessage("Introduce tu nombre: ");
        etRanking = new EditText(this);
        adRanking.setView(etRanking);

        adRanking.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String value =  etRanking.getText().toString();
                openRanking(value);
                reset();
            }
        });

        adRanking.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                reset();
            }
        });
    }

    private String getTimerString()
    {
        StringBuilder sBuilder = new StringBuilder();

        if (minutes < 10)
            sBuilder.append("0");
       sBuilder.append(minutes + ":");

        if (seconds < 10)
            sBuilder.append("0");
        sBuilder.append(seconds);

        return sBuilder.toString();
    }
}