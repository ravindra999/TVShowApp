package com.themoviedb.weektvshow.utils

import com.google.gson.ExclusionStrategy
import com.google.gson.FieldAttributes

class ExcludeSerializationStrategy : ExclusionStrategy {

    override fun shouldSkipField(f: FieldAttributes?): Boolean {
        val exclude: Exclude? = f?.getAnnotation(Exclude::class.java)
        return exclude != null && exclude.serialize
    }

    override fun shouldSkipClass(clazz: Class<*>?): Boolean {
        return false
    }
}

class ExcludeDeserializationStrategy : ExclusionStrategy {

    override fun shouldSkipField(f: FieldAttributes?): Boolean {
        val exclude: Exclude? = f?.getAnnotation(Exclude::class.java)
        return exclude != null && exclude.deserialize
    }

    override fun shouldSkipClass(clazz: Class<*>?): Boolean {
        return false
    }
}