package dev.pfjn.pnv

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.provider.BaseColumns
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import org.junit.Before

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class BdInstrumentedTest {
    private fun getAppContext(): Context =
        InstrumentationRegistry.getInstrumentation().targetContext
    @Before
    fun apagaBaseDados() {
        getAppContext().deleteDatabase(BdPnvOpenHelper.NOME_BASE_DADOS)
    }
    @Test
    fun consegueAbrirBaseDados() {
        val openHelper = BdPnvOpenHelper(getAppContext())
        val bd = openHelper.readableDatabase
        assert(bd.isOpen)
    }

/*    @Test
    fun consegueFecharBaseDados() {
        val openHelper = BdPnvOpenHelper(getAppContext())
        val bd = openHelper.readableDatabase
        assert(!bd.isOpen)
    }*/

    private fun getWritableDatabase() : SQLiteDatabase {
        val openHelper = BdPnvOpenHelper(getAppContext())
        return openHelper.writableDatabase
    }
    @Test
    fun consegueInserirDoencas() {
        val bd = getWritableDatabase()

        val doenca = Doenca("VHB")
        insereDoenca(bd, doenca)
    }

    private fun insereDoenca(bd: SQLiteDatabase, doenca: Doenca) {
       doenca.id = TabelaDoencas(bd).insere(doenca.toContentValues())
       assertNotEquals(-1, doenca.id)
    }

    @Test
    fun consegueInserirVacinas() {
        val bd = getWritableDatabase()

        val doenca = Doenca ("VHB")
        insereDoenca(bd, doenca)

        val vacina1 = Vacina("Vírus Hepatite B", doenca, "1 Dose: Nascimento, 2 Dose: 2 Meses, 3 Dose: 6 Meses")
        insereVacina(bd, vacina1)

        val doenca2 = Doenca("Hib")
        insereDoenca(bd, doenca2)

        val vacina2 = Vacina("Haemophilus influenzae tipo b", doenca2, "1 Dose: 2 Meses, 2 Dose: 4 Meses, 3 Dose: 6 Meses, 4 Dose: 18 Meses")
        insereVacina(bd, vacina2)
    }

    private fun insereVacina(bd: SQLiteDatabase, vacina: Vacina) {
        vacina.id = TabelaVacinas(bd).insere(vacina.toContentValues())
        assertNotEquals(-1, vacina.id)
    }

    @Test
    fun consegueLerDoencas() {
        val bd = getWritableDatabase()

        val doencaVHB = Doenca("VHB")
        insereDoenca(bd, doencaVHB)

        val doencaHib = Doenca("HIB")
        insereDoenca(bd, doencaHib)

        val tabelaDoencas = TabelaDoencas(bd)

        val cursor = tabelaDoencas.consulta(
            TabelaDoencas.CAMPOS,
            "${BaseColumns._ID}=?",
            arrayOf(doencaHib.id.toString()),
            null,
            null,
            null
        )

        assert(cursor.moveToNext())

        val doencaBD = Doenca.fromCursor(cursor)

        assertEquals(doencaHib, doencaBD)

        val cursorTodasDoencas = tabelaDoencas.consulta(
            TabelaDoencas.CAMPOS,
            null,
            null,
            null,
            null,
            TabelaDoencas.CAMPO_DESCRICAO
        )

        assert(cursorTodasDoencas.count > 1)
    }

    @Test
    fun consegueLerVacinas() {
        val bd = getWritableDatabase()

        val doenca = Doenca("VIP")
        insereDoenca(bd, doenca)

        val vacina1 = Vacina("Poliomelite", doenca,"1 Dose: 2 Meses, 2 Dose: 4 Meses, 3 Dose: 6 Meses, 4 Dose: 18 Meses, 5 Dose: 5 anos")
        insereVacina(bd, vacina1)

        val vacina2 = Vacina("Sarampo, parotidite epidemica, rubeola", doenca,"1 Dose: 12 Meses, 2 Dose: 5 anos")
        insereVacina(bd, vacina2)

        val tabelaVacinas = TabelaVacinas(bd)

        val cursor = tabelaVacinas.consulta(
            TabelaVacinas.CAMPOS,
            "${TabelaVacinas.CAMPO_ID}=?",
            arrayOf(vacina1.id.toString()),
            null,
            null,
            null
        )

        assert(cursor.moveToNext())

        val vacinaBD = Vacina.fromCursor(cursor)

        assertEquals(vacina1, vacinaBD)

        val cursorTodasVacinas = tabelaVacinas.consulta(
            TabelaVacinas.CAMPOS,
            null,
            null,
            null,
            null,
            TabelaVacinas.CAMPO_NOME
        )
        assert(cursorTodasVacinas.count > 1)
    }

    @Test
    fun consegueAlterarDoencas() {
        val bd = getWritableDatabase()

        val doenca = Doenca("...")
        insereDoenca(bd, doenca)

        doenca.descricao = "VHB"

        val registosAlterados = TabelaDoencas(bd).altera(
            doenca.toContentValues(),
            "${BaseColumns._ID}=?",
            arrayOf(doenca.id.toString())
        )

        assertEquals(1, registosAlterados)
    }

    @Test
    fun consegueAlterarVacinas() {
        val bd = getWritableDatabase()

        val doenca = Doenca ("VHB")
        insereDoenca(bd, doenca)

        val vacina = Vacina("Vírus Hepatite B", doenca, "1 Dose: Nascimento, 2 Dose: 2 Meses, 3 Dose: 6 Meses")
        insereVacina(bd, vacina)

        val novaDoenca = Doenca("Hib")
        insereDoenca(bd, novaDoenca)

        val novaVacina = Vacina("Haemophilus influenzae tipo b", novaDoenca, "1 Dose: 2 Meses, 2 Dose: 4 Meses, 3 Dose: 6 Meses, 4 Dose: 18 Meses")
        insereVacina(bd, novaVacina)

        vacina.nome = novaVacina.nome
        vacina.doenca = novaDoenca
        vacina.idade = novaVacina.idade

        val registosAlterados = TabelaVacinas(bd).altera(
            vacina.toContentValues(),
            "${BaseColumns._ID}=?",
            arrayOf(vacina.id.toString())
        )

        assertEquals(1, registosAlterados)
    }

    @Test
    fun consegueApagarDoencas() {
        val bd = getWritableDatabase()

        val doenca = Doenca("...")
        insereDoenca(bd, doenca)

        val registosEliminados = TabelaDoencas(bd).elimina(
            "${BaseColumns._ID}=?",
            arrayOf(doenca.id.toString())
        )

        assertEquals(1, registosEliminados)
    }

    @Test
    fun consegueApagarVacinas() {
        val bd = getWritableDatabase()

        val doenca = Doenca("VHB")
        insereDoenca(bd, doenca)

        val vacina = Vacina("Vírus Hepatite B", doenca, "1 Dose: Nascimento, 2 Dose: 2 Meses, 3 Dose: 6 Meses")
        insereVacina(bd, vacina)

        val registosEliminados = TabelaVacinas(bd).elimina(
            "${BaseColumns._ID}=?",
            arrayOf(vacina.id.toString())
        )

        assertEquals(1, registosEliminados)
    }
}