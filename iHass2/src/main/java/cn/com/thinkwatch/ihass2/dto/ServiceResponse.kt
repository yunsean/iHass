package cn.com.thinkwatch.ihass2.dto

import cn.com.thinkwatch.ihass2.model.JsonEntity
import cn.com.thinkwatch.ihass2.utils.Gsons
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import java.util.*

data class ServiceResponse(@SerializedName("states") var states: ArrayList<JsonEntity>? = null) {
    override fun toString(): String = Gsons.gson.toJson(this)
}
