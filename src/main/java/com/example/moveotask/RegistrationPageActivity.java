package com.example.moveotask;


import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

/**
 * This class defines registration page
 */
public class RegistrationPageActivity extends AppCompatActivity {

    EditText firstNameEditText;
    EditText lastNameEditText;
    EditText emailEditText;
    EditText passwordEditText;

    FirebaseAuth firebaseAuthentication;
    FirebaseDatabase database;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_page);

        database = FirebaseDatabase.getInstance("https://moveotask-c5bd6-default-rtdb.firebaseio.com/");
        firebaseAuthentication = FirebaseAuth.getInstance();

        firstNameEditText = findViewById(R.id.first_name_editText);
        lastNameEditText = findViewById(R.id.last_name_editText);
        emailEditText = findViewById(R.id.email_editText);
        passwordEditText = findViewById(R.id.password_editText);
    }

    /**
     * Retrive the data the user entered
     * @param view android studio class object
     */
    public void registerUser(View view) {

        String firstNameField = firstNameEditText.getText().toString();
        String lastNameField = lastNameEditText.getText().toString();
        String emailField = emailEditText.getText().toString();
        String passwordField = passwordEditText.getText().toString();
        authenticate(firstNameField, lastNameField, emailField, passwordField);
    }

    /**
     * check the data the user entered are proper
     * @param firstNameField forst name of the user
     * @param lasttNameField last name of the user
     * @param emailField email of the user
     * @param passwordField password of the user
     */
    public void authenticate(String firstNameField, String lasttNameField,
                             String emailField, String passwordField) {
        int flag = 0;
        if (firstNameField.isEmpty()) {
            firstNameEditText.setError("Full name is required!");
            firstNameEditText.requestFocus();
            flag = 1;
        }

        if (lasttNameField.isEmpty()) {
            lastNameEditText.setError("Last name is required");
            lastNameEditText.requestFocus();
            flag = 1;
        }

        if (emailField.isEmpty()) {
            emailEditText.setError("Email is required!");
            emailEditText.requestFocus();
            flag = 1;
        }

        if (passwordField.isEmpty()) {
            passwordEditText.setError("Password is required!");
            passwordEditText.requestFocus();
            flag = 1;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(emailField).matches()) {
            emailEditText.setError("You have entered an invalid email");
            emailEditText.requestFocus();
            flag = 1;
        }

        if (passwordField.length() < 6) {
            passwordEditText.setError("Password must be at least 6 characters");
            passwordEditText.requestFocus();
            flag = 1;
        }

        if(flag != 1)
        {
            firebaseAuthentication.createUserWithEmailAndPassword(emailField, passwordField)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            user = new User(firstNameField, lasttNameField, emailField,
                                    passwordField);
                            addUserToDatabase(user);
                        } else {
                            Toast.makeText(RegistrationPageActivity.this,
                                    "this email is already being used by another user",
                                    Toast.LENGTH_LONG).show();
                        }
                    });
        }
    }


    /**
     * Add the user to our database
     * @param user authenticated registration data of the user
     */
    public void addUserToDatabase(User user) {

        database.getReference("Users")
                .child(firebaseAuthentication.getCurrentUser()
                        .getUid()).setValue(user)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(RegistrationPageActivity.this,
                                "User registered successfully!",
                                Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(RegistrationPageActivity.this, MainActivity.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(RegistrationPageActivity.this,
                                "Problem with registering the user, please try again!",
                                Toast.LENGTH_LONG).show();
                    }
                });
    }
}