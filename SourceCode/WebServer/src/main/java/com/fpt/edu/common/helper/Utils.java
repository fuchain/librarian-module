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

    public JSONObject buildListEntity(List<?> list) throws JsonProcessingException {
        JSONObject jsonObject = new JSONObject();
        JSONArray arr = new JSONArray();
        ObjectMapper objectMapper = new ObjectMapper();
        for (int i = 0; i < list.size(); i++) {
            arr.put(new JSONObject(objectMapper.writeValueAsString(list.get(i))));
        }
        jsonObject.put(Constant.ITEMS, arr);
        return jsonObject;
    }


    public JSONObject buildRelatedLink(String requestPathtern, String method, JSONObject raw) {
        EndPoint endPoint = getEndPoint(requestPathtern, method);
        List<Link> linkList = endPoint.getLinkList();
        for (int i = 0; i < linkList.size(); i++) {
            Link l = linkList.get(i);
            getValueOfAKey(raw, l.getHref());

        }
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


}
