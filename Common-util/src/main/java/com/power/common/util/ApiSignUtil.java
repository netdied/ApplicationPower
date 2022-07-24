package com.power.common.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Formatter;
import java.util.HashMap;
import java.util.Map;

/**
 * OpenAPI Signature Generation Tool
 * <p>
 * Created by yu on 2017/7/27.
 */
public class ApiSignUtil {

    /**
     * Generate signature
     *
     * @param appID    appid
     * @param SECRET   secret
     * @param nonceStr nonce string
     * @return hash map
     */
    public static Map<String, Object> sign(String appID, String SECRET, String nonceStr) {
        Map<String, Object> ret = new HashMap<>();
        long timestamp = System.currentTimeMillis() / 1000L;
        String signature = ApiSignUtil.getSignature(appID, SECRET, nonceStr, timestamp);
        ret.put("nonceStr", nonceStr);
        ret.put("timestamp", timestamp);
        ret.put("signature", signature);
        ret.put("appId", appID);
        return ret;
    }

    /**
     * generates a signature according to the common parameters of the interface generated by the caller
     *
     * @param appID     appid
     * @param SECRET    secret
     * @param nonceStr  nonce string
     * @param timestamp timestamp of request
     * @return string
     */
    public static String getSignature(String appID, String SECRET, String nonceStr, long timestamp) {
        String string1;
        String signature = "";
        string1 = "app_id=" + appID + "&app_key" + SECRET + "&noncestr=" + nonceStr
                + "&timestamp=" + timestamp;
        try {
            MessageDigest crypt = MessageDigest.getInstance("SHA-1");
            crypt.reset();
            crypt.update(string1.getBytes(StandardCharsets.UTF_8));
            signature = byteToHex(crypt.digest());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return signature;
    }

    /**
     * Byte to hex
     *
     * @param hash byte array
     * @return String
     */
    private static String byteToHex(final byte[] hash) {
        Formatter formatter = new Formatter();
        for (byte b : hash) {
            formatter.format("%02x", b);
        }
        String result = formatter.toString();
        formatter.close();
        return result;
    }
}
