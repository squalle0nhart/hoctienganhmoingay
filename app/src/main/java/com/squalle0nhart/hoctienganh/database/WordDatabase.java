package com.squalle0nhart.hoctienganh.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;
import com.squalle0nhart.hoctienganh.model.PharseInfo;
import com.squalle0nhart.hoctienganh.model.WordInfo;
import com.squalle0nhart.hoctienganh.ultis.AppUltis;

import java.util.ArrayList;

/**
 * Created by squalle0nhart on 13/3/2017.
 */

public class WordDatabase extends SQLiteAssetHelper {
    public static final String DATABASE_NAME = "english.db";
    public static final int DATABASE_VERSION = 1;
    public static final String TABLE_WORD = "word";
    public static final String COLUNM_NAME = "sword";
    public static final String COLUMN_IS_READ = "fav";

    public static final int INDEX_WORD_NAME = 1;
    public static final int INDEX_WORD_READ = 2;
    public static final int INDEX_WORD_MEANING = 4;
    public static final int INDEX_WORD_IS_FAV = 5;
    public static final int INDEX_WORD_FULL_MEAN = 3;

    public static final String TABLE_APOPHTHEGM = "apophthegm";
    public static final int INDEX_APOPHTHEGM_EN = 0;
    public static final int INDEX_APOPHTHEGM_VI = 1;

    private static WordDatabase sWordDatabase;
    private static SQLiteDatabase sSqLiteDatabase;

    public WordDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    //Singletone pattern
    public static synchronized WordDatabase getInstance(Context context) {
        if (sWordDatabase == null) {
            sWordDatabase = new WordDatabase(context);
        }
        return sWordDatabase;
    }

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
     * Hàm lấy danh sách các từ trong database
     */
    public ArrayList<WordInfo> getListWords() {
        ArrayList<WordInfo> listWords = new ArrayList<>();
        Cursor cursor = null;
        try {
            openDatabase();
            cursor = sSqLiteDatabase.query(TABLE_WORD, null, null, null, null, null, null);
            while (cursor.moveToNext()) {
                String mean = cursor.getString(INDEX_WORD_MEANING);
                int temp = mean.toString().indexOf(")");
                WordInfo wordInfo = new WordInfo(cursor.getString(INDEX_WORD_NAME),
                        "/" + cursor.getString(INDEX_WORD_READ) + "/",
                        mean.substring(0, temp + 1),
                        mean.substring(temp + 1, mean.length()).replaceFirst(" ", ""),
                        cursor.getShort(INDEX_WORD_IS_FAV),
                        cursor.getString(INDEX_WORD_FULL_MEAN));
                listWords.add(wordInfo);
            }
        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            if (cursor != null && !cursor.isClosed()) cursor.close();
        }
        return listWords;
    }


    /**
     * Hàm lấy ngẫu nhiên trong sql
     *
     * @return
     */
    public WordInfo getRandomWord() {
        WordInfo wordInfo = null;
        int random = AppUltis.randomInt(0, 3000 - 1);
        Cursor cursor = null;
        try {
            openDatabase();
            cursor = sSqLiteDatabase.query(TABLE_WORD, null, null, null, null, null, "fav desc limit " + random + "," + "1");
            if (cursor.moveToNext()) {
                String mean = cursor.getString(INDEX_WORD_MEANING);
                int temp = mean.toString().indexOf(")");
                wordInfo = new WordInfo(cursor.getString(INDEX_WORD_NAME),
                        "/" + cursor.getString(INDEX_WORD_READ) + "/",
                        mean.substring(0, temp + 1),
                        mean.substring(temp + 1, mean.length()).replaceFirst(" ", ""),
                        cursor.getShort(INDEX_WORD_IS_FAV),
                        cursor.getString(INDEX_WORD_FULL_MEAN));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return wordInfo;
    }


    /**
     * Hàm lấy danh sách các từ trong database
     */
    public ArrayList<WordInfo> getListLearnedWords() {
        ArrayList<WordInfo> listWords = new ArrayList<>();
        Cursor cursor = null;
        try {
            openDatabase();
            cursor = sSqLiteDatabase.query(TABLE_WORD, null, COLUMN_IS_READ + " = ?", new String[]{"" + 1}, null, null, null);
            while (cursor.moveToNext()) {
                String mean = cursor.getString(INDEX_WORD_MEANING);
                int temp = mean.toString().indexOf(")");
                WordInfo wordInfo = new WordInfo(cursor.getString(INDEX_WORD_NAME),
                        "/" + cursor.getString(INDEX_WORD_READ) + "/",
                        mean.substring(0, temp + 1),
                        mean.substring(temp + 1, mean.length()).replaceFirst(" ", ""),
                        cursor.getShort(INDEX_WORD_IS_FAV),
                        cursor.getString(INDEX_WORD_FULL_MEAN));
                listWords.add(wordInfo);
            }
        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            if (cursor != null && !cursor.isClosed()) cursor.close();
        }
        return listWords;
    }


    public ArrayList<PharseInfo> getListQuote() {
        ArrayList<PharseInfo> listWords = new ArrayList<>();
        Cursor cursor = null;
        try {
            openDatabase();
            cursor = sSqLiteDatabase.query(TABLE_APOPHTHEGM, null, null, null, null, null, null);
            while (cursor.moveToNext()) {
                PharseInfo wordInfo = new PharseInfo(cursor.getString(INDEX_APOPHTHEGM_EN),
                        cursor.getString(INDEX_APOPHTHEGM_VI), 0);
                listWords.add(wordInfo);
            }
        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            if (cursor != null && !cursor.isClosed()) cursor.close();
        }
        return listWords;
    }

    /**
     * Tìm kiếm từ
     *
     * @param search nội dung tìm kiếm
     * @return trả về danh sách các từ tìm được
     */
    public ArrayList<WordInfo> getListWordSearch(String search) {
        ArrayList<WordInfo> listWords = new ArrayList<>();
        Cursor cursor = null;
        try {
            openDatabase();
            cursor = sSqLiteDatabase.query(TABLE_WORD, null, COLUNM_NAME + " like ?",
                    new String[]{search + "%"}, null, null, null);
            while (cursor.moveToNext()) {
                String mean = cursor.getString(INDEX_WORD_MEANING);
                int temp = mean.toString().indexOf(")");
                WordInfo wordInfo = new WordInfo(cursor.getString(INDEX_WORD_NAME),
                        "/" + cursor.getString(INDEX_WORD_READ) + "/",
                        mean.substring(0, temp + 1),
                        mean.substring(temp + 1, mean.length()).replaceFirst(" ", ""),
                        cursor.getShort(INDEX_WORD_IS_FAV),
                        cursor.getString(INDEX_WORD_FULL_MEAN));
                listWords.add(wordInfo);
            }
        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            if (cursor != null && !cursor.isClosed()) cursor.close();
        }
        return listWords;
    }


    /**
     * Tìm kiếm từ
     *
     * @param search nội dung tìm kiếm
     * @return trả về danh sách các từ tìm được
     */
    public ArrayList<WordInfo> getListLearnedWordSearch(String search) {
        ArrayList<WordInfo> listWords = new ArrayList<>();
        Cursor cursor = null;
        try {
            openDatabase();
            cursor = sSqLiteDatabase.query(TABLE_WORD, null,
                    COLUNM_NAME + " like ? AND " + COLUMN_IS_READ + " = ?",
                    new String[]{search + "%", "" + 1}, null, null, null);
            while (cursor.moveToNext()) {
                String mean = cursor.getString(INDEX_WORD_MEANING);
                int temp = mean.toString().indexOf(")");
                WordInfo wordInfo = new WordInfo(cursor.getString(INDEX_WORD_NAME),
                        "/" + cursor.getString(INDEX_WORD_READ) + "/",
                        mean.substring(0, temp + 1),
                        mean.substring(temp + 1, mean.length()).replaceFirst(" ", ""),
                        cursor.getShort(INDEX_WORD_IS_FAV),
                        cursor.getString(INDEX_WORD_FULL_MEAN));
                listWords.add(wordInfo);
            }
        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            if (cursor != null && !cursor.isClosed()) cursor.close();
        }
        return listWords;
    }

    /**
     * Cập nhật lại thông tin về 1 từ
     *
     * @param wordInfo
     * @return
     */
    public long updateWordInfo(WordInfo wordInfo) {
        openDatabase();
        try {
            final ContentValues contentValues = new ContentValues();
            contentValues.put(COLUMN_IS_READ, wordInfo.getIsRead());
            return sSqLiteDatabase.update(TABLE_WORD, contentValues, COLUNM_NAME + "= ?",
                    new String[]{wordInfo.getName()});
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }
}
