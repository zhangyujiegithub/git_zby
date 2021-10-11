package com.biaozhunyuan.tianyi.common.model.request;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 服务器返回结果数据实体
 * 
 * @author kjx
 * @since 2014/07/17 14:41
 * @param <T>
 *            数据实体类型
 */
public class ReturnModel<T> implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1476812092601265145L;

	public int Status;

	public String Message;

	public List<T> Data;

	public ReturnModel() {
		Data = new ArrayList<T>();
	}

	public ReturnModel(int Status) {
		this();
		this.Status = Status;
	}

	public ReturnModel(int status, String message, List<T> data) {
		super();
		Status = status;
		Message = message;
		Data = data;
	}

	public int getStatus() {
		return Status;
	}

	public void setStatus(int status) {
		Status = status;
	}

	public String getMessage() {
		return Message;
	}

	public void setMessage(String message) {
		Message = message;
	}

	public List<T> getData() {
		return Data;
	}

	public void setData(List<T> data) {
		Data = data;
	}

}
