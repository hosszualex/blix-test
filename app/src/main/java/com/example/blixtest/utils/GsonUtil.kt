package com.example.blixtest.utils

import com.google.gson.Gson
import java.lang.reflect.Type

object GsonUtil {
    private val gson = Gson()

    fun toJson(objectType: Any?): String {
        return gson.toJson(objectType) ?: ""
    }

    fun fromJsonObject(json: String?, classType: Type): Any? {
        if (json.isNullOrEmpty()) {
            return null
        }
        return gson.fromJson(json, classType)
    }
// TODO using the TypeToken for arrays
//    fun fromJsonArrayObject(json: String): ArrayList<Any> {
//
//    }
}
