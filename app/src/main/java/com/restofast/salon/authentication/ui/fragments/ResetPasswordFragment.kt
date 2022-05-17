package com.restofast.salon.authentication.ui.fragments

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.text.Editable
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputLayout
import com.restofast.salon.R
import com.restofast.salon.authentication.ui.viewmodels.ResetPasswordViewModel
import com.restofast.salon.databinding.ResetPasswordFragmentBinding
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.WithFragmentBindings

@AndroidEntryPoint
@WithFragmentBindings
class ResetPasswordFragment : Fragment() {

    private lateinit var viewModel: ResetPasswordViewModel
    private lateinit var binding: ResetPasswordFragmentBinding
    private val listaDeInputs: MutableList<TextInputLayout> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (activity as AppCompatActivity).supportActionBar?.title = "Restablecer Password"
        binding = ResetPasswordFragmentBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(ResetPasswordViewModel::class.java)
        // TODO: Use the ViewModel
    }

    override fun onStart() {
        super.onStart()

        setObservers()
        mostrarErrorGeneral(false)

        val emailRecibido = ResetPasswordFragmentArgs.fromBundle(requireArguments()).email
        if (!emailRecibido.isNullOrBlank()) {
            val editableEmail = Editable.Factory.getInstance().newEditable(emailRecibido)
            binding.inputEmail.editText?.text = editableEmail
        }


        val inputEmail = binding.inputEmail
        listaDeInputs.add(inputEmail)

        eliminarErroresSiTextoDeInputsCambia(listaDeInputs)

        binding.btnResetPassword.setOnClickListener {
            if (todosLosInputsSonValidos(listaDeInputs)) {
                mostrarErrorGeneral(false)
                val email = inputEmail.editText?.text.toString()
                viewModel.resetPassword(email)
            }
        }





    }



    private fun mostrarErrorGeneral(valor: Boolean) {
        binding.errorMessageEnResetPassword.isVisible = valor
    }
    private fun mostrarMensajeExitosoGeneral(message: String) {
        val btnPositiveText  =  "OK, VOLVER A LOGIN"
        mostrarDialogBtnPositive(message, btnPositiveText, ::volverAtras)
    }
    private fun mostrarDialogBtnPositive(mensajeAMostrar: String, btnPositiveText: String, actionForPositive: () -> Unit ) {
        //DIALOG enviar function en parámetro como ::actionForPositive
        MaterialAlertDialogBuilder(requireContext())
            .setMessage(mensajeAMostrar)
            .setPositiveButton(btnPositiveText) { _, _ ->
                actionForPositive()
            }.show()
        //FIN DE DIALOG
    }


    private fun setObservers() {
        viewModel.errorMessage.observeForever { result ->
            if (!result.isNullOrBlank()) {
                mostrarErrorGeneral(true)
                binding.errorMessageEnResetPassword.text = result
            } else {
                mostrarErrorGeneral(false)
            }
        }
        viewModel.successMessage.observeForever { result ->
            if (!result.isNullOrBlank()) {
                mostrarMensajeExitosoGeneral(result)
            }
        }

    }

    fun todosLosInputsSonValidos(lista: MutableList<TextInputLayout>): Boolean {
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

    private fun eliminarErroresSiTextoDeInputsCambia(lista: MutableList<TextInputLayout>){
        lista.forEach {
            it.editText?.doOnTextChanged { _, _, _, _ ->
                mostrarErrorGeneral(false)
                if (it.editText?.text?.length == 0){
                    it.error = "No debe estar vacío"
                }else{
                    it.error = ""
                }

            }
        }
    }


    private fun volverAtras() {
        requireActivity().onBackPressed()
    }






}