package com.lvmama.pub.vo;

import com.lvmama.pub.SqlAnalyzer;

import java.io.Serializable;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class DBWriteRecord implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final String VIRTUAL_TABLE_NAME = "DBWriteRecord.VIRTUAL_TABLE_NAME";
    private List<TableWriteRecord> writeRecords = new LinkedList();

    public DBWriteRecord() {
    }

    public List<TableWriteRecord> getWriteRecords() {
        return this.writeRecords;
    }

    public void setWriteRecords(List<TableWriteRecord> writeRecords) {
        this.writeRecords = writeRecords;
    }

    public void updated(String tableName) {
        if (tableName != null && tableName.length() != 0) {
            Iterator i$ = this.writeRecords.iterator();

            TableWriteRecord tableWriteRecord;
            do {
                if (!i$.hasNext()) {
                    this.writeRecords.add(new TableWriteRecord(SqlAnalyzer.getPureTableName(tableName), SqlAnalyzer.getSchema(tableName)));
                    return;
                }

                tableWriteRecord = (TableWriteRecord)i$.next();
            } while(!this.isSameTable(tableName, tableWriteRecord));

            tableWriteRecord.update();
        } else {
            this.updated("DBWriteRecord.VIRTUAL_TABLE_NAME");
        }
    }

    public boolean isUpdatedIn(Long second) {
        if (second == null) {
            return true;
        } else {
            Long now = System.currentTimeMillis();
            Date since = new Date(now - second * 1000L);
            return this.isUpdatedSince(since);
        }
    }

    public boolean isUpdatedSince(Date since) {
        return this.isUpdatedSince((String)null, since);
    }

    public boolean isUpdatedSince(String tableName, Date since) {
        if (this.writeRecords != null && this.writeRecords.size() != 0) {
            Iterator i$ = this.writeRecords.iterator();

            Date recordLastestWriteTime;
            do {
                TableWriteRecord tableWriteRecord;
                do {
                    do {
                        if (!i$.hasNext()) {
                            return false;
                        }

                        tableWriteRecord = (TableWriteRecord)i$.next();
                        recordLastestWriteTime = tableWriteRecord.getLastestWriteTime();
                    } while(tableName != null && !this.isSameTable(tableName, tableWriteRecord));
                } while(recordLastestWriteTime == null);
            } while(since != null && !recordLastestWriteTime.after(since));

            return true;
        } else {
            return false;
        }
    }

    private boolean isSameTable(String tableName, TableWriteRecord tableWriteRecord) {
        String schema1 = SqlAnalyzer.getSchema(tableName);
        String table1 = SqlAnalyzer.getPureTableName(tableName);
        String schema2 = tableWriteRecord.getSchema();
        String table2 = tableWriteRecord.getTableName();
        return schema1 != null && !"".equals(schema1) && schema2 != null && !"".equals(schema2) && !schema1.equalsIgnoreCase(schema2) ? false : table1.equalsIgnoreCase(table2);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        Iterator i$ = this.writeRecords.iterator();

        while(i$.hasNext()) {
            TableWriteRecord record = (TableWriteRecord)i$.next();
            sb.append(record.getSchema() == null ? "" : record.getSchema());
            sb.append("." + record.getTableName());
            sb.append("-");
            sb.append(record.getLastestWriteTime());
            sb.append(" ");
        }

        return sb.toString();
    }
}
