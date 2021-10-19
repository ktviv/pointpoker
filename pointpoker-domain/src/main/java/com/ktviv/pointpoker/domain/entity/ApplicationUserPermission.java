package com.ktviv.pointpoker.domain.entity;

public enum ApplicationUserPermission {

    POLL_CREATE("poll:create"),
    POLL_JOIN("poll:join"),
    POLL_VOTE("poll:vote"),
    POLL_RESET("poll:reset");

    private final String permission;

    ApplicationUserPermission(String permission) {
        this.permission = permission;
    }

    public String getPermission() {
        return permission;
    }
}
