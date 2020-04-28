package com.liveinpast.stress.common.exception;

/**
 * Token验证失败异常
 *
 * @author Live.InPast
 * @date 2018/10/23
 */
public class TokenValidException extends RuntimeException {

    /**
     * 构造函数
     */
    public TokenValidException() {

    }

    /**
     * 构造函数
     * @param errMsg
     */
    public TokenValidException(String errMsg) {
        super(errMsg);
    }

    /**
     * 构造函数
     * @param errMsg
     * @param cause
     */
    public TokenValidException(String errMsg, Throwable cause) {
        super(errMsg,cause);
    }

}
