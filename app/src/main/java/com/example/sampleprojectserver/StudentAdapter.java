package com.example.sampleprojectserver;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.StudentViewHolder> {
    private List<StudentObject> studentObjects;

    public StudentAdapter(List<StudentObject> studentObjects) {this.studentObjects = studentObjects;}

    @NonNull
    @Override
    public StudentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new StudentViewHolder(LayoutInflater.from(parent.getContext()).
                inflate(R.layout.item_student, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull StudentViewHolder holder, int position) {
        holder.bind(studentObjects.get(position));
    }

    @Override
    public int getItemCount() {
        return studentObjects.size();
    }

    class StudentViewHolder extends RecyclerView.ViewHolder {
        private TextView fullNameTv, courseTitleTv, scoreTv, firstCharacterTv;

        StudentViewHolder(@NonNull View itemView) {
            super(itemView);
            fullNameTv       = itemView.findViewById(R.id.tv_item_fullName);
            courseTitleTv    = itemView.findViewById(R.id.tv_item_course);
            scoreTv          = itemView.findViewById(R.id.tv_item_score);
            firstCharacterTv = itemView.findViewById(R.id.tv_item_firstCharacter);

        }

        @SuppressLint("SetTextI18n")
        void bind(StudentObject object) {
            fullNameTv.setText(object.getFirstName() + " " + object.getLastName());
            courseTitleTv.setText(object.getCourse());
            scoreTv.setText(String.valueOf(object.getScore()));
            firstCharacterTv.setText(object.getFirstName().substring(0, 1));
        }
    }
}
