package com.example.demo.helpers;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.TreeMap;

public final class AssetHelper {

    public static Map<String, String> createAsset(Object entity) throws Exception {
        Map<String, String> asset = new TreeMap<>();

        Class cls = entity.getClass();
        Field[] fields = cls.getDeclaredFields();
        for (Field field : fields) {
            String fieldName = field.getName();
            fieldName = fieldName
                .substring(0, 1).toUpperCase() +
                fieldName.substring(1);
            Method getter = cls.getDeclaredMethod("get" + fieldName);
            asset.put(fieldName, getter.invoke(entity).toString());
        }

        return asset;
    }
}
