package com.f90.telegram.bot.memorygymbot.util;

import com.f90.telegram.bot.memorygymbot.exception.InternalException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.MapType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Utility class for parsing json objects using Jackson data binding library
 */
public final class JsonUtil {

    private static ObjectMapper objectMapper;

    private JsonUtil() {
        throw new InternalException("Can't instantiate class JsonUtil");
    }

    /**
     * Creates an {@link ObjectMapper} for mapping json objects. Mapper can be configured here
     *
     * @return created {@link ObjectMapper}
     */
    public static ObjectMapper getMapper() {
        if (objectMapper == null) {
            objectMapper = new ObjectMapper();
            objectMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            objectMapper.registerModule(new JavaTimeModule());

        }
        return objectMapper;
    }

    /**
     * Maps json string to specified class
     *
     * @param <T>    generic parameter for tClass
     * @param json   string to parse
     * @param tClass class of object in which json will be parsed
     * @return mapped T class instance
     */
    public static <T> T toObject(String json, Class<T> tClass) {
        try {
            return getMapper().readValue(json, tClass);
        } catch (IOException e) {
            throw new InternalException("Error during conversion from json to object", e);
        }
    }

    /**
     * Writes specified object as string
     *
     * @param object object to write
     * @return result json
     */
    public static String toJson(Object object) {
        try {
            return getMapper().writeValueAsString(object);
        } catch (IOException e) {
            throw new InternalException("Error during conversion from object to json", e);
        }
    }

    /**
     * Maps json string to {@link ArrayList} of specified class object instances
     *
     * @param <T>    generic parameter for tClass
     * @param json   string to parse
     * @param tClass class of object in which json will be parsed
     * @return mapped T class instance
     */
    public static <T> List<T> arrayList(String json, Class<T> tClass) {
        try {
            TypeFactory typeFactory = getMapper().getTypeFactory();
            JavaType type = typeFactory.constructCollectionType(ArrayList.class, tClass);
            return getMapper().readValue(json, type);
        } catch (IOException e) {
            throw new InternalException("Error during conversion from Object to List", e);
        }
    }

    /**
     * Read json string to JsonNode
     *
     * @param json JSON String
     * @return JsonNode json node
     */
    public static JsonNode jsonToTree(String json) {
        try {
            return getMapper().readTree(json);
        } catch (IOException e) {
            throw new InternalException("Error during conversion from json to JsonNode", e);
        }
    }

    /**
     * Convert a json in Map
     *
     * @param <K>        generic class of object
     * @param <V>        generic value  of object
     * @param json       string to parse
     * @param keyClass   class of object
     * @param valueClass value of object
     * @return mapped Map class instance
     */
    public static <K, V> Map<K, V> toMap(String json, Class<K> keyClass, Class<V> valueClass) {
        try {
            MapType type = getMapper().getTypeFactory().constructMapType(HashMap.class, keyClass, valueClass);
            return getMapper().readValue(json, type);
        } catch (IOException e) {
            throw new InternalException("Error during conversion from json to JsonNode", e);
        }
    }

}
