package com.biaozhunyuan.tianyi.global;

import android.support.v4.util.LruCache;

import com.biaozhunyuan.tianyi.attch.AttachInfo;

import java.util.List;

/**
 * Created by k on 2016/11/16.
 */

public class AttachInfoCache extends LruCache<String,List<AttachInfo>> {

    private static AttachInfoCache mInstance;

    //最大缓存数量
    private final static int mMaxSize=2000;

    /**
     * @param maxSize for caches that do not override {@link #sizeOf}, this is
     *                the maximum number of entries in the cache. For all other caches,
     *                this is the maximum sum of the sizes of the entries in this cache.
     */
    private AttachInfoCache(int maxSize) {
        super(maxSize);
    }

    @Override
    protected int sizeOf(String key, List<AttachInfo> value) {
        return super.sizeOf(key, value);
    }

    @Override
    protected void entryRemoved(boolean evicted, String key, List<AttachInfo> oldValue, List<AttachInfo> newValue) {
        oldValue=null;
        super.entryRemoved(evicted, key, oldValue, newValue);
    }

    public static AttachInfoCache getInstance()
    {
        if(mInstance==null)
        {
            synchronized (AttachInfoCache.class)
            {
                if(mInstance==null)
                {
                    mInstance=new AttachInfoCache(mMaxSize);
                }
            }
        }
        return  mInstance;
    }

}
