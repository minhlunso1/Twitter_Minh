package minhna.submission.twitter_minh;

/**
 * Created by Administrator on 28-Mar-16.
 */
public class AC {
    public static final String CONSUMER_KEY = "tDbfayAz4hNrlGpN107mzSHBT";
    public static final String CONSUMER_SECRET = "EKHkan8qLAfuhmdyHfHew7VoBrfOHi3eH8KBrrlGkEa61Mq9OP";
    public static long owner_id = 0;
    public static String username = "";
    public static String profile_img_url = "";

    public static final String BASE_URL = "https://api.twitter.com/1.1";
    public static final String HOME_URL = "/statuses/home_timeline.json";
    public static final String POST_URL = "statuses/update.json";
    public static final String PROFILE_URL = "/account/verify_credentials.json";
    public static final String MENTION_URL = "/statuses/mentions_timeline.json";
}
