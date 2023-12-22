package id.ac.unri.driverfit.ui.maps

import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.AndroidEntryPoint
import id.ac.unri.driverfit.R
import id.ac.unri.driverfit.data.remote.payload.Place
import id.ac.unri.driverfit.databinding.FragmentMapSuggestionsBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch


@AndroidEntryPoint
class MapsFragment : Fragment(), MenuProvider, MapsAdapter.OnItemClickListener {

    private val viewModel: MapsViewModel by viewModels()

    private val adapter by lazy { MapsAdapter() }

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            viewModel.loadPlacesIfPermissionGranted(isGranted)
            if (isGranted) {
                lifecycleScope.launch(viewModel.viewModelScope.coroutineContext) {
                    viewModel.places.collectLatest {
                        val places = it.first()
                        adapter.submitData(places)
                    }
                }
            } else {
                AlertDialog.Builder(requireContext())
                    .setTitle("Permission Required")
                    .setMessage("You must allow this permission to use this feature")
                    .setPositiveButton("OK") { _, _ ->
                        getMyLocation()
                    }
                    .setNegativeButton("Cancel") { dialog, _ ->
                        dialog.dismiss()
                    }
                    .create()
                    .show()
            }
        }

    private var _binding: FragmentMapSuggestionsBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMapSuggestionsBinding.inflate(inflater, container, false)

        adapter.setOnItemClickListener(this)

        binding.rvNearestPlaces.adapter = adapter

        getMyLocation()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(this, viewLifecycleOwner, Lifecycle.State.RESUMED)

        adapter.addLoadStateListener {
            when (it.refresh) {
                is androidx.paging.LoadState.Loading -> {
                    binding.progressIndicator.visibility = View.VISIBLE
                }

                is androidx.paging.LoadState.Error -> {
                    binding.progressIndicator.visibility = View.GONE
                }

                is androidx.paging.LoadState.NotLoading -> {
                    binding.progressIndicator.visibility = View.GONE
                }
            }

        }

    }

    private fun getMyLocation() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            viewModel.loadPlacesIfPermissionGranted(true)
            lifecycleScope.launch(viewModel.viewModelScope.coroutineContext) {
                viewModel.places.collectLatest {
                    val places = it.first()
                    adapter.submitData(places)
                }
            }
        } else {
            requestPermissionLauncher.launch(android.Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.maps_menu, menu)
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        return when (menuItem.itemId) {
            R.id.action_filter_place -> {
                true
            }

            else -> false
        }
    }

    override fun onItemClick(items: Place) {
        // intent to google maps
        openGoogleMaps(items)
    }

    private fun openGoogleMaps(place: Place) {

        // Create a Uri for the Google Maps query
        val uri =
            Uri.parse("https://www.google.com/maps/search/?api=1&query=a&query_place_id=${place.placeId}")

        // Create an Intent to open Google Maps
        val mapIntent = Intent(Intent.ACTION_VIEW, uri)
        mapIntent.setPackage("com.google.android.apps.maps")
        if (mapIntent.resolveActivity(requireActivity().packageManager) != null) {
            startActivity(mapIntent)
        }

    }
}