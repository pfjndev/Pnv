package dev.pfjn.pnv

import android.database.sqlite.SQLiteDatabase
import android.provider.BaseColumns

class TabelaVacinas(db: SQLiteDatabase) : TabelaBD(db, NOME_TABELA) {
    override fun cria() {
        db.execSQL(
            "CREATE TABLE $NOME_TABELA " +
                    "($CHAVE_TABELA, " +
                    " $CAMPO_NOME TEXT NOT NULL, " +
                    " $CAMPO_IDADE, " +
                    " $CAMPO_FK_DOENCAS INTEGER NOT NULL," +
                    " FOREIGN KEY ($CAMPO_FK_DOENCAS)" +
                    " REFERENCES ${TabelaDoencas.NOME_TABELA}(${BaseColumns._ID}) " +
                    " ON DELETE RESTRICT)")
    }

    companion object {
        const val NOME_TABELA = "Vacina"
        const val CAMPO_NOME = "Nome"
        const val CAMPO_IDADE = "Idade"
        const val CAMPO_FK_DOENCAS = "id_vacina"

        val CAMPOS = arrayOf(BaseColumns._ID, CAMPO_NOME, CAMPO_IDADE, CAMPO_FK_DOENCAS)
    }
}
