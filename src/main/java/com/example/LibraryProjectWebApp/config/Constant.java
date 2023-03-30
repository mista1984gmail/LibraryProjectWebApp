package com.example.LibraryProjectWebApp.config;

public class Constant {

    //10 day return period 864_000_000L
    public final static Long BOOK_RETURN_DEADLINE_MS = 864_000_000L;
    public final static int BOOK_RETURN_DEADLINE_DAYS = 10;

    public final static String INIT_DELAY_SCHEDULE = "10000";
    //the reader must take the booking book during 2 days 172800000 ms
    public final static String BOOKING_CHECK_SCHEDULE = "172800000";
    public final static Long BOOK_TAKE_DEADLINE_MS = 172_800_000L;
}
