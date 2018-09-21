package com.nguyenvukhanhuygmail.environmental_protection.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.StorageReference;
import com.nguyenvukhanhuygmail.environmental_protection.R;
import com.nguyenvukhanhuygmail.environmental_protection.model.Problem;

import java.util.List;

/**
 * Created by Uy Nguyen on 9/21/2018.
 */

public class ProblemAdapter extends RecyclerView.Adapter<ProblemAdapter.ProblemViewHolder> {

    List<Problem> problemList;
    String acc_type;
    DatabaseReference mDatabase;
    StorageReference storageRef;

    public ProblemAdapter(List<Problem> problemList, String acc_type, DatabaseReference mDatabase, StorageReference storageRef) {
        this.problemList = problemList;
        this.acc_type = acc_type;
        this.mDatabase = mDatabase;
        this.storageRef = storageRef;
    }

    @NonNull
    @Override
    public ProblemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ProblemViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.problem_card, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ProblemViewHolder holder, final int position) {

        setTextCustom(holder.tv_header, problemList.get(position).getTitle());
        setTextCustom(holder.tv_describe, problemList.get(position).getDescribe());
        setTextCustom(holder.tv_location, problemList.get(position).getLocation());
        holder.isDone.setChecked(problemList.get(position).isState());
        holder.isDone.setVisibility(View.GONE);
        if (acc_type.equals("Người dân")) {
            holder.isDone.setVisibility(View.VISIBLE);

            holder.isDone.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                    if (isChecked) {
                        mDatabase
                                .getRoot()
                                .child("Problems")
                                .orderByChild("image_code")
                                .equalTo(problemList.get(position).getImage_code())
                                .getRef()
                                .removeValue();
                        StorageReference imageRef = storageRef
                                .child("ProblemImage")
                                .child(problemList.get(position).getImage_code());
                        for (int i = 1; i <= problemList.get(position).getImage_num(); i++) {
                            imageRef
                                    .child(String.valueOf(i))
                                    .delete();
                        }
                    }
                }
            });

        }

    }

    @Override
    public int getItemCount() {
        return problemList.size();
    }

    private void setTextCustom(TextView textView, String text) {
        textView.setMaxLines(1);
        textView.setEllipsize(TextUtils.TruncateAt.END);
        textView.setText(text);
    }


    public static class ProblemViewHolder extends RecyclerView.ViewHolder {

        TextView tv_header, tv_describe, tv_location;
        CheckBox isDone;
        public ProblemViewHolder(View view) {
            super(view);
            tv_header = (TextView) view.findViewById(R.id.tv_header);
            tv_describe = (TextView) view.findViewById(R.id.tv_describe);
            tv_location = (TextView) view.findViewById(R.id.tv_location);
            isDone = (CheckBox) view.findViewById(R.id.isDone);
        }

    }

}
