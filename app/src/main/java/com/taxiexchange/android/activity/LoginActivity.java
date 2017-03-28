package com.taxiexchange.android.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.taxiexchange.android.R;
import com.taxiexchange.android.config.Apps;
import com.taxiexchange.android.config.PreferenceManager;
import com.taxiexchange.android.config.retrofit.RestClient;
import com.taxiexchange.android.config.retrofit.TaxiExchangeApi;
import com.taxiexchange.android.model.request.LoginRequest;
import com.taxiexchange.android.model.response.DriverInfoResponse;
import com.taxiexchange.android.model.response.LoginResponse;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by hieu.nguyennam on 3/15/2017.
 */

public class LoginActivity extends AppCompatActivity {

    private final String TAG = getClass().getSimpleName();
    private static final int REQUEST_SIGNUP = 0;

    @BindView(R.id.input_mobile) EditText _mobileText;
    @BindView(R.id.input_password) EditText _passwordText;
    @BindView(R.id.btn_login) Button _loginButton;
    @BindView(R.id.link_signup) TextView _signupLink;
    private TaxiExchangeApi mApi;
    boolean loginValidated;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        initAPI();
        _loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                login();
            }
        });

        _signupLink.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Start the Signup activity
                Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
                startActivityForResult(intent, REQUEST_SIGNUP);
                finish();
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });
    }

    private void initAPI() {
        mApi = RestClient.getClient().create(TaxiExchangeApi.class);
    }


    public void login() {
        Log.d(TAG, "Login");
        getAccount();
//        if (!validate()) {
//            onLoginFailed();
//            return;
//        }
        _loginButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.show();

        // TODO: Implement your own authentication logic here.
        PreferenceManager.writePreference(Apps.IS_SIGN, true);
        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        // On complete call either onLoginSuccess or onLoginFailed
                        if(!loginValidated)
                            onLoginFailed();
                        else
                            onLoginSuccess();

                        progressDialog.dismiss();
                    }
                }, 3000);

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SIGNUP) {
            if (resultCode == RESULT_OK) {

                // TODO: Implement successful signup logic here
                // By default we just finish the Activity and log them in automatically
                this.finish();
            }
        }
    }

    @Override
    public void onBackPressed() {
        // Disable going back to the MainActivity
        moveTaskToBack(true);
    }

    public void onLoginSuccess() {
        _loginButton.setEnabled(true);
        initFirebase(PreferenceManager.getString(Apps.USER_MOBILE));
        finish();
    }

    public void onLoginFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();

        _loginButton.setEnabled(true);
    }

    private void getAccount(){
        String mobile = _mobileText.getText().toString();
        PreferenceManager.writePreference(Apps.USER_MOBILE, mobile);
        String password = _passwordText.getText().toString();
        String regId = FirebaseInstanceId.getInstance().getToken();
        LoginRequest loginRequest = new LoginRequest(mobile, password, regId);
        Call<LoginResponse> postLoginCall = mApi.login(loginRequest);
        postLoginCall.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                Log.d(TAG, "Login Success body = " + response.body());
                if (response.body() == null) {
                    Log.d(TAG, "Login Success Stt = " + response.isSuccessful());
                    loginValidated = false;
                    return;
                } else {
                    Log.d(TAG, "Login Success Stt = " + response.isSuccessful());
                    loginValidated = true;
                    LoginResponse postLogin = response.body();
                    PreferenceManager.writePreference(Apps.LOGIN_TOKEN, postLogin.getToken());
                    Log.d(TAG, "Login Success = " + postLogin.getMessage());
                    Log.d(TAG, "Login Success = " + postLogin.getToken());
                    Log.d(TAG, "Login Success = " + postLogin.getData().getMobile());
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Log.d(TAG, "Login Fails" + t.toString());
            }
        });

    }
    private void initFirebase(String usesMobile){
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://taxiexchange.firebaseio.com/");
        database.getReference().child(usesMobile).child("info").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() == null) {
                    Log.e(TAG, "Null");
                    return;
                }
                for(DataSnapshot driverInfo : dataSnapshot.getChildren()){
                    String name = (String) driverInfo.child("name").getValue();
                    String mobile = (String) driverInfo.child("mobile").getValue();
                    String car = (String) driverInfo.child("car").getValue();
                    double amount = (double) driverInfo.child("amount").getValue();
                    DriverInfoResponse driverInfoResponse = new DriverInfoResponse(amount, car, mobile, name);
                    Log.e(TAG, dataSnapshot.getValue().toString());
                    Log.e(TAG, driverInfoResponse.toString());
                    PreferenceManager.writePreference(Apps.USER_INFO_NAME, name);
                    PreferenceManager.writePreference(Apps.USER_INFO_MOBILE, mobile);
                    PreferenceManager.writePreference(Apps.USER_INFO_CAR, car);
                    PreferenceManager.writePreference(Apps.USER_INFO_AMOUNT, String.format("%,d", (int)(amount/1000)));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public boolean validate() {
        boolean valid = true;

        String mobile = _mobileText.getText().toString();
        String password = _passwordText.getText().toString();

//        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
//            _emailText.setError("enter a valid email address");
//            valid = false;
//        } else {
//            _emailText.setError(null);
//        }

        if (mobile.isEmpty() || mobile.length() < 10 || mobile.length() > 11) {
            _mobileText.setError("enter a valid mobile");
            valid = false;
        } else {
            _mobileText.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            _passwordText.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        return valid;
    }
}
