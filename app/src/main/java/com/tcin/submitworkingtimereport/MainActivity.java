package com.tcin.submitworkingtimereport;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Array;
import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    int quantity; // для изменения кол-ва смен
    double salary;//зарплата в час
    String textSalary = " руб./час";
    String positionsName;//выбор спинера
    int twelveHourShift = 12; //смена 12 часов
    int nineHourShift = 8; //смена 8 часа
    int fourHourShift = 4; //смена 4 часа
    TextView textViewSalary;

    Spinner spinner;
    ArrayList<String> listPositions;
    ArrayAdapter<String> adapterListPositions;

    HashMap<String, Double> positionsMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textViewSalary = findViewById(R.id.salaryTextView);

        numberOfShiftButtons();
        createSpinner();
        createMap();
        sendingReport();


    }

    void sendingReport(){
        EditText editTextPersonName = findViewById(R.id.editTextPersonName);
        EditText editTextPersonName2 = findViewById(R.id.editTextPersonName2);
        EditText editTextPersonName3 = findViewById(R.id.editTextPersonName3);
        Button buttonGenerateReport = findViewById(R.id.buttonGenerateReport);

        buttonGenerateReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Order order = new Order();
                order.firstName = editTextPersonName.getText().toString();
                order.lastName = editTextPersonName2.getText().toString();
                order.firstName1 = editTextPersonName3.getText().toString();
                order.positions = spinner.getSelectedItem().toString();
                order.quantity = quantity;
                order.oneHourWage = salary;

                if (positionsName.equals(Positions.APPRAISER)) {
                    order.oneMonthSalary = quantity * salary * twelveHourShift;
                    order.numberOfHours = quantity * twelveHourShift;

                }else if (positionsName.equals(Positions.DISTRIBUTOR)) {
                    order.oneMonthSalary = quantity * salary * twelveHourShift;
                    order.numberOfHours = quantity * twelveHourShift;

                }else if (positionsName.equals(Positions.CLEANER)) {
                    order.oneMonthSalary = quantity * salary * fourHourShift;
                    order.numberOfHours = quantity * fourHourShift;

                }else if (positionsName.equals(Positions.LOADER)) {
                    order.oneMonthSalary = quantity * salary * nineHourShift;
                    order.numberOfHours = quantity * nineHourShift;
        }
//                order.oneMonthSalary = quantity * salary * twelveHourShift;

                Intent intent = new Intent(MainActivity.this,Report.class);
                intent.putExtra("firstName",order.firstName);
                intent.putExtra("lastName",order.lastName);
                intent.putExtra("firstName1",order.firstName1);
                intent.putExtra("positions",order.positions);
                intent.putExtra("quantity",order.quantity);
                intent.putExtra("numberOfHours",order.numberOfHours);//кол-во часов
                intent.putExtra("oneHourWage",order.oneHourWage);
                intent.putExtra("oneMonthSalary",order.oneMonthSalary);

                if(editTextPersonName.getText().toString().equals("")) {
                    Toast.makeText(MainActivity.this,"Введите: Фамилию",Toast.LENGTH_LONG).show();
                }else if (editTextPersonName2.getText().toString().equals("")){
                    Toast.makeText(MainActivity.this,"Введите: Имя",Toast.LENGTH_LONG).show();
                }else if (editTextPersonName3.getText().toString().equals("")) {
                    Toast.makeText(MainActivity.this, "Введите: Отчество", Toast.LENGTH_LONG).show();
                }else{
                    startActivity(intent);
                }


            }
        });
    }

    //кнопки +- и текст между ними
    void numberOfShiftButtons(){
        TextView textNumber = findViewById(R.id.number_of_shifts);//кол-во смен

        Button buttonMinus = findViewById(R.id.minus);
        buttonMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(quantity != 0) {
                    quantity--;
                    textNumber.setText(String.valueOf(quantity));
                   timeTracking();
                }

            }
        });


        Button buttonPlus = findViewById(R.id.plus);
        buttonPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (quantity < 31) {
                    quantity++;
                    textNumber.setText(String.valueOf(quantity));
                    timeTracking();
                }
            }
        });


    }



    void createMap(){
        positionsMap = new HashMap<>();

        positionsMap.put(Positions.APPRAISER, Positions.SALARY_APPRAISER);
        positionsMap.put(Positions.DISTRIBUTOR, Positions.SALARY_DISTRIBUTOR);
        positionsMap.put(Positions.CLEANER, Positions.SALARY_CLEANER);
        positionsMap.put(Positions.LOADER, Positions.SALARY_LOADER);
    }

    //спинер
    void createSpinner(){
        spinner = findViewById(R.id.spinner_position);
        spinner.setOnItemSelectedListener(this);//установить выбранный слушатель
        listPositions = new ArrayList<>();

        listPositions.add(Positions.APPRAISER);
        listPositions.add(Positions.DISTRIBUTOR);
        listPositions.add(Positions.CLEANER);
        listPositions.add(Positions.LOADER);

        adapterListPositions = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, listPositions);
        adapterListPositions.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(adapterListPositions);


    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        positionsName = spinner.getSelectedItem().toString();//получили должность
        salary = positionsMap.get(positionsName);// по должности получили сумму
        timeTracking();

        TextView salaryOneHours = findViewById(R.id.textViewOneHours);
        switch (positionsName){
            case Positions.APPRAISER:
                salaryOneHours.setText(salary + textSalary);
                break;
            case Positions.DISTRIBUTOR:
                salaryOneHours.setText(salary + textSalary);
                break;
            case Positions.CLEANER:
                salaryOneHours.setText(salary + textSalary);
                break;
            case Positions.LOADER:
                salaryOneHours.setText(salary + textSalary);
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    void timeTracking(){
//        if (positionsName.equals(Positions.APPRAISER)) {
//            textViewSalary.setText(String.valueOf(quantity * salary * twelveHourShift));
//        }else if (positionsName.equals(Positions.DISTRIBUTOR)) {
//            textViewSalary.setText(String.valueOf(quantity * salary * twelveHourShift));
//        }else if (positionsName.equals(Positions.CLEANER)) {
//            textViewSalary.setText(String.valueOf(quantity * salary * fourHourShift));
//        }else if (positionsName.equals(Positions.LOADER)) {
//            textViewSalary.setText(String.valueOf(quantity * salary * nineHourShift));
//        }
        switch (positionsName){
            case Positions.APPRAISER:
                textViewSalary.setText(String.valueOf(quantity * salary * twelveHourShift));
                break;
            case Positions.DISTRIBUTOR:
                textViewSalary.setText(String.valueOf(quantity * salary * twelveHourShift));
                break;
            case Positions.CLEANER:
                textViewSalary.setText(String.valueOf(quantity * salary * fourHourShift));
                break;
            case Positions.LOADER:
                textViewSalary.setText(String.valueOf(quantity * salary * nineHourShift));
                break;
        }
        }
}