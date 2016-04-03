package minhna.submission.twitter_minh.view;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.makeramen.roundedimageview.RoundedImageView;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import minhna.submission.twitter_minh.R;
import minhna.submission.twitter_minh.TwitterApplication;
import minhna.submission.twitter_minh.TwitterClient;
import minhna.submission.twitter_minh.TwitterModel;
import minhna.submission.twitter_minh.build.EndlessRecyclerViewScrollListener;
import minhna.submission.twitter_minh.build.ItemAdapter;
import minhna.submission.twitter_minh.var.AS;

/**
 * Created by Administrator on 03-Apr-16.
 */
public class ProfileActivity extends AppCompatActivity {

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.tabLayout)
    TabLayout tabLayout;
    @Bind(R.id.img_ava)
    RoundedImageView imgAva;
    @Bind(R.id.tv_name)
    TextView tvName;
    @Bind(R.id.img_collapsing)
    ImageView imgCollapsing;
    @Bind(R.id.tv_tagline)
    TextView tvTagline;
    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;
    @Bind(R.id.swipeRefeshLayout)
    SwipeRefreshLayout swipeRefreshLayout;

    private int count = 10;
    private TwitterModel.UserModel userModel;
    private TwitterClient twitterClient;

    private List<TwitterModel> list;
    private ItemAdapter adapter;
    private LinearLayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ButterKnife.bind(this);

        twitterClient = TwitterApplication.getTwitterClient();
        if (getIntent().getParcelableExtra("user")!=null) {
            userModel = getIntent().getParcelableExtra("user");
        } else
            userModel = AS.myUser;
        if (savedInstanceState==null) {
            setSupportActionBar(toolbar);
            setupView();
        }
    }

    private void setupView() {
        Glide.with(this)
                .load(userModel.getProfileImageUrl())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imgAva);
        tvName.setText(userModel.getName());
        Glide.with(this)
                .load(userModel.getProfileBackgroundImageUrl())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imgCollapsing);
        setupTabLayout();
        tvTagline.setText(userModel.getTagline());

        list = new ArrayList<>();
        adapter = new ItemAdapter(this, list);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        recyclerView.addOnScrollListener(new EndlessRecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                getMoreData(page);
            }
        });

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                try {
                    getData(1);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright);
    }

    private void setupTabLayout() {
        tabLayout.addTab(tabLayout.newTab().setText(userModel.getFollwers_count() + " followers"));
        tabLayout.addTab(tabLayout.newTab().setText(userModel.getFollowing_count() + " following"));

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                showFollowDialog(tab);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                showFollowDialog(tab);
            }

            private void showFollowDialog(TabLayout.Tab tab) {
                FragmentManager fm = getSupportFragmentManager();
                String title = "";
                int type = tab.getPosition();
                if (type==0)
                    title = "Followers";
                else
                    title = "Following";
                FollowDialogFragment fragment = FollowDialogFragment.newInstance(title, type, userModel.getOwner_id(), userModel.getName());
                fragment.show(fm, "fragment_follow");
            }

        });
    }

    private void getMoreData(final int page) {
        twitterClient.getTwitterTimeline(userModel.getOwner_id(), userModel.getName(), 2, page + 1, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                super.onSuccess(statusCode, headers, response);
                try {
                    for (int i = 0; i < response.length(); i++) {
                        JSONObject object = response.getJSONObject(i);
                        TwitterModel model = TwitterModel.getTwitterModel(object);
                        list.add(model);
                    }
                    adapter.notifyItemRangeChanged((page * 10) - 1, count * page);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void getData(int page){
        twitterClient.getTwitterTimeline(userModel.getOwner_id(), userModel.getName(), 2, page, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                super.onSuccess(statusCode, headers, response);
                try {
                    list.clear();
                    for (int i = 0; i < response.length(); i++) {
                        JSONObject object = response.getJSONObject(i);
                        TwitterModel model = TwitterModel.getTwitterModel(object);
                        list.add(model);
                        swipeRefreshLayout.setRefreshing(false);
                    }
                    adapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    swipeRefreshLayout.setRefreshing(false);
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        if (!swipeRefreshLayout.isRefreshing()) {
            swipeRefreshLayout.post(new Runnable() {
                @Override
                public void run() {
                    swipeRefreshLayout.setRefreshing(true);
                }
            });
        }
        getData(1);
    }

}
