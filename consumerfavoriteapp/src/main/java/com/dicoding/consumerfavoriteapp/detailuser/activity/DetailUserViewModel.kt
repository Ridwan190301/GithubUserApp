package com.dicoding.consumerfavoriteapp.detailuser.activity

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.consumerfavoriteapp.User
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONObject

class DetailUserViewModel: ViewModel() {
    private val detailUsers = MutableLiveData<User>()
    
    fun setDetailUser(login: String) {
        val asyncClient = AsyncHttpClient()
        asyncClient.addHeader("Authorization", "token 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")
        asyncClient.addHeader("User-Agent", "request")

        asyncClient.get("https://api.github.com/users/$login", object : AsyncHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Array<Header>,
                                   responseBody: ByteArray) {
                try {
                    val result = String(responseBody)
                    val responseObject = JSONObject(result)
                    val user = User(
                        0,
                        responseObject.getString("name"),
                        responseObject.getString("login"),
                        responseObject.getString("avatar_url"),
                        responseObject.getString("company"),
                        responseObject.getString("location"),
                        responseObject.getInt("public_repos"),
                        responseObject.getInt("followers"),
                        responseObject.getInt("following")
                    )
                    detailUsers.postValue(user)
                } catch (e: Exception) {
                    Log.d("Exception", e.message.toString())
                }
            }

            override fun onFailure(
                statusCode: Int, headers: Array<Header>,
                responseBody: ByteArray, error: Throwable
            ) {
                Log.d("onFailure", error.message.toString())
            }

        })
    }

    fun getDetailUsers(): LiveData<User> {
        return detailUsers
    }
}