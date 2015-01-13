package com.vzhuan;

import java.security.MessageDigest;

public final class MD5 {
    public static final char[] hexDigitalArray = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    public MD5() {
    }

    public static final String getMessageDigest(byte[] var0) {
        try {
            MessageDigest var1 = MessageDigest.getInstance("MD5");
            var1.update(var0);
            var0 = var1.digest();
            int var2 = var0.length;
            char[] var3 = new char[var2 * 2];
            int var4 = 0;

            for(int var5 = 0; var4 < var2; ++var4) {
                byte var6 = var0[var4];
                var3[var5++] = hexDigitalArray[var6 >>> 4 & 15];
                var3[var5++] = hexDigitalArray[var6 & 15];
            }

            return new String(var3);
        } catch (Exception var7) {
            var7.printStackTrace();
            return null;
        }
    }

    public static final byte[] getRawDigest(byte[] var0) {
        try {
            MessageDigest var1 = MessageDigest.getInstance("MD5");
            var1.update(var0);
            return var1.digest();
        } catch (Exception var2) {
            var2.printStackTrace();
            return null;
        }
    }
}
