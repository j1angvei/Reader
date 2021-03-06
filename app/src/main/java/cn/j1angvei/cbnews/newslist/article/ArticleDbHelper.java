package cn.j1angvei.cbnews.newslist.article;

import android.app.Application;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import cn.j1angvei.cbnews.base.DbHelper;
import cn.j1angvei.cbnews.bean.Article;
import cn.j1angvei.cbnews.util.DateUtil;
import rx.Observable;

/**
 * Created by Wayne on 2016/8/20.
 * Store all articles from latest articles or topic articles
 */
@Singleton
public class ArticleDbHelper extends DbHelper<Article> {
    private static final String TAG = "ArticleDbHelper";
    private static final String DB_NAME = "article.db";
    private static final int DB_VERSION = 6;

    @Inject
    public ArticleDbHelper(Application context) {
        super(context, DB_NAME, null, DB_VERSION);
        mTableName = "article";
        mTableCreate = CREATE_TABLE + BLANK + mTableName + BLANK +
                LEFT_BRACKET +
                _ID + BLANK + TYPE_INTEGER + BLANK + PRIMARY_KEY + BLANK + AUTO_INCREMENT + COMMA +
                COL_SID + BLANK + TYPE_TEXT + BLANK + COMMA +
                COL_TITLE + BLANK + TYPE_TEXT + COMMA +
                COL_TOPIC + BLANK + TYPE_TEXT + COMMA +
                COL_SUMMARY + BLANK + TYPE_TEXT + COMMA +
                COL_COMMENT_NUM + BLANK + TYPE_TEXT + COMMA +
                COL_VIEWER_NUM + BLANK + TYPE_TEXT + COMMA +
                COL_TIME + BLANK + TYPE_TEXT + COMMA +
                COL_THUMB + BLANK + TYPE_TEXT + COMMA +
                COL_SOURCE_TYPE + BLANK + TYPE_TEXT + COMMA +
                COL_SOURCE + BLANK + TYPE_TEXT +
                RIGHT_BRACKET;
    }

    @Override
    public void create(Article item) {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put(COL_SID, item.getSid());
            values.put(COL_TITLE, item.getTitle());
            values.put(COL_TOPIC, item.getTopic());
            values.put(COL_SUMMARY, item.getSummary());
            values.put(COL_COMMENT_NUM, item.getCommentNum());
            values.put(COL_VIEWER_NUM, item.getViewerNum());
            values.put(COL_TIME, DateUtil.convertDefault(item.getTime()));
            values.put(COL_SOURCE, item.getSource());
            values.put(COL_SOURCE_TYPE, item.getType());
            values.put(COL_THUMB, item.getThumb());
            db.insertWithOnConflict(mTableName, null, values, SQLiteDatabase.CONFLICT_IGNORE);
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }

    @Override
    public Observable<Article> read(String query) {
        Cursor cursor = getReadableDatabase().rawQuery(query, null);
        List<Article> articles = new ArrayList<>();
        try {
            if (cursor.moveToFirst()) {
                do {
                    Article article = new Article();
                    article.setSid(cursor.getString(cursor.getColumnIndex(COL_SID)));
                    article.setTitle(cursor.getString(cursor.getColumnIndex(COL_TITLE)));
                    article.setTopic(cursor.getString(cursor.getColumnIndex(COL_TOPIC)));
                    article.setSummary(cursor.getString(cursor.getColumnIndex(COL_SUMMARY)));
                    article.setCommentNum(cursor.getString(cursor.getColumnIndex(COL_COMMENT_NUM)));
                    article.setViewerNum(cursor.getString(cursor.getColumnIndex(COL_VIEWER_NUM)));
                    try {
                        article.setTime(DateUtil.parseDefault(cursor.getString(cursor.getColumnIndex(COL_TIME))));
                    } catch (ParseException e) {
                        article.setTime(new Date());
                    }
                    article.setSource(cursor.getString(cursor.getColumnIndex(COL_SOURCE)));
                    article.setType(cursor.getString(cursor.getColumnIndex(COL_SOURCE_TYPE)));
                    article.setThumb(cursor.getString(cursor.getColumnIndex(COL_THUMB)));
                    articles.add(article);
                } while (cursor.moveToNext());
            }
        } finally {
            if (cursor != null && !cursor.isClosed())
                cursor.close();
        }
        return Observable.from(articles);
    }

    @Override
    public void delete(String selection, String[] args) {
        getWritableDatabase().delete(mTableName, selection, args);
        Log.d(TAG, "delete: " + selection + " " + Arrays.toString(args));
    }
}
