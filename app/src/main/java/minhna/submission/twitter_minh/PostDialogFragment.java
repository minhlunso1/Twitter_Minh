package minhna.submission.twitter_minh;


import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.makeramen.roundedimageview.RoundedImageView;

import org.apache.http.Header;
import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 28-Mar-16.
 */
public class PostDialogFragment extends DialogFragment {

    @Bind(R.id.img_ava)
    RoundedImageView img_ava;
    @Bind(R.id.tv_name)
    TextView tvName;
    @Bind(R.id.edt_body)
    EditText edtBody;
    @Bind(R.id.tv_count)
    TextView tvCount;
    @Bind(R.id.btn_tweet)
    Button btnTweet;

    public static  PostDialogFragment newInstance(String id) {
        PostDialogFragment frag = new PostDialogFragment();
        Bundle args = new Bundle();
        args.putString("id", id);
        frag.setArguments(args);

        return frag;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        return dialog;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_post, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        getDialog().setTitle("Post");

        btnTweet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //something wrong about this api
//                TwitterApplication.getTwitterClient().doPost(edtBody.getText().toString(), new JsonHttpResponseHandler() {
//                    @Override
//                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
//                        super.onSuccess(statusCode, headers, response);
//                        Toast.makeText(getActivity(), "Tweet successfully", Toast.LENGTH_SHORT).show();
//                        dismiss();
//                    }
//                });
                TwitterModel model = new TwitterModel();
                model.setBody(edtBody.getText().toString());
                model.setid(AC.OWNER_ID);
                model.setUser(new TwitterModel.UserModel("Name",null));
                MainActivity.list.add(model);
                MainActivity.adapter.notifyDataSetChanged();
                dismiss();
            }
        });

        edtBody.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                char[] c = s.toString().toCharArray();
                int count = 140 - c.length;
                tvCount.setText(String.valueOf(count));
                if (count > -1 && count < 140)
                    btnTweet.setEnabled(true);
                else
                    btnTweet.setEnabled(false);
            }
        });
    }

}
