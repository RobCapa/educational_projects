package com.example.data.database.contracts

object UserDbContract {

    const val TABLE_NAME = "users"

    object COLUMNS {
        const val ID = "id"
        const val FIRST_NAME = "first_name"
        const val LAST_NAME = "last_name"
        const val PHONE_NUMBER = "phone_number"
    }
}