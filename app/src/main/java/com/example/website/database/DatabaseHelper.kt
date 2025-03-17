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

        const val TABLE_USERS = "users"
        const val COLUMN_ID = "id"
        const val COLUMN_FIRST_NAME = "first_name"
        const val COLUMN_LAST_NAME = "last_name"
        const val COLUMN_EMAIL = "email"
        const val COLUMN_PASSWORD = "password"
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
        db.execSQL(createUsersTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_USERS")
        onCreate(db)
    }

    fun addUser(firstName: String, lastName: String, email: String, password: String): Boolean {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_FIRST_NAME, firstName.trim())
            put(COLUMN_LAST_NAME, lastName.trim())
            put(COLUMN_EMAIL, email.trim().lowercase())
            put(COLUMN_PASSWORD, password)
        }

        return try {
            val result = db.insertOrThrow(TABLE_USERS, null, values)
            result != -1L
        } catch (e: Exception) {
            Log.e("DatabaseHelper", "Error adding user: ${e.message}")
            false
        } finally {
            db.close()
        }
    }

    fun checkUser(email: String, password: String): Boolean {
        val db = this.readableDatabase
        var cursor: Cursor? = null
        var exists = false

        try {
            cursor = db.query(
                TABLE_USERS,
                arrayOf(COLUMN_ID),
                "$COLUMN_EMAIL = ? AND $COLUMN_PASSWORD = ?",
                arrayOf(email.trim().lowercase(), password),
                null, null, null
            )
            exists = cursor.count > 0
        } catch (e: Exception) {
            Log.e("DatabaseHelper", "Error checking user: ${e.message}")
        } finally {
            cursor?.close()
            db.close()
        }

        return exists
    }

    fun checkEmailExists(email: String): Boolean {
        val db = this.readableDatabase
        var cursor: Cursor? = null
        var exists = false

        try {
            cursor = db.query(
                TABLE_USERS,
                arrayOf(COLUMN_ID),
                "$COLUMN_EMAIL = ?",
                arrayOf(email.trim().lowercase()),
                null, null, null
            )
            exists = cursor.count > 0
        } catch (e: Exception) {
            Log.e("DatabaseHelper", "Error checking email: ${e.message}")
        } finally {
            cursor?.close()
            db.close()
        }

        return exists
    }

    fun getUserByEmail(email: String): User? {
        val db = this.readableDatabase
        var cursor: Cursor? = null
        var user: User? = null

        try {
            cursor = db.query(
                TABLE_USERS,
                arrayOf(COLUMN_ID, COLUMN_FIRST_NAME, COLUMN_LAST_NAME, COLUMN_EMAIL, COLUMN_PASSWORD),
                "$COLUMN_EMAIL = ?",
                arrayOf(email.trim().lowercase()),
                null, null, null
            )
            if (cursor.moveToFirst()) {
                user = User(
                    id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)),
                    firstName = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_FIRST_NAME)),
                    lastName = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_LAST_NAME)),
                    email = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EMAIL)),
                    password = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PASSWORD))
                )
            }
        } catch (e: Exception) {
            Log.e("DatabaseHelper", "Error fetching user: ${e.message}")
        } finally {
            cursor?.close()
            db.close()
        }

        return user
    }

    fun updateUser(email: String, firstName: String, lastName: String, password: String): Boolean {
        val db = this.writableDatabase
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
}

data class User(
    val id: Int,
    val firstName: String,
    val lastName: String,
    val email: String,
    val password: String
)
