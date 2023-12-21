package id.ac.unri.driverfit.ui.detection

import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import id.ac.unri.driverfit.databinding.FragmentDetectionBinding
import id.ac.unri.driverfit.ui.CameraActivity
import id.ac.unri.driverfit.ui.utils.cameraPermissionRequest
import id.ac.unri.driverfit.ui.utils.isPermissionGranted
import id.ac.unri.driverfit.ui.utils.openPermissionSetting
import java.io.File

@AndroidEntryPoint
class DetectionFragment : Fragment() {

    private val viewModel: DetectionViewModel by viewModels()

    private val cameraPermission = android.Manifest.permission.CAMERA
    private var _binding: FragmentDetectionBinding? = null

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                requestCameraAndStart(cameraLauncher)
                Snackbar.make(
                    requireView(),
                    "Camera permission granted",
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        }

    private lateinit var cameraLauncher: ActivityResultLauncher<Intent>

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        cameraLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult(),
            ::onCameraResult
        )

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetectionBinding.inflate(inflater, container, false)

        binding.btnRetake.setOnClickListener {
            requestCameraAndStart(cameraLauncher)
        }

        binding.btnFindPlace.setOnClickListener {
            findNavController().navigate(DetectionFragmentDirections.actionNavigationDetectionToNavigationMaps())
        }

        viewModel.image.observe(viewLifecycleOwner) {
            if (it != null) {
                val bmp = BitmapFactory.decodeFile(it.path)
                binding.civPicture.setImageBitmap(bmp)
                return@observe
            }
            requestCameraAndStart(cameraLauncher)
        }

        viewModel.result.observe(viewLifecycleOwner) {
            if (it != null && it.scores > 0.5F) {
                binding.tvResultMessage.text = "You are sleepy"
            } else {
                binding.tvResultMessage.text = "You are not sleepy"
            }
        }

        viewModel.loading.observe(viewLifecycleOwner) {
            binding.circularProgressBar.visibility = if (it) View.VISIBLE else View.GONE
        }

        return binding.root
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun requestCameraAndStart(cameraLauncher: ActivityResultLauncher<Intent>) {
        if (requireContext().isPermissionGranted(cameraPermission)) {
            val intent = Intent(requireContext(), CameraActivity::class.java)
            cameraLauncher.launch(intent)
        } else {
            requestCameraPermission()
        }
    }

    private fun requestCameraPermission() {
        when {
            shouldShowRequestPermissionRationale(cameraPermission) -> {
                requireContext().cameraPermissionRequest(
                    positive = { requireContext().openPermissionSetting() }
                )
            }

            else -> {
                requestPermissionLauncher.launch(cameraPermission)
            }
        }
    }

    private fun onCameraResult(result: ActivityResult) {
        if (result.resultCode != CameraActivity.RESULT_SUCCESS) {
            return
        }

        val data = result.data
        val imageFile = data?.getSerializableExtra(CameraActivity.KEY_IMAGE_RESULT) as File

        viewModel.setResult(imageFile)
    }
}