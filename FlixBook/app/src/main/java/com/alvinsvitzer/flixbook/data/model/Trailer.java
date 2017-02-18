package com.alvinsvitzer.flixbook.data.model;

import org.parceler.Parcel;

/**
 * Created by Alvin on 2/13/17.
 */

@Parcel
public class Trailer {

    public String mId;
    public String key;
    public String mName;
    public String mSite;
    public String mType;

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getSite() {
        return mSite;
    }

    public void setSite(String site) {
        mSite = site;
    }

    public String getType() {
        return mType;
    }

    public void setType(String type) {
        mType = type;
    }
}
