package android.example.speelenlees.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.example.speelenlees.R;
import android.example.speelenlees.activities.ui.homepages.HomeActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

//LOGIN
public class LogInActivity extends AppCompatActivity implements View.OnClickListener {
    static final String TAG = "LogInActivity";
    EditText et_email;
    EditText et_passwd;
    String email;
    String password;

    Button btn_login;
    CheckBox cb_remember_me;
    //TextView tv_no_account;

    SharedPreferences preferences;
    SharedPreferences.Editor editor; //om shared preferences aan te kunnen passen

    FirebaseAuth firebaseAuth;
    FirebaseAuth.AuthStateListener authStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in); //login layout koppelen
        //setTitle("Inloggen");

        initialize();

        btn_login.setOnClickListener(this);
        //tv_no_account.setOnClickListener(this);
    }

    private void initialize() {
        Log.i(TAG, "Initialized -> succeed");
        et_email = findViewById(R.id.et_email);
        et_passwd = findViewById(R.id.et_password);
        btn_login = findViewById(R.id.btn_login);
        //tv_no_account = findViewById(R.id.tv_no_account);
        cb_remember_me = findViewById(R.id.cb_remember_me);

        firebaseAuth = FirebaseAuth.getInstance();

        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
            }
        };

        preferences = getSharedPreferences("myApp", Context.MODE_PRIVATE);
        editor = preferences.edit();

        checkPreferences(); //kijken of credentials zijn opgeslagen
    }

    public void onClick(View v) {
        if (v.getId() == R.id.btn_login) {
            loginUser();
        }
        /*else if (v.getId() == R.id.tv_no_account) {
            redirectToSignUp();
        }*/
    }

    private void loginUser() {
        email = et_email.getText().toString();
        password = et_passwd.getText().toString();


        if (email.isEmpty()) {
            et_email.setError("Geef een email adres in.");
            et_email.requestFocus(); //kunnen op tab duwen
            Log.e(TAG, "Email field not filled in correctly");
        }  else if (password.isEmpty()) {
            et_passwd.setError("Voer een wachtwoord in");
            et_passwd.requestFocus();
            Log.e(TAG, "Password field not filled in correctly");
        } else  if (email.isEmpty() && password.isEmpty()) {
            Toast.makeText(this, "Zowel email als wachtwoord dienen ingevuld te zijn", Toast.LENGTH_LONG).show();
            Log.e(TAG, "Fields not filled in correctly");
        }
        else {
            // als alles is ingevuld
            //savePreferences(email, password);

            if (cb_remember_me.isChecked()) {
                editor.putString("android.example.speelenlees.checkbox", "True");
                editor.commit();
                editor.putString("android.example.speelenlees.email", email);
                editor.commit();
                editor.putString("android.example.speelenlees.password", password);
                editor.commit();
            } else {
                editor.putString("android.example.speelenlees.checkbox", "False");
                editor.commit();
                editor.putString("android.example.speelenlees.email", "");
                editor.commit();
                editor.putString("android.example.speelenlees.password", "");
                editor.commit();
            }

            login();
        }
    }


    /*private void initializeAuthStateListener() {
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
            }
        };
    }*/

    private void checkPreferences() {
        String  checkbox = preferences.getString("android.example.speelenlees.checkbox", "False");
        String email = preferences.getString("android.example.speelenlees.email", "");
        String password = preferences.getString("android.example.speelenlees.password", "");

        if (checkbox.equals("True")) {
            cb_remember_me.setChecked(true);
        } else {
            cb_remember_me.setChecked(false);
        }

        et_email.setText(email);
        et_passwd.setText(password);


    }

   /* private void savePreferences(String email, String password) {
        if (cb_remember_me.isChecked()) {
            editor.putString("android.example.speelenlees.checkbox", "True");
            editor.commit();
            editor.putString("android.example.speelenlees.email", email);
            editor.commit();
            editor.putString("android.example.speelenlees.password", password);
            editor.commit();
        } else {
            editor.putString("android.example.speelenlees.checkbox", "False");
            editor.commit();
            editor.putString("android.example.speelenlees.email", "");
            editor.commit();
            editor.putString("android.example.speelenlees.password", "");
            editor.commit();
        }

        Log.i(TAG, "Saved shared preferences");
    }*/

    private void login() {
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(LogInActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()) {
                    Intent intent = new Intent(LogInActivity.this, HomeActivity.class);
                    startActivity(intent);
                    Log.e(TAG, "Logged in successfull");
                } else {
                    Toast.makeText(LogInActivity.this, "Gebruikersnaam en/of wachtwoord incorrect. Probeer opnieuw", Toast.LENGTH_LONG).show();
                    Log.e(TAG, "Login not successfull");
                }

                /*if (!task.isSuccessful()) {
                    Toast.makeText(LogInActivity.this, "Gebruikersnaam en/of wachtwoord incorrect. Probeer opnieuw", Toast.LENGTH_LONG).show();

                    Log.e(TAG, "Something went wrong");
                }
                else {
                    Log.i(TAG, "Logged in successfully");
                    Intent intent = new Intent(LogInActivity.this, HomeActivity.class);
                    startActivity(intent);
                }*/
            }
        });
    }

    /*private void redirectToSignUp() {
        Intent intent = new Intent(LogInActivity.this, SignUpActivity.class);
        startActivity(intent);
    }*/

    /*@Override
    protected void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(authStateListener);
    }*/

}
