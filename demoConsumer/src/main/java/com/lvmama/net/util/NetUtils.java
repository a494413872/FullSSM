package com.lvmama.net.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;
import java.util.regex.Pattern;

public class NetUtils {
    private static Log logger = LogFactory.getLog(NetUtils.class);
    private static final Pattern IP_PATTERN = Pattern.compile("\\d{1,3}(\\.\\d{1,3}){3,5}$");

    public NetUtils() {
    }

    public static String getLocalIP() {
        InetAddress localAddress = getLocalAddress0();
        return localAddress == null ? "127.0.0.1" : localAddress.getHostAddress();
    }

    private static boolean isValidAddress(InetAddress address) {
        if (address != null && !address.isLoopbackAddress()) {
            String ip = address.getHostAddress();
            return ip != null && !ip.startsWith("0.0") && !"127.0.0.1".equals(ip) && IP_PATTERN.matcher(ip).matches();
        } else {
            return false;
        }
    }

    private static InetAddress getLocalAddress0() {
        InetAddress localAddress = null;

        try {
            localAddress = InetAddress.getLocalHost();
            if (isValidAddress(localAddress)) {
                return localAddress;
            }
        } catch (Exception var5) {
            logger.error(var5.getMessage(), var5);
        }

        try {
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            if (interfaces != null) {
                while(true) {
                    Enumeration addresses;
                    do {
                        if (!interfaces.hasMoreElements()) {
                            return localAddress;
                        }

                        NetworkInterface network = (NetworkInterface)interfaces.nextElement();
                        addresses = network.getInetAddresses();
                    } while(addresses == null);

                    while(addresses.hasMoreElements()) {
                        InetAddress address = (InetAddress)addresses.nextElement();
                        if (isValidAddress(address)) {
                            return address;
                        }
                    }
                }
            }
        } catch (Exception var6) {
            logger.error(var6.getMessage(), var6);
        }

        return localAddress;
    }

    public static void main(String[] args) {
        System.out.println(getLocalIP());
    }
}
