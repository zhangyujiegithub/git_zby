package com.biaozhunyuan.tianyi.newuihome;

import com.biaozhunyuan.tianyi.common.model.other.FunctionBoard;

import java.io.Serializable;
import java.util.List;

/**
 * 消息事件
 */

public class BoardMessageEvent implements Serializable{

    private List<FunctionBoard> functionList;

    public List<FunctionBoard> getFunctionList() {
        return functionList;
    }

    public BoardMessageEvent(List<FunctionBoard> functionList) {
        this.functionList = functionList;
    }
}
