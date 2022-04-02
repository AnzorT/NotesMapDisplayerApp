package com.example.moveotask;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * <p>
 *     Author: Anzor Torikashvili.
 *     <br>
 *     This class describes NotesManagementActivity activity.
 * </p>
 */
public class NotesManagementActivity extends AppCompatActivity {

    //all the objects available for the user
    DatePicker noteCreationDate;
    Button addNoteBtn, deleteNoteBtn;
    EditText noteTitleEditText, noteContentEditText;

    int noteId;
    double noteLocation;
    String email, editOrCreateNewNote;
    FirebaseAuth firebaseAuthentication;
    DatabaseReference databaseReference;
    LatLng currentLocation;

    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;
    private Boolean mLocationPermissionsGranted = false;


    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes_managment);

        //connect gadgets to variables
        noteContentEditText = findViewById(R.id.noteTextContent);
        noteTitleEditText = findViewById(R.id.noteTitle);
        noteCreationDate = findViewById(R.id.datePicker);
        deleteNoteBtn = findViewById(R.id.delete_or_discard_note);
        addNoteBtn = findViewById(R.id.edit_or_add_note);

        firebaseAuthentication = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance()
                .getReference("Users").child(Objects.
                        requireNonNull(firebaseAuthentication.getCurrentUser()).getUid());
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                email = Objects.requireNonNull(snapshot.child("email").getValue()).toString();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                //in case of failure
            }
        });

        noteLocation = getSharedPreferenceValue("noteLocation", "note_location");
        noteId = (int) getSharedPreferenceValue("noteID", "note_key");
        setNotesManagementPage();
    }


    /**
     * Set the value of the desirable SharedPreference.
     * @param sharedPreferencesName name of the sharedPreference we want to use.
     * @param sharedPreferencesVariable name of the variable we want to change it's value.
     */
    public void setSharedPreferenceValue(String sharedPreferencesName, String sharedPreferencesVariable, double valueToStore){

        SharedPreferences sp = getSharedPreferences(sharedPreferencesName, MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(sharedPreferencesVariable, Double.toString(valueToStore));
        editor.apply();
    }

    /**
     * Returns the value of the desirable SharedPreference.
     * @param sharedPreferencesName name of the sharedPreference we want to use.
     * @param sharedPreferencesVariable name of the variable we want to change.
     * @return value of the desirable SharedPreference.
     */
    public double getSharedPreferenceValue(String sharedPreferencesName, String sharedPreferencesVariable){

        SharedPreferences sharedPreferences = getSharedPreferences(sharedPreferencesName, MODE_PRIVATE);
        String sharedPreferencesValue = sharedPreferences.getString(sharedPreferencesVariable, "0");
        return Double.parseDouble(sharedPreferencesValue);
    }


    /**
     * <p>
     *     onClick listener of edit.
     *     <br>
     *     Check if user wants to edit existing note or create one.
     * </p>
     * @param view basic building block for user interface.
     */
    public void addOrEditNote(View view) {

        Intent intent = new Intent(NotesManagementActivity.this, BottomNavigationActivity.class);

        if (editOrCreateNewNote.equals("1")) {

            editExistingNote();
        }
        else {

            addNewNote();
        }
        startActivity(intent);
    }

    /**
     *<p>
     *     Edit an existing note the user has chosen with new data inserted to any of the following fields:
     *     <br>
     *     Date.
     *     <br>
     *     Note title.
     *     <br>
     *     Note content.
     *</p>
     */
    public void editExistingNote() {

        databaseReference = FirebaseDatabase.getInstance()
                .getReference("Notes").child(Objects.requireNonNull(firebaseAuthentication
                        .getCurrentUser()).getUid());

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot postSnapShot : snapshot.getChildren()) {

                    String compare = (getIntent().getStringExtra("NoteId"));
                    if (Objects.equals(postSnapShot.child("noteId").getValue(), compare)) {

                        databaseReference.child(Objects.requireNonNull(postSnapShot.getKey())).child("title").setValue(noteTitleEditText.getText().toString());
                        databaseReference.child(postSnapShot.getKey()).child("content").setValue(noteContentEditText.getText().toString());
                        databaseReference.child(postSnapShot.getKey()).child("date").child("dayOfMonth").setValue(noteCreationDate.getDayOfMonth());
                        databaseReference.child(postSnapShot.getKey()).child("date").child("monthValue").setValue(noteCreationDate.getMonth() + 1);
                        databaseReference.child(postSnapShot.getKey()).child("date").child("year").setValue(noteCreationDate.getYear());
                        Toast.makeText(NotesManagementActivity.this, "Note edited successfully", Toast.LENGTH_SHORT).show();
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    /**
     * <p>
     *     Creates new note for the user with data he inputted in the following fields:
     *     <br>
     *     noteTitleEditText.
     *     <br>
     *     noteContentEditText.
     *     <br>
     *     noteCreationDate.
     * </p>
     */
    public void addNewNote() {

        LocalTime now = LocalTime.now();
        ZonedDateTime dateTime = ZonedDateTime.of(noteCreationDate.getYear(),
                noteCreationDate.getMonth() + 1, noteCreationDate.getDayOfMonth() ,
                now.getHour(), now.getMinute(), now.getSecond(), now.getNano(), ZoneId.of("Israel"));
        Note note = new Note(Integer.toString(noteId), noteTitleEditText.getText().toString(),
                noteContentEditText.getText().toString(),email, currentLocation, dateTime);
        noteId++;
        addUserNoteToDatabase(note);
    }

    /**
     * Adds the new created note to the database under the user who created it.
     * @param note the new note we need to add to the database.
     */
    public void addUserNoteToDatabase(Note note) {

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        database.getReference("Notes")
                .child(Objects.requireNonNull(firebaseAuthentication.getCurrentUser())
                        .getUid()).push().setValue(note)
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()) {

                       Toast.makeText(NotesManagementActivity.this,
                               "Note successfully added.", Toast.LENGTH_SHORT).show();
                    } else {

                       Toast.makeText(NotesManagementActivity.this,
                               "Problem adding the note, please try again.", Toast.LENGTH_SHORT).show();
                    }
                });
        noteId++;
        noteLocation+= 0.00001;
        setSharedPreferenceValue("noteID", "note_key", noteId);
        setSharedPreferenceValue("noteLocation", "note_location", noteLocation);
    }

    /**
     * Sets the values of button's text, editTexts and datePicker.
     */
    @SuppressLint("SetTextI18n")
    private void setNotesManagementPage() {

        editOrCreateNewNote = getIntent().getStringExtra("Edit");

        if (editOrCreateNewNote.equals("1")) {

            String date = getIntent().getSerializableExtra("Date").toString().replace("-", ",");
            String[] subString = date.substring(0, date.indexOf("T")).split(",");
            noteCreationDate.updateDate(Integer.parseInt(subString[0]), Integer.parseInt(subString[1]) - 1
                    , Integer.parseInt(subString[2]));

            noteTitleEditText.setText(getIntent().getStringExtra("Title"));
            noteContentEditText.setText(getIntent().getStringExtra("Content"));
            addNoteBtn.setText("Save changes");
            deleteNoteBtn.setText("Delete note");
        } else {
            getLocationPermission();
            addNoteBtn.setText("Add note");
            deleteNoteBtn.setText("Discard note");
        }
    }

    /**
     * <p>
     *     onClick listener of delete_or_discard_note.
     *     <br>
     *     Deletes the note that was selected by the user.
     * </p>
     * @param view basic building block for user interface.
     */
    public void deleteOrDiscardNote(View view) {
        Intent intent = new Intent(NotesManagementActivity.this, BottomNavigationActivity.class);
        if(!editOrCreateNewNote.equals("0")) {

            databaseReference = FirebaseDatabase
                    .getInstance()
                    .getReference("Notes").child(Objects.requireNonNull(firebaseAuthentication
                            .getCurrentUser()).getUid());
            databaseReference.addValueEventListener(new ValueEventListener() {

                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    String noteId = getIntent().getStringExtra("NoteId");
                    for (DataSnapshot postSnapShot : snapshot.getChildren()) {
                        String res = Objects.requireNonNull(postSnapShot.child("noteId").getValue()).toString();
                        boolean result = res.equals(noteId);
                        if (result) {
                            postSnapShot.getRef().removeValue()
                                    .addOnCompleteListener(task ->
                                            Toast.makeText(NotesManagementActivity.this,
                                                    "Note was deleted", Toast.LENGTH_SHORT).show());
                            startActivity(intent);
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });
        }
        startActivity(intent);
    }


    /**
     * Gets the current location of the device.
     */
    public void getDeviceLocation(){

        FusedLocationProviderClient mFusedLocationProviderClient = LocationServices.
                getFusedLocationProviderClient(NotesManagementActivity.this);
        try {
            if(mLocationPermissionsGranted){
                Task<Location> location = mFusedLocationProviderClient.getLastLocation();
                location.addOnCompleteListener(task -> {
                    if(task.isSuccessful()){

                        //if location detection was unsuccessful give default latitude and longitude.
                        if(task.getResult() == null)
                        {
                            currentLocation = new LatLng(32,
                                    (32 + noteLocation));
                        }
                        else{
                            currentLocation = new LatLng(task.getResult().getLatitude(),
                                    (task.getResult().getLongitude() + noteLocation));

                        }

                    }else{
                        Toast.makeText(NotesManagementActivity.this,
                                "unable to get current location", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }catch (SecurityException e) {

        }
    }

    /**
     * <p>
     *     checks if we have permission for getting device location, if not then we request the user.
     *     <br>
     *     to share his location.
     * </p>
     */
    private void getLocationPermission(){
        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION};

        if(ContextCompat.checkSelfPermission(this.getApplicationContext(),
                FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){

            if(ContextCompat.checkSelfPermission(this.getApplicationContext(),
                    COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED){

                mLocationPermissionsGranted = true;
                getDeviceLocation();
            }else {
                ActivityCompat.requestPermissions(this, permissions,
                        LOCATION_PERMISSION_REQUEST_CODE);
            }
        }else {
            ActivityCompat.requestPermissions(this, permissions,
                    LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        mLocationPermissionsGranted = false;

        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {

            if (grantResults.length > 0) {

                for (int grantResult : grantResults) {

                    if (grantResult != PackageManager.PERMISSION_GRANTED) {

                        mLocationPermissionsGranted = false;
                        Toast.makeText(this, "You must enable location" +
                                "in order to add a new note.", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(this, BottomNavigationActivity.class);
                        startActivity(intent);
                        return;
                    }
                }
                mLocationPermissionsGranted = true;
                getDeviceLocation();
            }
        }
    }
}