package com.banking.bankingapplication.enums;


import com.banking.bankingapplication.constant.Authorities;

public enum Role {
    ROLE_USER(Authorities.USER_AUTHORITIES),
    ROLE_ADMIN(Authorities.ADMIN_AUTHORITIES);
    private String[] authorities;

    Role(String[] authorities) {
        this.authorities = authorities;
    }

    public String[] getAuthorities() {
        return authorities;
    }
}
