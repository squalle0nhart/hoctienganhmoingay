package com.squalle0nhart.hoctienganh.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;
import com.squalle0nhart.hoctienganh.model.PharseCategoryInfo;
import com.squalle0nhart.hoctienganh.model.PharseInfo;

import java.util.ArrayList;


/**
 * Created by squalle0nhart on 15/3/2017.
 */

public class PharseDatabase extends SQLiteAssetHelper {
    public static final String DATABASE_NAME = "english2.db";
    public static final int DATABASE_VERSION = 1;

    private static final String TABLE_PHARSE_CATEGORY = "v_s_t";
    private static final String TABLE_PHARSE = "v_s_d";
    private static final String COLUMN_IS_READ = "is_read";

    private static final int INDEX_PHARSE = 2;
    private static final int INDEX_PHARSE_MEAN = 3;
    private static final int INDEX_PHARSE_IS_READ = 4;
    private static final int INDEX_CATEGORY = 1;
    private static final int INDEX_CATEGORY_ID = 0;

    private static PharseDatabase sWordDatabase;
    private static SQLiteDatabase sSqLiteDatabase;

    public PharseDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    //Singletone pattern
    public static synchronized PharseDatabase getInstance(Context context) {
        if (sWordDatabase == null) {
            sWordDatabase = new PharseDatabase(context);
        }
        return sWordDatabase;
    }

    /**
     * Mở database
     *
     * @return
     */
    private static boolean openDatabase() {
        try {
            if (sSqLiteDatabase == null
                    || (sSqLiteDatabase != null && !sSqLiteDatabase.isOpen())) {
                sSqLiteDatabase = sWordDatabase.getWritableDatabase();
                return true;
            }
        } catch (SQLiteException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Lấy danh sách các chủ đề của từ vựng
     * @return
     */
    public ArrayList<PharseCategoryInfo> getListPharseCategory() {
        ArrayList<PharseCategoryInfo> listPharse = new ArrayList<>();
        Cursor cursor = null;
        try {
            openDatabase();
            cursor = sSqLiteDatabase.query(TABLE_PHARSE_CATEGORY, null, null, null, null, null, null);
            while (cursor.moveToNext()) {
                listPharse.add(new PharseCategoryInfo(cursor.getString(INDEX_CATEGORY),
                        cursor.getInt(INDEX_CATEGORY_ID)));
            }
        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            if (cursor != null && !cursor.isClosed()) cursor.close();
        }

        return listPharse;
    }

    public ArrayList<PharseInfo> getListPharseByCategory(int categoryID) {
        ArrayList<PharseInfo> listPharse = new ArrayList<>();
        Cursor cursor = null;
        try {
            openDatabase();
            cursor = sSqLiteDatabase.query(TABLE_PHARSE, null,
                    "level = ?", new String[]{"" + categoryID}
                    , null, null, null);
            while (cursor.moveToNext()) {
                listPharse.add(new PharseInfo(cursor.getString(INDEX_PHARSE),
                        cursor.getString(INDEX_PHARSE_MEAN),cursor.getInt(INDEX_PHARSE_IS_READ)));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null && !cursor.isClosed()) cursor.close();
        }
        return listPharse;
    }

    /**
     * Lấy danh sách các câu đã đánh dấu học
     * @return
     */
    public ArrayList<PharseInfo> getListLearnedPharse() {
        ArrayList<PharseInfo> listPharse = new ArrayList<>();
        Cursor cursor = null;
        try {
            openDatabase();
            cursor = sSqLiteDatabase.query(TABLE_PHARSE, null,
                    "is_read = ?", new String[]{"" + 1}
                    , null, null, null);
            while (cursor.moveToNext()) {
                listPharse.add(new PharseInfo(cursor.getString(INDEX_PHARSE),
                        cursor.getString(INDEX_PHARSE_MEAN),cursor.getInt(INDEX_PHARSE_IS_READ)));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null && !cursor.isClosed()) cursor.close();
        }
        return listPharse;
    }

    /**
     * Lấy danh sách theo xâu tìm kiếm
     * @param categoryID
     * @param search
     * @return
     */
    public ArrayList<PharseInfo> getListPharseByCategorySearch(int categoryID, String search) {
        ArrayList<PharseInfo> listPharse = new ArrayList<>();
        Cursor cursor = null;
        try {
            openDatabase();
            cursor = sSqLiteDatabase.query(TABLE_PHARSE, null,
                    "(level = ?) AND (t like ? OR m like ? )", new String[]{"" + categoryID,
                            "%" + search + "%", "%" + search + "%"}
                    , null, null, null);
            while (cursor.moveToNext()) {
                listPharse.add(new PharseInfo(cursor.getString(INDEX_PHARSE),
                        cursor.getString(INDEX_PHARSE_MEAN),cursor.getInt(INDEX_PHARSE_IS_READ)));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null && !cursor.isClosed()) cursor.close();
        }
        return listPharse;
    }

    /**
     * Cập nhật lại thông tin về 1 câu
     * @param wordInfo
     * @return
     */
    public long updatePharseInfo(PharseInfo wordInfo) {
        openDatabase();
        try {
            final ContentValues contentValues = new ContentValues();
            contentValues.put(COLUMN_IS_READ, wordInfo.getIsRead());
            return sSqLiteDatabase.update(TABLE_PHARSE, contentValues,"t = ?",
                    new String[]{wordInfo.getText()});
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }
}
