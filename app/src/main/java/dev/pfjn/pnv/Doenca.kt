package dev.pfjn.pnv

import android.content.ContentValues
import android.database.Cursor
import android.provider.BaseColumns

class Doenca (
    var descricao: String,
    var id: Long = -1
) {
    fun toContentValues() : ContentValues {
        val valores = ContentValues()

        valores.put(TabelaDoencas.CAMPO_DESCRICAO, descricao)

        return valores
    }

    companion object {
        fun fromCursor ( cursor: Cursor ) : Doenca {
            val posId = cursor.getColumnIndex(BaseColumns._ID)
            val posDescricao = cursor.getColumnIndex(TabelaDoencas.CAMPO_DESCRICAO)

            val id = cursor.getLong(posId)
            val descricao = cursor.getString(posDescricao)

            return Doenca(descricao, id)
        }
    }
}