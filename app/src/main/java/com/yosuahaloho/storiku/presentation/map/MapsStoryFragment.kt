package com.yosuahaloho.storiku.presentation.map

import android.content.Context
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.location.Geocoder
import android.os.Bundle
import android.os.StrictMode
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.yosuahaloho.storiku.R
import com.yosuahaloho.storiku.data.local.entity.StoryData
import com.yosuahaloho.storiku.databinding.CustomPinWindowBinding
import com.yosuahaloho.storiku.databinding.FragmentMapsStoryBinding
import com.yosuahaloho.storiku.utils.DataMapper.storyDataToModel
import dagger.hilt.android.AndroidEntryPoint
import java.io.IOException
import java.net.HttpURLConnection
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
        googleMap.uiSettings.isMapToolbarEnabled = true

        mapStoryViewModel.getStoryFromDatabase().observe(viewLifecycleOwner) { data ->
            data.forEach {
                val lati = it.lat ?: 0.0
                val longi = it.lon ?: 0.0

                googleMap.addMarker(
                    MarkerOptions().position(LatLng(lati, longi))
                        .title(it.name)
                )?.tag = it
            }
        }
        googleMap.setInfoWindowAdapter(this)
        googleMap.setOnInfoWindowClickListener {
            val dataStory = it.tag as StoryData
            val modelData = dataStory.storyDataToModel()
            goToDetailStory(modelData)
        }

        setMapStyle()

        val indo = LatLng(-2.0, 118.0)
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(indo, 4f))
    }

    private fun setMapStyle() {
        try {
            val success = googleMap.setMapStyle(
                MapStyleOptions.loadRawResourceStyle(
                    requireContext(),
                    R.raw.map_style
                )
            )
            if (!success) {
                Log.e("Set Map Style", "Style parsing failed.")
            }
        } catch (e: Resources.NotFoundException) {
            Log.e("Set Map Style Exception", "Error: $e")
        }
    }

    private fun goToDetailStory(dataStory: com.yosuahaloho.storiku.domain.model.DetailStory) {
        val toDetailStory =
            MapsStoryFragmentDirections.actionMapsStoryFragmentToDetailStoryFragment(dataStory)
        findNavController().navigate(toDetailStory)
    }

    override fun getInfoContents(marker: Marker): View? {
        return null
    }

    override fun getInfoWindow(marker: Marker): View {
        val binding = CustomPinWindowBinding.inflate(layoutInflater)
        val dataStory = marker.tag as StoryData

        binding.pinLocation.text =
            getAddressName(marker.position.latitude, marker.position.longitude)
        binding.pinName.text = dataStory.name
        binding.pinImage.setImageBitmap(
            urlToBitmapWithStrictMode(
                requireContext(),
                dataStory.photoUrl
            )
        )

        binding.pinDescription.text = dataStory.description

        return binding.root
    }

    private fun getAddressName(latitude: Double, longitude: Double): String? {
        var addressName: String? = null
        val geocoder = Geocoder(requireActivity(), Locale.getDefault())
        try {
            val list = geocoder.getFromLocation(latitude, longitude, 1)
            if (list != null && list.size != 0) {
                val listAlamat = list.first()
                addressName = StringBuilder()
                    .append(listAlamat.featureName + ", ")
                    .append(listAlamat.subLocality + ", ")
                    .append(listAlamat.locality + ", ")
                    .append(listAlamat.subAdminArea + ", ")
                    .append(listAlamat.adminArea + ", ")
                    .append(listAlamat.countryName)
                    .toString()
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return addressName
    }

    private fun urlToBitmapWithStrictMode(context: Context, url: String): Bitmap {
        return try {
            val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
            StrictMode.setThreadPolicy(policy)
            val connection: HttpURLConnection = URL(url).openConnection() as HttpURLConnection
            connection.apply {
                doInput = true
                connect()
            }
            BitmapFactory.decodeStream(connection.inputStream)
        } catch (e: IOException) {
            BitmapFactory.decodeResource(context.resources, R.drawable.image_sample)
        }
    }


}