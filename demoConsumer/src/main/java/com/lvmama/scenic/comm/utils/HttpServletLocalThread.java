/**
 *
 */
package com.lvmama.scenic.comm.utils;

import org.springframework.ui.Model;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author lancey
 */
public class HttpServletLocalThread {

    static class HttpContext {

        private HttpServletRequest request;
        private HttpServletResponse response;
        private Model model;
        private String fromPlaceName;
        private String maxBannerId;
        private String minBannerId;
        private String footBannerId;
        private String paramDataCode;
        private String cityId;
        private String provinceId;
        private String stationName;
        private Long fromPlaceId;
        private Long parentFromPlaceId;
        private String focusImgPlaceCode;
        private String fromPlaceCode;
        private String parentParentFromPlaceCode;
        private Map<String, Object> map = new HashMap<String, Object>();
        public HttpContext() {
        }

        public HttpServletRequest getRequest() {
            return request;
        }

        public void setRequest(HttpServletRequest request) {
            this.request = request;
        }

        public HttpServletResponse getResponse() {
            return response;
        }

        public void setResponse(HttpServletResponse response) {
            this.response = response;
        }

        public Model getModel() {
            return model;
        }

        public void setModel(Model model) {
            this.model = model;
        }

        public String getFromPlaceName() {
            return fromPlaceName;
        }

        public void setFromPlaceName(String fromPlaceName) {
            this.fromPlaceName = fromPlaceName;
        }


        public String getMaxBannerId() {
            return maxBannerId;
        }

        public void setMaxBannerId(String maxBannerId) {
            this.maxBannerId = maxBannerId;
        }

        public String getMinBannerId() {
            return minBannerId;
        }

        public void setMinBannerId(String minBannerId) {
            this.minBannerId = minBannerId;
        }

        public String getFootBannerId() {
            return footBannerId;
        }

        public void setFootBannerId(String footBannerId) {
            this.footBannerId = footBannerId;
        }

        public String getParamDataCode() {
            return paramDataCode;
        }

        public void setParamDataCode(String paramDataCode) {
            this.paramDataCode = paramDataCode;
        }


        public String getCityId() {
            return cityId;
        }

        public void setCityId(String cityId) {
            this.cityId = cityId;
        }

        public String getProvinceId() {
            return provinceId;
        }

        public void setProvinceId(String provinceId) {
            this.provinceId = provinceId;
        }

        public String getStationName() {
            return stationName;
        }

        public void setStationName(String stationName) {
            this.stationName = stationName;
        }

        public Long getFromPlaceId() {
            return fromPlaceId;
        }

        public void setFromPlaceId(Long fromPlaceId) {
            this.fromPlaceId = fromPlaceId;
        }

        public Long getParentFromPlaceId() {
            return parentFromPlaceId;
        }

        public void setParentFromPlaceId(Long parentFromPlaceId) {
            this.parentFromPlaceId = parentFromPlaceId;
        }

        public String getFocusImgPlaceCode() {
            return focusImgPlaceCode;
        }

        public void setFocusImgPlaceCode(String focusImgPlaceCode) {
            this.focusImgPlaceCode = focusImgPlaceCode;
        }

        public String getFromPlaceCode() {
            return fromPlaceCode;
        }

        public void setFromPlaceCode(String fromPlaceCode) {
            this.fromPlaceCode = fromPlaceCode;
        }

        public String getParentParentFromPlaceCode() {
            return parentParentFromPlaceCode;
        }

        public void setParentParentFromPlaceCode(String parentParentFromPlaceCode) {
            this.parentParentFromPlaceCode = parentParentFromPlaceCode;
        }

        public Map<String, Object> getMap() {
            return map;
        }

        public void setMap(Map<String, Object> map) {
            this.map = map;
        }
    }






    static ThreadLocal<HttpContext> threadLocal = new ThreadLocal<HttpContext>();

    public static void set(HttpServletRequest req, HttpServletResponse res) {
        initThreadLocal();
        threadLocal.get().setRequest(req);
        threadLocal.get().setResponse(res);
    }

    public static HttpServletRequest getRequest() {
        initThreadLocal();
        return threadLocal.get().getRequest();
    }

    public static HttpServletResponse getResponse() {
        initThreadLocal();
        return threadLocal.get().getResponse();
    }

    public static Model getModel() {
        initThreadLocal();
        return threadLocal.get().getModel();
    }

    public static void setModel(Model model) {
        initThreadLocal();
        threadLocal.get().setModel(model);
    }

    public static String getFromPlaceName() {
        initThreadLocal();
        return threadLocal.get().getFromPlaceName();
    }

    public static void setFromPlaceName(String fromPlaceName) {
        initThreadLocal();
        threadLocal.get().setFromPlaceName(fromPlaceName);
    }


    public static String getMaxBannerId() {
        initThreadLocal();
        return threadLocal.get().getMaxBannerId();
    }

    public static void setMaxBannerId(String maxBannerId) {
        initThreadLocal();
        threadLocal.get().setMaxBannerId(maxBannerId);
    }

    public static String getMinBannerId() {
        initThreadLocal();
        return threadLocal.get().getMinBannerId();
    }

    public static void setMinBannerId(String minBannerId) {
        initThreadLocal();
        threadLocal.get().setMinBannerId(minBannerId);
    }

    public static String getFootBannerId() {
        initThreadLocal();
        return threadLocal.get().getFootBannerId();
    }

    public static void setFootBannerId(String footBannerId) {
        initThreadLocal();
        threadLocal.get().setFootBannerId(footBannerId);
    }

    public static String getParamDataCode() {
        initThreadLocal();
        return threadLocal.get().getParamDataCode();
    }

    public static void setParamDataCode(String paramDataCode) {
        initThreadLocal();
        threadLocal.get().setParamDataCode(paramDataCode);
    }





    public static  String getCityId() {
        initThreadLocal();
        return threadLocal.get().getCityId();
    }

    public static void setCityId(String cityId) {
        initThreadLocal();
        threadLocal.get().setCityId(cityId);
    }

    public static String getProvinceId() {
        initThreadLocal();
        return threadLocal.get().getProvinceId();
    }

    public static void setProvinceId(String provinceId) {
        initThreadLocal();
        threadLocal.get().setProvinceId(provinceId);
    }

    public static String getStationName() {
        initThreadLocal();
        return threadLocal.get().getStationName();
    }

    public static void setStationName(String stationName) {
        initThreadLocal();
        threadLocal.get().setStationName(stationName);
    }

    public static Long getFromPlaceId() {
        initThreadLocal();
        return threadLocal.get().getFromPlaceId();
    }

    public static void setFromPlaceId(Long fromPlaceId) {
        initThreadLocal();
        threadLocal.get().setFromPlaceId(fromPlaceId);
    }

    public static Long getParentFromPlaceId() {
        initThreadLocal();
        return threadLocal.get().getParentFromPlaceId();
    }

    public static void setParentFromPlaceId(Long parentFromPlaceId) {
        initThreadLocal();
        threadLocal.get().setParentFromPlaceId(parentFromPlaceId);
    }

    public static String getFocusImgPlaceCode() {
        initThreadLocal();
        return threadLocal.get().getFocusImgPlaceCode();
    }

    public static void setFocusImgPlaceCode(String focusImgPlaceCode) {
        initThreadLocal();
        threadLocal.get().setFocusImgPlaceCode(focusImgPlaceCode);
    }

    public static String getFromPlaceCode() {
        initThreadLocal();
        return threadLocal.get().getFromPlaceCode();
    }

    public static void setFromPlaceCode(String fromPlaceCode) {
        initThreadLocal();
        threadLocal.get().setFromPlaceCode(fromPlaceCode);
    }

    public static String getParentParentFromPlaceCode() {
        initThreadLocal();
        return threadLocal.get().getParentParentFromPlaceCode();
    }

    public static void setParentParentFromPlaceCode(String parentParentFromPlaceCode) {
        initThreadLocal();
        threadLocal.get().setParentParentFromPlaceCode(parentParentFromPlaceCode);
    }
    public static Map<String, Object> getMap() {
        initThreadLocal();
        return threadLocal.get().getMap();
    }

    public static void setMap(Map<String, Object> map) {
        initThreadLocal();
        threadLocal.get().setMap(map);
    }








    private synchronized static void initThreadLocal() {
        if (threadLocal.get() == null) {
            threadLocal.set(new HttpContext());
        }
    }









    public static void clean() {
        threadLocal.remove();
    }
}
