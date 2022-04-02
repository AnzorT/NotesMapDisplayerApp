package com.example.moveotask;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

/**
 * <p>
 *      Author: Anzor Torikashvili.
 *      This class describes RecycleViewAdapter of RecyclerView object.
 * </p>
 */
public class RecycleViewAdapter extends RecyclerView.Adapter<RecycleViewAdapter.ViewHolder> {

    private ArrayList<Note> notesList;
    private OnItemClickListener mListener;

    public interface OnItemClickListener{
        void onItemClick(int position);
    }

    /**
     * <p>
     *     onClickListener of RecyclerView.
     *     <br>
     *     makes the items in our RecyclerView clickable and redirect us to NotesManagementActivity.
     * </p>
     * @param listener Interface definition for a callback to be invoked when an item in this AdapterView has been clicked.
     */
    public void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;
    }

    /**
     * This class describes ViewHolder.
     */
    public static class ViewHolder extends RecyclerView.ViewHolder{

        public TextView title;
        public TextView content;
        public TextView date;

        public ViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            content = itemView.findViewById(R.id.text);
            date = itemView.findViewById(R.id.date);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(listener != null){
                        int position = getAdapterPosition();

                        //Check if we clicked on a note
                        if(position != RecyclerView.NO_POSITION){
                            listener.onItemClick(position);
                        }
                    }
                }
            });
        }
    }

    /**
     * Takes the data it receives and puts it into our recyclerview
     * @param notes data that will be entered into recyclerview
     */
    public RecycleViewAdapter(ArrayList<Note> notes){
        notesList = notes;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_layout, parent, false);
        ViewHolder viewHolder = new ViewHolder(v, mListener);
        return viewHolder;
    }

    /**
     * Inserts data into the viewHolder.
     * @param holder the viewHolder we want to insert data into.
     * @param position the position of the note we now want to add to the viewHolder.
     */
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Note currentNote = notesList.get(position);
        holder.title.setText(currentNote.getTitle());
        holder.content.setText(currentNote.getContent());
        ZonedDateTime zonedDateTime = currentNote.getDate().withZoneSameInstant(ZoneId.of("Israel"));
        String formattedString = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss").format(zonedDateTime);
        holder.date.setText(formattedString);
    }

    /**
     * Return int type.
     * @return the size of our list of notes.
     */
    @Override
    public int getItemCount() {
        return notesList.size();
    }

    public interface OnNoteListener{
        void onNoteClick(int position);
    }
}
