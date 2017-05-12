package com.m.chittorgarh.View.Activities;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.m.chittorgarh.Halper.Sources;
import com.m.chittorgarh.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.pedant.SweetAlert.SweetAlertDialog;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ForgotPassword extends AppCompatActivity {
    EditText email,mobile;
    Button submitButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        setTitle("Forget Password");
        submitButton = (Button)findViewById(R.id.submit_btn);
        email = (EditText)findViewById(R.id.email);
        mobile = (EditText)findViewById(R.id.email);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (email.getText().toString().matches("")||mobile.getText().toString().matches("")){
                    Toast.makeText(ForgotPassword.this, "Enter Email Or Mobile For Reset Password", Toast.LENGTH_SHORT).show();
                }else {
                    if (email.getText().toString().length()>1){
                        vaiEmail();
                    }else {
                        vaiMobile();
                    }
                }
            }
        });
    }

    private void vaiEmail(){
        if(!emailValidator(email.getText().toString())){
            email.setError("Enter Valid Email");
            email.requestFocus();
        }else {
            if (isNetworkAvailable(getApplicationContext())){
                final SweetAlertDialog pDialog = new SweetAlertDialog(ForgotPassword.this, SweetAlertDialog.PROGRESS_TYPE);
                pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                pDialog.setTitleText("Loading");
                pDialog.setCancelable(true);
                pDialog.show();
                OkHttpClient httpClient = new OkHttpClient.Builder()
                        .connectTimeout(10, TimeUnit.SECONDS)
                        .writeTimeout(10, TimeUnit.SECONDS)
                        .readTimeout(30, TimeUnit.SECONDS)
                        .retryOnConnectionFailure(true)
                        .build();
                RequestBody params  = new FormBody.Builder().add("email",email.getText().toString()).build();
                Request request = new Request.Builder().url(Sources.Forgot).post(params).build();
                final Call call = httpClient.newCall(request);
                pDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        call.cancel();
                    }
                });

                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        Log.e("error",e.getMessage());
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                pDialog.changeAlertType(SweetAlertDialog.ERROR_TYPE);
                                pDialog.setTitleText("Connection Time Out");
                                pDialog.setContentText("Retry");
                                pDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                                        vaiMobile();
                                        pDialog.dismiss();
                                    }
                                });
                            }
                        });
                    }
                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        try {
                            JSONObject jsonObject = new JSONObject(response.body().string());
                            if (jsonObject.getString("status").matches("1")) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        pDialog.cancel();
                                        Toast.makeText(getApplicationContext(), "An email with a confirmation link has been sent your email address", Toast.LENGTH_LONG).show();
                                    }
                                });

                            }else if (jsonObject.getString("status").matches("0")){
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        pDialog.cancel();
                                        Toast.makeText(getApplicationContext(), "Invalid  Details", Toast.LENGTH_LONG).show();
                                    }
                                });
                            }
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    pDialog.cancel();
                                }
                            });

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }else {
                showDailog();
            }

        }
    }
    private void vaiMobile(){
        if(mobile.getText().length()<10){
            mobile.setError("Enter Valid Mobile");
            mobile.requestFocus();
        }else {
            if (isNetworkAvailable(getApplicationContext())){
                final SweetAlertDialog pDialog = new SweetAlertDialog(ForgotPassword.this, SweetAlertDialog.PROGRESS_TYPE);
                pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                pDialog.setTitleText("Loading");
                pDialog.setCancelable(true);
                pDialog.show();
                OkHttpClient httpClient = new OkHttpClient.Builder()
                        .connectTimeout(10, TimeUnit.SECONDS)
                        .writeTimeout(10, TimeUnit.SECONDS)
                        .readTimeout(30, TimeUnit.SECONDS)
                        .retryOnConnectionFailure(true)
                        .build();
                RequestBody params = new FormBody.Builder().add("mobile",mobile.getText().toString()).build();
                Request request = new Request.Builder().url(Sources.Forgot).post(params).build();
                final Call call = httpClient.newCall(request);
                pDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        call.cancel();
                    }
                });

                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        Log.e("error",e.getMessage());
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                pDialog.changeAlertType(SweetAlertDialog.ERROR_TYPE);
                                pDialog.setTitleText("Connection Time Out");
                                pDialog.setContentText("Retry");
                                pDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                                        vaiMobile();
                                        pDialog.dismiss();
                                    }
                                });
                            }
                        });
                    }
                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        Log.e("res",response.body().string());
                /*try {
                    JSONObject jsonObject = new JSONObject(response.body().string());
                    if (jsonObject.getString("status").matches("1")) {
                        pDialog.cancel();
                        Toast.makeText(getApplicationContext(), "An email with a confirmation link has been sent your email address", Toast.LENGTH_LONG).show();

                    }else if (jsonObject.getString("status").matches("0")){
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                pDialog.cancel();
                                Toast.makeText(getApplicationContext(), "Invalid  Details", Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            pDialog.cancel();
                        }
                    });

                } catch (JSONException e) {
                    e.printStackTrace();
                }*/
                    }
                });
            }else {
                showDailog();
            }
        }
    }
    public boolean emailValidator(String email) {
        Pattern pattern;
        Matcher matcher;
        final String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(email);
        return matcher.matches();
    }
    public static boolean isNetworkAvailable(Context context) {
        try {
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = cm.getActiveNetworkInfo();
            return (networkInfo != null && networkInfo.isConnected());
        } catch (Exception ex) {
            return false;
        }
    }
    private void showDailog(){
        final SweetAlertDialog dialog = new SweetAlertDialog(ForgotPassword.this,SweetAlertDialog.ERROR_TYPE);
        dialog.setTitleText("No Internet Connection");
        dialog.setConfirmText("Okay");
        dialog.setContentText("Please Connect To The Internet And Try Again");
        dialog.show();
    }

}
