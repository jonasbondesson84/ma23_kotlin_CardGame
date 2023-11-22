package com.example.cardgame

import android.os.Parcel
import android.os.Parcelable

data class Level (val level: Int, val rules: String, val scoreNeeded: Int, val done: Int, val gameMode: Int): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt() ?: 0,
        parcel.readString() ?: "",
        parcel.readInt() ?: 10,
        parcel.readInt() ?: 0,
        parcel.readInt() ?: 0

    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(level)
        parcel.writeString(rules)
        parcel.writeInt(scoreNeeded)
        parcel.writeInt(done)
        parcel.writeInt(gameMode)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Level> {
        override fun createFromParcel(source: Parcel): Level {
            return Level(source)
        }

        override fun newArray(size: Int): Array<Level?> {
            return arrayOfNulls(size)
        }
    }
}