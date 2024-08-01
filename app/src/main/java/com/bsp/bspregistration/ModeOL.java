package com.bsp.bspregistration;

public class ModeOL {

    public static boolean isOnline = false;


    public static void switchToOnline() {
        // Update the global variable to indicate online state
        isOnline = true;
    }

    public static void switchToOffline() {
        // Update the global variable to indicate offline state
        isOnline = false;
    }

}
