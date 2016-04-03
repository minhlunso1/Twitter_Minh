package minhna.submission.twitter_minh;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import minhna.submission.twitter_minh.utils.ParseRelativeData;

/**
 * Created by Administrator on 28-Mar-16.
 */
public class TwitterModel implements Parcelable {
    private String body;
    private long id;
    private UserModel user;
    private String createdTime;

    public static class UserModel implements Parcelable {
        private long owner_id;
        private String name;
        private String profileImageUrl;
        private String profileBackgroundImageUrl;
        private long follwers_count;
        private long following_count;
        private String tagline;

        public UserModel(long userId, String name, String profileImageUrl) {
            this.owner_id = userId;
            this.name = name;
            this.profileImageUrl = profileImageUrl;
        }

        public UserModel(long owner_id, String name, String profileImageUrl, String profileBackgroundImageUrl, long follwers_count, long following_count, String tagline) {
            this.owner_id = owner_id;
            this.name = name;
            this.profileImageUrl = profileImageUrl;
            this.profileBackgroundImageUrl = profileBackgroundImageUrl;
            this.follwers_count = follwers_count;
            this.following_count = following_count;
            this.tagline = tagline;
        }

        public static UserModel fromJSON(JSONObject jsonObject){
            try {
                return new UserModel(jsonObject.getLong("id"), jsonObject.getString("screen_name"), jsonObject.getString("profile_image_url"), jsonObject.getString("profile_background_image_url"), jsonObject.getLong("followers_count"), jsonObject.getLong("friends_count"), jsonObject.getString("description"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        public static List<UserModel> getFollowListfromJSON (JSONObject jsonObject){
            List<UserModel> userModels = new ArrayList<>();
            try {
                JSONArray users = jsonObject.getJSONArray("users");
                for (int i=0; i<users.length();i++) {
                    JSONObject user = users.getJSONObject(i);
                    UserModel userModel = new UserModel(user.getLong("id"), user.getString("screen_name"), user.getString("profile_image_url"));
                    userModels.add(userModel);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return userModels;
        }

        public long getOwner_id() {
            return owner_id;
        }

        public void setOwner_id(long owner_id) {
            this.owner_id = owner_id;
        }

        public String getProfileBackgroundImageUrl() {
            return profileBackgroundImageUrl;
        }

        public void setProfileBackgroundImageUrl(String profileBackgroundImageUrl) {
            this.profileBackgroundImageUrl = profileBackgroundImageUrl;
        }

        public long getFollwers_count() {
            return follwers_count;
        }

        public void setFollwers_count(long follwers_count) {
            this.follwers_count = follwers_count;
        }

        public long getFollowing_count() {
            return following_count;
        }

        public void setFollowing_count(long following_count) {
            this.following_count = following_count;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getProfileImageUrl() {
            return profileImageUrl;
        }

        public void setProfileImageUrl(String profileImageUrl) {
            this.profileImageUrl = profileImageUrl;
        }

        public String getTagline() {
            return tagline;
        }

        public void setTagline(String tagline) {
            this.tagline = tagline;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeLong(this.owner_id);
            dest.writeString(this.name);
            dest.writeString(this.profileImageUrl);
            dest.writeString(this.profileBackgroundImageUrl);
            dest.writeLong(this.follwers_count);
            dest.writeLong(this.following_count);
            dest.writeString(this.tagline);
        }

        protected UserModel(Parcel in) {
            this.owner_id = in.readLong();
            this.name = in.readString();
            this.profileImageUrl = in.readString();
            this.profileBackgroundImageUrl = in.readString();
            this.follwers_count = in.readLong();
            this.following_count = in.readLong();
            this.tagline =in.readString();
        }

        public static final Creator<UserModel> CREATOR = new Creator<UserModel>() {
            @Override
            public UserModel createFromParcel(Parcel source) {
                return new UserModel(source);
            }

            @Override
            public UserModel[] newArray(int size) {
                return new UserModel[size];
            }
        };
    }

    public static TwitterModel getTwitterModel(JSONObject jsonObject){
        Log.d("debug", "json:"+jsonObject.toString());
        TwitterModel model = new TwitterModel();
        try {
            model.setBody(jsonObject.getString("text"));
            model.setid(jsonObject.getLong("id"));
            model.setCreatedTime(ParseRelativeData.getRelativeTimeAgo(jsonObject.getString("created_at")));
            JSONObject userObject = jsonObject.getJSONObject("user");
            model.setUser(new UserModel(userObject.getLong("id"), userObject.getString("screen_name"), userObject.getString("profile_image_url")));
        } catch (JSONException e){
            e.printStackTrace();
        }
        return model;
    }

    public static TwitterModel getTwitterModel(JSONObject jsonObject, String status){
        TwitterModel model = new TwitterModel();
        try {
            model.setBody(status);
            model.setid(jsonObject.getLong("id"));
            model.setCreatedTime(ParseRelativeData.getRelativeTimeAgo(jsonObject.getString("created_at")));
            JSONObject userObject = jsonObject.getJSONObject("user");
            model.setUser(new UserModel(userObject.getLong("id"), userObject.getString("screen_name"), userObject.getString("profile_image_url")));
        } catch (JSONException e){
            e.printStackTrace();
        }
        return model;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public long getid() {
        return id;
    }

    public void setid(long id) {
        this.id = id;
    }

    public UserModel getUser() {
        return user;
    }

    public void setUser(UserModel user) {
        this.user = user;
    }

    public String getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(String createdTime) {
        this.createdTime = createdTime;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.body);
        dest.writeLong(this.id);
        dest.writeParcelable(this.user, flags);
        dest.writeString(this.createdTime);
    }

    public TwitterModel() {
    }

    protected TwitterModel(Parcel in) {
        this.body = in.readString();
        this.id = in.readLong();
        this.user = in.readParcelable(UserModel.class.getClassLoader());
        this.createdTime = in.readString();
    }

    public static final Creator<TwitterModel> CREATOR = new Creator<TwitterModel>() {
        @Override
        public TwitterModel createFromParcel(Parcel source) {
            return new TwitterModel(source);
        }

        @Override
        public TwitterModel[] newArray(int size) {
            return new TwitterModel[size];
        }
    };
}
