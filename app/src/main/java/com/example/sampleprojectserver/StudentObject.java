package com.example.sampleprojectserver;

public class StudentObject {

    private int    id;
    private String firstName;
    private String lastName;
    private String course;
    private int    score;

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
}
