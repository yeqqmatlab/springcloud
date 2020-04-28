package com.liveinpast.stress.common.helper;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * MD5工具类
 *
 * @author Live.InPast
 * @date 2018/10/23
 */
public class MD5Helper {

    /**
     * 空字符串
     */
    public static final String EMPTY_STRING = "";

    /**
     * 16/32位
     */
    private static final int BIT_16 = 16;

    /**
     * md5加密
     * @param str 明文
     * @param bit 16或32
     * @param capital true：转大写，false 小写
     * @return
     */
    public static String convert(String str,int bit,boolean capital){
        String result = core(str);
        if(bit == BIT_16){
            result = result.substring(8,24);
        }
        if(capital){
            result = result.toUpperCase();
        }
        return result;
    }

    /**
     * MD5加密算法
     * @param str 加密的字符串
     * @return
     */
    private static String core(String str) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(str.getBytes("utf-8"));
            byte[] b = md.digest();
            StringBuffer buf = new StringBuffer();
            for (int offset = 0; offset < b.length; offset++) {
                int i = b[offset];
                if (i < 0){
                    i += 256;
                }
                if (i < 16){
                    buf.append("0");
                }
                buf.append(Integer.toHexString(i));
            }
            return buf.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (Exception e){
            e.printStackTrace();
        }
        return EMPTY_STRING;
    }
}
