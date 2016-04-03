package minhna.submission.twitter_minh.view;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.codepath.oauth.OAuthLoginActionBarActivity;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
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
        if (isNetworkAvailable()) {
            TwitterApplication.getTwitterClient().getProfile(new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    super.onSuccess(statusCode, headers, response);
                        AS.myUser = TwitterModel.UserModel.fromJSON(response);

                        Intent i = new Intent(LoginActivity.this, MainActivity.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(i);
                        progressDialog.dismiss();
                }
            });
        } else {
            progressDialog.dismiss();
            Toast.makeText(this, "No Internet", Toast.LENGTH_LONG).show();
        }
	}

	// OAuth authentication flow failed, handle the error
	// i.e Display an error dialog or toast
	@Override
	public void onLoginFailure(Exception e) {
		e.printStackTrace();
		if (isNetworkAvailable() && isOnline())
			Toast.makeText(this, "Try login again.", Toast.LENGTH_LONG).show();
		else
			Toast.makeText(this, "No Internet", Toast.LENGTH_LONG).show();
	}

	// Click handler method for the button used to start OAuth flow
	// Uses the client to initiate OAuth authorization
	// This should be tied to a button used to login
	public void loginToRest(View view) {
		if (isNetworkAvailable())
			getClient().connect();
		else
			Toast.makeText(this, "No Internet", Toast.LENGTH_LONG).show();
	}

	private Boolean isNetworkAvailable() {
		ConnectivityManager connectivityManager
				= (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
		return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
	}

	public boolean isOnline() {
		Runtime runtime = Runtime.getRuntime();
		try {
			Process ipProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8");
			int     exitValue = ipProcess.waitFor();
			return (exitValue == 0);
		} catch (Exception e){
			e.printStackTrace();
		}
		return false;
	}

}
