package com.example.paul.livecoding.pojo_deserializer;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.Gson;

import java.lang.reflect.Type;
import java.util.List;

public class CategoriesDeserializer implements JsonDeserializer<List<Categories>> {

    @Override
    public List<Categories> deserialize(JsonElement json, Type listType, JsonDeserializationContext context) throws JsonParseException {

        return new Gson().fromJson(

                        json
                        .getAsJsonObject()
                        .get("results").getAsJsonArray(), listType);
    }
}