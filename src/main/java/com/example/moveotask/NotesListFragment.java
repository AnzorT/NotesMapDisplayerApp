package com.example.moveotask;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
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
import java.util.Collections;
import java.util.Comparator;

/**
 * This class describes the list of notes fragment
 */
public class NotesListFragment extends Fragment {

    private ArrayList<Notes> test;
    private DatabaseReference databaseReference;
    FirebaseAuth firebaseAuthentication;
    private RecyclerView recyclerView;
    private RecycleViewAdapter recyclerViewAdapter;
    private RecyclerView.LayoutManager recyclerViewManager;
    FrameLayout frameLayout;
    public NotesListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_notes_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        frameLayout = getActivity().findViewById(R.id.background_image);
        test = new ArrayList<>();
        firebaseAuthentication = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase
                .getInstance()
                .getReference("Notes").child(firebaseAuthentication.getCurrentUser()
                        .getUid());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot postSnapShot : snapshot.getChildren())
                {
                    Object object = postSnapShot.getValue();
                    try {
                        JSONObject json = new JSONObject(object.toString());
                        String title = json.getString("title").replace("_"," ");
                        int noteId = json.getInt("noteId");
                        String content = json.getString("content").replace("_"," ");
                        String userEmail = json.getString("userEmail");
                        String [] latlng = json.getString("position").replaceAll("[^\\,0123456789]", "").split(",");
                        double one =  Double.parseDouble(latlng[0]);
                        double two =  Double.parseDouble(latlng[1]);
                        LatLng position = new LatLng(one, two);
                        String date = json.getString("date");
                        date = date.replaceAll("[^\\,0123456789]", "");
                        String[] strArray = date.split(",");
                        //Timestamp timestamp = new Timestamp(Integer.parseInt(strArray[0] + strArray[3] + strArray[6]));
                        int year  = Integer.parseInt(strArray[6])-100 + 2000;
                        String timeStamp = (year+"-"+strArray[3]+"-"+strArray[0]+" "+strArray[1]+":"+strArray[7]+":"+strArray[2]+"."+strArray[4]).toString();
                        Timestamp timestamp = Timestamp.valueOf(timeStamp);
                        Notes note = new Notes(Integer.toString(noteId), title, content, userEmail, position, timestamp);
                        test.add(note);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                if(test.size() > 0){
                    frameLayout.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.happy));
                    Collections.sort(test, Comparator.comparing(Notes::getDate));
                    buildRecyclerView();
                }
                else
                {
                    frameLayout.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.sad));
//                    Toast.makeText(getContext(), "You have no notes to display",
//                            Toast.LENGTH_LONG).show();
                }
                frameLayout.setVisibility(View.VISIBLE);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void buildRecyclerView(){
        recyclerView = getView().findViewById(R.id.notesRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerViewManager = new LinearLayoutManager(getContext());
        recyclerViewAdapter = new RecycleViewAdapter(test);

        recyclerView.setLayoutManager(recyclerViewManager);
        recyclerView.setAdapter(recyclerViewAdapter);

        recyclerViewAdapter.setOnItemClickListener(position -> {
            Notes note = test.get(position);
            Intent intent = new Intent(getActivity(), NotesManagmentActivity.class);
            intent.putExtra("Title",note.getTitle());
            intent.putExtra("Content",note.getContent());
            intent.putExtra("Date",note.getDate());
            intent.putExtra("NoteId",note.getNoteId());
            intent.putExtra("Edit","1");
            startActivity(intent);
        });
    }
}