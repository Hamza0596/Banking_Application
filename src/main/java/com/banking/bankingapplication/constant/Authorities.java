package com.banking.bankingapplication.constant;

import lombok.AllArgsConstructor;

public class Authorities {
    private Authorities(){

    }
    public final static String[] USER_AUTHORITIES={"user:read"};
    public final static String[] ADMIN_AUTHORITIES={"user:read","user:update","user:create","user:delete"};

}
