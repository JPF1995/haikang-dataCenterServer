package com.haikang.datacenter.util;

import com.tangosol.net.CacheFactory;
import com.tangosol.net.NamedCache;
import com.tangosol.util.Filter;
import com.tangosol.util.QueryHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Set;

/**
 * Filename:    CoherenceUtil
 * Copyright:   Copyright (c)2017
 * Company:     YCIG
 *
 * @version: 1.0
 * @since: JDK 1.8.0_73
 * @Author: jpf
 * Create at:   2019-5-24 10:07
 * Description:
 */

public class CoherenceUtil {

    private static Logger logger = LoggerFactory.getLogger(CoherenceUtil.class);

    private static String propertyKey = "tangosol.coherence.cacheconfig";
    private static String propertyValue = "coherence-cache-config.xml";

    private static CoherenceUtil util = null;

    private NamedCache cache = null;

    private CoherenceUtil()
    {
        System.setProperty(propertyKey, propertyValue);

    }

    public static synchronized CoherenceUtil getConnection(String table) {
        if (null == util) {
            util = new CoherenceUtil();
        }
        CacheFactory.ensureCluster();
        util.cache = CacheFactory.getCache(table);
        return util;
    }

    public Filter getFilter(String cohql, Object[] params) {
        return QueryHelper.createFilter(cohql, params);
    }

    public Set entrySet(Filter filter)
    {
        return cache.entrySet(filter);
    }

    public  void put(Object key, Object value) {
        cache.put(key, value);
    }

    public Object get(Object key)
    {
        if (null != util) {
            Object obj = cache.get(key);
            return obj;
        }
        return null;
    }

    public Map gets(Set key)
    {
        if (null != util) {
            Map obj = cache.getAll(key);
            return obj;
        }
        return null;
    }

    public Map getAll() {

        Set set = cache.keySet();
        Map map = cache.getAll(set);
        return map;
    }
}
