package in.collectiva.tailoringordertracking.CommonFunction;

import java.util.HashMap;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import in.collectiva.tailoringordertracking.Login;

public class SessionManagement {
    // Shared Preferences
    SharedPreferences pref;

    // Editor for Shared preferences
    Editor editor;

    // Context
    Context _context;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Sharedpref file name
    private static final String PREF_NAME = "TOTSession";

    // All Shared Preferences Keys
    private static final String IS_LOGIN = "IsLoggedIn";
    public static final String KEY_KEEP_ME_LOGGED_IN = "KeepMeLoggedIn";

    // User name (make variable public to access from outside)
    public static final String KEY_NAME = "Name";
    public static final String KEY_MOBILENO = "MobileNo";
    public static final String KEY_SHOPNAME = "ShopName";
    public static final String KEY_USERID = "UserId";

    // Constructor
    public SessionManagement(Context context){
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    /**
     * Create login session
     * */
    public void createLoginSession(int UserId, String Name, String MobileNo, String ShopName, Boolean IsKeepMeLoggedIn){
        // Storing login value as TRUE
        editor.putBoolean(IS_LOGIN, true);

        // Storing Id in pref
        editor.putInt(KEY_USERID, UserId);

        // Storing name in pref
        editor.putString(KEY_NAME, Name);

        // Storing email in pref
        editor.putString(KEY_MOBILENO, MobileNo);

        editor.putString(KEY_SHOPNAME,  ShopName);

        if(IsKeepMeLoggedIn)
            editor.putString(KEY_KEEP_ME_LOGGED_IN, "Yes");
        else
            editor.putString(KEY_KEEP_ME_LOGGED_IN, "No");

        // commit changes
        editor.commit();
    }

    /**
     * Check login method wil check user login status
     * If false it will redirect user to login page
     * Else won't do anything
     * */
    public void checkLogin(){
        // Check login status
        if(!this.isLoggedIn()){
            // user is not logged in redirect him to Login Activity
            Intent i = new Intent(_context, Login.class);
            // Closing all the Activities
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            // Add new Flag to start new Activity
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            // Staring Login Activity
            _context.startActivity(i);
        }

    }


    /**
     * Get stored session data
     * */
    public HashMap<String, String> getUserDetails(){
        HashMap<String, String> user = new HashMap<String, String>();

        // user id
        user.put(KEY_USERID, String.valueOf(pref.getInt(KEY_USERID, 0)));

        // user name
        user.put(KEY_NAME, pref.getString(KEY_NAME, null));

        // Mobile NO
        user.put(KEY_MOBILENO, pref.getString(KEY_MOBILENO, null));

        // Mobile NO
        user.put(KEY_SHOPNAME, pref.getString(KEY_SHOPNAME, null));

        // Mobile NO
        //user.put(KEY_KEEP_ME_LOGGED_IN, String.valueOf(pref.getBoolean(KEY_KEEP_ME_LOGGED_IN, Boolean.FALSE)));
        user.put(KEY_KEEP_ME_LOGGED_IN, pref.getString(KEY_KEEP_ME_LOGGED_IN, "No"));

        // return user
        return user;
    }

    /**
     * Clear session details
     * */
    public void logoutUser(){
        // Clearing all data from Shared Preferences
        editor.clear();
        editor.commit();

        // After logout redirect user to Loing Activity
        Intent i = new Intent(_context, Login.class);
        // Closing all the Activities
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        // Add new Flag to start new Activity
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        // Staring Login Activity
        _context.startActivity(i);
    }

    /**
     * Quick check for login
     * **/
    // Get Login State
    public boolean isLoggedIn(){
        return pref.getBoolean(IS_LOGIN, false);
    }
}
