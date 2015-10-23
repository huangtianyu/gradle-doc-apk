package com.githang.gradledoc.datasource

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import groovy.transform.CompileStatic

/**
 * HTTP请求数据缓存
 * User: Geek_Soledad(msdx.android@qq.com)
 * Date: 2014-11-29
 * Time: 12:24
 * FIXME
 */
@CompileStatic
class HttpDBCache extends SQLiteOpenHelper {

    private static HttpDBCache instance

    private static final int VERSION = 2
    private static final String DB_NAME = "http_cache_db"

    private static final String TABLE_RESPONSE = "t_response"
    private static final String COL_ID = "_id"
    private static final String COL_URL = "url"
    private static final String COL_RESPONSE = "response"
    private static final String CREATE_RESPONSE =
            "CREATE TABLE '$TABLE_RESPONSE'($COL_ID INTEGER PRIMARY KEY AUTOINCREMENT, $COL_URL TEXT, $COL_RESPONSE TEXT)"
    private static final String CREATE_RESPONSE_INDEX =
            "CREATE UNIQUE INDEX unique_index_url ON $TABLE_RESPONSE ($COL_URL)"

//    private static final String TABLE_CONFIG = "t_config";
//    private static final String CRAETE_CONFIG = "CREATE TABLE 't_config'(_id INTEGER PRIMARY KEY AUTOINCREMENT, key TEXT, value TEXT)";


    private HttpDBCache(Context context) {
        super(context, DB_NAME, null, VERSION)
    }

    @Override
    void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_RESPONSE)
        db.execSQL(CREATE_RESPONSE_INDEX)
//        db.execSQL(CRAETE_CONFIG);
    }

    @Override
    void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    /**
     * 查询缓存的响应
     *
     * @param url
     * @return
     */
    String queryResponse(String url) {
        SQLiteDatabase db = getReadableDatabase()
        String sql = "select $COL_RESPONSE from $TABLE_RESPONSE where $COL_URL = ?"
        Cursor cursor = db.rawQuery(sql, url)
        String response = cursor.moveToNext() ? cursor.getString(cursor.getColumnIndex(COL_RESPONSE)) : null
        cursor.close()
        db.close()
        return response
    }

    /**
     * 缓存请求。
     *
     * @param url
     * @param response
     */
    void saveResponse(String url, String response) {
        SQLiteDatabase db = getWritableDatabase()
        ContentValues cv = new ContentValues()
        cv.put(COL_URL, url)
        cv.put(COL_RESPONSE, response)
        db.replace(TABLE_RESPONSE, null, cv)
        logMaxId(db)
        db.close()
    }

    private void logMaxId(SQLiteDatabase db) {
        String sql = "select max($COL_ID) AS maxId from $TABLE_RESPONSE"
        Cursor cursor = db.rawQuery(sql, null)
        if (cursor.moveToNext()) {
            int maxId = cursor.getInt(cursor.getColumnIndex("maxId"))
            Log.d("Cache", "id ..." + maxId)
        }
    }

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

    static synchronized HttpDBCache getInstance(Context context) {
        instance = instance ?: new HttpDBCache(context.getApplicationContext())
    }
}
