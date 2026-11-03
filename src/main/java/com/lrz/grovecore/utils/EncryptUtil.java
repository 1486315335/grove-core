package com.lrz.grovecore.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 加密工具类
 *
 * @author lirenzhi
 * @since 2023/08/16
 */
public class EncryptUtil {

    public static final String ZERO = "0";
    public static final String MD_5 = "MD5";
    public static final String SHA1 = "SHA-1";
    public static final String SHA256 = "SHA-256";
    public static final String AES = "AES";
    public static final String DES = "DES";

    /**
     * 编码格式；默认使用uft-8
     */
    public static final String charset = "utf-8";

    /**
     * DES
     */
    public static final int keySizeDES = 0;
    /**
     * AES
     */
    public static final int keySizeAES = 128;

    public static String encrypt_MD5(String password) {
        return encrypt(MD_5, password);
    }

    public static String encrypt_SHA1(String password) {
        return encrypt(SHA1, password);
    }

    public static String encrypt_SHA256(String password) {
        return encrypt(SHA256, password);
    }

    public static String encrypt(String algorithm, String password) {
        if (password == null) {
            return null;
        }
        try {
            MessageDigest messageDigest = MessageDigest.getInstance(algorithm);
            messageDigest.update(password.getBytes());
            return byte2hex(messageDigest.digest());
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public static String byte2hex(byte[] bytes) {
        StringBuilder sign = new StringBuilder();
        for (byte aByte : bytes) {
            String hex = Integer.toHexString(aByte & 0xFF);
            if (hex.length() == 1) {
                sign.append(ZERO);
            }
            sign.append(hex);
        }
        return sign.toString();
    }
}