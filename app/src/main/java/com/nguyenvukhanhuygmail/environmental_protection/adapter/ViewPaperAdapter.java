package com.nguyenvukhanhuygmail.environmental_protection.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.nguyenvukhanhuygmail.environmental_protection.R;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Uy Nguyen on 9/22/2018.
 */

public class ViewPaperAdapter extends PagerAdapter {

    List<String> image_problem;
    Activity activity;
    LayoutInflater inflater;

    public ViewPaperAdapter(List<String> image_problem, Activity activity) {
        this.image_problem = image_problem;
        this.activity = activity;
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

        inflater = (LayoutInflater) activity.getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.viewpaper_item, container, false);

        ImageView imageView = (ImageView) view.findViewById(R.id.image_problem);

        Picasso
                .get()
                .load(image_problem.get(position))
                .placeholder(R.drawable.loading)
                .error(R.drawable.error)
                .into(imageView);

        container.addView(view);

        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {

        ((ViewPager) container).removeView((View) object);

    }
}
