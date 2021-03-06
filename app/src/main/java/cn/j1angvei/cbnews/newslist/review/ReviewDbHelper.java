package cn.j1angvei.cbnews.newslist.review;

import android.app.Application;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import cn.j1angvei.cbnews.base.DbHelper;
import cn.j1angvei.cbnews.bean.Review;
import rx.Observable;

/**
 * Created by Wayne on 2016/8/20.
 */
@Singleton
public class ReviewDbHelper extends DbHelper<Review> {
    private static final String DB_NAME = "review.db";
    private static final int DB_VERSION = 4;

    @Inject
    public ReviewDbHelper(Application context) {
        super(context, DB_NAME, null, DB_VERSION);
        mTableName = "review";
        mTableCreate = CREATE_TABLE + BLANK + mTableName + BLANK +
                LEFT_BRACKET +
                COL_SID + BLANK + TYPE_TEXT + BLANK + BLANK + PRIMARY_KEY + COMMA +
                COL_TITLE + BLANK + TYPE_TEXT + COMMA +
                COL_TID + BLANK + TYPE_TEXT + COMMA +
                COL_COMMENT + BLANK + TYPE_TEXT + COMMA +
                COL_SOURCE_TYPE + BLANK + TYPE_TEXT + COMMA +
                COL_LOCATION + BLANK + TYPE_TEXT +
                RIGHT_BRACKET;
    }

    @Override
    public void create(Review item) {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put(COL_SID, item.getSid());
            values.put(COL_TITLE, item.getTitle());
            values.put(COL_TID, item.getTid());
            values.put(COL_COMMENT, item.getComment());
            values.put(COL_SOURCE_TYPE, item.getType());
            values.put(COL_LOCATION, item.getLocation());
            db.insertWithOnConflict(mTableName, null, values, SQLiteDatabase.CONFLICT_IGNORE);
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }

    @Override
    public Observable<Review> read(String query) {
        Cursor cursor = getReadableDatabase().rawQuery(query, null);
        List<Review> reviews = new ArrayList<>();
        try {
            if (cursor.moveToFirst()) {
                do {
                    Review review = new Review();
                    review.setSid(cursor.getString(cursor.getColumnIndex(COL_SID)));
                    review.setTitle(cursor.getString(cursor.getColumnIndex(COL_TITLE)));
                    review.setTid(cursor.getString(cursor.getColumnIndex(COL_TID)));
                    review.setComment(cursor.getString(cursor.getColumnIndex(COL_COMMENT)));
                    review.setType(cursor.getString(cursor.getColumnIndex(COL_SOURCE_TYPE)));
                    review.setLocation(cursor.getString(cursor.getColumnIndex(COL_LOCATION)));
                    reviews.add(review);
                } while (cursor.moveToNext());
            }
        } finally {
            if (cursor != null && !cursor.isClosed())
                cursor.close();
        }
        return Observable.from(reviews);
    }
}
