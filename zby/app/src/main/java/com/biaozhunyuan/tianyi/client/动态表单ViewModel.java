package com.biaozhunyuan.tianyi.client;

import com.biaozhunyuan.tianyi.common.model.form.字典项;
import com.biaozhunyuan.tianyi.common.model.form.表单字段;

import java.util.ArrayList;
import java.util.List;

public class 动态表单ViewModel {
	public ArrayList<表单字段> 表单字段s;
	public List<动态表单分类> 动态表单分类s;
	public List<List<字典项>> 字典s;
	// public List<String> 字典名称s ;
	public String 只读字段s;
	public String 隐藏字段s;
}