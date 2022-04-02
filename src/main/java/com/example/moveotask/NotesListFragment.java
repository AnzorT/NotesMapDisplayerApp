package com.example.moveotask;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Objects;

/**
 * <p>
 *      Author: Anzor Torikashvili.
 *      <br>
 *      This class describes NotesListFragment fragment.
 * </p>
 */
public class NotesListFragment extends Fragment {

    ArrayList<Note> listOfNotes;
    FirebaseAuth firebaseAuthentication;
    FrameLayout frameLayout;
    RecyclerView recyclerView;

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

        //connect gadgets to variables
        frameLayout = getView().findViewById(R.id.background_image);
        recyclerView = getView().findViewById(R.id.notesRecyclerView);

        BottomNavigationActivity bottomNavigationActivity = new BottomNavigationActivity();
        firebaseAuthentication = FirebaseAuth.getInstance();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Notes")
                .child(Objects.requireNonNull(firebaseAuthentication.getCurrentUser()).getUid());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                listOfNotes = bottomNavigationActivity.getDataFromDataBase(snapshot);
                preparePageForDisplay(listOfNotes);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    /**
     * <p>
     *     Sets the background image of the page and display the list of the created notes.
     *     <br>
     *     If the user has no notes set background image to sad.png .
     *     <br>
     *     Else set background image to happy.png .
     * </p>
     * @param listOfNotes list of the user's notes.
     */
    public void preparePageForDisplay(ArrayList<Note> listOfNotes) {

        //indicates if user has notes or not
        if(listOfNotes.size() > 0){
            frameLayout.setBackgroundResource(R.drawable.happy);
            buildRecyclerView();
        }
        else
        {
            frameLayout.setBackgroundResource(R.drawable.sad);
        }
        frameLayout.setVisibility(View.VISIBLE);
    }

    /**
     * <p>
     *     Prepares list of notes for display to the user.
     *     <br>
     *     the list will be sorted by date.
     * </p>
     */
    public void buildRecyclerView(){

        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager recyclerViewManager = new LinearLayoutManager(getContext());
        RecycleViewAdapter recyclerViewAdapter = new RecycleViewAdapter(listOfNotes);

        recyclerView.setLayoutManager(recyclerViewManager);
        recyclerView.setAdapter(recyclerViewAdapter);
        listOfNotes.sort(Comparator.comparing(Note::getDate));

        recyclerViewAdapter.setOnItemClickListener(position -> {

            Note note = listOfNotes.get(position);
            Intent intent = new Intent(getActivity(), NotesManagementActivity.class);
            intent.putExtra("Title",note.getTitle());
            intent.putExtra("Content",note.getContent());
            intent.putExtra("Date",note.getDate());
            intent.putExtra("NoteId",note.getNoteId());
            intent.putExtra("Edit","1");
            startActivity(intent);
        });
    }
}