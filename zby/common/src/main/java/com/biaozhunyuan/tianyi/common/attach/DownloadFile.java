package com.biaozhunyuan.tianyi.common.attach;

import java.io.Serializable;

public class DownloadFile implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7819138431175039951L;

    public String atttachId;

	public String attachName;

	public String url;

	// /以下三个字段用于下载时使用
	/** 已下载大小 */
	public int downloadSize;

	/** 文件大小 */
	public int totalSize;

	/** 下载状态 */
	public int downloadState;
}
