package com.example.moveotask;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

/**
 * <p>
 *     Author: Anzor Torikashvili.
 *     <br>
 *     This class Describes MapAndNotesFragment fragment that presents google map to the user.
 * </p>
 */
public class MapAndNotesFragment extends Fragment {

    MarkerOptions markerOptions;
    ArrayList<Note> listOfNotes;

    private final OnMapReadyCallback callback = new OnMapReadyCallback() {

        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */
        @Override
        public void onMapReady(@NonNull GoogleMap googleMap) {

            addMarkers(googleMap);
            googleMap.setOnMarkerClickListener(marker -> {

                int index = Integer.parseInt(Objects.requireNonNull(marker.getTitle()));
                Note note = listOfNotes.get(index);
                Intent intent = new Intent(getActivity(), NotesManagementActivity.class);
                intent.putExtra("Title",note.getTitle());
                intent.putExtra("Content",note.getContent());
                intent.putExtra("Date",note.getDate());
                intent.putExtra("NoteId",note.getNoteId());
                intent.putExtra("Edit","1");
                startActivity(intent);
                return true;
            });
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_map_and_notes, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);
        markerOptions = new MarkerOptions();
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {

            mapFragment.getMapAsync(callback);
        }
    }

    /**
     * Presents the user's notes on google map.
     * @param googleMap map object that will be presented to the user.
     */
    public void addMarkers(GoogleMap googleMap){

        listOfNotes = new ArrayList<>();
        BottomNavigationActivity bottomNavigationActivity = new BottomNavigationActivity();
        FirebaseAuth firebaseAuthentication = FirebaseAuth.getInstance();
        DatabaseReference databaseReference = FirebaseDatabase
                .getInstance()
                .getReference("Notes").child(Objects.requireNonNull(firebaseAuthentication.getCurrentUser())
                        .getUid());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                listOfNotes = bottomNavigationActivity.getDataFromDataBase(snapshot);
                addNotesLocationsToTheMap(googleMap);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    /**
     * Adds note pins to the map.
     * @param googleMap the map that will be displayed to the user.
     */
    public void addNotesLocationsToTheMap(GoogleMap googleMap) {

        if(listOfNotes.size() > 0) {

            for(int i = 0; i < listOfNotes.size() ; i++) {

                markerOptions.position(listOfNotes.get(i).getPosition()).title(""+i);
                googleMap.addMarker(markerOptions);
            }
        }
    }
}