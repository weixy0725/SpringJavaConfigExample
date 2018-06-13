package com.example.web.controller;
/**
 * Created by zhangchong on 2017/8/10.
 */
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;
import java.security.MessageDigest;
import com.google.gson.Gson;


public class AES {

    public static class User {

        public String user_id;
        public int timestamp;
    }


    // 加密
    public static String Encrypt(User one_user, String sKey) throws Exception {

        try {
            java.lang.reflect.Field field = Class.forName("javax.crypto.JceSecurity").getDeclaredField("isRestricted");
            field.setAccessible(true);
            field.set(null, java.lang.Boolean.FALSE);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        Gson gson = new Gson();
        String sSrc = gson.toJson(one_user);

        MessageDigest sha = MessageDigest.getInstance("SHA-256");
        sha.update(sKey.getBytes());
        byte[] raw = sha.digest();

        byte[] raw_half = new byte[16];
        System.arraycopy(raw,0,raw_half,0,16);

        MessageDigest iv_init = MessageDigest.getInstance("MD5");
        iv_init.update(sSrc.getBytes("UTF-8"));
        byte[] iv_raw = iv_init.digest();


        SecretKeySpec skeySpec = new SecretKeySpec(raw_half, "AES");
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");//"算法/模式/补码方式"
        IvParameterSpec iv = new IvParameterSpec(iv_raw);//使用CBC模式，需要一个向量iv，可增加加密算法的强度
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
        byte[] encrypted = cipher.doFinal(sSrc.getBytes("UTF-8"));

        byte[] encrypted_sum = new byte[encrypted.length+16];
        System.arraycopy(iv_raw, 0, encrypted_sum, 0, 16);
        System.arraycopy(encrypted, 0, encrypted_sum, 16, encrypted.length);


        String result = new BASE64Encoder().encode(encrypted_sum).toString().trim().replace("+","-").replace("/","_").replace("\r","").replace("\n","");//此处使用BASE64做转码功能，同时能起到2次加密的作用。
        while (result.endsWith("=")){
            result = result.substring(0, result.length()-1);
        }
        return result;
    }

    // 解密
    public static User Decrypt(String sSrc, String sKey) throws Exception {

        try {
            java.lang.reflect.Field field = Class.forName("javax.crypto.JceSecurity").getDeclaredField("isRestricted");
            field.setAccessible(true);
            field.set(null, java.lang.Boolean.FALSE);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        try {

            int trim_number = (4 - sSrc.length()%4)%4;
            String trim_str="";
            for(int i=0;i<trim_number;i++){
                trim_str += "=";
            }
            sSrc = (sSrc+trim_str).replace("_", "/").replace("-", "+");

            MessageDigest sha = MessageDigest.getInstance("SHA-256");
            sha.update(sKey.getBytes());
            byte[] raw = sha.digest();

            byte[] raw_half = new byte[16];
            System.arraycopy(raw,0,raw_half,0,16);

            byte[] encrypted_init = new BASE64Decoder().decodeBuffer(sSrc);//先用base64解密

            byte[] iv_raw = new byte[16];
            System.arraycopy(encrypted_init,0,iv_raw, 0, 16);

            byte[] encrypted=new byte[encrypted_init.length-16];
            System.arraycopy(encrypted_init,16,encrypted,0,encrypted_init.length-16);

            SecretKeySpec skeySpec = new SecretKeySpec(raw_half, "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            IvParameterSpec iv = new IvParameterSpec(iv_raw);
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);

            try {
                byte[] original = cipher.doFinal(encrypted);
                String originalString = new String(original,"UTF-8");
                Gson gson = new Gson();
                User one_user = gson.fromJson(originalString,User.class);
                return one_user;
            } catch (Exception e) {
                System.out.println(e.toString());
                return null;
            }
        } catch (Exception ex) {
            System.out.println(ex.toString());
            return null;
        }
    }

    public static void main(String[] args) throws Exception {

        User one_user = new AES.User();
        one_user.user_id = "1";
        one_user.timestamp = 1479770710;

        String cKey = "5a2a3805c4536d60e0a22f28";

        // 加密
        long lStart = System.currentTimeMillis();
        String enString = AES.Encrypt(one_user, cKey);
        System.out.println(enString.length());
        System.out.println("加密后的字串是：" + enString);

        long lUseTime = System.currentTimeMillis() - lStart;
        System.out.println("加密耗时：" + lUseTime + "毫秒");

        // 解密
        lStart = System.currentTimeMillis();
        one_user = AES.Decrypt(enString, cKey);
        System.out.println("解密后的字串是：" + new Gson().toJson(one_user));
        lUseTime = System.currentTimeMillis() - lStart;
        System.out.println("解密耗时：" + lUseTime + "毫秒");
    }
}
