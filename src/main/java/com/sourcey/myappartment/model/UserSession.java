package com.sourcey.myappartment.model;

/**
 * Created by diogo on 28/11/2017.
 */

public class UserSession {

    private static int USER_ID;
    private static String NAME;
    private static String ADDRESS;
    private static String EMAIL;
    private static int MOBILE_NUMBER;

    public static int getUserId() {
        return USER_ID;
    }

    public static void setUserId(int userId) {
        USER_ID = userId;
    }

    public static String getNAME() {
        return NAME;
    }

    public static void setNAME(String NAME) {
        UserSession.NAME = NAME;
    }

    public static String getADDRESS() {
        return ADDRESS;
    }

    public static void setADDRESS(String ADDRESS) {
        UserSession.ADDRESS = ADDRESS;
    }

    public static String getEMAIL() {
        return EMAIL;
    }

    public static void setEMAIL(String EMAIL) {
        UserSession.EMAIL = EMAIL;
    }

    public static int getMobileNumber() {
        return MOBILE_NUMBER;
    }

    public static void setMobileNumber(int mobileNumber) {
        MOBILE_NUMBER = mobileNumber;
    }

}
