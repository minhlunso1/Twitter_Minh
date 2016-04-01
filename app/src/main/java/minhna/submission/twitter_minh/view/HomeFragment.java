package minhna.submission.twitter_minh.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.loopj.android.http.JsonHttpResponseHandler;

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


public class HomeFragment extends Fragment {

    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;
    @Bind(R.id.swipeRefeshLayout)
    SwipeRefreshLayout swipeRefreshLayout;

    private int count = 10;
    private static long since_id = 1;

    private TwitterClient twitterClient;
    private List<TwitterModel> list;
    private ItemAdapter adapter;
    private LinearLayoutManager layoutManager;
    private AppCompatActivity activity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (AppCompatActivity) getActivity();
        twitterClient = TwitterApplication.getTwitterClient();
        activity.getSupportActionBar().show();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        list = new ArrayList<>();
        adapter = new ItemAdapter(activity, list);
        layoutManager = new LinearLayoutManager(activity);
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

    private void getMoreData(final int page) {
        twitterClient.getTwitterTimeline(0, page+1, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                super.onSuccess(statusCode, headers, response);
                try {
                    for (int i=0; i<response.length(); i++) {
                        JSONObject object = response.getJSONObject(i);
                        TwitterModel model = TwitterModel.getTwitterModel(object);
                        list.add(model);
                    }
                    adapter.notifyItemRangeChanged((page*10)-1, count*page);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void getData(int page){
        twitterClient.getTwitterTimeline(0, page, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                super.onSuccess(statusCode, headers, response);
                try {
                    list.clear();
                    for (int i=0; i<response.length(); i++) {
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

    public void updateView(TwitterModel model){
        callRefresh();
    }

    @Override
    public void onStart() {
        super.onStart();
        callRefresh();
    }

    private void callRefresh() {
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


