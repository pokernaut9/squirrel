package net.devcats.squirrel.Adapters;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import net.devcats.squirrel.Models.Image;
import net.devcats.squirrel.R;
import net.devcats.squirrel.Views.TouchImageView;

import java.util.List;

public class SquirrelPagerAdapter extends PagerAdapter {

    private Context mContext;
    private List<Image> mImages;

    public SquirrelPagerAdapter(Context context, List<Image> images) {
        mContext = context;
        mImages = images;
    }

    @Override
    public int getCount() {
        return mImages.size();
    }

    public void updateList(List<Image> images) {
        mImages = images;
        notifyDataSetChanged();
    }

    @Override
    public Object instantiateItem(ViewGroup collection, final int position) {

        final TouchImageView imgView = new TouchImageView(mContext);

        if (mImages.get(position).getImageBitmap() != null) {
            imgView.setImageBitmap(mImages.get(position).getImageBitmap());
        } else {
            imgView.setImageResource(R.drawable.ic_image_temp);
        }
        collection.addView(imgView);

        return imgView;
    }

    @Override
    public void destroyItem(ViewGroup collection, int position, Object view) {
        collection.removeView((TouchImageView) view);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return (view == object);
    }

}
