package com.example.moveotask;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Patterns;
import android.view.Gravity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

/**
 * author: Anzor Torikashvili
 * This class defines Login page activity
 */
public class MainActivity extends AppCompatActivity {

    //all the objects available for the user
    TextView emailEditText, passwordEditText;
    CheckBox remmemberMeCheckBox;
    FirebaseAuth firebaseAuthentication;
    FirebaseDatabase database;
    SharedPreferences sharedPreferences;

    //allows us to redirect from activity to activity
    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //check if user is already logged in
        sharedPreferences = getSharedPreferences("automatic login", MODE_PRIVATE);
        if(sharedPreferences.getString("remember","").equals("true")){
            redirectToAppPage();
        }

        //match the variables to the objects on screen
        emailEditText = findViewById(R.id.email_editText);
        passwordEditText = findViewById(R.id.password_editText);
        remmemberMeCheckBox = findViewById(R.id.remember_me_checkBox);
        database = FirebaseDatabase.getInstance();
        firebaseAuthentication = FirebaseAuth.getInstance();
    }

    /**
     * this method redirects us to the application page
     */
    private void redirectToAppPage() {

        intent = new Intent(MainActivity.this, BottomNavigationActivity.class);
        startActivity(intent);
    }

    /**
     * Redirects us to registration page
     * @param view drawing content on to the screen
     */
    public void redirectToRegistrationPage(View view) {

        intent = new Intent(MainActivity.this, RegistrationPageActivity.class);
        startActivity(intent);

    }

    /**
     * Checks if we entered a valid username and password (onClick method)
     * @param view drawing content on to the screen
     */
    public void LoginToTheApp(View view) {
        authenticateEmailAndPassword(emailEditText.getText().toString(),
                passwordEditText.getText().toString());
    }

    /**
     * validating email and password the user entered
     * @param emailText email the user entered
     * @param passwordText password the user entered
     */
    private void authenticateEmailAndPassword(String emailText, String passwordText) {

        int flag = 0;
        if (emailText.isEmpty()) {
            emailEditText.setError("You must fill up the email field");
            emailEditText.requestFocus();
            flag = 1;
        }
        
        if (passwordText.isEmpty()) {
            emailEditText.setError("You must fill up the password field");
            emailEditText.requestFocus();
            flag = 1;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(emailText).matches()) {
            emailEditText.setError("You have entered an invalid email");
            emailEditText.requestFocus();
            flag = 1;
        }

        if (passwordText.length() < 6) {
            passwordEditText.setError("Password must be at least 6 characters");
            passwordEditText.requestFocus();
            flag = 1;
        }

        if (flag == 0) {

            firebaseAuthentication.signInWithEmailAndPassword(emailText, passwordText).
                    addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(MainActivity.this, "login successfully\n" +
                                    " Hello " + emailEditText.getText().toString() + " welcome " +
                                            "to the App"
                                    , Toast.LENGTH_LONG).show();
                            redirectToAppPage();
                        } else {
                            Toast.makeText(MainActivity.this, "One of the fields " +
                                    "are incorrect, please try again", Toast.LENGTH_LONG).show();
                        }
                    });
        }
    }

    /**
     * remembers user, not remember user login 
     * @param view
     */
    public void createAutomaticLogin(View view) {

        if(remmemberMeCheckBox.isChecked()){
            sharedPreferences = getSharedPreferences("automatic login", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("remember", "true");
            editor.apply();
        }
        else{
            sharedPreferences = getSharedPreferences("automatic login", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("remember", "false");
            editor.apply();
        }
    }
}