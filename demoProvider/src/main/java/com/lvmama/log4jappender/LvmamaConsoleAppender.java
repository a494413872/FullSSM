package com.lvmama.log4jappender;

import com.lvmama.log.util.LogTrackContext;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Layout;
import org.apache.log4j.spi.LoggingEvent;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class LvmamaConsoleAppender extends ConsoleAppender {
    private DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss,SSS");
    private String hostName = null;
    private String appName = null;

    public LvmamaConsoleAppender() {
    }

    public String getAppName() {
        return this.appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
        LogTrackContext.appName = appName;
    }

    protected void subAppend(LoggingEvent event) {
        Date now = new Date();
        String prefix = "[LvLogS";
        String trackNumber = "track:\"" + StringUtils.trimToEmpty(LogTrackContext.getTrackNumber()) + "\"";
        String level = "LVL:\"" + this.getLevelStr(event) + "\"";
        String hostName = "HOST:\"" + this.getHostName() + "\"";
        String appName = "APP:\"" + StringUtils.trimToEmpty(this.getAppName()) + "\"";
        String parentAppName = "PAPP:\"" + StringUtils.trimToEmpty(LogTrackContext.getParentAppName()) + "\"";
        String className = "CLASS:\"" + event.getLoggerName() + "\"";
        String dateTime = "TIME:\"" + now.getTime() + "\"";
        String suffix = "[LvLogE";
        this.qw.write(this.format.format(now));
        this.qw.write(" " + this.getLevelStr(event));
        this.qw.write(prefix);
        this.qw.write("]");
        this.qw.write(super.layout.format(event));
        if (super.layout.ignoresThrowable()) {
            String[] s = event.getThrowableStrRep();
            if (s != null) {
                int len = s.length;

                for(int i = 0; i < len; ++i) {
                    this.qw.write(s[i]);
                    this.qw.write(Layout.LINE_SEP);
                }
            }
        }

        this.qw.write(suffix);
        this.qw.write(" " + dateTime);
        this.qw.write(" " + level);
        this.qw.write(" " + appName);
        this.qw.write(" " + parentAppName);
        this.qw.write(" " + hostName);
        this.qw.write(" " + trackNumber);
        this.qw.write(" " + className);
        this.qw.write("]");
        this.qw.write(Layout.LINE_SEP);
        if (this.shouldFlush(event)) {
            this.qw.flush();
        }

    }

    private String getHostName() {
        if (this.hostName == null) {
            try {
                Runtime runtime = Runtime.getRuntime();
                Process process = runtime.exec("hostname");
                InputStream is = process.getInputStream();
                InputStreamReader isr = new InputStreamReader(is);
                BufferedReader br = new BufferedReader(isr);
                this.hostName = br.readLine();
                IOUtils.closeQuietly(is);
                IOUtils.closeQuietly(isr);
                IOUtils.closeQuietly(br);
            } catch (Exception var6) {
            }
        }

        return this.hostName;
    }

    private String getLevelStr(LoggingEvent event) {
        String levelStr = "";
        switch(event.getLevel().toInt()) {
            case -2147483648:
                levelStr = "ALL";
                break;
            case 5000:
                levelStr = "TRACE";
                break;
            case 10000:
                levelStr = "DEBUG";
                break;
            case 20000:
                levelStr = "INFO";
                break;
            case 30000:
                levelStr = "WARN";
                break;
            case 40000:
                levelStr = "ERROR";
                break;
            case 50000:
                levelStr = "FATAL";
                break;
            case 2147483647:
                levelStr = "OFF";
        }

        return levelStr;
    }
}
