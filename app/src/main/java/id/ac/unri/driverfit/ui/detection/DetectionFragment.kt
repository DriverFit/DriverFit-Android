package id.ac.unri.driverfit.ui.detection

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import id.ac.unri.driverfit.databinding.FragmentDetectionBinding

class DetectionFragment : Fragment() {

    private var _binding: FragmentDetectionBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val detectionViewModel =
            ViewModelProvider(this)[DetectionViewModel::class.java]

        _binding = FragmentDetectionBinding.inflate(inflater, container, false)

        val textView: TextView = binding.textDashboard
        detectionViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}