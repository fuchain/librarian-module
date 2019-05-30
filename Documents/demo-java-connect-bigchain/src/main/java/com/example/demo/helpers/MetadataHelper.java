package com.example.demo.helpers;
import com.bigchaindb.model.MetaData;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class MetadataHelper {

    public static MetaData createMetadata(Object entity) throws Exception {
        MetaData metaData = new MetaData();
        Class cls = entity.getClass();
        Field[] fields = cls.getDeclaredFields();
        for (Field field : fields) {
            String fieldName = field.getName();
            fieldName = fieldName
                .substring(0, 1).toUpperCase() +
                fieldName.substring(1);
            Method getter = cls.getDeclaredMethod("get" + fieldName);
            metaData.setMetaData(fieldName, getter.invoke(entity).toString());
        }
        return metaData;
    }
}
