package com.example.lab6.Bai1;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.lab6.R;
import com.google.android.material.textfield.TextInputEditText;

public class Bai1Activity extends AppCompatActivity implements View.OnClickListener {
    private TextInputEditText edtFC;
    private Button btnF;
    private Button btnC;
    private TextView tvResult;
    int convertStyle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bai1);

        edtFC = (TextInputEditText) findViewById(R.id.edtF_C);
        btnF = (Button) findViewById(R.id.btnF);
        btnC = (Button) findViewById(R.id.btnC);
        tvResult = (TextView) findViewById(R.id.tvResult);

        btnF.setOnClickListener(this);
        btnC.setOnClickListener(this);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnF:
                invokeAsyncTask("Fahrenheit", Constants.F_TO_C_SOAP_ACTION,
                        Constants.F_TO_C_METHOD_NAME);
                convertStyle = 1;
                break;
            case R.id.btnC:
                invokeAsyncTask("Celsius", Constants.C_TO_F_SOAP_ACTION,
                        Constants.C_TO_F_METHOD_NAME);
                convertStyle = 0;
                break;
        }
    }

    //create and execute asynctask to get response from W3school server
    private void invokeAsyncTask(String convertParams, String soapAction,
                                 String methodName) {
        new ConvertTemperatureTask(this, soapAction, methodName,
                convertParams).execute(edtFC.getText()
                .toString().trim());
    }

    //call back data from background thread and set to views
    public void callBackDataFromAsyncTask(String result) {
        double resultTemperature = Double.parseDouble(result); //parse
        if (convertStyle == 0) {
            // C degree to F degree
            tvResult.setText(edtFC.getText().toString().trim() + " degreeCelsius = "
                    + String.format("%.2f", resultTemperature) + " degreeFahrenheit");
        } else {// F degree to C degree
            tvResult.setText(edtFC.getText().toString().trim() + " degreeFahrenheit = "
                    + String.format("%.2f", resultTemperature) + " degreeCelsius");
        }
    }
}