package com.example.moveotask;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import org.json.JSONException;
import org.json.JSONObject;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Objects;

/**
 * Describes the fragment that presents google map to the user
 */
public class MapAndNotesFragment extends Fragment {

    MarkerOptions markerOptions;
    ArrayList<Notes> test;
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
        public void onMapReady(GoogleMap googleMap) {
            LatLng sydney = new LatLng(-34, 151);
            googleMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
            addMarkers(googleMap);
            googleMap.setOnMarkerClickListener(marker -> {

                int index = Integer.parseInt(Objects.requireNonNull(marker.getTitle()));
                Notes note = test.get(index);
                Intent intent = new Intent(getActivity(), NotesManagmentActivity.class);
                intent.putExtra("Title",note.getTitle());
                intent.putExtra("Content",note.getContent());
                intent.putExtra("Date",note.getDate());
                intent.putExtra("NoteId",note.getNoteId());
                intent.putExtra("Edit","1");
                startActivity(intent);
                return true;
            });
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
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
     * Presents the user's notes on google map
     * @param googleMap map object that will be presented to the user
     */
    public void addMarkers(GoogleMap googleMap){

        test = new ArrayList<>();
        FirebaseAuth firebaseAuthentication = FirebaseAuth.getInstance();
        DatabaseReference databaseReference = FirebaseDatabase
                .getInstance("https://moveotask-c5bd6-default-rtdb.firebaseio.com/")
                .getReference("Notes").child(Objects.requireNonNull(firebaseAuthentication.getCurrentUser())
                        .getUid());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot postSnapShot : snapshot.getChildren())
                {
                    JSONObject json;
                    System.out.println(postSnapShot.getValue());
                    Object object = postSnapShot.getValue();
                    try {
                        json = new JSONObject(object.toString());
                        int noteId = json.getInt("noteId");
                        String title = json.getString("title").replace("_"," ");
                        String content = json.getString("content").replace("_"," ");
                        String userEmail = json.getString("userEmail");
                        String [] latlng = json.getString("position").
                                replaceAll("[^\\,0123456789]", "").split(",");
                        double one =  Double.parseDouble(latlng[0]);
                        double two =  Double.parseDouble(latlng[1]);
                        LatLng position = new LatLng(one, two);
                        String date = json.getString("date");
                        date = date.replaceAll("[^\\,0123456789]", "");
                        String[] strArray = date.split(",");
                        Timestamp timestamp = new Timestamp(Integer.parseInt(strArray[0] + strArray[3] + strArray[6]));
                        Notes note = new Notes(Integer.toString(noteId), title, content, userEmail, position, timestamp);
                        test.add(note);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                if(test.size() > 0){
                    for(int i = 0 ; i < test.size() ; i++)
                    {
                        markerOptions.position(test.get(i).getPosition()).title(""+i);
                        googleMap.addMarker(markerOptions);
                    }
                    Toast.makeText(getContext(), "Map loaded successfully",
                            Toast.LENGTH_LONG).show();
                }
                else
                {
                    Toast.makeText(getContext(), "No notes found for display",
                            Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

    }
}