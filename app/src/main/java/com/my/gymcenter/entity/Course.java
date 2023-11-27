package com.my.gymcenter.entity;

public class Course {
    private int id;
    private String course_name;
    private String course_describe;
    private int coach_id;
    public int get_id() { return id; }
    public void set_id(int id) { this.id = id; }
    public String get_course_name() { return course_name; }
    public void set_course_name(String course_name) { this.course_name = course_name; }
    public String get_course_describe() { return course_describe; }
    public void set_course_describe(String course_describe) { this.course_describe = course_describe; }
    public int get_coach_id() { return coach_id; }
    public void set_coach_id(int coach_id) { this.coach_id = coach_id; }
}
