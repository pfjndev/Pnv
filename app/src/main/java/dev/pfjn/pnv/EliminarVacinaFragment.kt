package dev.pfjn.pnv

import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import dev.pfjn.pnv.databinding.FragmentEliminarVacinaBinding

class EliminarVacinaFragment : Fragment() {
    private lateinit var vacina: Vacina
    private var _binding: FragmentEliminarVacinaBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentEliminarVacinaBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val activity = activity as MainActivity
        activity.fragment = this
        activity.idMenuAtual = R.menu.menu_eliminar

        vacina = EliminarVacinaFragmentArgs.fromBundle(requireArguments()).vacina

        binding.textViewNome.text = vacina.nome
        binding.textViewIdade.text = vacina.idade
        binding.textViewDoenca.text = vacina.doenca.descricao
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun processaOpcaoMenu(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_eliminar -> {
                eliminar()
                true
            }
            R.id.action_cancelar -> {
                voltaListaVacinas()
                true
            }
            else -> false
        }
    }

    private fun voltaListaVacinas() {
        findNavController().navigate(R.id.action_eliminarVacinaFragment_to_ListaVacinasFragment)
    }

    private fun eliminar() {
        val enderecoLivro = Uri.withAppendedPath(VacinasContentProvider.ENDERECO_VACINAS, vacina.id.toString())
        val numVacinasEliminadas = requireActivity().contentResolver.delete(enderecoLivro, null, null)

        if (numVacinasEliminadas == 1) {
            Toast.makeText(requireContext(), getString(R.string.vacina_eliminada_com_sucesso), Toast.LENGTH_LONG).show()
            voltaListaVacinas()
        } else {
            Snackbar.make(binding.textViewNome, getString(R.string.erro_eliminar_vacina), Snackbar.LENGTH_INDEFINITE)
        }
    }
}