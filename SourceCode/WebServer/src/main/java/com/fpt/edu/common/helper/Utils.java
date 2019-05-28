package com.fpt.edu.common.helper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fpt.edu.constant.Constant;
import com.fpt.edu.linkresource.EndPoint;
import com.fpt.edu.linkresource.EndPointDef;
import com.fpt.edu.linkresource.Link;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerMapping;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

@Component
public class Utils {
    @Autowired
    EndPointDef endPointDef;

    protected final Logger LOGGER = LogManager.getLogger(getClass());

    public JSONObject buildListEntity(List<?> list, HttpServletRequest httpServletRequest) throws JsonProcessingException {
        JSONObject jsonObject = new JSONObject();
        JSONArray arr = new JSONArray();
        ObjectMapper objectMapper = new ObjectMapper();
        EndPoint endPoint = getEndPoint(httpServletRequest.getAttribute(HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE).toString(), httpServletRequest.getMethod());
        if (endPoint.getIsCollection().equalsIgnoreCase(Constant.YES)) {
            for (int i = 0; i < list.size(); i++) {
                JSONObject jsonItem = new JSONObject(objectMapper.writeValueAsString(list.get(i)));
                httpServletRequest.getRequestURL().toString();

                arr.put(jsonItem);
            }
            jsonObject.put(Constant.ITEMS, arr);
        }
        return jsonObject;
    }

    public String buildItemDetailLink(String currentPath, JSONObject raw) {
        String id = raw.getString(Constant.ID);
        String result = currentPath.replaceAll(Constant.REGULAR_ID_EXP, id);
        return result;
    }


    public JSONObject buildRelatedLink(HttpServletRequest httpServletRequest, JSONObject raw, EndPoint endPoint) {
        List<Link> linkList = endPoint.getLinkList();
        JSONArray arr = new JSONArray();
        for (int i = 0; i < linkList.size(); i++) {
            JSONObject subLink = new JSONObject();
            Link l = linkList.get(i);
            getValueOfAKey(raw, l.getHref());
            subLink.put(Constant.TITLE, l.getTitle());
            subLink.put(Constant.HREF, buildServerRootPath(httpServletRequest) + l.getHref());
            arr.put(subLink);
        }
        raw.put(Constant.LINK, arr);
        return raw;
    }

    private String getValueOfAKey(JSONObject object, String keyName) {
        Iterator<?> it = object.keys();
        if (it.hasNext()) {
            String currentKeyname = (String) it.next();
            Object currentKeyValue = object.get(currentKeyname);
            if (currentKeyValue instanceof JSONObject) {
                getValueOfAKey((JSONObject) currentKeyValue, keyName);
            } else if (currentKeyValue instanceof JSONArray) {
                JSONArray arr = (JSONArray) currentKeyValue;
                for (int i = 0; i < arr.length(); i++) {
                    getValueOfAKey(arr.getJSONObject(i), keyName);
                }
            }
            if (currentKeyValue instanceof String && currentKeyname.equals(keyName)) {
                return currentKeyValue.toString();
            }
        }

        return "";

    }


    private EndPoint getEndPoint(String requestPattern, String method) {
        for (int i = 0; i < endPointDef.getListEndpoints().size(); i++) {
            EndPoint endPoint = endPointDef.getListEndpoints().get(i);
            if (method.equalsIgnoreCase(endPoint.getMethod()) && requestPattern.equalsIgnoreCase(endPoint.getLinkSelf())) {
                return endPoint;
            }
        }
        return null;
    }


    // build the root path for the server like http://localhost:9090/api/v1
    public String buildServerRootPath(HttpServletRequest httpServletRequest) {
        return
                httpServletRequest.getScheme() + "://" +
                        httpServletRequest.getServerName() + ":" +
                        httpServletRequest.getServerPort() +
                        httpServletRequest.getContextPath();

    }


}
