package com.my.gymcenter.entity;

public class Notice {
    private int id;
    private String notice_content;
	public int get_id() {
        return id;
    }

    public void set_id(int courseId) {
        this.id = id;
    }

    public String get_notice_content() {
        return notice_content;
    }

    public void set_notice_content(String notice_content) {
        this.notice_content = notice_content;
    }

}
