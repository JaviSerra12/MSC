package com.example.msc.data.remote.database

import com.google.firebase.firestore.FirebaseFirestore

//Define que va a hacer la base de datos
class FirebaseDatabaseProvider : DatabaseProvider {
    override fun getDb(): FirebaseFirestore {
        return FirebaseFirestore.getInstance()
    }


}