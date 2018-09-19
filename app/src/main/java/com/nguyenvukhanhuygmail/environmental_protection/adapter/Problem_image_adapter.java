package com.nguyenvukhanhuygmail.environmental_protection.adapter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.nguyenvukhanhuygmail.environmental_protection.R;

import java.util.List;

/**
 * Created by Uy Nguyen on 9/19/2018.
 */

public class Problem_image_adapter extends RecyclerView.Adapter<Problem_image_adapter.SimpleViewHolder> {

    List<byte[]> image_problem;

    public Problem_image_adapter(List<byte[]> image_problem) {
        this.image_problem = image_problem;
    }

    @NonNull
    @Override
    public SimpleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SimpleViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.image_card, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull SimpleViewHolder holder, int position) {
        Bitmap bitmap = BitmapFactory.decodeByteArray(image_problem.get(position), 0, image_problem.get(position).length);
        holder.iv_problem.setImageBitmap(bitmap);
    }

    @Override
    public int getItemCount() {
        return image_problem.size();
    }

    public static class SimpleViewHolder extends RecyclerView.ViewHolder {

        ImageView iv_problem;
        public SimpleViewHolder(View view) {
            super(view);
            iv_problem = (ImageView) view.findViewById(R.id.iv_problem);
        }

    }

}
