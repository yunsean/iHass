package cn.com.thinkwatch.ihass2.dto

import cn.com.thinkwatch.ihass2.model.JsonEntity
import cn.com.thinkwatch.ihass2.utils.Gsons
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import java.util.*

data class AutomationResponse(
        @SerializedName("result") var result: String? = null,
        @SerializedName("message") var message: String? = null)
