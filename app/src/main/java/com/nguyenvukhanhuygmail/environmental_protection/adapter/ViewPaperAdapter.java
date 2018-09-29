package com.nguyenvukhanhuygmail.environmental_protection.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.nguyenvukhanhuygmail.environmental_protection.R;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Uy Nguyen on 9/22/2018.
 */

public class ViewPaperAdapter extends PagerAdapter {

    List<String> image_problem;
    Context context;

    public ViewPaperAdapter(List<String> image_problem, Context context) {
        this.image_problem = image_problem;
        this.context = context;
    }

    @Override
    public int getCount() {
        return image_problem.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {

        View view = LayoutInflater.from(context).inflate(R.layout.viewpaper_item, container, false);

        ImageView imageView = view.findViewById(R.id.image_problem);

        Picasso
                .get()
                .load(image_problem.get(position))
                .placeholder(R.drawable.loading)
                .error(R.drawable.error)
                .into(imageView);

        container.addView(view);

        Animation animation = AnimationUtils.loadAnimation(context, R.anim.lv_item_slide_from_bottom);
        view.startAnimation(animation);

        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {

        container.removeView((View) object);

    }
}
