package com.common.ui.model;

import android.os.Parcel;
import android.os.Parcelable;

public class GuideModel implements Parcelable {

    public int[] imgResourceIds;
    public String className;
    public boolean isShowDots;
    public String jumpButtonText;
    public int jumpButtonResId;

    protected GuideModel(Parcel in) {
        className = in.readString();
        imgResourceIds = new int[in.readInt()];
        in.readIntArray(imgResourceIds);
        isShowDots = in.readByte() != 0;
        jumpButtonText = in.readString();
        jumpButtonResId = in.readInt();
    }

    public GuideModel() {
    }

    public static final Creator<GuideModel> CREATOR = new Creator<GuideModel>() {
        @Override
        public GuideModel createFromParcel(Parcel in) {
            return new GuideModel(in);
        }

        @Override
        public GuideModel[] newArray(int size) {
            return new GuideModel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(className);
        dest.writeInt(imgResourceIds.length);
        dest.writeIntArray(imgResourceIds);
        dest.writeByte((byte) (isShowDots ? 1 : 0));
        dest.writeString(jumpButtonText);
        dest.writeInt(jumpButtonResId);
    }

}
