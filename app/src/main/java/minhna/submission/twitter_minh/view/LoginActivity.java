package minhna.submission.twitter_minh.view;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.codepath.oauth.OAuthLoginActionBarActivity;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import minhna.submission.twitter_minh.TwitterModel;
import minhna.submission.twitter_minh.var.AS;
import minhna.submission.twitter_minh.R;
import minhna.submission.twitter_minh.TwitterApplication;
import minhna.submission.twitter_minh.TwitterClient;

public class LoginActivity extends OAuthLoginActionBarActivity<TwitterClient> {

    ProgressDialog progressDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
	}

	// OAuth authenticated successfully, launch primary authenticated activity
	// i.e Display application "homepage"
	@Override
	public void onLoginSuccess() {
         progressDialog = ProgressDialog.show(this, "Loading",
                "Please wait", true, true);
		 TwitterApplication.getTwitterClient().getProfile(new JsonHttpResponseHandler(){
			 @Override
			 public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
				 super.onSuccess(statusCode, headers, response);
				 try {
                     AS.myUser = new TwitterModel.UserModel(response.getLong("id"), response.getString("name"), response.getString("profile_image_url"), response.getString("profile_background_image_url"), response.getLong("followers_count"), response.getLong("friends_count"), response.getString("description"));

                     Intent i = new Intent(LoginActivity.this, MainActivity.class);
					 i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                     startActivity(i);
                     progressDialog.dismiss();
				 } catch (JSONException e) {
                     progressDialog.dismiss();
					 e.printStackTrace();
				 }
			 }
		 });
	}

	// OAuth authentication flow failed, handle the error
	// i.e Display an error dialog or toast
	@Override
	public void onLoginFailure(Exception e) {
		e.printStackTrace();
	}

	// Click handler method for the button used to start OAuth flow
	// Uses the client to initiate OAuth authorization
	// This should be tied to a button used to login
	public void loginToRest(View view) {
		getClient().connect();
	}

}
