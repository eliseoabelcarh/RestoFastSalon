package com.restofast.salon.orderTaking.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.restofast.salon.databinding.SalonFragmentBinding
import com.restofast.salon.entities.enums.RoleType
import com.restofast.salon.entities.order.Table
import com.restofast.salon.entities.persons.Empleado
import com.restofast.salon.orderTaking.ui.adapters.TableDistributionAdapter
import com.restofast.salon.orderTaking.ui.viewmodels.SalonViewModel
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.WithFragmentBindings

@AndroidEntryPoint
@WithFragmentBindings
class SalonFragment : Fragment(), TableDistributionAdapter.OnTableClickListener {

    companion object {
        fun newInstance() = SalonFragment()
    }

    private lateinit var viewModel: SalonViewModel
    private lateinit var binding: SalonFragmentBinding

    private lateinit var tableDistributionAdapter: TableDistributionAdapter
    private var empleadoActual: Empleado? = null

    private val listaDePermisosParaEstaVista = mutableListOf(
        RoleType.MOZO, RoleType.ADMIN, RoleType.CAJERO, RoleType.DUENIO,
        RoleType.SUPERVISOR,
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (activity as AppCompatActivity).supportActionBar?.title = ""
        binding = SalonFragmentBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(SalonViewModel::class.java)
        viewModel.cargarCurrentEmpleado()
    }

    override fun onStart() {
        super.onStart()
        setObservers()

    }

    private fun setObservers() {
        viewModel.currentEmpleado.observeForever { empleado ->
            try {
                empleadoActual = empleado
                validarEmpleado(empleadoActual)
                tableDistributionAdapter = TableDistributionAdapter(
                    empleadoActual!!.restauranteID,
                    empleadoActual!!.sucursalID,
                    requireContext(),
                    this
                )
                val dist = tableDistributionAdapter.getViewBindingSinEmparejar()
                viewModel.loadTablesDistribution(empleado.restauranteID, empleado.sucursalID,dist)

            }catch (e: Exception){
                e.message?.let { habilitarError(it) }
            }
        }
        viewModel.errorMessage.observeForever { result ->
            habilitarError(result)
        }
        viewModel.tablesDistribution.observeForever { listaTables ->
            if (listaTables.isNotEmpty()) {
                habilitarError("")
                binding.contenedorDistribucionDeMesas.removeAllViews()
                if (listaTables != null) {
                    val bin = tableDistributionAdapter.getViewBindingEmparejada(listaTables)
                    (bin.root.parent as ViewGroup?)?.removeAllViews()
                    binding.contenedorDistribucionDeMesas.addView(bin.root)
                }
            } else {
                habilitarError("NO hay lista de Mesas en DB")
            }


        }
    }//FIN DE SETOBSERVERS

    fun validarEmpleado(empleado: Empleado?) {
        if (empleado == null ) {
            throw Exception("NO SE PUDO CARGAR EMPLEADO")
        }
        if(!empleado.yaTieneAsignadoRestauranteYSucursal()){
            throw Exception("Empleado aún no se le asigno Sucursal o Restaurante")
        }
        if(!empleado.isActive){
            throw Exception("Empleado aparece como INACTIVO")
        }
        if(!empleado.tieneAlgunPermisoSegunRoleTypes(listaDePermisosParaEstaVista)){
            throw  Exception ("Empleado no tiene Permisos para esta área")
        }
    }
    override fun onTableCardClick(table: Table) {
        val orderTableID = table.orderTableID
        if(orderTableID.isNullOrBlank()){
            viewModel.createOrderTableIDForTable(table).observeForever { newOrderTableID ->
                redirectToCarritoOrder(newOrderTableID)
            }
        }else{
            redirectToCarritoOrder(table.orderTableID!!)
        }
    }


    private fun redirectToCarritoOrder(orderID: String){
        val action = SalonFragmentDirections.actionSalonFragmentToCarritoOrderFragment(orderID)
        this.findNavController().navigate(action)
        this.onDestroy()
    }

    private fun habilitarError(message: String) {
        if (message.isNotBlank()) {
            binding.contenedorDistribucionDeMesas.removeAllViews()
            binding.errorMessageSalon.visibility = View.VISIBLE
            binding.errorMessageSalon.text = message
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        } else {
            binding.errorMessageSalon.visibility = View.GONE
            binding.errorMessageSalon.text = ""
        }


    }

}