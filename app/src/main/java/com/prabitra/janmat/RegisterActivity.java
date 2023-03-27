package com.prabitra.janmat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.concurrent.TimeUnit;

public class RegisterActivity extends AppCompatActivity {
    TextView getotpbtn,registerLoginTxt,loginpage,alreadyhaveanaccount;
    EditText username,phoneno,otp;
    String otpnumber;
    String phonenumber,name="";
    String  verificationId="";
    FirebaseAuth firebaseAuth;
    UserDetails userDetails;
    boolean movedToVerify = true;
    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        getotpbtn=findViewById(R.id.getotp);
        registerLoginTxt=findViewById(R.id.Register_login_txt);
        username=findViewById(R.id.Username);
        phoneno=findViewById(R.id.UserPhoneNo);
        otp=findViewById(R.id.otp);
        loginpage=findViewById(R.id.loginpage);
        alreadyhaveanaccount=findViewById(R.id.alreadyhaveanaccount);
        firebaseAuth=FirebaseAuth.getInstance();


        pd=new ProgressDialog(RegisterActivity.this);
        pd.setTitle("Please wait");
        pd.setCancelable(false);
        pd.setCanceledOnTouchOutside(false);
        pd.setMessage("Verifying Mobile number");

        if(firebaseAuth.getCurrentUser()!=null){
            Intent ihome=new Intent(RegisterActivity.this,HomeActivity.class);
            startActivity(ihome);
            finish();
        }

        loginpage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(username.getVisibility()==View.VISIBLE) {
                    registerLoginTxt.setText("Login");
                    username.setVisibility(View.GONE);
                    loginpage.setText("Register Here");
                    alreadyhaveanaccount.setText("New User?");
                }
                else{
                    registerLoginTxt.setText("Register");
                    username.setVisibility(View.VISIBLE);
                    loginpage.setText("Login here");
                    alreadyhaveanaccount.setText("Already have an account?");
                }
            }
        });


      getotpbtn.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
                  if (otp.getVisibility() == View.VISIBLE) {
                      otpnumber = otp.getText().toString();
                      verifyotp(otpnumber);
                      pd.setTitle("Verifying OTP");
                      pd.setMessage("Please wait");
                      pd.show();

                  } else {
                      getotp();
                  }
          }
      });
    }

    private void verifyotp(String otpnumber) {
        if (otpnumber != null) {
            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, otpnumber);
            signInWithPhoneAuthCredential(credential);
        }
    }
    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            if(username.getVisibility()== View.VISIBLE) {
                                Toast.makeText(RegisterActivity.this, "Registeration Sucessful", Toast.LENGTH_SHORT).show();
                                SetDataToFirebase();
                            }
                            else{
                                Toast.makeText(RegisterActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                                pd.dismiss();
                                Intent ihome=new Intent(RegisterActivity.this,HomeActivity.class);
                                startActivity(ihome);
                                finish();
                            }
                        } else {
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                Toast.makeText(RegisterActivity.this, "Invalid OTP", Toast.LENGTH_SHORT).show();
                                pd.dismiss();
                            }
                        }
                    }
                });
    }
    private void SetDataToFirebase() {
        userDetails=new UserDetails(username.getText().toString(),phonenumber);
        FirebaseFirestore.getInstance().document("UserDetails"+"/"+phonenumber ).set(userDetails).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(RegisterActivity.this, "intent", Toast.LENGTH_SHORT).show();
                pd.dismiss();
                Intent ihome=new Intent(RegisterActivity.this,HomeActivity.class);
                startActivity(ihome);
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(RegisterActivity.this, "setting data to firebase to failed", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void getotp() {
        phonenumber=phoneno.getText().toString();
        name=username.getText().toString();
        if(username.getVisibility()==View.VISIBLE) {
            if (name.isEmpty()) {
                Toast.makeText(getApplicationContext(), "Please enter your name", Toast.LENGTH_SHORT).show();
                return;
            }
        }
        if (phonenumber.length() != 10) {
            Toast.makeText(getApplicationContext(), "Please enter 10 digit mobile number", Toast.LENGTH_SHORT).show();
            return;
        }
        String phonePattern = "^[6789]\\d{9}$";
        if (!phonenumber.matches(phonePattern)) {
            Toast.makeText(getApplicationContext(), "Please enter a valid number", Toast.LENGTH_SHORT).show();
            return;
        }

        phonenumber = "+91" + phonenumber;
        String finalPhoneNum = phonenumber;
        pd.show();
    FirebaseFirestore.getInstance().document("UserDetails" + "/" + phonenumber).get()
            .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    if (documentSnapshot != null && documentSnapshot.exists()) {
                        // User already registerd...
                        if(username.getVisibility()==View.VISIBLE) {
                            Toast.makeText(RegisterActivity.this, "Number already exist", Toast.LENGTH_SHORT).show();
                            pd.dismiss();
                        }
                        else{
                            otp.setEnabled(true);
                            firebaseAuth = FirebaseAuth.getInstance();
                            PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;

                            mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                                @Override
                                public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                    super.onCodeSent(s, forceResendingToken);
                                    pd.dismiss();
                                    otp.setEnabled(true);
                                    getotpbtn.setEnabled(true);
                                    otp.setVisibility(View.VISIBLE);
                                    getotpbtn.setText("Verify OTP");
                                    verificationId = s;
                                    requestSmsPermission();
                                    new OtpReceiver().setOtpText(otp);
                                    if (movedToVerify && otp.getText().toString().length() == 6) {
                                        verifyotp(otp.getText().toString());
                                        movedToVerify = !movedToVerify;
                                    }
                                }

                                @Override
                                public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                                    final String code = phoneAuthCredential.getSmsCode();
                                    if (code != null) {
                                        otp.setText(code);
                                        if (movedToVerify && otp.getText().toString().length() == 6) {
                                            verifyotp(otp.getText().toString());
                                            movedToVerify = !movedToVerify;
                                        }
                                    } else {
                                        pd.dismiss();
                                    }
                                }

                                @Override
                                public void onVerificationFailed(@NonNull FirebaseException e) {
                                    Log.d("otp", "onVerificationFailed:" + e.getMessage());
                                    Toast.makeText(RegisterActivity.this, "Registration Failed :" + e.getMessage(), Toast.LENGTH_LONG).show();
                                    otp.setEnabled(true);
                                    pd.dismiss();
                                }
                            };

                            PhoneAuthOptions options =
                                    PhoneAuthOptions.newBuilder(firebaseAuth)
                                            .setPhoneNumber(finalPhoneNum)       // Phone number to verify
                                            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                                            .setActivity(RegisterActivity.this)                 // Activity (for callback binding)
                                            .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                                            .build();
                            PhoneAuthProvider.verifyPhoneNumber(options);
                        }
                    } else {
                        if (username.getVisibility() != View.VISIBLE) {
                            Toast.makeText(RegisterActivity.this, "Phone No. Not Registered", Toast.LENGTH_SHORT).show();
                        } else {
                            otp.setEnabled(true);
                            firebaseAuth = FirebaseAuth.getInstance();
                            PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;

                            mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                                @Override
                                public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                    super.onCodeSent(s, forceResendingToken);
                                    pd.dismiss();
                                    otp.setEnabled(true);
                                    getotpbtn.setEnabled(true);
                                    otp.setVisibility(View.VISIBLE);
                                    getotpbtn.setText("Verify OTP");
                                    verificationId = s;
                                    requestSmsPermission();
                                    new OtpReceiver().setOtpText(otp);
                                    if (movedToVerify && otp.getText().toString().length() == 6) {
                                        verifyotp(otp.getText().toString());
                                        movedToVerify = !movedToVerify;
                                    }
                                }

                                @Override
                                public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                                    final String code = phoneAuthCredential.getSmsCode();
                                    if (code != null) {
                                        otp.setText(code);
                                        if (movedToVerify && otp.getText().toString().length() == 6) {
                                            verifyotp(otp.getText().toString());
                                            movedToVerify = !movedToVerify;
                                        }
                                    } else {
                                        pd.dismiss();
                                    }
                                }

                                @Override
                                public void onVerificationFailed(@NonNull FirebaseException e) {
                                    Log.d("otp", "onVerificationFailed:" + e.getMessage());
                                    Toast.makeText(RegisterActivity.this, "Registration Failed :" + e.getMessage(), Toast.LENGTH_LONG).show();
                                    otp.setEnabled(true);
                                    pd.dismiss();
                                }
                            };

                            PhoneAuthOptions options =
                                    PhoneAuthOptions.newBuilder(firebaseAuth)
                                            .setPhoneNumber(finalPhoneNum)       // Phone number to verify
                                            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                                            .setActivity(RegisterActivity.this)                 // Activity (for callback binding)
                                            .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                                            .build();
                            PhoneAuthProvider.verifyPhoneNumber(options);


                        }
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(RegisterActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    pd.dismiss();
                }
            });

    }
    private void requestSmsPermission() {
        String permission = Manifest.permission.RECEIVE_SMS;
        int grant = ContextCompat.checkSelfPermission(RegisterActivity.this, permission);
        if (grant != PackageManager.PERMISSION_GRANTED) {
            String[] permissionList = new String[1];
            permissionList[0] = permission;
            ActivityCompat.requestPermissions(RegisterActivity.this, permissionList, 1);
        }
    }


}