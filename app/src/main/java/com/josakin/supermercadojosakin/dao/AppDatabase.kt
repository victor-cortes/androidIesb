package com.josakin.supermercadojosakin.dao

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import com.josakin.supermercadojosakin.entidades.Produto

/**
 * Created by victor on 10/04/18.
 */

@Database(entities = [(Produto::class)], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun produtoDao() : ProdutoDAO
}