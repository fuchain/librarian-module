package com.fpt.edu.common.helper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fpt.edu.constant.Constant;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

@Component
public class Utils {
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


}
