package minhna.submission.twitter_minh;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 13-Mar-16.
 */
public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemViewHolder> {
    private List<TwitterModel> list;
    private Context context;

    public ItemAdapter(Context context, List<TwitterModel> list) {
        this.list = list;
        this.context = context;
    }

    @Override
    public ItemAdapter.ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_timeline, parent, false);
        ItemViewHolder viewHolder = new ItemViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ItemAdapter.ItemViewHolder holder, int position) {
        final TwitterModel item = list.get(position);
        final TwitterModel.UserModel user = item.getUser();
            Glide.with(context)
                    .load(user.getProfileImageUrl())
                    .placeholder(R.mipmap.ic_launcher)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .centerCrop()
                    .into(holder.userAva);
            holder.userName.setText(user.getName());
            holder.text.setText(item.getBody());
    }

    @Override
    public int getItemCount() {
        return list != null ? list.size() : 0;
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder{

        @Bind(R.id.img_ava)
        RoundedImageView userAva;
        @Bind(R.id.tv_name)
        TextView userName;
        @Bind(R.id.tv_text)
        TextView text;


        public ItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}

