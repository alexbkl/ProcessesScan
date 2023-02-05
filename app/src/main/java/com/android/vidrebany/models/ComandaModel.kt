package com.android.vidrebany.models

import android.os.Parcel
import android.os.Parcelable

class ComandaModel(var id: String,
                   var address: String,
                   var albaraNum: String,
                   var clientNum: String,
                   var date: String,
                   var firstTel: String,
                   var secondTel: String,
                   var observations: String,
                   var pdfUrl: String,
                   var status: String,
                   var time: String,
                   var transporterUid: String,
                   var transName: String) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(address)
        parcel.writeString(albaraNum)
        parcel.writeString(clientNum)
        parcel.writeString(date)
        parcel.writeString(firstTel)
        parcel.writeString(secondTel)
        parcel.writeString(observations)
        parcel.writeString(pdfUrl)
        parcel.writeString(status)
        parcel.writeString(time)
        parcel.writeString(transporterUid)
        parcel.writeString(transName)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ComandaModel> {
        override fun createFromParcel(parcel: Parcel): ComandaModel {
            return ComandaModel(parcel)
        }

        override fun newArray(size: Int): Array<ComandaModel?> {
            return arrayOfNulls(size)
        }
    }
}