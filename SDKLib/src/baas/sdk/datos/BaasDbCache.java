package baas.sdk.datos;

import baas.sdk.datos.BaasContract.Cache;
import baas.sdk.datos.BaasContract.ColaSinc;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class BaasDbCache extends SQLiteOpenHelper {

	 // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 2;
    public static final String DATABASE_NAME = "Cache.db";
    
    private static final String TEXT_TYPE = " TEXT";
    private static final String TYPE_INTEGER = " INTEGER";
    private static final String COMMA_SEP = ",";
    private static final String SQL_CREATE_ENTRIES =
        "CREATE TABLE " + Cache.TABLE_NAME + " (" +
        Cache._ID + " INTEGER PRIMARY KEY," +
        Cache.COLUMN_NAME_ITEM_ID+ TEXT_TYPE + COMMA_SEP +
        Cache.COLUMN_NAME_JSON+ TEXT_TYPE + 
        // Any other options for the CREATE command
        " )";

    private static final String SQL_DELETE_ENTRIES =
        "DROP TABLE IF EXISTS " + Cache.TABLE_NAME;

    
    public BaasDbCache(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }
    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

}
