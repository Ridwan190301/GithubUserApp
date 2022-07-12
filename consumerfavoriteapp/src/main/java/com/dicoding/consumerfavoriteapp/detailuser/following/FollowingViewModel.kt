package com.dicoding.consumerfavoriteapp.detailuser.following

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.consumerfavoriteapp.User
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONArray
import org.json.JSONObject
import java.util.*

class FollowingViewModel: ViewModel() {
    private val listUsers = MutableLiveData<ArrayList<User>>()
    val listData = ArrayList<User>()

    fun setFollowingUser(username: String) {
        val asyncClient = AsyncHttpClient()
        asyncClient.addHeader("Authorization", "token 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")
        asyncClient.addHeader("User-Agent", "request")

        asyncClient.get("https://api.github.com/users/$username/following",
            object : AsyncHttpResponseHandler() {
                override fun onSuccess(statusCode: Int, headers: Array<Header>,
                                       responseBody: ByteArray) {
                    try {
                        val result = String(responseBody)
                        val responseArray = JSONArray(result)

                        for (i in 0 until responseArray.length()) {
                            val usersObject = responseArray.getJSONObject(i)
                            val urlObject = usersObject.getString("url")
                            detailUser(urlObject)
                        }
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

    private fun detailUser(urlObject: String) {
        val asyncClient = AsyncHttpClient()
        asyncClient.addHeader("Authorization", "token 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")
        asyncClient.addHeader("User-Agent", "request")

        asyncClient.get(urlObject, object : AsyncHttpResponseHandler() {
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
                    listData.add(user)
                    listUsers.postValue(listData)
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

    fun getUsers(): LiveData<ArrayList<User>> {
        return listUsers
    }
}