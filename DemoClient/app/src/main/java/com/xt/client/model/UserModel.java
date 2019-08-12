package com.xt.client.model;

import android.os.Parcel;
import android.os.Parcelable;


public class UserModel implements Parcelable {

    public String name;
    public int pid;
    public String processName;

    public UserModel() {
    }

    protected UserModel(Parcel in) {
        name = in.readString();
        pid = in.readInt();
        processName = in.readString();
    }

    public static final Creator<UserModel> CREATOR = new Creator<UserModel>() {
        @Override
        public UserModel createFromParcel(Parcel in) {

            return new UserModel(in);
        }

        @Override
        public UserModel[] newArray(int size) {
            return new UserModel[size];
        }
    };

    @Override
    public int describeContents() {
        return Parcelable.CONTENTS_FILE_DESCRIPTOR;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeInt(pid);
        dest.writeString(processName);
    }
}
