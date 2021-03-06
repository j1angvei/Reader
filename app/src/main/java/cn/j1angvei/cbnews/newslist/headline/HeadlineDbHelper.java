package cn.j1angvei.cbnews.newslist.headline;

import android.app.Application;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import cn.j1angvei.cbnews.base.DbHelper;
import cn.j1angvei.cbnews.bean.Headline;
import cn.j1angvei.cbnews.util.DbUtil;
import rx.Observable;

/**
 * Created by Wayne on 2016/8/20.
 */
@Singleton
public class HeadlineDbHelper extends DbHelper<Headline> {
    private static final String DB_NAME = "headline.db";
    private static final int DB_VERSION = 6;
    private final DbUtil mDbUtil;

    @Inject
    public HeadlineDbHelper(Application context, DbUtil dbUtil) {
        super(context, DB_NAME, null, DB_VERSION);
        mDbUtil = dbUtil;
        mTableName = "headline";
        mTableCreate = CREATE_TABLE + BLANK + mTableName + BLANK +
                LEFT_BRACKET +
                COL_SID + BLANK + TYPE_TEXT + BLANK + PRIMARY_KEY + BLANK + COMMA +
                COL_TITLE + BLANK + TYPE_TEXT + COMMA +
                COL_SUMMARY + BLANK + TYPE_TEXT + COMMA +
                COL_THUMB + BLANK + TYPE_TEXT + COMMA +
                COL_SOURCE_TYPE + BLANK + TYPE_TEXT + COMMA +
                COL_RELATED_NEWS + BLANK + TYPE_TEXT +
                RIGHT_BRACKET;
    }

    @Override
    public void create(Headline item) {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put(COL_SID, item.getSid());
            values.put(COL_TITLE, item.getTitle());
            values.put(COL_SUMMARY, item.getSummary());
            values.put(COL_THUMB, item.getThumb());
            values.put(COL_SOURCE_TYPE, item.getType());
            values.put(COL_RELATED_NEWS, mDbUtil.convertNewsList(item.getRelatedNews()));
            db.insertWithOnConflict(mTableName, null, values, SQLiteDatabase.CONFLICT_REPLACE);
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }

    @Override
    public Observable<Headline> read(String query) {
        Cursor cursor = getReadableDatabase().rawQuery(query, null);
        List<Headline> headlines = new ArrayList<>();
        try {
            if (cursor.moveToFirst()) {
                do {
                    Headline headline = new Headline();
                    headline.setSid(cursor.getString(cursor.getColumnIndex(COL_SID)));
                    headline.setTitle(cursor.getString(cursor.getColumnIndex(COL_TITLE)));
                    headline.setSummary(cursor.getString(cursor.getColumnIndex(COL_SUMMARY)));
                    headline.setThumb(cursor.getString(cursor.getColumnIndex(COL_THUMB)));
                    headline.setType(cursor.getString(cursor.getColumnIndex(COL_SOURCE_TYPE)));
                    headline.setRelatedNews(mDbUtil.parseNewsList(cursor.getString(cursor.getColumnIndex(COL_RELATED_NEWS))));
                    headlines.add(headline);
                } while (cursor.moveToNext());
            }
        } finally {
            if (cursor != null && !cursor.isClosed())
                cursor.close();
        }
        return Observable.from(headlines);
    }
}
