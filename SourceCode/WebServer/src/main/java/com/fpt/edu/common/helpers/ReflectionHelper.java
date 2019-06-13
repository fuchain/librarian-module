package com.fpt.edu.common.helpers;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ReflectionHelper {

    public static Object partialUpdate(Object inDBObject, Object fromBodyObject) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Class reflectionBody = fromBodyObject.getClass();
        Class reflectionDB = inDBObject.getClass();
        for (int i = 0; i < reflectionBody.getMethods().length; i++) {
            Method method = reflectionBody.getMethods()[i];
            System.out.println(method.getName());
            if (isGetter(method)) {
                Class returnType = method.getReturnType();
                Method setMethod = reflectionDB.getMethod(convertGeter2Setter(method.getName()), returnType);
                if(method.invoke(fromBodyObject)!=null){
                    setMethod.invoke(inDBObject,method.invoke(fromBodyObject));
                }
            }
        }
        return inDBObject;
    }

    private static boolean isGetter(Method method) {
        String methodName = method.getName();
        if ( !(methodName.contains("getClass"))&& (methodName.startsWith("get") || methodName.startsWith("is"))) {
            return true;
        }
        return false;
    }

    private static boolean isSetter(Method method) {
        String methodName = method.getName();
        if (methodName.startsWith("set")) {
            return true;
        }
        return false;
    }

    private static String convertGeter2Setter(String getter) {
        return getter.replace("get", "set");

    }


}
