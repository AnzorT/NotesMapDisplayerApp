package com.example.moveotask;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

/**
 * This class describes the construction of RecyclerView object
 */
public class RecycleViewAdapter extends RecyclerView.Adapter<RecycleViewAdapter.ViewHolder> {

    private ArrayList<Notes> notesList;
    private OnItemClickListener mListener;
    public interface OnItemClickListener{
        void onItemClick(int position);
    }

    // make our recyclerView clickable
    public void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;
    }

    //this class describes the construction of each object insdie our recyclerview
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
     * Takes the data it recieves and puts it into our recyclerview
     * @param notes data that will be entered into recyclerview
     */
    public RecycleViewAdapter(ArrayList<Notes> notes){
        notesList = notes;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_layout, parent, false);
        ViewHolder viewHolder = new ViewHolder(v, mListener);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Notes currentNote = notesList.get(position);
        holder.title.setText(currentNote.getTitle());
        holder.content.setText(currentNote.getContent());
        holder.date.setText(currentNote.getDate().toString());
    }

    @Override
    public int getItemCount() {
        return notesList.size();
    }

    public interface OnNoteListener{
        void onNoteClick(int position);
    }
}
