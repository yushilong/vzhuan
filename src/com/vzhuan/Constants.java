package com.vzhuan;

import android.content.Context;
import android.telephony.TelephonyManager;

/**
 * Created by lscm on 2015/1/7.
 */
public class Constants {
    public static final String HOST = "http://www.dongchengxijiu.net";
    public static boolean DEBUG = false;
    //接口start
    public static final String REGISTER_GETCODE = "/api/app/more?ac=vzhuan&did=" + getDid();
    public static final String REGISTER = "/api/user/regist";
    public static final String REGISTER_CHECK_ACCESS = "/api/user/checkAccess";
    public static final String USERINFO = "/api/user/info";
    public static final String GET_ADS = "/api/adw/ads/android";
    public static final String ALREADY_SUBMIT_REFERRRERINFO = "/api/user/alreadySubmitTheReferrerInfo";
    public static final String SUBMIT_REFERRER = "/api/user/submitReferrer";
    public static final String UPDATE_POINT = "/api/adw/android/callback?adType=22,MIJIFEN";
    public static final String CHECK_UPDATE = "/api/app/version?ac=vzhuan";
    //接口end
    public static final String primary_token_did = "8mStOqOeKdqhcjwpG6AAcHrSdiN5FjOzp";
    public static final String primary_token_uid = "8pIRGOgoknwq7jDkgl2eBXnkdbx8OFPE";
    //
    public static final long time_refresh = 2 * 60 * 1000;

    public static final String getImei(Context context) {
        return ((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
    }

    public static String getDid() {
        String preMd5 = Constants.primary_token_did + Constants.getImei(MainApplication.getInstance()) + Constants.primary_token_did;
        return MD5.getMessageDigest(preMd5.getBytes());
    }
}
