package com.yosuahaloho.storiku.presentation.add_story

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.yosuahaloho.storiku.R
import com.yosuahaloho.storiku.databinding.FragmentAddStoryBinding
import com.yosuahaloho.storiku.utils.Result
import com.yosuahaloho.storiku.utils.reduceFileImage
import com.yosuahaloho.storiku.utils.rotateFile
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

@AndroidEntryPoint
class AddStoryFragment : Fragment() {

    private var _binding: FragmentAddStoryBinding? = null
    private val binding get() = _binding!!
    private var getFile: File? = null
    private val viewModel: AddStoryViewModel by viewModels()
    private lateinit var selfLocation: FusedLocationProviderClient
    private lateinit var locationCallback: LocationCallback


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        super.onCreate(savedInstanceState)
        _binding = FragmentAddStoryBinding.inflate(inflater, container, false)
        val activityFragment = (requireActivity() as AppCompatActivity)
        activityFragment.supportActionBar?.title = "Add Story"
        activityFragment.supportActionBar?.setDisplayHomeAsUpEnabled(true)

        selfLocation = LocationServices.getFusedLocationProviderClient(requireActivity())
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationRes: LocationResult) {
                viewModel.currentLocationLatitude.postValue(locationRes.lastLocation?.latitude)
                viewModel.currentLocationLongitude.postValue(locationRes.lastLocation?.longitude)
                uploadStory()
            }
        }


        val args = AddStoryFragmentArgs.fromBundle(requireArguments())

        getPicture(args)
        setupButton()

        return binding.root
    }

    private fun setupButton() {
        binding.btnSend.setOnClickListener {
            showDialogLocation()
        }
    }

    private fun showDialogLocation() {
        MaterialAlertDialogBuilder(
            requireContext(),
            R.style.ThemeOverlay_Material3_MaterialAlertDialog
        )
            .setMessage(R.string.upload_location)
            .setNeutralButton(getString(R.string.cancel)) { _, _ -> }
            .setPositiveButton(getString(R.string.yes)) { _, _ ->
                getMyLocation()
            }
            .setNegativeButton(getString(R.string.no)) { _, _ ->
                viewModel.makeLocationEmpty()
                uploadStory()
            }
            .show()
    }

    private val requestPermission = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        when {
            permissions[Manifest.permission.ACCESS_COARSE_LOCATION] ?: false -> {
                getMyLocation()
            }

            permissions[Manifest.permission.ACCESS_FINE_LOCATION] ?: false -> {
                getMyLocation()
            }

            else -> {}
        }
    }

    private fun getMyLocation() {
        if (checkPermission(Manifest.permission.ACCESS_COARSE_LOCATION) &&
            checkPermission(Manifest.permission.ACCESS_FINE_LOCATION)
        ) {
            if (isLocationOn()) {
                selfLocation.lastLocation.addOnCompleteListener(requireActivity()) { task ->
                    val location: Location? = task.result
                    if (location != null) {
                        viewModel.currentLocationLatitude.postValue(location.latitude)
                        viewModel.currentLocationLongitude.postValue(location.longitude)
                        uploadStory()
                    } else {
                        val locationRequest =
                            LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 10000).build()

                        selfLocation =
                            LocationServices.getFusedLocationProviderClient(requireActivity())
                        selfLocation.requestLocationUpdates(
                            locationRequest, locationCallback, Looper.myLooper()
                        )
                    }
                }
            } else {
                Toast.makeText(requireContext(), "Please turn on location", Toast.LENGTH_SHORT)
                    .show()
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)
            }
        } else {
            requestPermission.launch(
                arrayOf(
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            )
        }
    }

    private fun checkPermission(permission: String): Boolean {
        return ContextCompat.checkSelfPermission(
            requireContext(),
            permission
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun getPicture(args: AddStoryFragmentArgs) {
        val picturePath = args.picture
        val isBackCamera = args.isBackCamera
        val isFromCamera = args.isFromCamera

        val imageFile = File(picturePath)

        imageFile.also {
            if (isFromCamera) {
                rotateFile(it, isBackCamera)
            }
            getFile = it
            binding.previewImage.setImageBitmap(BitmapFactory.decodeFile(it.path))
        }
    }

    private fun uploadStory() {
        if (getFile != null) {
            val file = reduceFileImage(getFile as File)
            val description =
                binding.inputDescription.text.toString().toRequestBody("text/plain".toMediaType())

            val requestImageFile = file.asRequestBody("image/*".toMediaTypeOrNull())
            val imageMultiPart =
                MultipartBody.Part.createFormData("photo", file.name, requestImageFile)

            val latViewModel = viewModel.currentLocationLatitude.value.toString()
            val lonViewModel = viewModel.currentLocationLongitude.value.toString()

            val isLatNull = if (latViewModel == "0.0") null else latViewModel
            val isLonNull = if (lonViewModel == "0.0") null else lonViewModel

            val lat = isLatNull?.toRequestBody("text/plain".toMediaType())
            val lon = isLonNull?.toRequestBody("text/plain".toMediaType())

            lifecycleScope.launch {
                viewModel.uploadStory(imageMultiPart, description, lat, lon).collect {
                    when (it) {
                        is Result.Success -> {
                            binding.progressLayout.visibility = View.INVISIBLE
                            Toast.makeText(
                                requireActivity(),
                                resources.getString(R.string.success_add_new_story),
                                Toast.LENGTH_SHORT
                            )
                                .show()

                            val toListFragment =
                                AddStoryFragmentDirections.actionAddStoryFragmentToListStoryFragment()
                            toListFragment.isFromAddStory = true
                            findNavController().navigate(toListFragment)
                        }

                        is Result.Loading -> {
                            binding.progressLayout.visibility = View.VISIBLE
                        }

                        is Result.Error -> {
                            binding.progressLayout.visibility = View.INVISIBLE
                            val errorMessage =
                                if (it.error == "\"description\" is not allowed to be empty") {
                                    resources.getString(R.string.error_fill_description)
                                } else {
                                    it.error
                                }
                            Toast.makeText(requireActivity(), errorMessage, Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
                }
            }
        }
    }

    private fun isLocationOn(): Boolean {
        val locationManager =
            requireActivity().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        setHasOptionsMenu(true)
    }

    @Deprecated("Deprecated in Java")
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                (requireActivity() as AppCompatActivity).onBackPressedDispatcher.onBackPressed()
            }
        }
        return super.onOptionsItemSelected(item)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}