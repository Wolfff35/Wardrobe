package com.wolff.wardrobe.localdb;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.wolff.wardrobe.localdb.DbSchema.WItemTable;
import com.wolff.wardrobe.objects.WItem;

import java.util.UUID;

/**
 * Created by wolff on 11.04.2017.
 */

public class DbCursorWrapper extends CursorWrapper {

    public DbCursorWrapper(Cursor cursor) {
        super(cursor);
    }
    public WItem getWItem(){
        String uuidString = getString(getColumnIndex(WItemTable.Cols.UUID));
        String title = getString(getColumnIndex(WItemTable.Cols.TITLE));
        String season = getString(getColumnIndex(WItemTable.Cols.SEASON));
        int minT = getInt(getColumnIndex(WItemTable.Cols.MIN_T));
        int maxT = getInt(getColumnIndex(WItemTable.Cols.MAX_T));
        WItem item = new WItem(UUID.fromString(uuidString));
        item.setTitle(title);
        item.setSeason(season);
        item.setMinTemp(minT);
        item.setMaxTemp(maxT);
        return item;
    }
}
