package id.ac.unri.driverfit.ui.setting

import android.app.AlertDialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import id.ac.unri.driverfit.databinding.ChooseThemeDialogBinding
import id.ac.unri.driverfit.databinding.FragmentSettingsBinding
import id.ac.unri.driverfit.ui.AuthActivity

@AndroidEntryPoint
class SettingsFragment : Fragment() {

    private val viewModel: SettingViewModel by viewModels()

    private var _binding: FragmentSettingsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Initialize the binding
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)

        binding.btnThemeSetting.setOnClickListener {
            // Membuat dialog untuk memilih gambar dari camera atau gallery
            val pictureDialogBinding: ChooseThemeDialogBinding =
                ChooseThemeDialogBinding.inflate(LayoutInflater.from(context))

            val dialog: AlertDialog = AlertDialog.Builder(requireContext())
                .setView(pictureDialogBinding.root)
                .create()

            dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

            when (viewModel.darkTheme.value) {
                true -> pictureDialogBinding.rbLight.isChecked = true
                false -> pictureDialogBinding.rbDark.isChecked = true
                null -> pictureDialogBinding.rbSystemDefault.isChecked = true
            }

            pictureDialogBinding.rbSystemDefault.setOnClickListener {
                // Set theme to system default
                viewModel.darkTheme(null)
                dialog.dismiss()
            }

            pictureDialogBinding.rbLight.setOnClickListener {
                // Set theme to light
                viewModel.darkTheme(true)
                dialog.dismiss()
            }

            pictureDialogBinding.rbDark.setOnClickListener {
                // Set theme to dark
                viewModel.darkTheme(false)
                dialog.dismiss()
            }

            dialog.show()
        }

        binding.btnLogout.setOnClickListener {
            viewModel.signOut()
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.darkTheme.observe(viewLifecycleOwner) {
            when (it) {
                true -> {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                }

                false -> {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                }

                null -> {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
                }
            }
        }

        viewModel.user.observe(viewLifecycleOwner) {
            if (it == null) {
                val intent = Intent(requireContext(), AuthActivity::class.java)
                startActivity(intent)
                requireActivity().finish()
            }

        }

        viewModel.loading.observe(viewLifecycleOwner) {
            binding.circularProgressBar.visibility = if (it) View.VISIBLE else View.GONE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
