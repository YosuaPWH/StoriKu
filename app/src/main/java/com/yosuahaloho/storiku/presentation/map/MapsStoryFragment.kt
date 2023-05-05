package com.yosuahaloho.storiku.presentation.map

import android.graphics.BitmapFactory
import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.yosuahaloho.storiku.R
import com.yosuahaloho.storiku.data.local.entity.StoryData
import com.yosuahaloho.storiku.databinding.CustomPinWindowBinding
import com.yosuahaloho.storiku.databinding.FragmentMapsStoryBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException
import java.net.URL
import java.util.Locale

@AndroidEntryPoint
class MapsStoryFragment : Fragment(), OnMapReadyCallback, GoogleMap.InfoWindowAdapter {

    private var _binding: FragmentMapsStoryBinding? = null
    private val binding get() = _binding!!
    private lateinit var googleMap: GoogleMap
    private val mapStoryViewModel: MapsStoryViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMapsStoryBinding.inflate(inflater, container, false)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(this)

        val activityFragment = (requireActivity() as AppCompatActivity)
        activityFragment.supportActionBar?.hide()

        return binding.root
    }


    override fun onMapReady(gmap: GoogleMap) {
        googleMap = gmap
        googleMap.uiSettings.isCompassEnabled = true
        googleMap.uiSettings.isZoomControlsEnabled = true

        mapStoryViewModel.getStoryFromDatabase().observe(viewLifecycleOwner) { data ->
            data.forEach {
                val lati = it.lat ?: 0.0
                val longi = it.lon ?: 0.0

                googleMap.addMarker(
                    MarkerOptions().position(LatLng(lati, longi))
                        .title(it.name)
                        .snippet(getAddressName(lati, longi))
                )?.tag = it
            }
        }
        googleMap.setInfoWindowAdapter(this)
    }

    override fun getInfoContents(marker: Marker): View? {
        return null
    }

    override fun getInfoWindow(marker: Marker): View {
        val binding = CustomPinWindowBinding.inflate(layoutInflater)
        val dataStory: StoryData = marker.tag as StoryData

        binding.pinLocation.text =
            getAddressName(marker.position.latitude, marker.position.longitude)
        binding.pinName.text = dataStory.name
        Thread {
            run {
                try {
                    binding.pinImage.setImageBitmap(
                        BitmapFactory.decodeStream(
                            URL(dataStory.photoUrl).openConnection().getInputStream()
                        )
                    )
                } catch (e: Exception) {
                    Log.e("MapsStoryFragment", e.toString())
                }
            }
        }

        binding.pinDescription.text = dataStory.description

        return binding.root
    }

    private fun getAddressName(latitude: Double, longitude: Double): String? {
        var addressName: String? = null
        val geocoder = Geocoder(requireActivity(), Locale.getDefault())
        try {
            val list = geocoder.getFromLocation(latitude, longitude, 1)
            if (list != null && list.size != 0) {
                addressName = list[0].getAddressLine(0)
//                list[0].postalCode
                Log.d("GETADDRESS", "getAddrss: $addressName")
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return addressName
    }


}