package net.devcats.squirrel.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import net.devcats.squirrel.Models.ModelAlbum;
import net.devcats.squirrel.R;

import java.util.List;

public class UserAlbumsGridViewAdapter extends BaseAdapter {

    private Context mContext;
    private List<ModelAlbum> mAlbums;

    public UserAlbumsGridViewAdapter(Context context, List<ModelAlbum> albums) {
        mContext = context;
        mAlbums = albums;
    }

    @Override
    public int getCount() {
        return mAlbums.size();
    }

    @Override
    public ModelAlbum getItem(int position) {
        return mAlbums.get(position);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {

        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.gridview_user_albums, viewGroup, false);
        }

        ModelAlbum modelAlbum = mAlbums.get(position);

        ImageView imgAlbumImage = (ImageView) convertView.findViewById(R.id.imgAlbumImage);

        if (modelAlbum.getAlbumImage() == null) {
            imgAlbumImage.setImageResource(R.drawable.ic_image_temp);
        } else {
            imgAlbumImage.setImageBitmap(modelAlbum.getAlbumImage());
        }

        TextView tvAlbumName = (TextView) convertView.findViewById(R.id.tvAlbumName);
        tvAlbumName.setText(modelAlbum.getName());

        TextView tvAlbumImageCount = (TextView) convertView.findViewById(R.id.tvAlbumImageCount);
        tvAlbumImageCount.setText(mContext.getString(R.string.image_count, modelAlbum.getImageCount()));

        return convertView;
    }
}
