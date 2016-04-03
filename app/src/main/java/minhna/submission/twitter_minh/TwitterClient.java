package minhna.submission.twitter_minh;

import org.scribe.builder.api.Api;
import org.scribe.builder.api.TwitterApi;

import android.content.Context;

import com.codepath.oauth.OAuthBaseClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import minhna.submission.twitter_minh.var.AC;

public class TwitterClient extends OAuthBaseClient {
	public static final Class<? extends Api> REST_API_CLASS = TwitterApi.class; // Change this
	public static final String REST_URL = AC.BASE_URL; // Change this, base API URL
	public static final String REST_CONSUMER_KEY = AC.CONSUMER_KEY;       // Change this
	public static final String REST_CONSUMER_SECRET = AC.CONSUMER_SECRET; // Change this
	public static final String REST_CALLBACK_URL = "oauth://cpTwitter"; // Change this (here and in manifest)

	public TwitterClient(Context context) {
		super(context, REST_API_CLASS, REST_URL, REST_CONSUMER_KEY, REST_CONSUMER_SECRET, REST_CALLBACK_URL);
	}

    public void getTwitterTimeline(long user_id, String screen_name, int type, int page, AsyncHttpResponseHandler handler){
        String apiUrl = "";
        if (type==0)//homeline
            apiUrl = getApiUrl(AC.HOME_URL);
        else if (type==1)//mention
            apiUrl = getApiUrl(AC.MENTION_URL);
		else if (type==2)//usertimeline
			apiUrl = getApiUrl(AC.USER_TIMELINE_URL);
        RequestParams params = new RequestParams();
        params.put("count", 10);
        params.put("since_id", 1);
        params.put("page", page);
        params.put("user_id", user_id);
        params.put("screen_name", screen_name);
        client.get(apiUrl, params, handler);
    }

	public void doPost(String text, AsyncHttpResponseHandler handler){
		String apiUrl = getApiUrl(AC.POST_URL);
		RequestParams params = new RequestParams();
		params.put("status", text);
		client.post(apiUrl, params, handler);
	}

	public void getProfile(AsyncHttpResponseHandler handler){
		String apiUrl = getApiUrl(AC.PROFILE_URL);
		client.get(apiUrl, null, handler);
	}

	public void getWhoFollowed(int type, long user_id, String screen_name, AsyncHttpResponseHandler handler){
		String apiUrl = "";
		if (type==0)//followers
			apiUrl = getApiUrl(AC.FOLLOWERS_URL);
		else if (type==1)//following
			apiUrl = getApiUrl(AC.FRIENDS_URL);
        RequestParams params = new RequestParams();
        params.put("user_id", user_id);
        params.put("screen_name", screen_name);
		client.get(apiUrl, params, handler);
	}

	public void getAnotherUserInfo(long user_id, String screen_name, AsyncHttpResponseHandler handler){
		String apiUrl = getApiUrl(AC.USER_URL);
		RequestParams params = new RequestParams();
		params.put("user_id", user_id);
        params.put("screen_name", screen_name);
		client.get(apiUrl, params, handler);
	}

	/* 1. Define the endpoint URL with getApiUrl and pass a relative path to the endpoint
	 * 	  i.e getApiUrl("statuses/home_timeline.json");
	 * 2. Define the parameters to pass to the request (query or body)
	 *    i.e RequestParams params = new RequestParams("foo", "bar");
	 * 3. Define the request method and make a call to the client
	 *    i.e client.get(apiUrl, params, handler);
	 *    i.e client.post(apiUrl, params, handler);
	 */
}