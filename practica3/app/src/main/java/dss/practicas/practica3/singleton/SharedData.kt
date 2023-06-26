package dss.practicas.practica3.singleton

// Singleton class to save the JWT auth token for the user
object SharedData {
    private var authToken: String = ""

    fun setAuthToken(token: String) {
        authToken = token
    }

    fun getAuthToken(): String {
        return authToken
    }
}