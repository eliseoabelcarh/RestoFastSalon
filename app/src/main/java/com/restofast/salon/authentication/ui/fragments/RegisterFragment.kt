package com.restofast.salon.authentication.ui.fragments

import android.content.Context
import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import com.google.android.material.textfield.TextInputLayout
import com.restofast.salon.MainActivity
import com.restofast.salon.R
import com.restofast.salon.authentication.ui.viewmodels.RegisterViewModel
import com.restofast.salon.databinding.RegisterFragmentBinding
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.WithFragmentBindings

@AndroidEntryPoint
@WithFragmentBindings
class RegisterFragment : Fragment() {

    /* EJEMPLO
       @Inject
       lateinit var userDao: UserDao
   */

    private lateinit var viewModel: RegisterViewModel
    private lateinit var binding: RegisterFragmentBinding
    private val listaDeInputs: MutableList<TextInputLayout> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (activity as AppCompatActivity).supportActionBar?.title = "Register"
        binding = RegisterFragmentBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(RegisterViewModel::class.java)
    }

    override fun onStart() {
        super.onStart()

        setObservers()
        mostrarErrorGeneral(false)


        val inputName = binding.inputName
        val inputLastname = binding.inputLastname
        val inputDNI = binding.inputDNI
        val inputEmail = binding.inputEmail
        val inputPassword = binding.inputPassword
        val inputConfirmPassword = binding.inputConfirmPassword
        listaDeInputs.add(inputName)
        listaDeInputs.add(inputLastname)
        listaDeInputs.add(inputDNI)
        listaDeInputs.add(inputEmail)
        listaDeInputs.add(inputPassword)
        listaDeInputs.add(inputConfirmPassword)

        eliminarErroresSiTextoDeInputsCambia(listaDeInputs)

        binding.btnCreateAccount.setOnClickListener {
            if (todosLosInputsSonValidos(listaDeInputs)) {
                hideKeyboard()
                mostrarCircularProgress(true)
                mostrarErrorGeneral(false)
                val name = inputName.editText?.text.toString()
                val lastname = inputLastname.editText?.text.toString()
                val dni = inputDNI.editText?.text.toString()
                val email = inputEmail.editText?.text.toString()
                val password = inputPassword.editText?.text.toString()
                val confirmPassword = inputConfirmPassword.editText?.text.toString()
                viewModel.registrarUsuario(name, lastname,dni, email, password, confirmPassword)
            }
        }


        mostrarCircularProgress(false)


    }//fin de start

    fun hideKeyboard() {
        val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(requireView().windowToken, 0)
    }

    
    private fun mostrarCircularProgress(value: Boolean) {
        binding.circularProgressScreen.circularProgress.isVisible = value
    }

    private fun mostrarErrorGeneral(valor: Boolean) {
        binding.errorMessageEnRegisterFragment.isVisible = valor
    }

    private fun setObservers() {
        viewModel.userIsSuccesfullyRegistered.observeForever { result ->
            if (result) {
                startActivity(Intent(activity, MainActivity::class.java))
                activity?.finish()
            }
        }

        viewModel.errorMessage.observeForever { result ->
            if (!result.isNullOrBlank()) {
                mostrarCircularProgress(false)
                mostrarErrorGeneral(true)
                binding.errorMessageEnRegisterFragment.text = result
            } else {
                mostrarErrorGeneral(false)
            }
        }

    }

    private fun todosLosInputsSonValidos(lista: MutableList<TextInputLayout>): Boolean {
        var resultado: Boolean = true
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


}