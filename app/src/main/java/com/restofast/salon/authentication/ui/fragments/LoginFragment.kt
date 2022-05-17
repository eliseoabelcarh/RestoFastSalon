package com.restofast.salon.authentication.ui.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.textfield.TextInputLayout
import com.restofast.salon.authentication.ui.viewmodels.LoginViewModel
import com.restofast.salon.databinding.LoginFragmentBinding
import com.restofast.salon.orderTaking.ui.activities.ActivityOrderTaking
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.WithFragmentBindings

@AndroidEntryPoint
@WithFragmentBindings
class LoginFragment : Fragment() {

    private lateinit var viewModel: LoginViewModel
    private lateinit var binding: LoginFragmentBinding
    private val listaDeInputs: MutableList<TextInputLayout> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (activity as AppCompatActivity).supportActionBar?.title = "Login"
        binding = LoginFragmentBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(LoginViewModel::class.java)
        // TODO: Use the ViewModel
    }

    override fun onStart() {
        super.onStart()

        viewModel.checkIfUserIsAuthenticatedAndRedirect()
        // viewModel.checkIfRoomIsEmpty()

        setObservers()

        val inputEmail = binding.inputEmail
        val inputPassword = binding.inputPassword
        listaDeInputs.add(inputEmail)
        listaDeInputs.add(inputPassword)

        eliminarErroresSiTextoDeInputsCambia(listaDeInputs)

        binding.btnLogin.setOnClickListener {
            hideKeyboard()
            if (todosLosInputsSonValidos(listaDeInputs)) {
                mostrarCircularProgress(true)
                mostrarErrorGeneral(false)
                val email = inputEmail.editText?.text.toString()
                val password = inputPassword.editText?.text.toString()
                viewModel.loginUsuario(email, password)
            }
        }

        binding.btnResetPassword.setOnClickListener {
            val email = binding.inputEmail.editText?.text.toString()
            val action = LoginFragmentDirections.actionLoginFragmentToResetPasswordFragment(email)
            this.findNavController().navigate(action)
        }

        mostrarCircularProgress(false)


    }//FIN ONSTART


    private fun mostrarCircularProgress(value: Boolean) {
        binding.circularProgressScreen.circularProgress.isVisible = value
    }

    private fun mostrarErrorGeneral(valor: Boolean) {
        binding.errorMessageEnLoginFragment.isVisible = valor
    }

    private fun setObservers() {
        viewModel.userIsAuthenticated.observeForever { result ->
            if (result) {
                startActivity(Intent(activity, ActivityOrderTaking::class.java))
                activity?.finish()
            }
        }

        viewModel.errorMessage.observeForever { result ->
            if (!result.isNullOrBlank()) {
                mostrarCircularProgress(false)
                mostrarErrorGeneral(true)
                binding.errorMessageEnLoginFragment.text = result
                Toast.makeText(context, result, Toast.LENGTH_LONG).show()
            } else {
                mostrarErrorGeneral(false)
            }
        }


    }

    fun todosLosInputsSonValidos(lista: MutableList<TextInputLayout>): Boolean {
        var resultado = true
        lista.forEach {
            if (!inputEsValido(it)) {
                resultado = false
            }
        }
        return resultado
    }

    private fun inputEsValido(input: TextInputLayout): Boolean {
        return if (input.editText?.text?.length == 0) {
            input.error = "Campo Requerido"
            false
        } else {
            input.error = ""
            true
        }
    }

    private fun eliminarErroresSiTextoDeInputsCambia(lista: MutableList<TextInputLayout>) {
        lista.forEach {
            it.editText?.doOnTextChanged { _, _, _, _ ->
                mostrarErrorGeneral(false)
                if (it.editText?.text?.length == 0) {
                    it.error = "No debe estar vacío"
                } else {
                    it.error = ""
                }

            }
        }
    }


    fun hideKeyboard() {
        val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(requireView().windowToken, 0)
    }


}