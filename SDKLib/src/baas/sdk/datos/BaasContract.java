package baas.sdk.datos;

import android.provider.BaseColumns;

public abstract class BaasContract {
	public BaasContract() {}

    /* Inner class that defines the table contents */
    public static abstract class ColaSinc implements BaseColumns {
        public static final String TABLE_NAME = "ColaSinc";
        public static final String COLUMN_NAME_ITEM_ID = "itemid";
        public static final String COLUMN_NAME_ACCION = "accion";
        public static final String COLUMN_NAME_JSON = "json";
    }
    
    public static abstract class Cache implements BaseColumns {
    	public static final String TABLE_NAME = "Cache";
        public static final String COLUMN_NAME_ITEM_ID = "itemid";
        public static final String COLUMN_NAME_JSON = "json";
    }
}
