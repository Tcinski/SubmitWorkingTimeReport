package com.tcin.submitworkingtimereport;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class Report extends AppCompatActivity {
    String sendText;
    Button buttonReport;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        Intent receivedReportIntent = getIntent();
        String firstName = receivedReportIntent.getStringExtra("firstName");
        String lastName = receivedReportIntent.getStringExtra("lastName");
        String firstName1 = receivedReportIntent.getStringExtra("firstName1");
        String positions = receivedReportIntent.getStringExtra("positions");
        int quantity = receivedReportIntent.getIntExtra("quantity",0);
        double numberOfHours = receivedReportIntent.getDoubleExtra("numberOfHours",0);
        double oneHourWage = receivedReportIntent.getDoubleExtra("oneHourWage",0);
        double oneMonthSalary = receivedReportIntent.getDoubleExtra("oneMonthSalary",0);


        sendText = "Фамилия: " + firstName + "\n" +
                   "Имя: " + lastName + "\n" +
                   "Отчество: " + firstName1 + "\n" +
                    "Должность: " + positions + "\n" +
                    "Количество смен: " + quantity + "\n" +
                    "Количество часов: " + numberOfHours + "\n" +
                    "Ставка в час: " + oneHourWage + " руб." + "\n"  +
                    "Зарплата за месяц: " + oneMonthSalary + "\n";


        TextView reportTextView = findViewById(R.id.textViewReport);
        reportTextView.setText(sendText);



        submitReport();

    }

    void submitReport(){
        buttonReport = findViewById(R.id.buttonReport);

        buttonReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_SEND); // интент для отправки
                intent.setType("*/*");
                intent.putExtra(Intent.EXTRA_TEXT,sendText);

                try {
                    startActivity(Intent.createChooser(intent,"Отправляю..."));
                    finish();
                }catch (ActivityNotFoundException anfe){
                    Toast.makeText(Report.this, "Укажите службу отправки...",Toast.LENGTH_LONG).show();
                }
            }
        });

    }
}