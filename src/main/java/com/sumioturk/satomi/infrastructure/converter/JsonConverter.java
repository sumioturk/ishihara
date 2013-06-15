package com.sumioturk.satomi.infrastructure.converter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

/**
 * Json converter interface
 */
public interface JsonConverter<T> {

    /**
     * Convert obj to a json
     *
     * @param obj {@link T} object
     * @return {@link JSONObject} output json
     */
    JSONObject toJson(T obj);

    /**
     * Construct object from a json
     *
     * @param json {@link JSONObject} json input
     * @return {@link T} output object
     */
    T fromJson(JSONObject json);

    /**
     * Convert obj to String
     * @param obj {@link T}
     * @return JSON in text representation
     */
    String toJsonString(T obj);


    /**
     * Convert JSON String to object
     * @param string
     * @return {@link T}
     */
    T fromJsonString(String string);


    /**
     * Convert array of obj to JSONArray of corresponding JSONObject
     * @param obj
     * @return
     */
    JSONArray toJsonArray(List<T> obj);


    /**
     * Convert JSONArray to corresponding list of obj
     * @param jsonArray
     * @return
     */
    List<T> fromJsonArray(JSONArray jsonArray);

}
