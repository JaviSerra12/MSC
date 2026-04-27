package com.example.msc.data.remote.database


//Se encarga de crear o gestionar la conexión real a la base de datos.
class ConectionDB(private val databaseProvider: DatabaseProvider) {
    val db = databaseProvider.getDb()
}