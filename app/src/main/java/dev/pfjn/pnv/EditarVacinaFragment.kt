package dev.pfjn.pnv

import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.SimpleCursorAdapter
import androidx.loader.app.LoaderManager
import androidx.loader.content.CursorLoader
import androidx.loader.content.Loader
import androidx.navigation.fragment.findNavController
import dev.pfjn.pnv.databinding.FragmentEditarVacinaBinding
import android.widget.Toast

private const val ID_LOADER_DOENCAS = 0

class EditarVacinaFragment : Fragment(), LoaderManager.LoaderCallbacks<Cursor> {
    private var vacina: Vacina?= null
    private var _binding: FragmentEditarVacinaBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentEditarVacinaBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val loader = LoaderManager.getInstance(this)
        loader.initLoader(ID_LOADER_DOENCAS, null, this)

        val activity = activity as MainActivity
        activity.fragment = this
        activity.idMenuAtual = R.menu.menu_guardar_cancelar

        val vacina = EditarVacinaFragmentArgs.fromBundle(requireArguments()).vacina

        if (vacina != null) {
            activity.atualizaTitulo(R.string.editar_vacina_label)

            binding.editTextNome.setText(vacina.nome)
            binding.editTextIdade.setText(vacina.idade)
        } else {
            activity.atualizaTitulo(R.string.nova_vacina_label)
        }

        this.vacina = vacina
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun processaOpcaoMenu(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_guardar -> {
                guardar()
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
        findNavController().navigate(R.id.action_editarVacinaFragment_to_ListaVacinasFragment)
    }

    private fun guardar() {
        val nome = binding.editTextNome.text.toString()
        if (nome.isBlank()) {
            binding.editTextNome.error = getString(R.string.nome_obrigatorio)
            binding.editTextNome.requestFocus()
            return
        }

        val doencaId = binding.spinnerDoencas.selectedItemId
        val idade = binding.editTextIdade.text.toString()
        if (idade.isBlank()) {
            binding.editTextIdade.error = getString(R.string.idade_obrigatoria)
            binding.editTextIdade.requestFocus()
            return
        }

        if (vacina == null) {
            val vacina = Vacina(
                nome,
                Doenca("?", doencaId),
                idade
            )

            insereVacina(vacina)
        } else {
            val vacina = vacina!!
            vacina.nome = nome
            vacina.doenca = Doenca("?", doencaId)
            vacina.idade = idade

            alteraVacina(vacina)
        }
    }

    private fun alteraVacina(vacina: Vacina) {
        val enderecoVacina = Uri.withAppendedPath(VacinasContentProvider.ENDERECO_VACINAS, vacina.id.toString())
        val vacinasAlteradas = requireActivity().contentResolver.update(enderecoVacina, vacina.toContentValues(), null, null)

        if (vacinasAlteradas == 1) {
            Toast.makeText(requireContext(), R.string.vacina_guardada_com_sucesso, Toast.LENGTH_LONG).show()
            voltaListaVacinas()
        } else {
            binding.editTextNome.error = getString(R.string.erro_guardar_vacina)
        }
    }

    private fun insereVacina(
        vacina: Vacina
    ) {

        val id = requireActivity().contentResolver.insert(
            VacinasContentProvider.ENDERECO_VACINAS,
            vacina.toContentValues()
        )

        if (id == null) {
            binding.editTextNome.error = getString(R.string.erro_guardar_vacina)
            return
        }

        Toast.makeText(
            requireContext(),
            getString(R.string.vacina_guardada_com_sucesso),
            Toast.LENGTH_SHORT
        ).show()

        voltaListaVacinas()
    }


    /**
     * Instantiate and return a new Loader for the given ID.
     *
     *
     * This will always be called from the process's main thread.
     *
     * @param id The ID whose loader is to be created.
     * @param args Any arguments supplied by the caller.
     * @return Return a new Loader instance that is ready to start loading.
     */
    override fun onCreateLoader(id: Int, args: Bundle?): Loader<Cursor> {
        return CursorLoader(
            requireContext(),
            VacinasContentProvider.ENDERECO_DOENCAS,
            TabelaDoencas.CAMPOS,
            null, null,
            TabelaDoencas.CAMPO_DESCRICAO
        )
    }

    /**
     * Called when a previously created loader is being reset, and thus
     * making its data unavailable.  The application should at this point
     * remove any references it has to the Loader's data.
     *
     *
     * This will always be called from the process's main thread.
     *
     * @param loader The Loader that is being reset.
     */
    override fun onLoaderReset(loader: Loader<Cursor>) {
        if (_binding != null) {
            binding.spinnerDoencas.adapter = null
        }
    }

    /**
     * Called when a previously created loader has finished its load.  Note
     * that normally an application is *not* allowed to commit fragment
     * transactions while in this call, since it can happen after an
     * activity's state is saved.  See [ FragmentManager.openTransaction()][androidx.fragment.app.FragmentManager.beginTransaction] for further discussion on this.
     *
     *
     * This function is guaranteed to be called prior to the release of
     * the last data that was supplied for this Loader.  At this point
     * you should remove all use of the old data (since it will be released
     * soon), but should not do your own release of the data since its Loader
     * owns it and will take care of that.  The Loader will take care of
     * management of its data so you don't have to.  In particular:
     *
     *
     *  *
     *
     *The Loader will monitor for changes to the data, and report
     * them to you through new calls here.  You should not monitor the
     * data yourself.  For example, if the data is a [android.database.Cursor]
     * and you place it in a [android.widget.CursorAdapter], use
     * the [android.widget.CursorAdapter] constructor *without* passing
     * in either [android.widget.CursorAdapter.FLAG_AUTO_REQUERY]
     * or [android.widget.CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER]
     * (that is, use 0 for the flags argument).  This prevents the CursorAdapter
     * from doing its own observing of the Cursor, which is not needed since
     * when a change happens you will get a new Cursor throw another call
     * here.
     *  *  The Loader will release the data once it knows the application
     * is no longer using it.  For example, if the data is
     * a [android.database.Cursor] from a [android.content.CursorLoader],
     * you should not call close() on it yourself.  If the Cursor is being placed in a
     * [android.widget.CursorAdapter], you should use the
     * [android.widget.CursorAdapter.swapCursor]
     * method so that the old Cursor is not closed.
     *
     *
     *
     * This will always be called from the process's main thread.
     *
     * @param loader The Loader that has finished.
     * @param data The data generated by the Loader.
     */
    override fun onLoadFinished(loader: Loader<Cursor>, data: Cursor?) {
        if (data == null) {
            binding.spinnerDoencas.adapter = null
            return
        }

        binding.spinnerDoencas.adapter = SimpleCursorAdapter(
            requireContext(),
            android.R.layout.simple_list_item_1,
            data,
            arrayOf(TabelaDoencas.CAMPO_DESCRICAO),
            intArrayOf(android.R.id.text1),
            0
        )

        mostraDoencaSelecionadaSpinner()
    }

    private fun mostraDoencaSelecionadaSpinner() {
        if (vacina == null) return

        val idDoenca = vacina!!.doenca.id

        val ultimaDoenca = binding.spinnerDoencas.count - 1
        for (i in 0..ultimaDoenca) {
            if (idDoenca == binding.spinnerDoencas.getItemIdAtPosition(i)) {
                binding.spinnerDoencas.setSelection(i)
                return
            }
        }
    }
}