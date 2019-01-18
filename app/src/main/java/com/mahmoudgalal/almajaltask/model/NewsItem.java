/*
 * Copyright (c) 2019. Mahmoud Galal.
 */

package com.mahmoudgalal.almajaltask.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class NewsItem implements Parcelable {

    @SerializedName("nid")
    public String id;
    public String title;
    @SerializedName("main_img")
    public String mainImg;
    public String details;
    @SerializedName("created_date")
    public String creationDate;
    @SerializedName("section_name")
    public String sectionName;

    private NewsItem(Parcel in) {
        id = in.readString();
        title = in.readString();
        mainImg = in.readString();
        details = in.readString();
        creationDate = in.readString();
        sectionName = in.readString();
    }

    public static final Creator<NewsItem> CREATOR = new Creator<NewsItem>() {
        @Override
        public NewsItem createFromParcel(Parcel in) {
            return new NewsItem(in);
        }

        @Override
        public NewsItem[] newArray(int size) {
            return new NewsItem[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(title);
        parcel.writeString(mainImg);
        parcel.writeString(details);
        parcel.writeString(creationDate);
        parcel.writeString(sectionName);
    }
}
