package dss.practicas.practica3.activities

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.*
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import dss.practicas.practica3.singleton.SharedData
import dss.practicas.practica3.databinding.ActivityLoginBinding
import org.json.JSONObject
import java.io.UnsupportedEncodingException


class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val url = "http://10.0.2.2:8080/rest/login"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.loginButton.setOnClickListener{
            val username = binding.username.text.toString()
            val password = binding.password.text.toString()
            login(username, password)
        }

    }

    private fun login(username: String, password: String) {
        // Create Volley RequestQueue
        val queue = Volley.newRequestQueue(this)

        // Our data
        val jsonBody = JSONObject()
        jsonBody.put("email", username)
        jsonBody.put("password", password)
        val requestBody = jsonBody.toString()

        // Create request itself. Quite an overrided constructor
        val jsonObjectRequest: JsonObjectRequest =
            object : JsonObjectRequest(
                Method.POST, url, null,
                Response.Listener {
                    Toast.makeText(this, "Inicio de sesión correcto", Toast.LENGTH_SHORT).show()
                    // Launch new activity
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                },
                Response.ErrorListener { response ->
                    val alert = AlertDialog.Builder(this)
                    alert.setTitle("Error")
                    alert.setMessage("El nombre de usuario y/o la contraseña son incorrectos")
                    alert.setPositiveButton("OK", null)
                    alert.show()
                    Log.i("Error", response.toString())
                }) {

                override fun getBodyContentType(): String {
                    return "application/json; charset=utf-8"
                }

                // Encode our data to be sent
                override fun getBody(): ByteArray {
                    return try {
                        requestBody.toByteArray(charset("utf-8"))
                    } catch (uee: UnsupportedEncodingException) {
                        return null!!
                    }
                }

                // Get cookies / JWT / whatever from response
                override fun parseNetworkResponse(response: NetworkResponse?): Response<JSONObject> {
                    var newResponse = NetworkResponse(
                        response?.statusCode!!,
                        response.data,
                        response.headers,
                        response.notModified
                    )
                    try {
                        // In some APIs, AUTH response body is empty, which triggers an exception in Volley, so we kind of bypass it
                        if (response.data.size === 0) {
                            val responseData = "{}".toByteArray(charset("UTF8"))
                            newResponse = NetworkResponse(
                                response.statusCode,
                                responseData,
                                response.headers,
                                response.notModified
                            )
                        }
                        // Save JWT in our singleton across the app
                        SharedData.setAuthToken(response.headers?.get("Authorization")!!)

                    } catch (e: UnsupportedEncodingException) {
                        e.printStackTrace()
                    }
                    return super.parseNetworkResponse(newResponse)
                }

            }
        // Send request
        queue.add(jsonObjectRequest)
    }

}

