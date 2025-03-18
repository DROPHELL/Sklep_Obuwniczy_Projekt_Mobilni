package com.example.website.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "BidBoots.db"
        private const val DATABASE_VERSION = 1

        // Таблиця користувачів
        const val TABLE_USERS = "users"
        const val COLUMN_ID = "id"
        const val COLUMN_FIRST_NAME = "first_name"
        const val COLUMN_LAST_NAME = "last_name"
        const val COLUMN_EMAIL = "email"
        const val COLUMN_PASSWORD = "password"

        // Таблиця гаманця (Wallet)
        const val TABLE_WALLET = "wallet"
        const val COLUMN_WALLET_ID = "wallet_id"
        const val COLUMN_WALLET_EMAIL = "email"
        const val COLUMN_BALANCE = "balance"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createUsersTable = """
            CREATE TABLE $TABLE_USERS (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_FIRST_NAME TEXT NOT NULL,
                $COLUMN_LAST_NAME TEXT NOT NULL,
                $COLUMN_EMAIL TEXT NOT NULL UNIQUE,
                $COLUMN_PASSWORD TEXT NOT NULL
            )
        """.trimIndent()

        val createWalletTable = """
            CREATE TABLE $TABLE_WALLET (
                $COLUMN_WALLET_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_WALLET_EMAIL TEXT NOT NULL UNIQUE,
                $COLUMN_BALANCE REAL NOT NULL DEFAULT 0.0,
                FOREIGN KEY ($COLUMN_WALLET_EMAIL) REFERENCES $TABLE_USERS($COLUMN_EMAIL)
            )
        """.trimIndent()

        db.execSQL(createUsersTable)
        db.execSQL(createWalletTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_USERS")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_WALLET")
        onCreate(db)
    }

    // ✅ Додаємо користувача в базу даних
    fun addUser(firstName: String, lastName: String, email: String, password: String): Boolean {
        if (checkEmailExists(email)) {
            Log.e("DatabaseHelper", "User with email $email already exists")
            return false
        }

        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_FIRST_NAME, firstName.trim())
            put(COLUMN_LAST_NAME, lastName.trim())
            put(COLUMN_EMAIL, email.trim().lowercase())
            put(COLUMN_PASSWORD, password)
        }

        return try {
            val result = db.insert(TABLE_USERS, null, values)
            if (result != -1L) {
                createWalletForUser(email)
            }
            result != -1L
        } catch (e: Exception) {
            Log.e("DatabaseHelper", "Error adding user: ${e.message}")
            false
        } finally {
            db.close()
        }
    }

    // ✅ Перевіряємо, чи користувач існує (email + пароль)
    fun checkUser(email: String, password: String): Boolean {
        val db = readableDatabase
        val query = "SELECT * FROM $TABLE_USERS WHERE $COLUMN_EMAIL = ? AND $COLUMN_PASSWORD = ?"
        val cursor = db.rawQuery(query, arrayOf(email.trim().lowercase(), password))
        val exists = cursor.count > 0
        cursor.close()
        db.close()
        return exists
    }

    // ✅ Перевіряємо, чи email вже існує
    fun checkEmailExists(email: String): Boolean {
        val db = readableDatabase
        val query = "SELECT * FROM $TABLE_USERS WHERE $COLUMN_EMAIL = ?"
        val cursor = db.rawQuery(query, arrayOf(email.trim().lowercase()))
        val exists = cursor.count > 0
        cursor.close()
        db.close()
        return exists
    }

    // ✅ Отримуємо користувача за email
    fun getUserByEmail(email: String): User? {
        val db = readableDatabase
        val cursor: Cursor? = db.query(
            TABLE_USERS,
            arrayOf(COLUMN_ID, COLUMN_FIRST_NAME, COLUMN_LAST_NAME, COLUMN_EMAIL, COLUMN_PASSWORD),
            "$COLUMN_EMAIL = ?",
            arrayOf(email.trim().lowercase()),
            null, null, null
        )

        return cursor?.use {
            if (it.moveToFirst()) {
                User(
                    id = it.getInt(it.getColumnIndexOrThrow(COLUMN_ID)),
                    firstName = it.getString(it.getColumnIndexOrThrow(COLUMN_FIRST_NAME)),
                    lastName = it.getString(it.getColumnIndexOrThrow(COLUMN_LAST_NAME)),
                    email = it.getString(it.getColumnIndexOrThrow(COLUMN_EMAIL)),
                    password = it.getString(it.getColumnIndexOrThrow(COLUMN_PASSWORD))
                )
            } else {
                null
            }
        }.also { cursor?.close(); db.close() }
    }

    // ✅ Оновлення даних користувача
    fun updateUser(email: String, firstName: String, lastName: String, password: String): Boolean {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_FIRST_NAME, firstName)
            put(COLUMN_LAST_NAME, lastName)
            put(COLUMN_PASSWORD, password)
        }

        return try {
            val result = db.update(TABLE_USERS, values, "$COLUMN_EMAIL = ?", arrayOf(email))
            result > 0
        } catch (e: Exception) {
            Log.e("DatabaseHelper", "Error updating user: ${e.message}")
            false
        } finally {
            db.close()
        }
    }

    // ✅ Створення гаманця при реєстрації користувача
    private fun createWalletForUser(email: String): Boolean {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_WALLET_EMAIL, email.trim().lowercase())
            put(COLUMN_BALANCE, 0.0)
        }

        return try {
            db.insert(TABLE_WALLET, null, values) != -1L
        } catch (e: Exception) {
            Log.e("DatabaseHelper", "Error creating wallet: ${e.message}")
            false
        } finally {
            db.close()
        }
    }

    // ✅ Оновлення балансу гаманця
    fun saveWalletBalance(email: String, balance: Float) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_BALANCE, balance)
        }

        try {
            db.update(TABLE_WALLET, values, "$COLUMN_WALLET_EMAIL = ?", arrayOf(email))
        } catch (e: Exception) {
            Log.e("DatabaseHelper", "Error updating wallet balance: ${e.message}")
        } finally {
            db.close()
        }
    }

    // ✅ Отримання балансу гаманця
    fun getWalletBalance(email: String): Float {
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT $COLUMN_BALANCE FROM $TABLE_WALLET WHERE $COLUMN_WALLET_EMAIL = ?", arrayOf(email))
        return if (cursor.moveToFirst()) {
            val balance = cursor.getFloat(0)
            cursor.close()
            db.close()
            balance
        } else {
            cursor.close()
            db.close()
            0.0f
        }
    }
}

data class User(
    val id: Int,
    val firstName: String,
    val lastName: String,
    val email: String,
    val password: String
)
