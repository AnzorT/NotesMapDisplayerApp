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
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DataSnapshot;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;

/**
 * <p>
 *     Author: Anzor Torikashvili.
 *     <br>
 *     This class describes BottomNavigationActivity activity.
 * </p>
 */
public class BottomNavigationActivity extends AppCompatActivity {

    ActivityBottomNavigationBinding binding;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        changeView(new NotesListFragment());
        binding = ActivityBottomNavigationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
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

    /**
     * Changes the window (fragment) that will be displayed in the layout of the activity.
     * @param fragment the window we want to present to the user.
     */
    public void changeView(Fragment fragment){

        Bundle data = new Bundle();
        fragment.setArguments(data);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.layout_frame, fragment);
        fragmentTransaction.commit();
    }

    /**
     *<p>
     *     onClick method of floating_action_button.
     *     <br>
     *     Redirects us to NotesManagmentActivity to create a new note.
     *</p>
     * @param view basic building block for user interface.
     */
    public void createNewNote(View view) {

        Intent intent = new Intent(BottomNavigationActivity.this, NotesManagementActivity.class);
        intent.putExtra("Email",getIntent().getStringExtra("Email"));
        intent.putExtra("Edit","0");
        startActivity(intent);
    }

    /**
     *<p>
     *     onClick method of logout_floating_action_button.
     *     <br>
     *     Redirects us back to login page (MainActivity).
     *</p>
     * @param view view basic building block for user interface.
     */
    public void logOut(View view) {

        sharedPreferences = getSharedPreferences("automatic login", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("remember", "false");
        editor.apply();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    /**
     * Retrieve all the notes of the user.
     * @param snapshot the id of the user in firebase that we want to get his notes.
     * @return list of all the notes that belong to the user.
     */
    public ArrayList<Note>  getDataFromDataBase(DataSnapshot snapshot){

        ArrayList<Note> listOfNotes = new ArrayList<>();
        for (DataSnapshot postSnapShot : snapshot.getChildren()) {

            Object object = postSnapShot.getValue();
            Gson gson = new Gson();
            JsonObject json = gson.toJsonTree(object).getAsJsonObject();
            String title = json.get("title").getAsString();
            String content = json.get("content").getAsString();
            String noteId = json.get("noteId").getAsString();
            String userEmail = json.get("userEmail").getAsString();
            JsonObject position = json.get("position").getAsJsonObject();
            double latitude = position.get("latitude").getAsDouble();
            double longitude = position.get("longitude").getAsDouble();
            ZonedDateTime dateTime = getDate(json);
            Note note = new Note(noteId, title, content, userEmail, new LatLng(latitude, longitude), dateTime);
            listOfNotes.add(note);
        }
        return listOfNotes;
    }

    /**
     * Arranges the date in ZonedDateTime format.
     * @param json contains the data on date.
     * @return ZonedDateTime object which contains the date of note was created.
     */
    private ZonedDateTime getDate(JsonObject json) {

        JsonObject jsonDate = json.get("date").getAsJsonObject();
        int year = jsonDate.get("year").getAsInt();
        int month = jsonDate.get("monthValue").getAsInt();
        int day = jsonDate.get("dayOfMonth").getAsInt();
        int hour = jsonDate.get("hour").getAsInt();
        int minute = jsonDate.get("minute").getAsInt();
        int second = jsonDate.get("second").getAsInt();
        int nano = jsonDate.get("nano").getAsInt();
        ZonedDateTime dateTime = ZonedDateTime.of(year, month, day, hour, minute, second, nano, ZoneId.of("Israel"));
        return dateTime;
    }
}