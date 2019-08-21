package kr.ac.kpu.walktogether

import android.os.Parcel
import android.os.Parcelable

/*

사용자 -> 이름, 이메일, 프로필사진, 키, 나이
주치의 -> 이름, 이메일,

위 데이터를 가지는 'Exam'이라는 이름의 Kotlin Class 파일 생성

*/

//여기에 프로필 사진, 키, 나이 추가
class Exam constructor(var name : String, var email:String): Parcelable{

    constructor(parcel:Parcel): this(
        parcel.readString(),
        parcel.readString()) {

    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeString(email)


    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Exam>{

        override fun createFromParcel(parcel: Parcel): Exam {
            return Exam(parcel)
        }

        override fun newArray(size: Int): Array<Exam?> {
            return arrayOfNulls(size)
        }

    }

}