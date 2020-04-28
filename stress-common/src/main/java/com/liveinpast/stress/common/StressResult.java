package com.liveinpast.stress.common;


import com.google.common.base.Strings;

import java.io.Serializable;

/**
 * 知心慧学微服务统一返回值
 *
 * @author Live.InPast
 * @date 2018/10/23
 */
public class StressResult<T> implements Serializable {

	/**
	 * 成功
	 */
	public static final String SUCCESS = "00";

	/**
	 * 通用失败(业务异常)
	 */
	public static final String FAIL = "01";
	public final static String ERR_OK_MSG = "执行成功";
	public final static String ERR_FAIL_MSG = "执行失败";

	/**
	 * 最大整数,超过该数前端将丢失精度
	 */
	private static final Long MAX_VALUE_TO_STRING = 9007199254740992L;


	/**
	 * 操作码
	 * 00 代表成功
	 * XX 代表失败
	 */
	private String errCode;

	/**
	 * 操作说明
	 */
	private String errMsg;

	/**
	 * 附加数据
	 */
	private T data;

	private StressResult(){}

	private StressResult(String errCode){
		this.errCode = errCode;
	}

	/**
	 * 成功
	 * @return
	 */
	public static StressResult success(){
		StressResult result = new StressResult(SUCCESS);
		result.errMsg = ERR_OK_MSG;
		return result;
	}

	/**
	 * 失败
	 * @return
	 */
	public static StressResult fail(){
		StressResult result = new StressResult(FAIL);
		result.errMsg = ERR_FAIL_MSG;
		return result;
	}

	/**
	 * 失败
	 * @return
	 */
	public static StressResult fail(String errCode){
		StressResult result = new StressResult(FAIL);
		result.errCode = errCode;
		result.errMsg = ERR_FAIL_MSG;
		return result;
	}

	/**
	 * 设置返回结果
	 * @param errMsg
	 * @return
	 */
	public StressResult msg(String errMsg){
		this.errMsg = errMsg;
		return this;
	}

	/**
	 * 设置返回数据
	 * @return
	 */
	public StressResult data(T data){
		this.data = data;
		return this;
	}

	/**
	 * 内部服务数据获取
	 * @return
	 */
	public T get(){
		if (Strings.isNullOrEmpty(this.errCode)) {
			throw new RuntimeException(this.errMsg);
		}
		if (!this.errCode.equals(SUCCESS)){
			throw new RuntimeException(this.errMsg);
		}
		if (this.data==null){
			return null;
		}
		return this.data;
	}

	public void setErrCode(String errCode) {
		this.errCode = errCode;
	}

	public void setErrMsg(String errMsg) {
		this.errMsg = errMsg;
	}

	public void setData(T data) {
		this.data = data;
	}

	public String getErrCode() {
		return errCode;
	}

	public String getErrMsg() {
		return errMsg;
	}

	public T getData() {
		return data;
	}

}
