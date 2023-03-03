package com.android.vidrebany.models

import android.os.Parcel
import android.os.Parcelable

class ServeiTecnicModel(
    var key: String,
    var actionDate: Long,
    var albaraFile: String,
    var albaraFileName: String,
    var albaraNumber: String,
    var albaraType: String,
    var codeDistributor: String,
    var currentDate: Long,
    var description: String,
    var documents: ArrayList<String>,
    var documentsNames: ArrayList<String>,
    var emailDistributor: String,
    var finalClientAddress: String,
    var finalClientName: String,
    var finalClientPhone: String,
    var isMesura: Boolean,
    var nameDistributor: String,
    var tecnicName: String,
    var tecnicId: String,
    var comentarisTecnic: String?,
    var stateServei: String) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readLong(),
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readLong(),
        parcel.readString()!!,
        parcel.createStringArrayList()!!,
        parcel.createStringArrayList()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readByte() != 0.toByte(),
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString(),
        parcel.readString()!!
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(key)
        parcel.writeLong(actionDate)
        parcel.writeString(albaraFile)
        parcel.writeString(albaraFileName)
        parcel.writeString(albaraNumber)
        parcel.writeString(albaraType)
        parcel.writeString(codeDistributor)
        parcel.writeLong(currentDate)
        parcel.writeString(description)
        parcel.writeStringList(documents)
        parcel.writeStringList(documentsNames)
        parcel.writeString(emailDistributor)
        parcel.writeString(finalClientAddress)
        parcel.writeString(finalClientName)
        parcel.writeString(finalClientPhone)
        parcel.writeByte(if (isMesura) 1 else 0)
        parcel.writeString(nameDistributor)
        parcel.writeString(tecnicName)
        parcel.writeString(tecnicId)
        parcel.writeString(comentarisTecnic)
        parcel.writeString(stateServei)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ServeiTecnicModel> {
        override fun createFromParcel(parcel: Parcel): ServeiTecnicModel {
            return ServeiTecnicModel(parcel)
        }

        override fun newArray(size: Int): Array<ServeiTecnicModel?> {
            return arrayOfNulls(size)
        }
    }
}