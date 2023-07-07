package com.banking.bankingapplication.constant;


public class Authorities {
    private Authorities(){

    }
    public  static final  String[] USER_AUTHORITIES={"user:read"};
    public  static final String[] ADMIN_AUTHORITIES={"user:read","user:update","user:create","user:delete"};

}
