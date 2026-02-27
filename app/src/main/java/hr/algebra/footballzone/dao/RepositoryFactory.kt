package hr.algebra.footballzone.dao

import android.content.Context


fun getRepository(context: Context?, tableName: String): Repository =
    DBRepository(context, tableName)

