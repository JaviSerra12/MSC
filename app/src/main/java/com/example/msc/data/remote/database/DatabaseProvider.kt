package com.example.msc.data.remote.database

import com.google.firebase.firestore.FirebaseFirestore


//Define que operaciones se pueden realizar en la base de datos.
interface DatabaseProvider {
    fun getDb() : FirebaseFirestore
}