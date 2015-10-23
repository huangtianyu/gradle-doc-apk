package com.githang.gradledoc.datasource

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

import java.util.Locale
import java.util.concurrent.locks.Lock

/**
 * HTTP请求数据缓存
 * User: Geek_Soledad(msdx.android@qq.com)
 * Date: 2014-11-29
 * Time: 12:24
 * FIXME
 */
class HttpDBCache//    private static final String TABLE_CONFIG = "t_config";
//    private static final String CRAETE_CONFIG = "CREATE TABLE 't_config'(_id INTEGER PRIMARY KEY AUTOINCREMENT, key TEXT, value TEXT)";


private constructor(context: Context) : SQLiteOpenHelper(context, HttpDBCache.DB_NAME, null, HttpDBCache.VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(CREATE_RESPONSE)
        db.execSQL(CREATE_RESPONSE_INDEX)
        //        db.execSQL(CRAETE_CONFIG);
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
    }

    /**
     * 查询缓存的响应

     * @param url
     * *
     * @return
     */
    fun queryResponse(url: String): String? {
        val db = readableDatabase
        val cursor = db.query(TABLE_RESPONSE, arrayOf<String>(COL_ID, COL_URL, COL_RESPONSE),
                COL_URL + "=?", arrayOf<String>(url), null, null, null)
        var response: String? = null
        if (cursor.moveToNext()) {
            response = cursor.getString(cursor.getColumnIndex(COL_RESPONSE))
        }
        cursor.close()
        db.close()
        return response
    }

    /**
     * 缓存请求。

     * @param url
     * *
     * @param response
     */
    fun saveResponse(url: String, response: String) {
        val db = writableDatabase
        val cv = ContentValues()
        cv.put(COL_URL, url)
        cv.put(COL_RESPONSE, response)
        db.replace(TABLE_RESPONSE, null, cv)
        logMaxId(db)
        db.close()
    }

    private fun logMaxId(db: SQLiteDatabase) {
        val sql = "select max($COL_ID) AS maxId from $TABLE_RESPONSE"
        val cursor = db.rawQuery(sql, null)
        if (cursor.moveToNext()) {
            val maxId = cursor.getInt(cursor.getColumnIndex("maxId"))
            Log.d("Cache", "id ..." + maxId)
        }
    }

    companion object {

        private var instance: HttpDBCache? = null

        private val VERSION = 2
        private val DB_NAME = "http_cache_db"

        private val TABLE_RESPONSE = "t_response"
        private val COL_ID = "_id"
        private val COL_URL = "url"
        private val COL_RESPONSE = "response"
        private val CREATE_RESPONSE =
                "CREATE TABLE '$TABLE_RESPONSE'($COL_ID INTEGER PRIMARY KEY AUTOINCREMENT, $COL_URL TEXT, $COL_RESPONSE TEXT)"
        private val CREATE_RESPONSE_INDEX = "CREATE UNIQUE INDEX unique_index_url ON $TABLE_RESPONSE ($COL_URL)"

        //    /**
        //     * 保存配置。
        //     * @param key
        //     * @param value
        //     */
        //    public void saveConfig(String key, String value) {
        //
        //    }
        //
        //    /**
        //     * 查询配置。
        //     * @param key
        //     * @param defaultValue
        //     * @return
        //     */
        //    public String queryConfig(String key, String defaultValue) {
        //
        //        return null;
        //    }

        @Synchronized fun getInstance(context: Context): HttpDBCache? {
            if (instance == null) {
                instance = HttpDBCache(context.applicationContext)
            }
            return instance
        }

    }
}
