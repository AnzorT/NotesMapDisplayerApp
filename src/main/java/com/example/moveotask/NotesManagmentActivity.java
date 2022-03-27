package com.example.moveotask;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

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

import java.sql.Timestamp;
import java.time.LocalTime;
import java.util.Objects;

/**
 * This class describes the edit, creating new note activity
 */
public class NotesManagmentActivity extends AppCompatActivity {

    String editOrCreateNewNote;
    DatePicker datePicker;
    Button addNoteBtn, deleteNoteBtn;
    EditText noteTitle, noteTextContent;
    String email;
    SharedPreferences sharedPreferencesNoteId;
    FirebaseAuth firebaseAuthentication;
    DatabaseReference databaseReference;
    LatLng currentLocation;
    int noteId;
    Location userLocation;

    private static final String TAG = "NoteManagmentActivity";
    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;
    private Boolean mLocationPermissionsGranted = false;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes_managment);


        firebaseAuthentication = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance()
                .getReference("Users").child(firebaseAuthentication.getCurrentUser().getUid());
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                email = snapshot.child("email").getValue().toString();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                //in case of failure
            }
        });
        sharedPreferencesNoteId = getSharedPreferences("noteID", MODE_PRIVATE);
        String test = sharedPreferencesNoteId.getString("note_key", "");
        editOrCreateNewNote = getIntent().getStringExtra("Edit");
        noteId = Integer.parseInt(test);
        //connect gadgets to variables
        noteTextContent = findViewById(R.id.noteTextContent);
        noteTitle = findViewById(R.id.noteTitle);
        datePicker = findViewById(R.id.datePicker);
        deleteNoteBtn = findViewById(R.id.delete);
        addNoteBtn = findViewById(R.id.edit);

        if (editOrCreateNewNote.equals("1")) {
            String date = getIntent().getSerializableExtra("Date").toString().replace("-", ",");
            int iend = date.indexOf(" ");
            String[] subString = date.substring(0, iend).split(",");
            datePicker.updateDate(Integer.parseInt(subString[0]), Integer.parseInt(subString[1]) - 1
                    , Integer.parseInt(subString[2]));
            noteTitle.setText(getIntent().getStringExtra("Title"));
            noteTextContent.setText(getIntent().getStringExtra("Content"));
            addNoteBtn.setText("Save changes");
            deleteNoteBtn.setText("Delete note");
        } else {
            addNoteBtn.setText("Add note");
            deleteNoteBtn.setText("Discard note");
        }
        getLocationPermission();
        //getDeviceLocation();
    }
    //button action listener
    public void addOrEditNote(View view) {
        if (editOrCreateNewNote.equals("1")) {
            editExistingNote();

        } else {
            addNewNote();
        }
    }

    /**
     * Edits the fields of a note that exists
     */
    private void editExistingNote() {

        databaseReference = FirebaseDatabase.getInstance()
                .getReference("Notes").child(Objects.requireNonNull(firebaseAuthentication.getCurrentUser()).getUid());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot postSnapShot : snapshot.getChildren()) {
                    String comp = (getIntent().getStringExtra("NoteId"));
                    if (postSnapShot.child("noteId").getValue().equals(comp)) {
                        databaseReference.child(Objects.requireNonNull(postSnapShot.getKey())).child("title").setValue(noteTitle.getText().toString().replace(" ", "_"));
                        databaseReference.child(postSnapShot.getKey()).child("content").setValue(noteTextContent.getText().toString().replace(" ", "_"));
                        databaseReference.child(postSnapShot.getKey()).child("date").child("date").setValue(datePicker.getDayOfMonth());
                        databaseReference.child(postSnapShot.getKey()).child("month").child("month").setValue(datePicker.getMonth());
                        databaseReference.child(postSnapShot.getKey()).child("year").child("year").setValue(datePicker.getYear());
                        Toast.makeText(NotesManagmentActivity.this, "Note edited successfully", Toast.LENGTH_LONG).show();
                        break;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        Intent intent = new Intent(NotesManagmentActivity.this, BottomNavigationActivity.class);
        startActivity(intent);
    }

    /**
     * Creates new node
     */
    public void addNewNote() {


        LocalTime time = LocalTime.now();
        String getTime = time.toString();
        currentLocation = new LatLng(noteId + 5, 0);
        String date = (datePicker.getYear()) + "-" + (datePicker.getMonth()) + "-" + (datePicker.getDayOfMonth()) + " " + getTime;
        Notes note = new Notes(Integer.toString(noteId), noteTitle.getText().toString().replace(" ","_"),
                noteTextContent.getText().toString().replace(" ","_"),
                email,
                currentLocation,
                Timestamp.valueOf(date));
        noteId++;
        addUserNoteToDatabase(note);
    }

    /**
     * Adds the new created node to the database
     * @param note the note we need to add to the database
     */
    public void addUserNoteToDatabase(Notes note) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        database.getReference("Notes")
                .child(Objects.requireNonNull(firebaseAuthentication.getCurrentUser())
                        .getUid()).push().setValue(note)
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        Toast.makeText(NotesManagmentActivity.this, "Note successfully added.", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toast.makeText(NotesManagmentActivity.this, "Problem adding the note, please try again.", Toast.LENGTH_SHORT).show();
                    }
                });
        SharedPreferences sp = getSharedPreferences("noteID", MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("note_key", Integer.toString(noteId));
        editor.commit();
        Intent intent = new Intent(NotesManagmentActivity.this, BottomNavigationActivity.class);
        startActivity(intent);
    }

    /**
     * Deletes a note the user selected.
     * @param view user interface object
     */
    public void deleteNote(View view) {

        if(editOrCreateNewNote.equals("0")){
            Intent intent = new Intent(NotesManagmentActivity.this, BottomNavigationActivity.class);
            startActivity(intent);
        }
        databaseReference = FirebaseDatabase
                .getInstance()
                .getReference("Notes").child(Objects.requireNonNull(firebaseAuthentication.getCurrentUser())
                        .getUid());
         databaseReference.addValueEventListener(new ValueEventListener() {
             @Override
             public void onDataChange(@NonNull DataSnapshot snapshot) {
                 String noteId = getIntent().getStringExtra("NoteId");
                 for(DataSnapshot postSnapShot : snapshot.getChildren()) {
                     String res = postSnapShot.child("noteId").getValue().toString();
                     boolean result = res.equals(noteId);
                     if(result){
                            postSnapShot.getRef().removeValue().addOnCompleteListener(task -> Toast.makeText(NotesManagmentActivity.this, "Note was deleted", Toast.LENGTH_SHORT).show());
                     }
                 }
             }
             @Override
             public void onCancelled(@NonNull DatabaseError error) {
             }
         });
        Intent intent = new Intent(NotesManagmentActivity.this, BottomNavigationActivity.class);
        startActivity(intent);
    }


    /**
     * gets the location of the device
     */
    public void getDeviceLocation(){
         mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(NotesManagmentActivity.this);
        try {
            if(mLocationPermissionsGranted){
                Task location = mFusedLocationProviderClient.getLastLocation();
                location.addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        Location userLocation = (Location) task.getResult();
                    }else{
                        Toast.makeText(NotesManagmentActivity.this, "unable to get current location", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }catch (SecurityException e){
        }
    }

    /**
     * <p>checks if we have permission for getting device location, if not then we request the user
     * to share his location
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
        Log.d(TAG, "onRequestsPermissionsResult: called");

        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0) {
                for (int grantResult : grantResults) {
                    if (grantResult != PackageManager.PERMISSION_GRANTED) {
                        mLocationPermissionsGranted = false;
                        return;
                    }
                }
                mLocationPermissionsGranted = true;
            }
        }
    }
}