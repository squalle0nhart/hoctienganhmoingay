/*
package com.squalle0nhart.hoctienganh.database;

import android.util.Base64;
import android.util.Log;

import java.security.MessageDigest;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public final class Decrypt {
    public static boolean f1336a;
    private static final byte[] f1337b;

    static {
        f1337b = new byte[]{(byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0};
        f1336a = false;
    }

    public static String decrypt( String str2) {
        try {
            String str = "bmd1eWVubmd1eWVuZ2lhcA==";
            SecretKeySpec a = Decrypt.m3443a(str);
            Decrypt.m3446b("base64EncodedCipherText", str2);
            byte[] decode = Base64.decode(str2, 2);
            Decrypt.m3444a("decodedCipherText", decode);
            byte[] a2 = Decrypt.m3445a(a, f1337b, decode);
            Decrypt.m3444a("decryptedBytes", a2);
            String str3 = new String(a2, "UTF-8");
            Decrypt.m3446b("message", str3);
            return str3;
        } catch (Exception e) {
            if (f1336a) {
                Log.e("AESCrypt", "UnsupportedEncodingException ", e);
            }
        }
        return "";
    }

    private static String m3442a(byte[] bArr) {
        char[] cArr = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
        char[] cArr2 = new char[(bArr.length * 2)];
        for (int i = 0; i < bArr.length; i++) {
            int i2 = bArr[i] & 255;
            cArr2[i * 2] = cArr[i2 >>> 4];
            cArr2[(i * 2) + 1] = cArr[i2 & 15];
        }
        return new String(cArr2);
    }

    public static SecretKeySpec m3443a(String str) {
        try {
            MessageDigest instance = MessageDigest.getInstance("SHA-256");
            byte[] bytes = str.getBytes("UTF-8");
            instance.update(bytes, 0, bytes.length);
            byte[] digest = instance.digest();
            Decrypt.m3444a("SHA-256 key ", digest);
            return new SecretKeySpec(digest, "AES");
        } catch (Exception e) {
            return null;
        }
    }

    private static void m3444a(String str, byte[] bArr) {
        if (f1336a) {
            Log.d("AESCrypt", str + "[" + bArr.length + "] [" + Decrypt.m3442a(bArr) + "]");
        }
    }

    public static byte[] m3445a(SecretKeySpec secretKeySpec, byte[] bArr, byte[] bArr2) {
        try {
            Cipher instance = Cipher.getInstance("AES/CBC/PKCS7Padding");
            instance.init(2, secretKeySpec, new IvParameterSpec(bArr));
            byte[] doFinal = instance.doFinal(bArr2);
            Decrypt.m3444a("decryptedBytes", doFinal);
            return doFinal;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "".getBytes();
    }

    private static void m3446b(String str, String str2) {
        if (f1336a) {
            Log.d("AESCrypt", str + "[" + str2.length() + "] [" + str2 + "]");
        }
    }
}
*/
