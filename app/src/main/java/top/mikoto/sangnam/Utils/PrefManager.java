package top.mikoto.sangnam.Utils;

import android.content.Context;
import android.content.SharedPreferences;

public class PrefManager {
    private final SharedPreferences pref;
    private final SharedPreferences.Editor editor;
    private final Context _context;

    // shared pref mode
    private final int PRIVATE_MODE = 0;

    // Shared preferences file name
    private static final String PREF_NAME = "sangnam";

    private static final String IS_FIRST_TIME_LAUNCH = "IsFirstTimeLaunch";
    private static final String LATEST_ID = "LatestID";

    public PrefManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void setFirstTimeLaunch(boolean isFirstTime) {
        editor.putBoolean(IS_FIRST_TIME_LAUNCH, isFirstTime);
        editor.commit();
    }

    public boolean isFirstTimeLaunch() {
        return pref.getBoolean(IS_FIRST_TIME_LAUNCH, true);
    }


}
