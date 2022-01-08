package com.hmall.common.dto;

public class UserHolder {
    private static final ThreadLocal<Long> tl = new ThreadLocal<>();

    public static void setUser(Long userId) {
        tl.set(userId);
    }
    public static Long getUser() {
        return tl.get();
    }
    public static void removeUser(){
        tl.remove();
    }
}
