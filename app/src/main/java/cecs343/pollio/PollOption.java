package cecs343.pollio;

import android.os.Parcel;
import android.os.Parcelable;

public class PollOption implements Parcelable{


    private String text;
    private int votes;

    public PollOption(String text, int votes) {
        this.text = text;
        this.votes = votes;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getVotes() {
        return votes;
    }

    public void setVotes(int votes) {
        this.votes = votes;
    }


    /* PARCELABLE INTERFACE */

    //For parcelable interface
    private PollOption(Parcel in) {
        text = in.readString();
        votes = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(text);
        dest.writeInt(votes);

    }

    public static final Creator<PollOption> CREATOR = new Creator<PollOption>() {
        @Override
        public PollOption createFromParcel(Parcel in) {
            return new PollOption(in);
        }

        @Override
        public PollOption[] newArray(int size) {
            return new PollOption[size];
        }
    };

    /* END INTERFACE */
}
