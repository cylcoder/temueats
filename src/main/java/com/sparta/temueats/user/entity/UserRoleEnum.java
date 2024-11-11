package com.sparta.temueats.user.entity;
public enum UserRoleEnum {
    MASTER(Authority.MASTER),
    MANAGER(Authority.MANAGER),
    OWNER(Authority.OWNER),
    CUSTOMER(Authority.CUSTOMER);

    private final String authority;

    UserRoleEnum(String authority) {
        this.authority = authority;
    }

    public String getAuthority() {
        return this.authority;
    }

    public static class Authority {
        public static final String MASTER = "MASTER";
        public static final String MANAGER = "MANAGER";
        public static final String OWNER = "OWNER";
        public static final String CUSTOMER = "CUSTOMER";
    }
}