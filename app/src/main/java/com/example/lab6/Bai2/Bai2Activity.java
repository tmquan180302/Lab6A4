package com.example.lab6.Bai2;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.lab6.R;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class Bai2Activity extends AppCompatActivity {
    private EditText amountEditText;
    private Spinner fromCurrencySpinner;
    private Spinner toCurrencySpinner;
    private Button convertButton;
    private TextView resultTextView;

    private static final String NAMESPACE = "http://www.webserviceX.NET/";
    private static final String URL = "http://www.webservicex.net/CurrencyConvertor.asmx";
    private static final String SOAP_ACTION = "http://www.webserviceX.NET/ConversionRate";
    private static final String METHOD_NAME = "ConversionRate";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bai2);

        amountEditText = findViewById(R.id.amountEditText);
        fromCurrencySpinner = findViewById(R.id.fromCurrencySpinner);
        toCurrencySpinner = findViewById(R.id.toCurrencySpinner);
        convertButton = findViewById(R.id.convertButton);
        resultTextView = findViewById(R.id.resultTextView);

        // Thêm danh sách mã tiền tệ vào Spinner
        List<String> currencies = new ArrayList<>();
        currencies.add("USD");
        currencies.add("EUR");
        currencies.add("JPY");
        // Thêm các loại tiền tệ khác nếu cần

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, currencies);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        fromCurrencySpinner.setAdapter(adapter);
        toCurrencySpinner.setAdapter(adapter);

        convertButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fromCurrency = fromCurrencySpinner.getSelectedItem().toString();
                String toCurrency = toCurrencySpinner.getSelectedItem().toString();
                double amount = Double.parseDouble(amountEditText.getText().toString());

                ConvertCurrencyTask task = new ConvertCurrencyTask();
                task.execute(fromCurrency, toCurrency, String.valueOf(amount));
            }
        });
    }

    private class ConvertCurrencyTask extends AsyncTask<String, Void, Double> {
        @Override
        protected Double doInBackground(String... params) {
            String fromCurrency = params[0];
            String toCurrency = params[1];
            double amount = Double.parseDouble(params[2]);

            try {
                SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
                request.addProperty("FromCurrency", fromCurrency);
                request.addProperty("ToCurrency", toCurrency);

                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                envelope.setOutputSoapObject(request);

                HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
                androidHttpTransport.call(SOAP_ACTION, envelope);

                SoapPrimitive response = (SoapPrimitive) envelope.getResponse();
                double conversionRate = Double.parseDouble(response.toString());

                return amount * conversionRate;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Double result) {
            super.onPostExecute(result);
            if (result != null) {
                DecimalFormat df = new DecimalFormat("#.##");
                resultTextView.setText("Result: " + df.format(result));
            } else {
                resultTextView.setText("Error occurred during conversion.");
            }
        }
    }
}