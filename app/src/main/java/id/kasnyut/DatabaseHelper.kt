package id.kasnyut

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    companion object {
        private const val DATABASE_NAME = "myapp.db"
        private const val DATABASE_VERSION = 1
        private const val TABLE_NAME = "items"
        private const val COLUMN_ID = "_id"
        private const val COLUMN_NAME = "name"
        private const val COLUMN_TYPE = "type"
        private const val COLUMN_AMOUNT = "amount"
    }
    fun deleteItem(item: Item) {
        val db = writableDatabase
        db.delete(TABLE_NAME, "$COLUMN_ID=?", arrayOf(item.id.toString()))
        db.close()
    }

    fun insertItem(item: Item) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_NAME, item.name)
            put(COLUMN_AMOUNT, item.amount)
            put(COLUMN_TYPE, item.type)
        }
        db.insert(TABLE_NAME, null, values)
        db.close()
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("CREATE TABLE $TABLE_NAME ($COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT, $COLUMN_NAME TEXT, $COLUMN_TYPE INTEGER, $COLUMN_AMOUNT INTEGER)")
        var values = ContentValues().apply {
            put(COLUMN_NAME, "Gaji")
            put(COLUMN_TYPE, 1)
            put(COLUMN_AMOUNT, 1000000)
        }
        db.insert(TABLE_NAME, null, values)
        values = ContentValues().apply {
            put(COLUMN_NAME, "Roti O")
            put(COLUMN_TYPE, 0)
            put(COLUMN_AMOUNT, 20000)
        }
        db.insert(TABLE_NAME, null, values)
        values = ContentValues().apply {
            put(COLUMN_NAME, "Fiesta Chicken Nugget")
            put(COLUMN_TYPE, 0)
            put(COLUMN_AMOUNT, 60000)
        }
        db.insert(TABLE_NAME, null, values)

    }


    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // Handle database schema upgrades here
    }
}
