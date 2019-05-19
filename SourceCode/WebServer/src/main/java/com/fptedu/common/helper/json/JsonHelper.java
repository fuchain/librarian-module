package com.fptedu.common.helper.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class JsonHelper implements IJsonHelper {

    private Gson gson;

    public JsonHelper() {
        this.gson = new GsonBuilder().serializeNulls().create();
    }


    @Override
    public String toJson(Object src) {
        return this.gson.toJson(src);
    }
}
