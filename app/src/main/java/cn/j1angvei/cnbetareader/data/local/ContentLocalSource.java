package cn.j1angvei.cnbetareader.data.local;

import javax.inject.Inject;
import javax.inject.Singleton;

import cn.j1angvei.cnbetareader.bean.Content;
import cn.j1angvei.cnbetareader.data.local.helper.ContentDbHelper;
import rx.Observable;

import static android.provider.BaseColumns._ID;
import static cn.j1angvei.cnbetareader.data.local.helper.DbHelper.BLANK;
import static cn.j1angvei.cnbetareader.data.local.helper.DbHelper.LIKE;
import static cn.j1angvei.cnbetareader.data.local.helper.DbHelper.QUOTE;
import static cn.j1angvei.cnbetareader.data.local.helper.DbHelper.SELECT_FROM;
import static cn.j1angvei.cnbetareader.data.local.helper.DbHelper.WHERE;

/**
 * Created by Wayne on 2016/7/25.
 */
@Singleton
public class ContentLocalSource implements LocalSource<Content> {
    private final ContentDbHelper mDbHelper;

    @Inject
    public ContentLocalSource(ContentDbHelper dbHelper) {
        mDbHelper = dbHelper;
    }

    @Override
    public void create(Content item) {
        mDbHelper.create(item);
    }

    @Override
    public Observable<Content> read(String... args) {
        String sid = args[0];
        String query = SELECT_FROM + BLANK + mDbHelper.getTableName() + BLANK +
                WHERE + BLANK + _ID + BLANK + LIKE + BLANK +
                QUOTE + sid + QUOTE;
        return mDbHelper.read(query);
    }

    @Override
    public void update(Content item) {

    }

    @Override
    public void delete(Content item) {

    }

}
