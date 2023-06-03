package dev.pfjn.pnv

import android.content.ContentValues
import android.database.Cursor
import android.provider.BaseColumns

class Vacina (
    var nome: String,
    var idDoenca: Long,
    var idade: String,
    var id: Long = -1
) {
    fun toContentValues() : ContentValues {
        val valores = ContentValues()

        valores.put(TabelaVacinas.CAMPO_NOME, nome)
        valores.put(TabelaVacinas.CAMPO_IDADE, idade)
        valores.put(TabelaVacinas.CAMPO_FK_DOENCAS, idDoenca)

        return valores
    }

    companion object {
        fun fromCursor ( cursor : Cursor) : Vacina {
            val posId = cursor.getColumnIndex(BaseColumns._ID)
            val posNome = cursor.getColumnIndex(TabelaVacinas.CAMPO_NOME)
            val posIdade = cursor.getColumnIndex(TabelaVacinas.CAMPO_IDADE)
            val posDoencaFK = cursor.getColumnIndex(TabelaVacinas.CAMPO_FK_DOENCAS)

            val id = cursor.getLong(posId)
            val nome = cursor.getString(posNome)
            val idade = cursor.getString(posIdade)
            val doencaId = cursor.getLong(posDoencaFK)

            return Vacina(nome, doencaId, idade, id)
        }
    }
}