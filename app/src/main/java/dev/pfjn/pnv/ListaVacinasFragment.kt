package dev.pfjn.pnv

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import androidx.recyclerview.widget.LinearLayoutManager
import dev.pfjn.pnv.databinding.FragmentListaVacinasBinding
/**
 * A simple [Fragment] subclass.
 * Use the [ListaVacinasFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ListaVacinasFragment : Fragment() {
    private var _binding: FragmentListaVacinasBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_lista_vacinas, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapterLivros = AdapterVacinas()
        binding.recyclerViewVacinas.adapter = adapterLivros
        binding.recyclerViewVacinas.layoutManager = LinearLayoutManager(requireContext())
    }
    companion object {
    }
}