package petrov.ivan.deliverymobile.ui.activity.delivery

import android.os.Bundle
import com.arellomobile.mvp.presenter.InjectPresenter
import com.fasterxml.jackson.core.type.TypeReference
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
import petrov.delivery.webapi.CompanyPlace
import petrov.ivan.deliverymobile.R
import petrov.ivan.deliverymobile.presentation.presenter.delivery.CompanyOnMapPresenter
import petrov.ivan.deliverymobile.presentation.view.delivery.CompanyOnMapView
import petrov.ivan.deliverymobile.ui.base.BaseActivity


class CompanyOnMapActivity : BaseActivity(), CompanyOnMapView, OnMapReadyCallback {

    @InjectPresenter
    lateinit var presenter: CompanyOnMapPresenter
    private lateinit var companyPlaces: List<CompanyPlace>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_company_on_map)

        intent.extras?.let {
            if (it.containsKey(PARAM_COMPANY_PLACE)) {
                it.getString(PARAM_COMPANY_PLACE)?.let { str ->
                    companyPlaces = deliveryComponents.getObectMapper().readValue(str, object: TypeReference<List<CompanyPlace>>(){})
                }
            }
        }

        // set map fragment
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    fun showMarkerOnMap(googleMap: GoogleMap) {
        val builder = LatLngBounds.Builder()
        companyPlaces.forEach {
            val latLng = LatLng(it.coords.lat, it.coords.lon)
            builder.include(latLng)

            addMarkerToMap(googleMap, latLng, it)
        }
        val bounds = builder.build()

        googleMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, (50 * resources.displayMetrics.density).toInt()))
    }

    private fun addMarkerToMap(googleMap: GoogleMap, latLng: LatLng, it: CompanyPlace) {
        googleMap.addMarker(
            MarkerOptions()
                .position(latLng)
                .title(it.address)
        )
    }

    override fun onMapReady(googleMap: GoogleMap) {
        showMarkerOnMap(googleMap)
    }

    companion object {
        const val TAG = "CompanyAdrOnMap"
        const val PARAM_COMPANY_PLACE = "company_place"
    }
}
