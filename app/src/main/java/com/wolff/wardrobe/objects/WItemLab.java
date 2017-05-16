package com.wolff.wardrobe.objects;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.util.Log;

import com.wolff.wardrobe.localdb.DbCursorWrapper;
import com.wolff.wardrobe.localdb.DbHelper;
import com.wolff.wardrobe.localdb.DbSchema.WItemTable;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by wolff on 03.04.2017.
 */

public class WItemLab {
    private static WItemLab sWItemLab;

    private List<WItem> mWItemList;

    private Context mContext;
    private SQLiteDatabase mDatabase;
    private WItemLab(Context context){
        mContext = context.getApplicationContext();
        mDatabase = new DbHelper(mContext).getWritableDatabase();

   }

    public static WItemLab get(Context context){
        if(sWItemLab==null){
            sWItemLab = new WItemLab(context);
        }
        return sWItemLab;
    }
/*    public List<WItem> getWItems(){
        DbCursorWrapper cursor = queryWItem(null,null);
        mWItemList = new ArrayList<>();
        try{
            cursor.moveToFirst();
            while (!cursor.isAfterLast()){
                mWItemList.add(cursor.getWItem());
                cursor.moveToNext();
            }
        }
        finally {
            cursor.close();
        }
        return mWItemList;
    }
  */
    public List<WItem> getSelectedWItems(String[] whereArgs){
        DbCursorWrapper cursor = queryWItem(whereArgs);
        mWItemList = new ArrayList<>();
        try{
            cursor.moveToFirst();
            while (!cursor.isAfterLast()){
                mWItemList.add(cursor.getWItem());
                cursor.moveToNext();
            }
        }catch (Exception e){
            Log.e("ERROR SELECT",""+e.getLocalizedMessage());

        }
        finally {
            cursor.close();
        }
        return mWItemList;
    }
    public WItem getWItem(UUID id){
         DbCursorWrapper cursor = queryOneWItem(WItemTable.Cols.UUID+" = ?",new String[]{id.toString()});
        try{
            if (cursor.getCount()==0){
                return null;
            }
            cursor.moveToFirst();
            return cursor.getWItem();
        }
        finally {
            cursor.close();
        }

    }

    private static ContentValues getContentValues(WItem item){
        ContentValues values = new ContentValues();
        values.put(WItemTable.Cols.UUID,item.getId().toString());
        values.put(WItemTable.Cols.TITLE,item.getTitle());
        values.put(WItemTable.Cols.SEASON,item.getSeason());
        values.put(WItemTable.Cols.MIN_T,item.getMinTemp());
        values.put(WItemTable.Cols.MAX_T,item.getMaxTemp());

        return values;
    }
    public void addWItem(WItem item){
        ContentValues values = getContentValues(item);
        mDatabase.insert(WItemTable.TABLE_NAME,null,values);
        Log.e("DB ADD","ADD");
     }
    public void updateWItem(WItem item){
        ContentValues values = getContentValues(item);
        mDatabase.update(WItemTable.TABLE_NAME,values,WItemTable.Cols.UUID+" = ?",new String[]{item.getId().toString()});
        Log.e("DB UPDATE","UPDATE"+item.getId()+" = = = "+item.getTitle());
    }
    public void deleteWItem(WItem item){
        mDatabase.delete(WItemTable.TABLE_NAME,WItemTable.Cols.UUID+" = ?",new String[]{item.getId().toString()});
    }
    private DbCursorWrapper queryOneWItem(String whereClause,
                                       String[] whereArgs) {
         SQLiteDatabase db = new DbHelper(mContext).getReadableDatabase();
        String[] columns = null;
        String groupBy = null;
        String having = null;
        String orderBy = null;
        Cursor cursor = db.query(
                WItemTable.TABLE_NAME,
                columns, // Columns - null выбирает все столбцы
                whereClause,
                whereArgs,
                groupBy, // groupBy
                having, // having
                orderBy);

        return new DbCursorWrapper(cursor);

    }

    private DbCursorWrapper queryWItem(String[] whereArgs) {
        Cursor cursor=null;
        if(whereArgs==null) {
            cursor = mDatabase.rawQuery("SELECT * FROM " + WItemTable.TABLE_NAME + " WHERE " + WItemTable.Cols.MIN_T + ">= -50 AND " + WItemTable.Cols.MAX_T + "<= 50", null);
        }else if(whereArgs.length==2){
            cursor = mDatabase.rawQuery("SELECT * FROM " + WItemTable.TABLE_NAME + " WHERE " + WItemTable.Cols.MIN_T + ">= " + whereArgs[0] + " AND " + WItemTable.Cols.MAX_T + "<= " + whereArgs[1], null);
        }else if(whereArgs.length==3){
            cursor = mDatabase.rawQuery("SELECT * FROM " + WItemTable.TABLE_NAME + " WHERE " + WItemTable.Cols.MIN_T + ">= " + whereArgs[0] + " AND " + WItemTable.Cols.MAX_T + "<= " + whereArgs[1]+" AND "+WItemTable.Cols.SEASON+" = '"+whereArgs[2]+"'", null);
        }
            //Log.e("WITEMLAB","cutsor = "+cursor.getCount());
        return new DbCursorWrapper(cursor);
    }
    public File getPhotoFile(WItem item){
        File externalFilesDir = mContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        if(externalFilesDir==null){
            return null;
        }
        return new File(externalFilesDir,item.getPhotoFileName());
    }
}
