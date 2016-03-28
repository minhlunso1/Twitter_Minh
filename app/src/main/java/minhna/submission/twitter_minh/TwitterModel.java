package minhna.submission.twitter_minh;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 28-Mar-16.
 */
public class TwitterModel implements Parcelable {
    private String body;
    private long id;
    private UserModel user;


    public static class UserModel implements Parcelable {
        private String name;
        private String profileImageUrl;

        public UserModel(String name, String profileImageUrl) {
            this.name = name;
            this.profileImageUrl = profileImageUrl;
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

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.name);
            dest.writeString(this.profileImageUrl);
        }

        protected UserModel(Parcel in) {
            this.name = in.readString();
            this.profileImageUrl = in.readString();
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
        TwitterModel model = new TwitterModel();
        try {
            model.setBody(jsonObject.getString("text"));
            model.setid(jsonObject.getLong("id"));
            JSONObject userObject = jsonObject.getJSONObject("user");
            model.setUser(new UserModel(userObject.getString("name"), userObject.getString("profile_image_url")));
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.body);
        dest.writeLong(this.id);
        dest.writeParcelable(this.user, flags);
    }

    public TwitterModel() {
    }

    protected TwitterModel(Parcel in) {
        this.body = in.readString();
        this.id = in.readLong();
        this.user = in.readParcelable(UserModel.class.getClassLoader());
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
