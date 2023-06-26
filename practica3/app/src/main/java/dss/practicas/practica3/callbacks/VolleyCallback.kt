package dss.practicas.practica3.callbacks

// When Volley finishes the request, it will call the onSuccess() or onError() methods,
// we receive the trigger on the calling function and do something depending on the result
interface VolleyCallback {
    fun onSuccess()
    fun onError()
}