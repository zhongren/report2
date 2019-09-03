package com.newproj.core.encrypt;

import java.security.MessageDigest;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;


public class CryptoUtil {

    private static final String ALGORITHM_SHA_256 = "SHA-256";
    private static final String ALGORITHM_MD5 = "MD5";
    private static final String ALGORITHM_PKCS5PADDING = "AES/CBC/PKCS5Padding";

    public static String sha256Encode(String data) {
        return (data == null || data.isEmpty()) ? null : byteToHex(digest(ALGORITHM_SHA_256, data.getBytes()));
    }

    public static String md5Encode(String data) {
        return (data == null || data.isEmpty()) ? null : byteToHex(digest(ALGORITHM_MD5, data.getBytes()));
    }

    public static String doubleMd5Encode(String data) {
        return (data == null || data.isEmpty()) ? null : md5Encode(md5Encode(data));
    }

    private static String byteToHex(byte[] data) {
        String hex = null;

        if (data != null && data.length > 0) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < data.length; i++)
                sb.append(String.format("%02x", data[i] & 0xFF));

            hex = sb.toString();
        }
        return hex;
    }

    private static byte[] digest(String algorithm, byte[] data) {
        return digest(algorithm, data, 0, data.length);
    }

    private static byte[] digest(String algorithm, byte[] data, int offset, int length) {
        byte[] digest = null;

        if (data != null && data.length > 0 && data.length >= length + offset) {
            try {
                MessageDigest md = MessageDigest.getInstance(algorithm);
                md.update(data, offset, length);
                digest = md.digest();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return digest;
    }

    //
    public static byte[] tokenEncrypt(byte[] data, SecretKey key, byte[] iv) {
        return crypt(ALGORITHM_PKCS5PADDING, Cipher.ENCRYPT_MODE, key, iv, data);
    }

    public static byte[] tokenDecrypt(byte[] data, SecretKey key, byte[] iv) {
        return crypt(ALGORITHM_PKCS5PADDING, Cipher.DECRYPT_MODE, key, iv, data);
    }

    private static byte[] crypt(String algorithm, int mode, SecretKey key, byte[] iv, byte[] data) {
        return crypt(algorithm, mode, key, iv, data, 0, data.length);
    }

    private static byte[] crypt(String algorithm, int mode, SecretKey key, byte[] iv, byte[] data, int offset, int length) {
        byte[] result = null;

        if (data != null && data.length > 0 && data.length >= length + offset) {
            try {
                Cipher cipher = Cipher.getInstance(algorithm);
                cipher.init(mode, key, new IvParameterSpec(iv));
                result = cipher.doFinal(data, offset, length);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    // Functions for xor encrypt & decrypt.
    public static String xorEncrypt(String plainData, String key) {
        String cipherData = null;

        if (plainData != null && !plainData.isEmpty()
                && key != null && !key.isEmpty()) {
            byte[] byteData = plainData.getBytes(), byteKey = key.getBytes();
            for (int i = 0; i < byteData.length; i++)
                byteData[i] ^= byteKey[i % byteKey.length];
            cipherData = Base64.getEncoder().encodeToString(byteData);
        }
        return cipherData;
    }

    public static String xorDecrypt(String cipherData, String key) {
        String plainData = null;

        if (cipherData != null && !cipherData.isEmpty()
                && key != null && !key.isEmpty()) {
            byte[] byteData = Base64.getDecoder().decode(cipherData), byteKey = key.getBytes();
            for (int i = 0; i < byteData.length; i++)
                byteData[i] ^= byteKey[i % byteKey.length];

            plainData = new String(byteData);
        }
        return plainData;
    }

    // Functions for random password.
    public static String generateDigitsPassword(int length) {
        return randomPassword(length, new char[][]{{'0', '9'}});
    }

    public static String generateComplexPassword(int length) {
        return randomPassword(length, new char[][]{{'0', '9'}, {'a', 'z'}, {'A', 'Z'}});
    }

    private static String randomPassword(int length, char[][] dataPool) {
        String random = null;

        if (length > 0 && dataPool != null && dataPool.length > 0) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < length; i++) {
                char[] temp = dataPool[(int) Math.floor(Math.random() * dataPool.length)];
                sb.append((char)(temp[0] + (int) Math.floor(Math.random() * (temp[1] - temp[0] + 1))));
            }
            random = sb.toString();
        }
        return random;
    }
    
    public static void main(String[] args){
    	System.out.println( sha256Encode("111111"));
    }
}
