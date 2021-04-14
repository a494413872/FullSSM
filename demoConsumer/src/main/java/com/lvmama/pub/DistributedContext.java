package com.lvmama.pub;

import com.lvmama.pub.vo.DBWriteRecord;
import com.lvmama.pub.vo.TableWriteRecord;
import net.sf.json.JSONObject;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class DistributedContext {
    private static final ThreadLocal<Map<String, Serializable>> context = new ThreadLocal();

    public DistributedContext() {
    }

    public static void putToContext(Map<String, Serializable> map) {
        if (map != null && !map.isEmpty()) {
            getContext().putAll(map);
        }
    }

    public static Map<String, Serializable> getContext() {
        if (context.get() == null) {
            init();
        }

        return (Map)context.get();
    }

    public static void init() {
        HashMap<String, Serializable> map = new HashMap();
        map.put("DB_WRITE_RECORD", new DBWriteRecord());
        context.set(map);
    }

    public static void remove() {
        context.remove();
    }

    public static Serializable get(String key) {
        return (Serializable)getContext().get(key);
    }

    public static void put(String key, Serializable value) {
        getContext().put(key, value);
    }

    public static void remove(String key) {
        getContext().remove(key);
    }

    public static DBWriteRecord getDBWriteRecord() {
        return (DBWriteRecord)getContext().get("DB_WRITE_RECORD");
    }

    public static String toJSON() {
        if (context == null) {
            return "";
        } else {
            try {
                return JSONObject.fromObject(getContext()).toString();
            } catch (Exception var1) {
                return "";
            }
        }
    }

    public static Map<String, Serializable> toObject(String jsonString) {
        if (jsonString != null && jsonString.trim().length() != 0) {
            try {
                Map<String, Class> childMap = new HashMap();
                childMap.put("DB_WRITE_RECORD", DBWriteRecord.class);
                childMap.put("writeRecords", TableWriteRecord.class);
                JSONObject jsonObject = JSONObject.fromObject(jsonString);
                Map pojo;
                if (childMap != null && !childMap.isEmpty()) {
                    pojo = (Map)JSONObject.toBean(jsonObject, Map.class, childMap);
                } else {
                    pojo = (Map)JSONObject.toBean(jsonObject, Map.class);
                }

                return pojo;
            } catch (Exception var4) {
                return new HashMap();
            }
        } else {
            return new HashMap();
        }
    }

    public static boolean isLvmamaHessian() {
        return get("isLvmamaHessian") != null && "Y".equalsIgnoreCase(get("isLvmamaHessian").toString());
    }

    public static void setLvmamaHessian() {
        put("isLvmamaHessian", "Y");
    }

    public static void main(String[] args) {
        DBWriteRecord dbWriteRecord = new DBWriteRecord();
        dbWriteRecord.updated((String)null);
        dbWriteRecord.updated("perm_user");
        put("DB_WRITE_RECORD", dbWriteRecord);
        System.out.println(getContext());
        String jsonString = toJSON();
        putToContext(toObject((String)null));
        putToContext(toObject(""));
        System.out.println(toObject(jsonString));
    }
}
