package com.doctoryun.common;

/**
 * Created by admin on 2016/9/24.
 */
public class AuthorityHelper {
    private static AuthorityHelper instance;
    private boolean isQualifyed;//认证


    private AuthorityHelper() {
    }

    public static AuthorityHelper getInstatnce() {
        if (instance == null) {
            instance = new AuthorityHelper();
        }
        return instance;
    }


    public boolean isQualifyed() {
        return Preference.getBoolean(Constant.PREFERENCE_IS_QUALIFYED, false);
    }

    public void setIsQualifyed(boolean isQualifyed) {
        Preference.putBoolean(Constant.PREFERENCE_IS_QUALIFYED, isQualifyed);
        this.isQualifyed = isQualifyed;
    }
}
