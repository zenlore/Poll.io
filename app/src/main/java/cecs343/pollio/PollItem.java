package cecs343.pollio;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.widget.RadioGroup;

import java.util.ArrayList;

public class PollItem implements Parcelable {

    private String title;
    private ArrayList<PollOption> options;
    private int voted;
    private boolean favorited;

    public PollItem(String title, boolean favorited) {
        this.title = title;
        this.options = new ArrayList<PollOption>();
        this.favorited = favorited;
        voted = -1;
    }

    public boolean toggleFavorited() {
        favorited = !favorited;
        return favorited;
    }

    public boolean getFavorited() {
        return favorited;
    }

    public int getVoted() {
        return voted;
    }

    //Method to cast a vote for a specific option index
    public void vote(int voted) {
        this.voted = voted;
    }

    public String getTitle() {
        return title;
    }

    public int getNumOptions() { return options.size(); }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setOptions(ArrayList<PollOption> opts){
        options = opts;
    }

    public ArrayList<PollOption> getOptions() {
        return options;
    }

    public void setupRadiogroup(Context context, RadioGroup rg) {

    }

    /* PARCELABLE INTERFACE */

    //For parcelable interface
    private PollItem(Parcel in) {
        title = in.readString();
        in.readList(options, PollOption.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeList(options);
    }

    public static final Creator<PollItem> CREATOR = new Creator<PollItem>() {
        @Override
        public PollItem createFromParcel(Parcel in) {
            return new PollItem(in);
        }

        @Override
        public PollItem[] newArray(int size) {
            return new PollItem[size];
        }
    };

    /* END INTERFACE */
}
