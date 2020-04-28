package com.liveinpast.stress.common.helper;


import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Live.InPast on 2018/11/16.
 */
public class RSAHelper {

    /**
     * String to hold name of the encryption algorithm.
     */
    public static final String ALGORITHM = "RSA";

    /**
     * Charset
     */

    private static final Charset UTF_8 = Charset.forName("utf-8");

    /**
     * 密钥长度
     */
    public static final int KEY_SIZE = 1024;

    /**
     * RSA最大加密明文大小
     */
    private static final int MAX_ENCRYPT_BLOCK = 117;

    /** *//**
     * RSA最大解密密文大小
     */
    private static final int MAX_DECRYPT_BLOCK = 128;


    public static Map<String, String> createKeys(int keySize) throws NoSuchAlgorithmException {
        SecureRandom sr = new SecureRandom();
        //为RSA算法创建一个KeyPairGenerator对象
        KeyPairGenerator kpg = KeyPairGenerator.getInstance(ALGORITHM);
        //初始化KeyPairGenerator对象,密钥长度
        kpg.initialize(keySize,sr);
        //生成密匙对
        KeyPair keyPair = kpg.generateKeyPair();
        //得到公钥
        String publicKeyStr = new String(Base64.getEncoder().encode(keyPair.getPublic().getEncoded()),UTF_8);
        //得到私钥
        String privateKeyStr = new String(Base64.getEncoder().encode(keyPair.getPrivate().getEncoded()),UTF_8);
        Map<String, String> keyPairMap = new HashMap<>();
        keyPairMap.put("publicKey", publicKeyStr);
        keyPairMap.put("privateKey", privateKeyStr);
        return keyPairMap;
    }

    /**
     * 得到公钥
     * @param publicKey 密钥字符串（经过base64编码）
     * @throws Exception
     */
    public static RSAPublicKey getPublicKey(String publicKey) throws NoSuchAlgorithmException, InvalidKeySpecException {
        //通过X509编码的Key指令获得公钥对象
        KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(Base64.getDecoder().decode(publicKey));
        return (RSAPublicKey) keyFactory.generatePublic(x509KeySpec);
    }

    /**
     * 得到私钥
     * @param privateKey 密钥字符串（经过base64编码）
     * @throws Exception
     */
    public static RSAPrivateKey getPrivateKey(String privateKey) throws NoSuchAlgorithmException, InvalidKeySpecException {
        //通过PKCS#8编码的Key指令获得私钥对象
        KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(Base64.getDecoder().decode(privateKey));
        return (RSAPrivateKey) keyFactory.generatePrivate(pkcs8KeySpec);
    }

    /**
     * 公钥加密
     * @param data
     * @param publicKey
     * @return
     */
    public static String publicEncrypt(String data, RSAPublicKey publicKey) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException, IOException {
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        return new String(Base64.getEncoder().encode(block(cipher,data.getBytes(UTF_8),MAX_ENCRYPT_BLOCK)),UTF_8);
    }

    /**
     * 私钥解密
     * @param data
     * @param privateKey
     * @return
     */

    public static String privateDecrypt(String data, RSAPrivateKey privateKey) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException, IOException {
        byte[] decode = Base64.getDecoder().decode(data.getBytes(UTF_8));
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        return new String(block(cipher,decode,MAX_DECRYPT_BLOCK),UTF_8);
    }

    /**
     * 私钥加密
     * @param data
     * @param privateKey
     * @return
     */

    public static String privateEncrypt(String data, RSAPrivateKey privateKey) throws InvalidKeyException, NoSuchPaddingException, NoSuchAlgorithmException, BadPaddingException, IllegalBlockSizeException, IOException {
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, privateKey);
        return new String(Base64.getEncoder().encode(block(cipher,data.getBytes(UTF_8),MAX_ENCRYPT_BLOCK)),UTF_8);
    }

    /**
     * 公钥解密
     * @param data
     * @param publicKey
     * @return
     */

    public static String publicDecrypt(String data, RSAPublicKey publicKey) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException, IOException {
        byte[] decode = Base64.getDecoder().decode(data.getBytes(UTF_8));
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, publicKey);
        return new String(block(cipher,decode,MAX_DECRYPT_BLOCK),UTF_8);
    }


    /**
     * 对数据分段加密
     */
    private static byte[] block(Cipher cipher,byte[] data,int maxBlock) throws BadPaddingException, IllegalBlockSizeException, IOException {
        int dataLength = data.length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offSet = 0;
        byte[] cache;
        int i = 0;
        while (dataLength - offSet > 0) {
            if (dataLength - offSet > maxBlock) {
                cache = cipher.doFinal(data, offSet, maxBlock);
            } else {
                cache = cipher.doFinal(data, offSet, dataLength - offSet);
            }
            out.write(cache, 0, cache.length);
            offSet = (++i) * maxBlock;
        }
        byte[] byteData = out.toByteArray();
        out.close();
        return byteData;
    }

    public static void main(String[] args) throws InvalidKeySpecException, NoSuchAlgorithmException, IllegalBlockSizeException, InvalidKeyException, BadPaddingException, NoSuchPaddingException, IOException, NoSuchProviderException {
        long time1 = System.currentTimeMillis();
        Map<String, String> keyMap = createKeys(KEY_SIZE);
        String  publicKey = keyMap.get("publicKey");
        String  privateKey = keyMap.get("privateKey");
        System.out.println("公钥: \n\r" + publicKey);
        System.out.println("私钥： \n\r" + privateKey);
        System.out.println("公钥加密——私钥解密");
        String str = "哈哈哈哈哈哈哈哈哈哈sdfsfs哈哈哈哈哈哈哈哈哈哈哈哈哈哈";
        System.out.println("\r明文：\r\n" + str);
        System.out.println("\r明文大小：\r\n" + str.getBytes().length);
        String encodedData = privateEncrypt(str, getPrivateKey(privateKey));
        System.out.println("密文：\r\n" + encodedData);
        long time2 = System.currentTimeMillis();
        String decodedData = publicDecrypt(encodedData, getPublicKey(publicKey));
        System.out.println("解密后文字: \r\n" + decodedData);
        System.out.println("加密时间："+ (time2 - time1));
        System.out.println("解密时间："+ (System.currentTimeMillis() - time2));
    }


}
