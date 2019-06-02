package com.fpt.edu.common.helper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;
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
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@Component
public class Utils {
    @Autowired
    EndPointDef endPointDef;

    protected final Logger LOGGER = LogManager.getLogger(getClass());

    public JSONObject buildListEntity(List<?> list, HttpServletRequest httpServletRequest) throws JsonProcessingException {
        JSONObject jsonObject = new JSONObject();
        JSONArray arr = new JSONArray();
        ObjectMapper objectMapper = new ObjectMapper();
        Hibernate5Module hbm = new Hibernate5Module();
        objectMapper.registerModule(hbm);
        EndPoint endPoint = getEndPoint(httpServletRequest.getAttribute(HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE).toString(), httpServletRequest.getMethod());
        if (endPoint.getIsCollection().equalsIgnoreCase(Constant.YES)) {
            for (int i = 0; i < list.size(); i++) {
                JSONObject jsonItem = new JSONObject(objectMapper.writeValueAsString(list.get(i)));
                String instanceLink = buildServerRootPath(httpServletRequest) + endPoint.getItemLink();
                instanceLink = instanceLink.replaceAll(Constant.REGULAR_ID_EXP, jsonItem.get(Constant.ID).toString());
                jsonItem.put(Constant.LINK, instanceLink);
                arr.put(jsonItem);
            }
            jsonObject.put(Constant.ITEMS, arr);
        }
        return jsonObject;
    }


    public JSONObject convertObjectToJSONObject(Object o) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = objectMapper.writeValueAsString(o);
        return new JSONObject(jsonString);
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

    // Build the root path for the server like http://localhost:9090/api/v1
    public String buildServerRootPath(HttpServletRequest httpServletRequest) {
        return
                httpServletRequest.getScheme() + "://" +
                        httpServletRequest.getServerName() + ":" +
                        httpServletRequest.getServerPort() +
                        httpServletRequest.getContextPath();

    }

    //get pin from random number
    public String getPin() {
        Random random = new Random();
        int number = random.nextInt(999999);

        return String.format("%06d", number);
    }

    //get duration between 2 dates
    public static long getDuration(Date oldDate, Date newDate, TimeUnit timeUnit) {
        long diffInMillies = newDate.getTime() - oldDate.getTime();
        return timeUnit.convert(diffInMillies, TimeUnit.MILLISECONDS);
    }


    public Object partialUpdate(Object inDBObject, Object fromBodyObject) {
        Class reflection = fromBodyObject.getClass();
        for (int i = 0; i < reflection.getMethods().length; i++) {
            Method method= reflection.getMethods()[i];
            System.out.println(method.getName());
           // fromBodyObject.get



        }






        return inDBObject;
    }


}
