package com.biaozhunyuan.tianyi.common.utils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Executors 创建单例线程
 */

public class ExecutorUtils {

    private Runnable runnable;

    public ExecutorUtils(Runnable runnable){
        this.runnable = runnable;
        ExecutorService singleThreadExecutor = Executors.newSingleThreadExecutor();
        singleThreadExecutor.execute(runnable);
    }

}
