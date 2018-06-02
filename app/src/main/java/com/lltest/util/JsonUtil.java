package com.lltest.util;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * LOG Utilities
 */

public class JsonUtil {

    private static final String TAG = Constants.APP_PREFIX + JsonUtil.class.getSimpleName();

    // TODO: define more json keys here:

    // common response data keys:
    private static final String KEY_DATA = "data";
    private static final String KEY_STATUS = "status";
    private static final String KEY_BODY = "body";
    private static final String KEY_REASON = "reason";
    private static final String KEY_EXTRA = "extra";

    /**
     * check if a string is a valid json string or json array string
     *
     * @param test
     * @return
     */
    public static boolean isValidJsonOrJsonArray(String test) {
        try {
            new JSONObject(test);
        } catch (JSONException ex) {
            // in case JSONArray is valid as well...
            try {
                new JSONArray(test);
            } catch (JSONException ex1) {
                return false;
            }
        }
        return true;
    }

    /**
     * Convert json string to java object
     *
     * @param jsonString
     * @param type
     * @param <T>
     * @return
     */
    public static <T> T fromJsonString(final String jsonString, Class<T> type) {
        if (TextUtils.isEmpty(jsonString)) {
            return null;
        }
        if (!isValidJsonOrJsonArray(jsonString)) {
            return null;
        }
        T result = null;
        try {
            result = new Gson().fromJson(jsonString, type);
        } catch (JsonSyntaxException e) {
            LogUtil.e(TAG, "toJsonString: JsonSyntaxException on fromJson!");
            e.printStackTrace();
        } catch (Exception e) {
            LogUtil.e(TAG, "toJsonString: Exception on fromJson!");
            e.printStackTrace();
        }
        return result;
    }

    /**
     * Convert an object to json string
     *
     * @param object
     * @param <T>
     * @return
     */
    public static <T> String toJsonString(final T object) {
        if (null == object) {
            return null;
        }
        String result = null;
        try {
            result = new Gson().toJson(object);
        } catch (Exception e) {
            LogUtil.e(TAG, "toJsonString: Exception on toJson!");
            e.printStackTrace();
        }
        return result;
    }

    /**
     * Message body is corresponding to CommonData inside CommonResponse defined on server side.
     *
     * @param body
     * @return
     */
    public static JSONObject getDataFromMsgBody(final String body) {
        if (TextUtils.isEmpty(body)) {
            return null;
        }
        if (!isValidJsonOrJsonArray(body)) {
            return null;
        }
        JSONObject bodyJson;
        try {
            bodyJson = new JSONObject(body);
        } catch (JSONException e) {
            LogUtil.e(TAG, "getDataFromMsgBody: failed to generate JSONException from msg body.");
            e.printStackTrace();
            return null;
        }
        //LogUtil.d(TAG, "bodyJson: " + String.valueOf(bodyJson));

        if (!bodyJson.has(KEY_DATA)) {
            LogUtil.w(TAG, "getDataFromMsgBody: no data content from msg body.");
            return null;
        }
        return bodyJson.optJSONObject(KEY_DATA);
    }

    private static JSONObject getInnerBodyFromData(final JSONObject dataJsonObj) {
        if (null == dataJsonObj) {
            LogUtil.e(TAG, "getInnerBodyFromData: null data json from msg body.");
            return null;
        }
        if (!dataJsonObj.has(KEY_BODY)) {
            LogUtil.e(TAG, "getInnerBodyFromData: no body content from msg body.");
            return null;
        }
        return dataJsonObj.optJSONObject(KEY_BODY);
    }

    private static int getInnerStatusFromData(final JSONObject dataJsonObj) {
        if (null == dataJsonObj) {
            LogUtil.e(TAG, "getInnerStatusFromData: null data json from msg body.");
            return -1;
        }
        if (!dataJsonObj.has(KEY_STATUS)) {
            LogUtil.e(TAG, "getInnerStatusFromData: no status content from msg body.");
            return -1;
        }
        return dataJsonObj.optInt(KEY_STATUS);
    }

    private static String getInnerReasonFromData(final JSONObject dataJsonObj) {
        if (null == dataJsonObj) {
            LogUtil.e(TAG, "getInnerReasonFromData: null data json from msg body.");
            return "";
        }
        if (!dataJsonObj.has(KEY_REASON)) {
            LogUtil.e(TAG, "getInnerReasonFromData: no reason content from msg body.");
            return "";
        }
        return dataJsonObj.optString(KEY_REASON);
    }

    private static String getInnerExtraFromData(final JSONObject dataJsonObj) {
        if (null == dataJsonObj) {
            LogUtil.e(TAG, "getInnerExtraFromData: null data json from msg body.");
            return "";
        }
        if (!dataJsonObj.has(KEY_EXTRA)) {
            LogUtil.e(TAG, "getInnerExtraFromData: no extra content from msg body.");
            return "";
        }
        return dataJsonObj.optString(KEY_EXTRA);
    }

    public static JSONObject getInnerBodyFromMsgBody(final String body) {
        JSONObject dataJson = getDataFromMsgBody(body);
        return getInnerBodyFromData(dataJson);
    }

    public static int getInnerStatusFromMsgBody(final String body) {
        JSONObject dataJson = getDataFromMsgBody(body);
        return getInnerStatusFromData(dataJson);
    }

    public static String getInnerReasonFromMsgBody(final String body) {
        JSONObject dataJson = getDataFromMsgBody(body);
        return getInnerReasonFromData(dataJson);
    }

    public static String getInnerExtraFromMsgBody(final String body) {
        JSONObject dataJson = getDataFromMsgBody(body);
        return getInnerExtraFromData(dataJson);
    }

}
