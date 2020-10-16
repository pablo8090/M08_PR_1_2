package com.example.m08_pr_1_2;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.util.TypedValue;
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


    private AlertDialog adRanking;
    private EditText etRanking;



    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        reset();

        // Setting TextViews
            // Timer view
        txtTimer = findViewById(R.id.txtTimer);
        txtTimer.setText("");
        txtTimer.setTextColor(Color.BLACK);
            // Log view
        final TextView txtLog = findViewById(R.id.txtLog);
        txtLog.setText("");
        txtLog.setTextColor(Color.RED);
        txtLog.setTextSize(TypedValue.COMPLEX_UNIT_SP,17);


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
                String logReg = null;

                if (sIn.equals("") || sIn == null)
                {
                    logReg = "No has introducido ningun numero!!";
                }
                else
                {
                    int in = Integer.parseInt(sIn);

                    if (number > in)
                    {
                        logReg = "El numero que buscas es mas grande que " + in;
                        attempts++;
                    }
                    else if (number < in)
                    {
                        logReg = "El numero que buscas es mas pequeño que " + in;
                        attempts++;
                    }
                    else
                    {
                        timerActive = false;
                        recordTime = time;
                        logReg = "Has encontrado el numero " + number + " en " + attempts + " intentos y " + recordTime + " segundos!";
                        showRankingDialog();

                    }
                }

                txtLog.append(getTimerString() + " > " + logReg + "\n");
                if ((txtLog.getLineCount() * txtLog.getLineHeight() > txtLog.getHeight()))
                {
                   CharSequence logContent = txtLog.getText();
                   int i = 0;
                   for (i = 0; i < logContent.length(); i++)
                   {
                       if (logContent.charAt(i) == '\n')
                           break;
                   }

                   txtLog.setText(logContent.subSequence(i+1,logContent.length()));
                }
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
        minutes = 0;
        TextView log = findViewById(R.id.txtLog);
        log.setText("");
        TextView att = findViewById(R.id.txtAttempts);
        att.setText("Intentos: 0");
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


    private void showRankingDialog()
    {
        adRanking.show();
        adRanking.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String value =  etRanking.getText().toString();
                if (value.length() <= 10 && value.length() >= 1)
                {
                    adRanking.dismiss();
                    openRanking(value);
                }
                else
                {
                    Toast t = Toast.makeText(getApplicationContext(), "El nombre tiene que contener entre 1 y 10 caracteres", Toast.LENGTH_LONG);
                    t.show();
                }


                reset();
            }
        });
    }
    private void setRankingDialog() {
        AlertDialog.Builder adb = new AlertDialog.Builder(this);
        adb.setTitle("¿Quieres guardar tu puntuación en el Ranking?");
        adb.setMessage("Introduce tu nombre: ");
        etRanking = new EditText(this);
        adb.setView(etRanking);

        adb.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        adb.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                reset();
            }
        });

        adRanking = adb.create();
        adRanking.setCanceledOnTouchOutside(false);

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