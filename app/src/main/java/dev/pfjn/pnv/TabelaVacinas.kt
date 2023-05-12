package dev.pfjn.pnv

import android.database.sqlite.SQLiteDatabase
import android.provider.BaseColumns

class TabelaVacinas(db: SQLiteDatabase) : TabelaBD(db, NOME_TABELA) {
    override fun cria() {
        db.execSQL(
            "CREATE TABLE $NOME_TABELA " +
                    "($CHAVE_TABELA, " +
                    "$CAMPO_NOME TEXT NOT NULL, " +
                    "$CAMPO_IDADE, " +
                    "id_vacina INTEGER NOT NULL," +
                    " FOREIGN KEY (id_vacina) REFERENCES ${TabelaDoencas.NOME_TABELA}(${BaseColumns._ID}) " +
                    "ON DELETE RESTRICT)")
    }

    companion object {
        const val NOME_TABELA = "Vacinas"
        const val CAMPO_NOME = "Nome"
        const val CAMPO_IDADE = "Idade"
    }
}
