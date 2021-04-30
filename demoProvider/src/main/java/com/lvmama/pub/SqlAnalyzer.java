package com.lvmama.pub;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class SqlAnalyzer {
    private static final String SCHEMA_SEPRATOR = "\\.";
    private static final Logger LOG = Logger.getLogger(SqlAnalyzer.class);

    public SqlAnalyzer() {
    }

    public static boolean isWrite(String sql) {
        boolean isWrite = false;

        try {
            isWrite = isUpdate(sql) || isInsert(sql) || isDelete(sql);
        } catch (Exception var3) {
            LOG.error("isWrite error, sql:" + sql, var3);
        }

        return isWrite;
    }

    public static String getWrittenTable(String sql) {
        String tableNamesStr = null;

        try {
            if (!isWrite(sql)) {
                return null;
            }

            sql = sql.toUpperCase().trim();
            if (isUpdate(sql)) {
                tableNamesStr = getUpdatTable(sql);
            } else if (isInsert(sql)) {
                tableNamesStr = getInsertTable(sql);
            } else if (isDelete(sql)) {
                tableNamesStr = getDeleteTable(sql);
            }
        } catch (Exception var3) {
            LOG.error("getWrittenTable error, sql:" + sql, var3);
        }

        return tableNamesStr == null ? null : tableNamesStr.trim();
    }

    public static String getSchema(String tableNameWithSchema) {
        if (StringUtils.isEmpty(tableNameWithSchema)) {
            return null;
        } else {
            String[] schemaAndTable = tableNameWithSchema.split("\\.");
            return schemaAndTable.length != 2 ? null : schemaAndTable[0];
        }
    }

    public static String getPureTableName(String tableNameWithSchema) {
        if (StringUtils.isEmpty(tableNameWithSchema)) {
            return null;
        } else {
            String[] schemaAndTable = tableNameWithSchema.split("\\.");
            if (schemaAndTable.length == 1) {
                return schemaAndTable[0];
            } else {
                return schemaAndTable.length == 2 ? schemaAndTable[1] : null;
            }
        }
    }

    private static String getDeleteTable(String sql) {
        sql = sql.toUpperCase().trim();
        String tmp = StringUtils.substringAfter(sql, "FROM").trim();
        String tableNamesStr = StringUtils.substringBefore(tmp, " ");
        if (StringUtils.isEmpty(tableNamesStr)) {
            tableNamesStr = tmp;
        }

        return tableNamesStr;
    }

    private static String getInsertTable(String sql) {
        sql = sql.toUpperCase().trim();
        String tmp = StringUtils.substringAfter(sql, "INTO").trim();
        String s1 = StringUtils.substringBefore(tmp, "(");
        String s2 = StringUtils.substringBefore(tmp, " ");
        return getShortestStr(Arrays.asList(s1, s2));
    }

    private static String getUpdatTable(String sql) {
        sql = sql.toUpperCase().trim();
        String tmp = StringUtils.substringAfter(sql, "UPDATE").trim();
        return StringUtils.substringBefore(tmp, " ");
    }

    private static boolean isUpdate(String sql) {
        return sql != null && sql.trim().toUpperCase().startsWith("UPDATE");
    }

    private static boolean isInsert(String sql) {
        return sql != null && sql.trim().toUpperCase().startsWith("INSERT");
    }

    private static boolean isDelete(String sql) {
        return sql != null && sql.trim().toUpperCase().startsWith("DELETE");
    }

    private static String getShortestStr(List<String> list) {
        String tmp = null;
        Iterator i$ = list.iterator();

        while(true) {
            String s;
            do {
                do {
                    do {
                        if (!i$.hasNext()) {
                            return tmp;
                        }

                        s = (String)i$.next();
                    } while(s == null);
                } while(s.length() == 0);
            } while(tmp != null && tmp.length() <= s.length());

            tmp = s;
        }
    }

    public static void main(String[] args) throws Exception {
        String[] sqls = new String[]{" update lvmama_super.mybatis_test set name_='a' where id_=1       ", " update lvmama_super.mybatis_test  set name_='a' where id_=1      ", " update lvmama_super.mybatis_test t set t.name_='a' where id_=1   ", " update lvmama_super.mybatis_test  t set t.name_='a' where id_=1  ", " insert into lvmama_super.mybatis_test(id_,name_) values(1,'a')   ", " insert into lvmama_super.mybatis_test (id_,name_) values(1,'a')  ", " insert into lvmama_super.mybatis_test values(1,'a')              ", " insert into  lvmama_super.mybatis_test t values(1,'a')           ", " insert into lvmama_super.mybatis_test  values(1,'a')             ", " insert into lvmama_super.mybatis_test select 1,'a' from dual     ", " delete from lvmama_super.mybatis_test  t  where t.id_=1          ", " delete from lvmama_super.mybatis_test  where id_=1               ", " delete   from  lvmama_super.mybatis_test                            ", " delete  from  lvmama_super.mybatis_test", " delete  from  mybatis_test "};
        String[] arr$ = sqls;
        int len$ = sqls.length;

        for(int i$ = 0; i$ < len$; ++i$) {
            String sql = arr$[i$];
            System.out.println(getSchema(getWrittenTable(sql)) + "****" + getPureTableName(getWrittenTable(sql)));
        }

    }
}
