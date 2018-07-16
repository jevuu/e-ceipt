package com.example.d8.myapplication;

import java.util.ArrayList;
import java.util.List;

/*
    This class is to store all information of users and their receipts
 */
public class Information {
    public static String RECEIPTSLOCALFILENAME = "_receipts.txt";


    public static ArrayList<Receipt> receipts = new ArrayList<Receipt>();
    public static authUser authUser;
    public static Receipt receipt = new Receipt();

    public static ArrayList<String> categories = new ArrayList<String>();

    //public static User user = new User();
}
