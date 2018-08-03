package cn.com.thinkwatch.ihass2.api

import android.content.Context
import android.support.v4.app.Fragment
import cn.com.thinkwatch.ihass2.HassApplication
import cn.com.thinkwatch.ihass2.dto.ServiceRequest
import cn.com.thinkwatch.ihass2.model.JsonEntity
import cn.com.thinkwatch.ihass2.model.Period
import cn.com.thinkwatch.ihass2.model.service.Domain
import io.reactivex.Observable
import retrofit2.Call
import retrofit2.http.*
import java.util.*

interface RestApi {
    @GET("/api/bootstrap")
    fun rawBootstrap(@Header("x-ha-access") password: String): Call<String>

    @GET("/api/states")
    fun rawStates(@Header("x-ha-access") password: String): Observable<String>

    @GET("/api/states")
    fun getStates(@Header("x-ha-access") password: String): Observable<ArrayList<JsonEntity>>

    @GET("/api/services")
    fun getServices(@Header("x-ha-access") password: String): Observable<ArrayList<Domain>>

    @GET("/api/states/{entityId}")
    fun getState(@Header("x-ha-access") password: String, @Path("entityId") entityId: String): Observable<JsonEntity>

    @POST("/api/services/{domain}/{service}")
    fun callService(@Header("x-ha-access") password: String?, @Path("domain") domain: String?, @Path("service") service: String?, @Body json: ServiceRequest): Observable<ArrayList<JsonEntity>>

    @POST("/api/states/{entityId}")
    fun setState(@Header("x-ha-access") password: String?, @Path("entityId") entityId: String?, @Body json: JsonEntity): Observable<JsonEntity?>

    @GET("/api/history/period/{timestamp}")
    fun getHistory(@Header("x-ha-access") password: String, @Path("timestamp") timestamp: String?, @Query("filter_entity_id") entityId: String? = null): Observable<ArrayList<ArrayList<Period>>>
//
//    @GET("/api/logbook/{timestamp}")
//    fun getLogbook(@Header("x-ha-access") password: String, @Path("timestamp") domain: String): Call<ArrayList<LogSheet>>

    companion object {
        var jsonApi: RestApi? = null
        get() {
            if (field == null) field = BaseApi.api(HassApplication.application.haHostUrl)
            return field
        }
    }
}

inline val Context.hassApi: RestApi
    get() = RestApi.jsonApi!!
inline val Fragment.hassApi: RestApi
    get() = RestApi.jsonApi!!