package minhna.submission.twitter_minh.build;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.makeramen.roundedimageview.RoundedImageView;

import org.apache.http.Header;
import org.json.JSONObject;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import minhna.submission.twitter_minh.R;
import minhna.submission.twitter_minh.TwitterApplication;
import minhna.submission.twitter_minh.TwitterClient;
import minhna.submission.twitter_minh.TwitterModel;
import minhna.submission.twitter_minh.view.MainActivity;
import minhna.submission.twitter_minh.view.ProfileActivity;

/**
 * Created by Administrator on 13-Mar-16.
 */
public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemViewHolder> {
    private List<TwitterModel> list;
    private Context context;
    private TwitterClient client;

    public ItemAdapter(Context context, List<TwitterModel> list) {
        this.list = list;
        this.context = context;
        this.client = TwitterApplication.getTwitterClient();
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
                .error(R.drawable.ic_hearing_white_36dp)
                .centerCrop()
                .into(holder.userAva);
        holder.userName.setText(user.getName());
        holder.text.setText(item.getBody());
        holder.tvTime.setText(item.getCreatedTime());

        holder.userName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showOtherProfile(user);
            }
        });
        holder.userAva.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showOtherProfile(user);
            }
        });
    }

    public void showOtherProfile(TwitterModel.UserModel user){
        client.getAnotherUserInfo(user.getOwner_id(), user.getName(), new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                TwitterModel.UserModel userModel = TwitterModel.UserModel.fromJSON(response);
                Intent i = new Intent(context, ProfileActivity.class);
                i.putExtra("user", userModel);
                i.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                context.startActivity(i);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                Toast.makeText(context, responseString, Toast.LENGTH_SHORT).show();
            }
        });
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
        @Bind(R.id.tv_time)
        TextView tvTime;

        public ItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}

