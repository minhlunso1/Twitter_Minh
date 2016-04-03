package minhna.submission.twitter_minh.view;


import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONObject;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import minhna.submission.twitter_minh.R;
import minhna.submission.twitter_minh.TwitterApplication;
import minhna.submission.twitter_minh.TwitterModel;
import minhna.submission.twitter_minh.build.FollowAdapter;

/**
 * Created by Administrator on 28-Mar-16.
 */
public class FollowDialogFragment extends DialogFragment{

    @Bind(R.id.list_item)
    ListView lv;

    private int type;
    private long user_id;
    private String screen_name;
    List<TwitterModel.UserModel> list;
    private FollowDialogFragment fragment;

    public static FollowDialogFragment newInstance(String title, int type, long user_id, String screen_name) {
        FollowDialogFragment frag = new FollowDialogFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        args.putInt("type", type);
        args.putLong("user_id", user_id);
        args.putString("screen_name", screen_name);
        frag.setArguments(args);

        return frag;
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        fragment = this;
        return dialog;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_user_list, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        Bundle args = getArguments();
        type = args.getInt("type");
        user_id = args.getLong("user_id");
        screen_name = args.getString("screen_name");
        getDialog().setTitle(args.getString("title"));


    }

    @Override
    public void onStart() {
        super.onStart();
        TwitterApplication.getTwitterClient().getWhoFollowed(type, user_id, screen_name, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                list = TwitterModel.UserModel.getFollowListfromJSON(response);
                FollowAdapter adapter = new FollowAdapter(getActivity(), list);
                lv.setAdapter(adapter);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                Toast.makeText(getActivity(), responseString, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @OnClick ({R.id.btn_close})
    public void close(){
        dismiss();
    }

}
