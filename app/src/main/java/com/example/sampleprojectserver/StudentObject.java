package com.example.sampleprojectserver;

import android.os.Parcel;
import android.os.Parcelable;

import org.jetbrains.annotations.NotNull;

public class StudentObject implements Parcelable {

    private int    id;
    private String firstName;
    private String lastName;
    private String course;
    private int    score;

    public static final Parcelable.Creator<StudentObject> CREATOR = new Parcelable.Creator<StudentObject>() {
        @Override
        public StudentObject createFromParcel(Parcel source) {return new StudentObject(source);}

        @Override
        public StudentObject[] newArray(int size) {return new StudentObject[size];}
    };

    public StudentObject() {}

    protected StudentObject(@NotNull Parcel in) {
        this.id        = in.readInt();
        this.firstName = in.readString();
        this.lastName  = in.readString();
        this.course    = in.readString();
        this.score     = in.readInt();
    }

    public int getScore()                      { return score;}

    public void setScore(int score)            { this.score = score;}

    public String getCourse()                  { return course;}

    public void setCourse(String course)       { this.course = course;}

    public String getLastName()                { return lastName;}

    public void setLastName(String lastName)   { this.lastName = lastName;}

    public String getFirstName()               { return firstName;}

    public void setFirstName(String firstName) { this.firstName = firstName;}

    public int getId()                         { return id;}

    public void setId(int id)                  { this.id = id;}

    @Override
    public int describeContents() { return 0; }

    @Override
    public void writeToParcel(@NotNull Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.firstName);
        dest.writeString(this.lastName);
        dest.writeString(this.course);
        dest.writeInt(this.score);
    }
}
