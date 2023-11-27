package com.my.gymcenter.entity;

public class ResponseData<T> {
    private String result;
    private T data;
    public T get_data() {
        return data;
    }
    public String get_result() {
        return result;
    }
}
