package cn.j1angvei.cnbetareader.converter;

import com.google.gson.Gson;

import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import cn.j1angvei.cnbetareader.bean.Bookmark;
import cn.j1angvei.cnbetareader.bean.News;
import rx.Observable;

/**
 * Created by Wayne on 2016/7/26.
 */
@Singleton
public class BookmarkConverter extends NewsConverter<Bookmark> {
    @Inject
    public BookmarkConverter(Gson gson) {
        super(gson);
    }

    @Override
    public Bookmark to(String json) {
        News news = mGson.fromJson(json, News.class);
        Bookmark bookmark = new Bookmark();
        bookmark.setSid(news.getSid());
        bookmark.setTitle(news.getTitle());
        bookmark.setTime(new Date());
        return bookmark;
    }

    @Override
    public List<Bookmark> toList(String json) {
        return null;
    }

    @Override
    public Observable<Bookmark> toObservable(String json) {
        Bookmark bookmark = to(json);
        if (bookmark == null) {
            return Observable.empty();
        }
        return Observable.just(bookmark);
    }
}
