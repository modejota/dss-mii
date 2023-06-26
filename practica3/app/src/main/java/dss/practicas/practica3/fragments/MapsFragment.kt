package dss.practicas.practica3.fragments

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import dss.practicas.practica3.R
import dss.practicas.practica3.activities.MainActivity


class MapsFragment : Fragment()  {

    @SuppressLint("MissingPermission")
    private val realTimeCallback = OnMapReadyCallback { googleMap ->

        if ((ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) ||
            (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED)){
            googleMap.isMyLocationEnabled = true
            val locationManager = activity?.getSystemService(Context.LOCATION_SERVICE) as LocationManager
            val location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
            if (location != null) {
                val myLocation = LatLng(location.latitude, location.longitude)
                googleMap.addMarker(MarkerOptions().position(myLocation).title("Mi tienda favorita"))
                googleMap.moveCamera(CameraUpdateFactory.newLatLng(myLocation))
                val campos = CameraPosition.Builder()
                    .target(myLocation)
                    .zoom(17F)
                    .bearing(90F)
                    .tilt(30F)
                    .build()
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(campos))
            } else {
                Toast.makeText(context, "No se ha podido obtener la posición", Toast.LENGTH_SHORT).show()
            }
        } else {
            val etsiit = LatLng(37.1969881, -3.6247262)
            googleMap.addMarker(MarkerOptions().position(etsiit).title("La tienda de la ETSIIT"))
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(etsiit))
            val campos = CameraPosition.Builder()
                .target(etsiit)
                .zoom(17F)
                .bearing(90F)
                .tilt(30F)
                .build()
            googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(campos))
            Toast.makeText(context, "Sin permisos redirigimos a la ETSIIT", Toast.LENGTH_SHORT).show()
        }

        googleMap.setOnMarkerClickListener { marker ->
            //Show an alert dialog with the marker info
            val alert = AlertDialog.Builder(context)
            alert.setTitle(marker.title)
            alert.setMessage("¿Quieres ir a la tienda de esta ubicación?")
            alert.setPositiveButton("Sí") { _, _ ->
                // Redirect to the main activity, so the user can see the list of products
                val intent = Intent(context, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
            }
            alert.setNegativeButton("No") { _, _ -> }
            alert.show()
            true
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_maps, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(realTimeCallback)
    }

}