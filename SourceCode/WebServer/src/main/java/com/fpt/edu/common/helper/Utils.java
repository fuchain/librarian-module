package com.fpt.edu.common.helper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;
import com.fpt.edu.constant.Constant;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@Component

public class Utils {

    protected final Logger LOGGER = LogManager.getLogger(getClass());

    public JSONObject buildListEntity(List<?> list, HttpServletRequest httpServletRequest) throws JsonProcessingException {
        JSONObject jsonObject = new JSONObject();
        JSONArray arr = new JSONArray();
        ObjectMapper objectMapper = new ObjectMapper();
        Hibernate5Module hbm = new Hibernate5Module();
        objectMapper.registerModule(hbm);
        jsonObject.put(Constant.ITEMS, arr);
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


    // Build the root path for the server like http://localhost:9090/api/v1
    public String buildServerRootPath(HttpServletRequest httpServletRequest) {
        return
                httpServletRequest.getScheme() + "://" +
                        httpServletRequest.getServerName() + ":" +
                        httpServletRequest.getServerPort() +
                        httpServletRequest.getContextPath();

    }

    // Get pin from random number
    public String getPin() {
        Random random = new Random();
        int number = random.nextInt(Constant.RANDOM_BOUND);

        return String.format("%06d", number);
    }

    // Get duration between 2 dates
    public long getDuration(Date oldDate, Date newDate, TimeUnit timeUnit) {
        long diffInMillies = newDate.getTime() - oldDate.getTime();
        return timeUnit.convert(diffInMillies, TimeUnit.MILLISECONDS);
    }


}
