package gspot.com.sportify.utils;

import com.firebase.client.Firebase;

import java.util.ArrayList;
import java.util.List;

import gspot.com.sportify.Model.Gathering;
import gspot.com.sportify.Model.SportTypes;

/**
 * Created by yunfanyang on 5/2/16.
 */
public class App {
    public static Firebase dbref = new Firebase(Constants.FIREBASE_URL);
    public static List<Gathering> mGatherings = new ArrayList<>();
    //public static List<String> mSportTypes = new SportTypes().getSportTypes();
    /* current gathering selected*/
    public static Gathering mCurrentGathering = null;

    /*filter: show based on skill level*/
    public static boolean [] mCurrentSkillLevels = { false, false, false };
    /*Match my availability*/
    public static boolean mMatch_My_Availability;
    /*filter: show closed events*/
    public static boolean mIsPrivateEvent;
}