package minhna.submission.twitter_minh.build;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.List;

import minhna.submission.twitter_minh.R;
import minhna.submission.twitter_minh.TwitterModel;

/**
 * Created by Administrator on 03-Apr-16.
 */

public class FollowAdapter extends ArrayAdapter<TwitterModel.UserModel> {

    private Context context;
    private int count;

    public FollowAdapter(Context context, List<TwitterModel.UserModel> items) {
        super(context, 0, items);
        this.context = context;
        this.count = items.size();
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        TwitterModel.UserModel item = getItem(position);
        if (convertView==null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_follow, parent, false);
        }

        TextView tvComment1 = (TextView)convertView.findViewById(R.id.tv_name);
        RoundedImageView imgAva = (RoundedImageView) convertView.findViewById(R.id.img_ava);

        tvComment1.setText(item.getName());
        Glide.with(context)
                .load(item.getProfileImageUrl())
                .placeholder(R.mipmap.twitter_logo)
                .into(imgAva);

        return convertView;
    }

    @Override
    public int getCount() {
        return count;
    }
}
