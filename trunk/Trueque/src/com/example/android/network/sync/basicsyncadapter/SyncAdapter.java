/*
 * Copyright 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.network.sync.basicsyncadapter;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.android.network.sync.basicsyncadapter.util.SelectionBuilder;

import android.accounts.Account;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SyncResult;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import baas.sdk.Factory;
import baas.sdk.ISDKJson;
import baas.sdk.datos.BaasContract;
import baas.sdk.datos.BaasDbHelper;
import baas.sdk.datos.BaasContract.ColaSinc;
import baas.sdk.messages.Message;
import baas.sdk.utils.Constants;
import baas.sdk.utils.exceptions.NotInitilizedException;

/**
 * Define a sync adapter for the app.
 *
 * <p>This class is instantiated in {@link SyncService}, which also binds SyncAdapter to the system.
 * SyncAdapter should only be initialized in SyncService, never anywhere else.
 *
 * <p>The system calls onPerformSync() via an RPC call through the IBinder object supplied by
 * SyncService.
 */
class SyncAdapter extends AbstractThreadedSyncAdapter {
    public static final String TAG = "SyncAdapter";

    /**
     * Content resolver, for performing database operations.
     */
    private final ContentResolver mContentResolver;

    /**
     * Project used when querying content provider. Returns all known fields.
     */
    private static final String[] PROJECTION = new String[] {
    	BaasContract.ColaSinc._ID,
    	BaasContract.ColaSinc.COLUMN_NAME_ITEM_ID,
    	BaasContract.ColaSinc.COLUMN_NAME_ACCION,
    	BaasContract.ColaSinc.COLUMN_NAME_JSON,
            };

    // Constants representing column positions from PROJECTION.
    public static final int COLUMN_ID = 0;
    public static final int COLUMN_ITEM_ID = 1;
    public static final int COLUMN_ACCION = 2;
    public static final int COLUMN_JSON = 3;
    
    /**
     * Constructor. Obtains handle to content resolver for later use.
     */
    public SyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
        mContentResolver = context.getContentResolver();
    }

    /**
     * Constructor. Obtains handle to content resolver for later use.
     */
    public SyncAdapter(Context context, boolean autoInitialize, boolean allowParallelSyncs) {
        super(context, autoInitialize, allowParallelSyncs);
        mContentResolver = context.getContentResolver();
    }

    /**
     * Called by the Android system in response to a request to run the sync adapter. The work
     * required to read data from the network, parse it, and store it in the content provider is
     * done here. Extending AbstractThreadedSyncAdapter ensures that all methods within SyncAdapter
     * run on a background thread. For this reason, blocking I/O and other long-running tasks can be
     * run <em>in situ</em>, and you don't have to set up a separate thread for them.
     .
     *
     * <p>This is where we actually perform any work required to perform a sync.
     * {@link AbstractThreadedSyncAdapter} guarantees that this will be called on a non-UI thread,
     * so it is safe to peform blocking I/O here.
     *
     * <p>The syncResult argument allows you to pass information back to the method that triggered
     * the sync.
     */
    @Override
    public void onPerformSync(Account account, Bundle extras, String authority,
                              ContentProviderClient provider, SyncResult syncResult) {
        Log.i(TAG, "Beginning network synchronization");
        Log.i("SyncAdapterMDF", "Beginning network synchronization");
        SelectionBuilder sb = new SelectionBuilder();
        BaasDbHelper db = new BaasDbHelper(getContext());
        SQLiteDatabase sdb =db.getWritableDatabase();
        sb.table(ColaSinc.TABLE_NAME);
        Cursor c =sb.query(sdb, PROJECTION, BaasContract.ColaSinc._ID);
        try {
			ISDKJson sdk = Factory.getJsonSDK();
        while(c.moveToNext()){
        	Log.i(TAG, "Consigo un elemento de la cola");
				JSONObject jobj = new JSONObject(c.getString(COLUMN_JSON));
				String accion = c.getString(COLUMN_ACCION);
				Message m = null;
				if(accion == Constants.create){
					m=sdk.addJson(jobj,false);
					Log.i(TAG, "Hago add");
				}else if (accion == Constants.update){
					m=sdk.updateJson(c.getInt(COLUMN_ITEM_ID), jobj,false);
					Log.i(TAG, "Hago update");
				}
				if(m.codigo!=Constants.Exito){
					continue;
				}else{
					Log.i(TAG, "Entro a borrar de la cola");
					String selection = ColaSinc._ID+ " LIKE ?";
					// Specify arguments in placeholder order.
					String[] selectionArgs = { String.valueOf(c.getInt(COLUMN_ID)) };
					// Issue SQL statement.
					sdb.delete(ColaSinc.TABLE_NAME, selection, selectionArgs);
					Log.i(TAG, "Salgo de borrar de la cola");
				}
        }} catch (JSONException e) {
			}catch (NotInitilizedException e) {
				Log.i(TAG, "Excepcion, factory no inicilizado");
			} catch (ClientProtocolException e) {
				Log.i(TAG, "CPE Exception");
			} catch (IOException e) {
				Log.i(TAG, "IOException");
			} 
        
	      Log.i(TAG, "Network synchronization complete");
    }

    
    
    
    /**
     * Read XML from an input stream, storing it into the content provider.
     *
     * <p>This is where incoming data is persisted, committing the results of a sync. In order to
     * minimize (expensive) disk operations, we compare incoming data with what's already in our
     * database, and compute a merge. Only changes (insert/update/delete) will result in a database
     * write.
     *
     * <p>As an additional optimization, we use a batch operation to perform all database writes at
     * once.
     *
     * <p>Merge strategy:
     * 1. Get cursor to all items in feed<br/>
     * 2. For each item, check if it's in the incoming data.<br/>
     *    a. YES: Remove from "incoming" list. Check if data has mutated, if so, perform
     *            database UPDATE.<br/>
     *    b. NO: Schedule DELETE from database.<br/>
     * (At this point, incoming database only contains missing items.)<br/>
     * 3. For any items remaining in incoming list, ADD to database.
     */
//    public void updateLocalFeedData(final InputStream stream, final SyncResult syncResult)
//            throws IOException, XmlPullParserException, RemoteException,
//            OperationApplicationException, ParseException {
////        final FeedParser feedParser = new FeedParser();
////        final ContentResolver contentResolver = getContext().getContentResolver();
////
////        Log.i(TAG, "Parsing stream as Atom feed");
////        final List<FeedParser.Entry> entries = feedParser.parse(stream);
////        Log.i(TAG, "Parsing complete. Found " + entries.size() + " entries");
////
////
////        ArrayList<ContentProviderOperation> batch = new ArrayList<ContentProviderOperation>();
////
////        // Build hash table of incoming entries
//////        HashMap<String, FeedParser.Entry> entryMap = new HashMap<String, FeedParser.Entry>();
//////        for (FeedParser.Entry e : entries) {
//////            entryMap.put(e.id, e);
//////        }
////
////        // Get list of all items
////        Log.i(TAG, "Fetching local entries for merge");
////        Uri uri = FeedContract.Entry.CONTENT_URI; // Get all entries
//////        Cursor c = contentResolver.query(uri, PROJECTION, null, null, null);
////        assert c != null;
////        Log.i(TAG, "Found " + c.getCount() + " local entries. Computing merge solution...");
//
//        // Find stale data
//        int id;
//        String entryId;
//        String title;
//        String link;
//        long published;
////        while (c.moveToNext()) {
////            syncResult.stats.numEntries++;
////            id = c.getInt(COLUMN_ID);
////            entryId = c.getString(COLUMN_ENTRY_ID);
////            title = c.getString(COLUMN_TITLE);
////            link = c.getString(COLUMN_LINK);
////            published = c.getLong(COLUMN_PUBLISHED);
////            FeedParser.Entry match = entryMap.get(entryId);
////            if (match != null) {
////                // Entry exists. Remove from entry map to prevent insert later.
////                entryMap.remove(entryId);
////                // Check to see if the entry needs to be updated
////                Uri existingUri = FeedContract.Entry.CONTENT_URI.buildUpon()
////                        .appendPath(Integer.toString(id)).build();
////                if ((match.title != null && !match.title.equals(title)) ||
////                        (match.link != null && !match.link.equals(link)) ||
////                        (match.published != published)) {
////                    // Update existing record
////                    Log.i(TAG, "Scheduling update: " + existingUri);
////                    batch.add(ContentProviderOperation.newUpdate(existingUri)
////                            .withValue(FeedContract.Entry.COLUMN_NAME_TITLE, title)
////                            .withValue(FeedContract.Entry.COLUMN_NAME_LINK, link)
////                            .withValue(FeedContract.Entry.COLUMN_NAME_PUBLISHED, published)
////                            .build());
////                    syncResult.stats.numUpdates++;
////                } else {
////                    Log.i(TAG, "No action: " + existingUri);
////                }
////            } else {
////                // Entry doesn't exist. Remove it from the database.
////                Uri deleteUri = FeedContract.Entry.CONTENT_URI.buildUpon()
////                        .appendPath(Integer.toString(id)).build();
////                Log.i(TAG, "Scheduling delete: " + deleteUri);
////                batch.add(ContentProviderOperation.newDelete(deleteUri).build());
////                syncResult.stats.numDeletes++;
////            }
////        }
////        c.close();
//
//        // Add new items
////        for (FeedParser.Entry e : entryMap.values()) {
////            Log.i(TAG, "Scheduling insert: entry_id=" + e.id);
////            batch.add(ContentProviderOperation.newInsert(FeedContract.Entry.CONTENT_URI)
////                    .withValue(FeedContract.Entry.COLUMN_NAME_ENTRY_ID, e.id)
////                    .withValue(FeedContract.Entry.COLUMN_NAME_TITLE, e.title)
////                    .withValue(FeedContract.Entry.COLUMN_NAME_LINK, e.link)
////                    .withValue(FeedContract.Entry.COLUMN_NAME_PUBLISHED, e.published)
////                    .build());
////            syncResult.stats.numInserts++;
////        }
//        Log.i(TAG, "Merge solution ready. Applying batch update");
////        mContentResolver.applyBatch(FeedContract.CONTENT_AUTHORITY, batch);
//        mContentResolver.notifyChange(
//                FeedContract.Entry.CONTENT_URI, // URI where data was modified
//                null,                           // No local observer
//                false);                         // IMPORTANT: Do not sync to network
//        // This sample doesn't support uploads, but if *your* code does, make sure you set
//        // syncToNetwork=false in the line above to prevent duplicate syncs.
//    }
}
