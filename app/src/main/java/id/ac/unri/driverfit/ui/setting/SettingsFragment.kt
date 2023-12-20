package id.ac.unri.driverfit.ui.setting

import android.app.AlertDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import id.ac.unri.driverfit.databinding.ChooseThemeDialogBinding
import id.ac.unri.driverfit.databinding.FragmentSettingsBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SettingsFragment : Fragment() {

    private val viewModel: SettingViewModel by viewModels()

    private lateinit var binding: FragmentSettingsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Initialize the binding
        binding = FragmentSettingsBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launch {
            viewModel.darkTheme.collectLatest {
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
        }

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
    }

}
