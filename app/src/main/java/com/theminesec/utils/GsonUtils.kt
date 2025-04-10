package com.theminesec.utils

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import java.lang.reflect.Type
import java.time.Instant

object GsonUtils {
    val gson: Gson by lazy {
        GsonBuilder()
            .registerTypeAdapter(Instant::class.java, object : JsonSerializer<Instant> {
                override fun serialize(p0: Instant?, p1: Type?, p2: JsonSerializationContext?): com.google.gson.JsonElement =
                    com.google.gson.JsonPrimitive(p0?.toString())
            })
            .setPrettyPrinting().create()
    }
}