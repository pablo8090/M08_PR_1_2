package com.example.m08_pr_1_2;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    private int number;
    private int attempts;
    private int recordAtt;

    public static final String EXTRA_MESSAGE = "com.example.m08_pr_1_2.MESSAGE";
    public static final String EXTRA_BITMAP  = "com.example.m08_pr_1_2.BITMAP";

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

    private Bitmap rankingBitmap;
    private String rankingNick;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        reset();

        // Setting TextViews
            // Timer view
        txtTimer = findViewById(R.id.txtTimer);
        txtTimer.setText("");
            // Log view
        final TextView txtLog = findViewById(R.id.txtLog);
        txtLog.setText("");
        txtLog.setTextColor(Color.RED);
        txtLog.setTextSize(TypedValue.COMPLEX_UNIT_SP,17);

        // Set the task for the timer, which executes every second for have a timer
        setTimerTask();

        // Set and prepare the dialog
        setRankingDialog();

        // Set attempts textview
        final TextView txtAttempts = findViewById(R.id.txtAttempts);
        txtAttempts.setText("Fallos: " + attempts);

        // Gets edit text
        final EditText editNumber = findViewById(R.id.editNumber);

        // Setting the validation button
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
                    if (in < 1 || in > 100)
                    {
                        logReg = "El numero introducido no esta entre 1 y 100!!";
                    }
                    else
                    {
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
                        else // Number found
                        {
                            // Stops the timer, saves the record time and opens the ranking dialog
                            timerActive = false;
                            recordTime = time + (minutes*60);
                            recordAtt = attempts;
                            logReg = "Has encontrado el numero " + number + " en " + recordAtt + " intentos y " + recordTime + " segundos!";
                            showRankingDialog();

                        }
                    }

                }

                // Every click on this validate button will generate an action which is saved in the log
                // This function add the new register to the log, also it manages deleting old logs for avoid cutting off the text
                manageLog(txtLog, logReg);

                // reset attempts textview and the edit text
                txtAttempts.setText("Fallos: " + attempts);
                editNumber.setText("");

            }
        });
    }

    private void openRanking() {
        // Opens activity ranking with the intent message for save the new result
        Intent intent = new Intent(MainActivity.this, Ranking.class);
        String message = rankingNick + "," + recordAtt + "," + recordTime;
        intent.putExtra(EXTRA_MESSAGE,message);
        intent.putExtra(EXTRA_BITMAP, rankingBitmap);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK)
        {
            Bundle extras = data.getExtras();
            rankingBitmap = (Bitmap) extras.get("data");
            openRanking();
        }




    }

    private void getCameraPhoto() {
        Intent takePictureIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);

        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, 1);
        }
        startActivityForResult(takePictureIntent, 1);

    }

    private void reset()
    {
        // Resets all parameters for new game
        timerActive = true;
        number = (int) (Math.random() * 100 + 1);
        number = 1;
        attempts = 0;
        time = 0;
        minutes = 0;
        TextView log = findViewById(R.id.txtLog);
        log.setText("");
        TextView att = findViewById(R.id.txtAttempts);
        att.setText("Fallos: 0");
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
        // Show the ranking dialog and after show it gets the possitive button an override his click listener for control the input.
        adRanking.show();
        adRanking.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String value =  etRanking.getText().toString();

                if ((value.length() <= 10 && value.length() >= 1) && !value.contains("\n"))
                {
                    rankingNick = value;
                    adRanking.dismiss();
                    getCameraPhoto();

                }
                else
                {
                    Toast t = Toast.makeText(getApplicationContext(), "El nombre tiene que contener entre 1 y 10 caracteres y sin saltos de linea", Toast.LENGTH_LONG);
                    t.show();
                }


                reset();
            }
        });
    }
    private void setRankingDialog() {
        // Builds ranking dialog
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
        // This function returns a 00:00 format timer string
        StringBuilder sBuilder = new StringBuilder();

        if (minutes < 10)
            sBuilder.append("0");
       sBuilder.append(minutes + ":");

        if (seconds < 10)
            sBuilder.append("0");
        sBuilder.append(seconds);

        return sBuilder.toString();
    }

    private void manageLog(TextView txtLog, String logReg){
        // Save last '\n' before new append
        int lastChar = txtLog.getText().length() - 1;
        txtLog.append(getTimerString() + " > " + logReg + "\n");



        if ((txtLog.getLineCount() * txtLog.getLineHeight() > txtLog.getHeight()))
        {
            CharSequence logContent = txtLog.getText();
            CharSequence lastAppend = logContent.subSequence(lastChar+1, logContent.length());

            //i will be the index of the first line end -- '\n'
            int i = 0;
            for (i = 0; i < logContent.length(); i++)
            {
                if (logContent.charAt(i) == '\n')
                    break;
            }


            // Erase one or two registers depending if appended text needs more or less space for not being cut off.

            if (txtLog.getPaint().measureText(logContent, 0, i) > txtLog.getWidth())
            {
                // First register fills more than one line so we can just erase it
                txtLog.setText(logContent.subSequence(i+1,logContent.length()));
            }
            else
            {
                if (txtLog.getPaint().measureText(lastAppend, 0, lastAppend.length()) > txtLog.getWidth())
                {
                    // In this case first register only fill one line and last append fill two, so we need to erase two registers
                    // Finding second register...
                    int j;
                    for (j = i+1; j < logContent.length(); j++)
                    {
                        if (logContent.charAt(j) == '\n')
                            break;
                    }
                    txtLog.setText(logContent.subSequence(j+1,logContent.length()));
                }
                else
                {
                    // Last append only fills one line so we can just erase first register
                    txtLog.setText(logContent.subSequence(i+1,logContent.length()));
                }
            }

        }

    }
}