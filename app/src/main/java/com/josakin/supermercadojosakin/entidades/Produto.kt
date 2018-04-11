package com.josakin.supermercadojosakin.entidades

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import android.os.Build
import android.support.annotation.RequiresApi
import java.io.File
import java.util.*

/**
 * Created by victor on 10/04/18.
 */

@Entity(tableName = "produto")
data class Produto (
    @PrimaryKey(autoGenerate = true) var uid: Int = 0,
    @ColumnInfo(name = "name") var name: String = "",
    @ColumnInfo(name = "foto") var foto: String = "",
    @ColumnInfo(name = "valor") var valor: Double = 0.0) {

    @RequiresApi(Build.VERSION_CODES.O)
    fun converteToBase64(filePath: String) : String    {
        val bytes = File(filePath).readBytes()
        val base64 = Base64.getEncoder().encodeToString(bytes)

        return base64
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun converteBase64ParaBytes(base64: String, pathFile: String): Unit {
        val imageByteArray = Base64.getDecoder().decode(base64)
        File(pathFile).writeBytes(imageByteArray)
    }

}