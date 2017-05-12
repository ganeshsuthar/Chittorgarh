package com.m.chittorgarh.View.Activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.m.chittorgarh.Halper.FormattedTextView;
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

public class Login extends AppCompatActivity {
    FormattedTextView textView,forgotPassword;
    Button login_btn;
    EditText email,password;
    private SharedPreferences pref;
    SweetAlertDialog pDialog;
    CheckBox savePasswrod;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setTitle("Login");
        pref = getSharedPreferences("myPref", Context.MODE_PRIVATE);
        textView = (FormattedTextView)findViewById(R.id.reg);
        forgotPassword = (FormattedTextView)findViewById(R.id.forgotPassword);
        login_btn = (Button) findViewById(R.id.login_btn);
        email = (EditText)findViewById(R.id.email);
        password = (EditText)findViewById(R.id.password);
        savePasswrod = (CheckBox)findViewById(R.id.savePass);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login.this,Registration.class));
            }
        });

        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (email.getText().toString().matches("")){
                    email.setError("Enter Email");
                    email.requestFocus();
                }else if (!emailValidator(email.getText().toString())){
                    email.setError("Invalid Email");
                    email.requestFocus();
                }else if (password.getText().toString().matches("")){
                        password.setError("Enter Password");
                        password.requestFocus();
                }else if (password.getText().toString().length()<6){
                    password.setError("Enter Valid Password");
                    password.requestFocus();
                }else {
                    if (isNetworkAvailable(getApplicationContext())) {
                        login();
                    }else {
                        showDailog();
                    }
                }
            }
        });

        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login.this,ForgotPassword.class));
            }
        });

    }


    private void login(){
        pDialog = new SweetAlertDialog(Login.this, SweetAlertDialog.PROGRESS_TYPE);
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

        RequestBody params = new FormBody.Builder().add("email",email.getText().toString()).add("password",password.getText().toString()).build();
        Request request = new Request.Builder().url(Sources.Login).post(params).build();
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
                                login();
                                pDialog.dismiss();
                            }
                        });
                    }
                });
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                SharedPreferences.Editor editor = pref.edit();
                try {
                    JSONObject jsonObject = new JSONObject(response.body().string());
                    if (jsonObject.getString("status").matches("1")) {
                        JSONObject object = jsonObject.getJSONObject("user");
                        editor.putString("customer_id",object.getString("customer_id"));
                        editor.putString("firstname",object.getString("firstname"));
                        editor.putString("lastname",object.getString("lastname"));
                        editor.putString("email",object.getString("email"));
                        editor.putString("telephone",object.getString("telephone"));
                        editor.putString("address_id",object.getString("address_id"));
                        editor.putString("status",object.getString("status"));
                        editor.putString("approved",object.getString("approved"));
                        editor.putString("date_added",object.getString("date_added"));
                        if (savePasswrod.isChecked()){
                            editor.putBoolean("userStatus", true);
                        }
                        editor.apply();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                pDialog.cancel();
                                startActivity(new Intent(getApplicationContext(), Home.class));
                                finish();
                            }
                        });

                    }else if (jsonObject.getString("status").matches("0")){
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                pDialog.cancel();
                                Toast.makeText(getApplicationContext(), "Invalid Login Details", Toast.LENGTH_LONG).show();
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
        final SweetAlertDialog dialog = new SweetAlertDialog(Login.this,SweetAlertDialog.ERROR_TYPE);
        dialog.setTitleText("No Internet Connection");
        dialog.setConfirmText("Okay");
        dialog.setContentText("Please Connect To The Internet And Try Again");
        dialog.show();
    }


}
