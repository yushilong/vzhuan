package com.vzhuan;

public class JsonHelper
{
    public enum JSON_TYPE
    {
        /**JSONObject*/
        JSON_TYPE_OBJECT,
        /**JSONArray*/
        JSON_TYPE_ARRAY,
        /**不是JSON格式的字符串*/
        JSON_TYPE_ERROR
    }

    public static JSON_TYPE getJSONType(String str)
    {
        char[] strChar = str.substring(0, 1).toCharArray();
        char firstChar = strChar[0];
        if (firstChar == '{')
        {
            return JSON_TYPE.JSON_TYPE_OBJECT;
        }
        else if (firstChar == '[')
        {
            return JSON_TYPE.JSON_TYPE_ARRAY;
        }
        else
        {
            return JSON_TYPE.JSON_TYPE_ERROR;
        }
    }
}
