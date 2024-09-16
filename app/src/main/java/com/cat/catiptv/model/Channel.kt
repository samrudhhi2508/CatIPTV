/*
 * Created by Samyak Kamble on 9/16/24, 9:01 PM
 *  Copyright (c) 2024 . All rights reserved.
 *  Last modified 9/16/24, 9:01 PM
 */

package com.cat.catiptv.model

import android.os.Parcel
import android.os.Parcelable

data class Channel(
    val name: String,
    val logoUrl: String,
    val streamUrl: String
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: ""
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeString(logoUrl)
        parcel.writeString(streamUrl)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Channel> {
        override fun createFromParcel(parcel: Parcel): Channel {
            return Channel(parcel)
        }

        override fun newArray(size: Int): Array<Channel?> {
            return arrayOfNulls(size)
        }
    }
}
