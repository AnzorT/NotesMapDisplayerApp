package com.example.moveotask;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.moveotask.databinding.ActivityBottomNavigationBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

public class BottomNavigationActivity extends AppCompatActivity {


    ActivityBottomNavigationBinding binding;
    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBottomNavigationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        changeView(new NotesListFragment());
        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()){
                case R.id.notesListActivity:
                    changeView(new NotesListFragment());
                    break;
                case R.id.mapsAndNotesActivity:
                    changeView(new MapAndNotesFragment());
                    break;
            }
            return true;

        });
    }

    public void changeView(Fragment fragment){
        Bundle data = new Bundle();
        //data.putParcelableArrayList("Notes", userNotes);
        fragment.setArguments(data);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.layout_frame, fragment);
        fragmentTransaction.commit();
    }

    public void manageNotes(View view) {
        Intent intent = new Intent(BottomNavigationActivity.this, NotesManagmentActivity.class);
        intent.putExtra("Email",getIntent().getStringExtra("Email"));
        intent.putExtra("Edit","0");
        startActivity(intent);
    }

    public void logOut(View view) {
        sharedPreferences = getSharedPreferences("automatic login", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("remember", "false");
        editor.apply();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}