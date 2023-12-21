package id.ac.unri.driverfit.ui.signup

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.LiveData
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout
import dagger.hilt.android.AndroidEntryPoint
import id.ac.unri.driverfit.databinding.FragmentSignupBinding
import id.ac.unri.driverfit.ui.MainActivity

@AndroidEntryPoint
class SignupFragment : Fragment() {

    private val viewModel: SignUpViewModel by viewModels()

    private var _binding: FragmentSignupBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentSignupBinding.inflate(inflater, container, false)

        with(binding) {
            nameHint.setErrorIconDrawable(0)
            emailHint.setErrorIconDrawable(0)
            passwordHint.setErrorIconDrawable(0)
            konfirmasiPasswordHint.setErrorIconDrawable(0)
            observeError(viewModel.name, nameHint)
            observeError(viewModel.email, emailHint)
            observeError(viewModel.password, passwordHint)
            observeError(viewModel.confirmPassword, konfirmasiPasswordHint)
        }

        binding.bindViewModel = viewModel

        binding.btnNavigateToLogin.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.btnSignup.setOnClickListener {
            viewModel.signUp()
        }

        viewModel.isLoogedIn.observe(viewLifecycleOwner) {
            if (it != null) {
                val intent = Intent(requireContext(), MainActivity::class.java)
                startActivity(intent)
                requireActivity().finish()
            }
        }

        viewModel.snackbar.observe(viewLifecycleOwner) {
            Snackbar.make(binding.root, it, Snackbar.LENGTH_SHORT).show()
        }

        viewModel.fulfilled.observe(viewLifecycleOwner) {
            binding.btnSignup.isEnabled = it
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

    private fun observeError(
        liveData: LiveData<Pair<String, Int?>>,
        errorView: TextInputLayout
    ) {
        liveData.observe(viewLifecycleOwner) { (_, errorMessageResId) ->
            errorView.error = errorMessageResId?.let { getString(it) }
        }
    }
}