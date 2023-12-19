package id.ac.unri.driverfit.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import id.ac.unri.driverfit.R
import id.ac.unri.driverfit.databinding.FragmentHomeBinding
import id.ac.unri.driverfit.ui.MainActivity

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private val viewModel: HomeViewModel by viewModels()

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        viewModel.user.observe(viewLifecycleOwner) {
            (requireActivity() as MainActivity).title = "Halo, ${it?.name}"
            Glide.with(requireContext())
                .load(it?.photo)
                .placeholder(R.drawable.profile_image_placeholder)
                .into(binding.civPicture)
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}