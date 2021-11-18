package com.resurrection.popuptranslator.data.remote

import kotlinx.coroutines.*
import org.json.JSONArray
import org.json.JSONException
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder

class TranslateAPI(
    private var langFrom: String?,
    private var langTo: String?,
    private var text: String?,
    private var listener: TranslateListener?
) {

    var resp: String? = null
    var url: String? = null

    interface TranslateListener {
        fun onSuccess(translatedText: String?)
        fun onFailure(ErrorText: String?)
    }

    init {
        setupTranslate()
    }

    private fun setupTranslate() {
        CoroutineScope(Dispatchers.IO).launch {
            val request = async(Dispatchers.IO) {
                listener?.let {
                    try {
                        url = "https://translate.googleapis.com/translate_a/single?" +
                                "client=gtx&" + "sl=" + langFrom + "&tl=" + langTo + "&dt=t&q=" +
                                URLEncoder.encode(text, "UTF-8")

                        val connection = URL(url).openConnection() as HttpURLConnection
                        connection.setRequestProperty("User-Agent", "Mozilla/5.0")
                        val bufferedReader =
                            BufferedReader(InputStreamReader(connection.inputStream))
                        var inputLine: String?
                        val response = StringBuffer()
                        while (bufferedReader.readLine().also { inputLine = it } != null) {
                            response.append(inputLine)
                        }
                        bufferedReader.close()
                        resp = response.toString()
                    } catch (e: Exception) {
                    }
                }
            }
            request.await()
            withContext(Dispatchers.Main) {
                var temp = ""
                if (resp == null) {
                    listener!!.onFailure("Network Error")
                } else {
                    try {
                        withContext(Dispatchers.IO) {
                            val total = JSONArray(resp)[0] as JSONArray
                            for (i in 0 until total.length()) {
                                val currentLine = total[i] as JSONArray
                                temp += currentLine[0].toString()
                            }
                        }
                        if (temp.length > 2) {
                            listener!!.onSuccess(temp)
                        } else {
                            listener!!.onFailure("Invalid Input String")
                        }
                    } catch (e: JSONException) {
                        listener!!.onFailure(e.localizedMessage)
                        e.printStackTrace()
                    }
                }
            }
        }
    }
}