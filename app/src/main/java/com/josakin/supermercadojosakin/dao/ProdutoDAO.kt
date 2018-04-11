package com.josakin.supermercadojosakin.dao

import android.arch.persistence.room.*
import com.josakin.supermercadojosakin.entidades.Produto

/**
 * Created by victor on 10/04/18.
 */

@Dao
interface ProdutoDAO {
    @Insert
    fun insertProduto(produto: Produto)

    @Update
    fun alteraProduto(produto: Produto)

    @Delete
    fun removeProduto(produto: Produto)

    @Query("SELECT * FROM PRODUTO")
    fun findAll() : List<Produto>
}