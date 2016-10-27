package net.devcats.squirrel.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import net.devcats.squirrel.Models.Image;
import net.devcats.squirrel.R;

import java.util.List;

public class ModelListViewAdapter extends BaseAdapter {

    private LayoutInflater layoutInflater;
    private List<Image> mImageList;

    public ModelListViewAdapter(Context context, List<Image> imageList) {
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mImageList = imageList;
    }

    public void updateData(List<Image> imageList) {
        mImageList = imageList;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mImageList.size();
    }

    @Override
    public Image getItem(int position) {
        return mImageList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return mImageList.get(position).getImageId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.listview_model, parent, false);
        }

        Image image = getItem(position);

        ImageView imgModelImage1 = (ImageView) convertView.findViewById(R.id.imgModelImage1);

        if (image.getImageBitmap() != null) {
            imgModelImage1.setAdjustViewBounds(true);
            imgModelImage1.setImageBitmap(image.getImageBitmap());
        } else {
            imgModelImage1.setAdjustViewBounds(false);
            imgModelImage1.setImageResource(R.drawable.ic_image_temp);
        }

        return convertView;
    }
}
