package hr.algebra.footballzone.dao

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import androidx.core.database.sqlite.transaction

class DBRepository(context: Context?, private val tableName: String) : Repository {
    private val dbHelper = FootballZoneDbHelper(context)

    override fun insert(values: ContentValues?) =
        dbHelper.writableDatabase.insert(tableName, null, values)

    override fun query(
        projection: Array<String>?,
        selection: String?,
        selectionArgs: Array<String>?,
        sortOrder: String?
    ): Cursor =
        dbHelper.readableDatabase.query(
            tableName,
            projection,
            selection,
            selectionArgs,
            null,
            null,
            sortOrder
        )

    override fun update(values: ContentValues?, selection: String?, selectionArgs: Array<String>?) =
        dbHelper.writableDatabase.update(tableName, values, selection, selectionArgs)

    override fun delete(selection: String?, selectionArgs: Array<String>?) =
        dbHelper.writableDatabase.delete(tableName, selection, selectionArgs)

    override fun bulkInsert(values: Array<out ContentValues?>): Int {
        val db = dbHelper.writableDatabase
        var successfulInserts = 0

        db.transaction {
            successfulInserts = values.filterNotNull().count { value ->
                insertWithOnConflict(
                    tableName,
                    null,
                    value,
                    SQLiteDatabase.CONFLICT_REPLACE
                ) != -1L
            }
        }
        return successfulInserts
    }
}