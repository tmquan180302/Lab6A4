package com.example.lab6.Bai1;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;

import com.example.lab6.MainActivity;

import org.ksoap2.serialization.SoapObject;

public class ConvertTemperatureTask extends AsyncTask<String, Void, String> {
    ProgressDialog pDialog;
    private Bai1Activity activity;
    private String soapAction;
    private String methodName;
    private String paramsName;

    public ConvertTemperatureTask(Bai1Activity activity, String soapAction,
                                  String methodName, String paramsName) {
        this.soapAction = soapAction;
        this.methodName = methodName;
        this.paramsName = paramsName;
        this.activity = activity;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        pDialog = new ProgressDialog(activity);
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);
        pDialog.show();
    }

    @Override
    protected String doInBackground(String... params) {
//create a new soap request object
        SoapObject request = new SoapObject(Constants.NAME_SPACE,
                methodName);
//add properties for soap object
        request.addProperty(paramsName, params[0]);
//request to server and get Soap Primitive response
        return WebServiceCall.callWSThreadSoapPrimitive(Constants.URL,
                soapAction, request);
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        if (pDialog.isShowing()) {
            pDialog.dismiss();
        }
        if (result == null) {
            Log.i("check", "cannot get result");
        } else {
            Log.i("check", result);
//invoke call back method of Activity
            activity.callBackDataFromAsyncTask(result);
        }
    }
}
