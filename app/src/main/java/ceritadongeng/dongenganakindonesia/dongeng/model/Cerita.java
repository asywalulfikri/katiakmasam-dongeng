package ceritadongeng.dongenganakindonesia.dongeng.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Cerita implements Parcelable {

    public String id;
    public String title;
    public String content;
    public String type;
    public String createdAt;
    public String image1;
    public String image2;
    public String image3;
    public String sumber;
    public String category;
    public String languange;


    public Cerita() {}

    public Cerita(Parcel in) {
        id              = in.readString();
        title           = in.readString();
        content         = in.readString();
        type            = in.readString();
        createdAt       = in.readString();
        image1          = in.readString();
        image2          = in.readString();
        image3          = in.readString();
        sumber          = in.readString();
        category        = in.readString();
        languange       = in.readString();
    }

    public static final Parcelable.Creator<Cerita> CREATOR = new Parcelable.Creator<Cerita>() {

        @Override
        public Cerita createFromParcel(Parcel in) {

            return new Cerita(in);
        }

        @Override
        public Cerita[] newArray(int size) {

            return new Cerita[size];
        }

    };

    @Override
    public int describeContents() {

        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {

        out.writeString(id);
        out.writeString(title);
        out.writeString(content);
        out.writeString(type);
        out.writeString(createdAt);
        out.writeString(image1);
        out.writeString(image2);
        out.writeString(image3);
        out.writeString(sumber);
        out.writeString(category);
        out.writeString(languange);
    }
}
