package id.ac.unri.driverfit.ui.signin

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
import id.ac.unri.driverfit.databinding.FragmentSigninBinding
import id.ac.unri.driverfit.ui.MainActivity

@AndroidEntryPoint
class SigninFragment : Fragment() {

    private val viewModel: SigninViewModel by viewModels()

    private var _binding: FragmentSigninBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentSigninBinding.inflate(inflater, container, false)

        viewModel.isLoogedIn.observe(viewLifecycleOwner) {
            if (it != null) {
                val intent = Intent(requireContext(), MainActivity::class.java)
                startActivity(intent)
                requireActivity().finish()
            }
        }

        with(binding) {
            bindViewModel = viewModel
            emailHint.setErrorIconDrawable(0)
            passwordHint.setErrorIconDrawable(0)
            observeError(viewModel.email, emailHint)
            observeError(viewModel.password, passwordHint)
        }

        viewModel.fulfilled.observe(viewLifecycleOwner) {
            binding.btnSignin.isEnabled = it
        }

        binding.btnSignin.setOnClickListener {
            viewModel.signIn()
        }

        viewModel.loading.observe(viewLifecycleOwner) {
            binding.circularProgressBar.visibility = if (it) View.VISIBLE else View.GONE
        }

        viewModel.snackbar.observe(viewLifecycleOwner) {
            Snackbar.make(binding.root, it, Snackbar.LENGTH_SHORT).show()
        }

        binding.btnNavigateToSignUp.setOnClickListener {
            findNavController().navigate(SigninFragmentDirections.actionLoginFragmentToSignupFragment())
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