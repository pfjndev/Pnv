package dev.pfjn.pnv

import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteQueryBuilder
import android.provider.BaseColumns

class TabelaVacinas(db: SQLiteDatabase) : TabelaBD(db, NOME_TABELA) {
    override fun cria() {
        db.execSQL("CREATE TABLE $NOME_TABELA ($CHAVE_TABELA, $CAMPO_NOME TEXT NOT NULL, $CAMPO_IDADE TEXT, $CAMPO_FK_DOENCAS INTEGER NOT NULL, FOREIGN KEY ($CAMPO_FK_DOENCAS) REFERENCES ${TabelaDoencas.NOME_TABELA}(${BaseColumns._ID}) ON DELETE RESTRICT)")
    }

    override fun consulta(
        colunas: Array<String>,
        selecao: String?,
        argsSelecao: Array<String>?,
        groupby: String?,
        having: String?,
        orderby: String?
    ): Cursor {
        val sql = SQLiteQueryBuilder()
        sql.tables = "$NOME_TABELA INNER JOIN ${TabelaDoencas.NOME_TABELA} ON ${TabelaDoencas.CAMPO_ID}=$CAMPO_FK_DOENCAS"

        return sql.query(db, colunas, selecao, argsSelecao, groupby, having, orderby)
    }

    companion object {
        const val NOME_TABELA = "vacinas"

        const val CAMPO_ID = "$NOME_TABELA.${BaseColumns._ID}"
        const val CAMPO_NOME = "nome"
        const val CAMPO_IDADE = "idade"
        const val CAMPO_FK_DOENCAS = "id_vacina"
        const val CAMPO_DESC_DOENCA = TabelaDoencas.CAMPO_DESCRICAO

        val CAMPOS = arrayOf(CAMPO_ID, CAMPO_NOME, CAMPO_IDADE, CAMPO_FK_DOENCAS, CAMPO_DESC_DOENCA)
    }
}
