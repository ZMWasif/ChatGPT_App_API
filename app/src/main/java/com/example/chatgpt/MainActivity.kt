package com.example.chatgpt

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException

class MainActivity : ComponentActivity() {
    private val client = OkHttpClient()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val etQuestion = findViewById<EditText>(R.id.etQuestions)
        val btnSubmit = findViewById<Button>(R.id.btnSubmit)
        val textResponse = findViewById<TextView>(R.id.textResponse)

        btnSubmit.setOnClickListener{
            val question= etQuestion.text.toString()
            Toast.makeText(this,question,Toast.LENGTH_SHORT).show()
            getResponse(question){
                response -> runOnUiThread {
textResponse.text = response
            }
            }
        }
    }

    fun getResponse(question: String, callback: (String) -> Unit){
        val apiKey = "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"
val url = "https://api.openai.com/v1/completions"
        val requestBody= """{
            "model": "text-davinci-003",
            "prompt": "$question",
            "max_tokens": 500,
            "temperature": 0
 }
        """.trimIndent()

        val request = Request.Builder()
            .url(url)
            .addHeader("Content-Type", "application/json;")
            .addHeader("Authorization", "Bearer $apiKey")
            .post(requestBody.toRequestBody("application/json".toMediaTypeOrNull()))
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("error", "API failed",e)
            }

            override fun onResponse(call: Call, response: Response) {
                val body= response.body?.string()
                if (body != null) {
                    Log.v("Data", body)
                }
                else
                {
                    Log.v("Data", "empty")
                }
                val jsonObject= JSONObject(body)
                val jsonArray: JSONArray= jsonObject.getJSONArray("choices")
                val textResult = jsonArray.getJSONObject(0).getString("text")
                callback(textResult)
            }

        })
        }
    }



