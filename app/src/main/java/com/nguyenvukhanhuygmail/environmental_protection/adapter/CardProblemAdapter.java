package com.nguyenvukhanhuygmail.environmental_protection.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.StorageReference;
import com.nguyenvukhanhuygmail.environmental_protection.R;
import com.nguyenvukhanhuygmail.environmental_protection.model.Problem;

import java.util.List;

/**
 * Created by Uy Nguyen on 9/29/2018.
 */

public class CardProblemAdapter extends BaseAdapter {

    List<Problem> problemList;
    String acc_type, uID;
    DatabaseReference mDatabase;
    StorageReference storageRef;
    Context context;

    public CardProblemAdapter(List<Problem> problemList, String acc_type, String uID, DatabaseReference mDatabase, StorageReference storageRef, Context context) {
        this.problemList = problemList;
        this.acc_type = acc_type;
        this.uID = uID;
        this.mDatabase = mDatabase;
        this.storageRef = storageRef;
        this.context = context;
    }

    private class ViewHolder {
        TextView tv_header, tv_describe, tv_location;
        CheckBox isDone;
    }

    @Override
    public int getCount() {
        return problemList.size();
    }

    @Override
    public Object getItem(int i) {
        return problemList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return problemList.get(i).getImage_num();
    }

    @Override
    public View getView(final int position, View view, ViewGroup viewGroup) {

        ViewHolder viewHolder;

        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.problem_card, viewGroup, false);

            viewHolder = new ViewHolder();
            viewHolder.tv_header = (TextView) view.findViewById(R.id.tv_header);
            viewHolder.tv_describe = (TextView) view.findViewById(R.id.tv_describe);
            viewHolder.tv_location = (TextView) view.findViewById(R.id.tv_location);
            viewHolder.isDone = (CheckBox) view.findViewById(R.id.isDone);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        setTextCustom(viewHolder.tv_header, problemList.get(position).getTitle());
        setTextCustom(viewHolder.tv_describe, problemList.get(position).getDescribe());
        setTextCustom(viewHolder.tv_location, problemList.get(position).getLocation());
        viewHolder.isDone.setVisibility(View.GONE);
        if (acc_type.equals("Người dân")) {
            viewHolder.isDone.setVisibility(View.VISIBLE);

            viewHolder.isDone.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                    if (isChecked) {
                        mDatabase
                                .getRoot()
                                .child("Problems")
                                .child(uID)
                                .child(problemList.get(position).getDate())
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

        Animation animation = AnimationUtils.loadAnimation(context, R.anim.lv_item_slide_from_bottom);
        view.startAnimation(animation);

        return view;
    }

    private void setTextCustom(TextView textView, String text) {
        textView.setMaxLines(1);
        textView.setEllipsize(TextUtils.TruncateAt.END);
        textView.setText(text);
    }

}
