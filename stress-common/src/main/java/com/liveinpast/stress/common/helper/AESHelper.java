package com.liveinpast.stress.common.helper;

import org.apache.commons.codec.binary.Base64;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/**
 * @author Live.InPast
 * @date 2020/4/23
 */
public class AESHelper {

    /**
     * 默认的加密算法
     */
    private static final String KEY_ALGORITHM = "AES";

    /**
     * 默认的加密算法
     */
    private static final String DEFAULT_CIPHER_ALGORITHM = "AES/ECB/PKCS5Padding";

    /**
     * AES 加密操作
     *
     * @param content  待加密内容
     * @param password 加密密码
     * @return 返回Base64转码后的加密数据
     */
    public static String encrypt(String content, String password) throws NoSuchPaddingException, NoSuchAlgorithmException, UnsupportedEncodingException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        // 创建密码器
        Cipher cipher = Cipher.getInstance(DEFAULT_CIPHER_ALGORITHM);
        byte[] byteContent = content.getBytes("utf-8");
        //初始化为加密模式的密码器
        cipher.init(Cipher.ENCRYPT_MODE, getSecretKey(password));
        //加密
        byte[] result = cipher.doFinal(byteContent);
        //通过Base64转码返回
        return Base64.encodeBase64String(result);
    }

    /**
     * AES 解密操作
     *
     * @param content
     * @param password
     * @return
     */
    public static String decrypt(String content, String password) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException, UnsupportedEncodingException {
        //实例化
        Cipher cipher = Cipher.getInstance(DEFAULT_CIPHER_ALGORITHM);
        //使用密钥初始化，设置为解密模式
        cipher.init(Cipher.DECRYPT_MODE, getSecretKey(password));
        //执行操作
        byte[] result = cipher.doFinal(Base64.decodeBase64(content));
        return new String(result, "utf-8");
    }

    /**
     * 生成加密秘钥
     *
     * @return
     */
    private static SecretKeySpec getSecretKey(final String password) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        //返回生成指定算法密钥生成器的 KeyGenerator 对象
        KeyGenerator kg = KeyGenerator.getInstance(KEY_ALGORITHM);
        //AES 要求密钥长度为 128
        SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
        secureRandom.setSeed(password.getBytes("utf-8"));
        kg.init(128, secureRandom);
        //生成一个密钥
        SecretKey secretKey = kg.generateKey();
        //转换为AES专用密钥
        return new SecretKeySpec(secretKey.getEncoded(), KEY_ALGORITHM);
    }

    public static void main(String[] args) throws NoSuchPaddingException, BadPaddingException, NoSuchAlgorithmException, IllegalBlockSizeException, UnsupportedEncodingException, InvalidKeyException {
        String content = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ1bmlvbklkIjoiIiwib3BlbklkIjoiIiwiaXNzIjoienN5Iiwic2VydmljZUdyb3VwIjoidGVhY2hlciIsImV4dHJhVXNlcklkIjoiIiwidXNlck5hbWUiOiIiLCJ1c2VySWQiOiIxNzUzNjQwODUzOTc1NTUwNDQ3MCIsInV1aWQiOiI2ODgzODZiYy1lZWFjLTQyZDAtYWZhNy04NmExM2VkM2ZkZjAiLCJhdWQiOiIxNzUzNjQwODUzOTc1NTUwNDQ3MCIsInNjaG9vbElkIjoxMDUsImNsaWVudCI6MSwiZXhwIjoxNjE4NzQwMTczLCJpYXQiOjE1ODc2MzYxNzN9.H6OkIl73g0EYSJ_KMX1H7BvTBkDqkaP2FrGk174brUI";
        String key = "1234";
        System.out.println("content:" + content);
        String s1 = AESHelper.encrypt(content, key);
        System.out.println("s1:" + s1);

        long time1 = System.currentTimeMillis();
        for (int i = 0; i < 1; i++) {
            AESHelper.decrypt(s1, key);
        }
        System.out.println(System.currentTimeMillis() - time1);

    }

}
