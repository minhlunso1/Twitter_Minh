package minhna.submission.twitter_minh;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements PostDialogFragment.MyListener {

    @Bind(R.id.swipeRefeshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;

    public static int count = 10;
    public static long since_id = 1;

    private TwitterClient twitterClient;
    public static List<TwitterModel> list;
    public static ItemAdapter adapter;
    public static LinearLayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fm = getSupportFragmentManager();
                PostDialogFragment fragment = PostDialogFragment.newInstance("");
                fragment.show(fm, "fragment_post");
            }
        });
        twitterClient = TwitterApplication.getTwitterClient();

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
    }

    private void getMoreData(final int page) {
        twitterClient.getTwitterTimeline(page+1, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                super.onSuccess(statusCode, headers, response);
                try {
                    for (int i=count-10; i<count; i++) {
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

    @Override
    protected void onStart() {
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
//        if (id == R.id.action_settings) {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }

    public void getData(int page){
        twitterClient.getTwitterTimeline(page, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                super.onSuccess(statusCode, headers, response);
                try {
                    list.clear();
                    for (int i=count-10; i<count; i++) {
                        JSONObject object = response.getJSONObject(i);
                        TwitterModel model = TwitterModel.getTwitterModel(object);
                        list.add(model);
                    }
                    adapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    @Override
    public void onUpdateList(JSONObject object, String status) {
        list.clear();
        getMoreData(1);
    }
}
