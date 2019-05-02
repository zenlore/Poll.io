package cecs343.pollio;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.widget.RadioGroup;

import java.util.ArrayList;
import java.util.HashMap;

public class PollItem implements Parcelable {

    private int pollID;
    private String title;
    private ArrayList<PollOption> options;
    private int voted;
    private boolean favorited;
    private double latitude;
    private double longitude;

    public PollItem(String title, boolean favorited, int id) {
        pollID = id;
        this.title = title;
        this.options = new ArrayList<PollOption>();
        this.favorited = favorited;
        voted = -1;
    }

    public HashMap<String, String> getArgs() {
        HashMap<String, String> args = new HashMap<>();

        args.put("title", title);
        args.put("latitude", Double.toString(latitude));
        args.put("longitude", Double.toString(longitude));

        String optionString = "";
        for (PollOption po : options) {
            optionString += po.getText() + ",";
        }
        optionString = optionString.substring(0, optionString.length() - 1);

        args.put("options", optionString);

        return args;
    }

    public int getTotalVotes() {
        int total = 0;
        for(PollOption po : options) {
            total += po.getVotes();;
        }
        return total;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
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

    public int getPollID() {
        return pollID;
    }

    public void checkVoted(String vote) {
        int count = 0;
        for(PollOption option : options) {
            if (option.getText().equals(vote)) {
                voted = count;
            }
            count++;
        }
    }

    public void setOptions(ArrayList<PollOption> opts){
        options = opts;
    }

    public void addPollOption(PollOption opt) {
        options.add(opt);
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
        pollID = in.readInt();
        voted = in.readInt();
        favorited = in.readByte() == 0;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeList(options);
        dest.writeInt(pollID);
        dest.writeInt(voted);
        dest.writeByte((byte)(favorited ? 1 : 0));
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
