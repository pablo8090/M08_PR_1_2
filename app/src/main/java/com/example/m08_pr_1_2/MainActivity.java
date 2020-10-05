package com.example.m08_pr_1_2;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private int number;
    private int attempts;

    private void reset()
    {
        number = (int) (Math.random() * 100 + 1);
        attempts = 0;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        reset();


        final TextView txtAttempts = findViewById(R.id.txtAttempts);
        txtAttempts.setText("Intentos: " + attempts);

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
                        toastText = "Has encontrado el numero " + number + " en " + attempts + " intentos!";
                        reset();
                    }
                }

                Toast t = Toast.makeText(getApplicationContext(), toastText, Toast.LENGTH_SHORT);
                t.show();

                txtAttempts.setText("Intentos: " + attempts);
                editNumber.setText("");
            }
        });

    }
}