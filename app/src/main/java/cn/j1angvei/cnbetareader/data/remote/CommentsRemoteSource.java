package cn.j1angvei.cnbetareader.data.remote;

import java.io.IOException;

import javax.inject.Inject;
import javax.inject.Singleton;

import cn.j1angvei.cnbetareader.bean.Comments;
import cn.j1angvei.cnbetareader.converter.CommentsConverter;
import cn.j1angvei.cnbetareader.data.remote.api.CBApiWrapper;
import cn.j1angvei.cnbetareader.exception.ResponseParseException;
import okhttp3.ResponseBody;
import rx.Observable;
import rx.functions.Func1;

/**
 * Created by Wayne on 2016/7/23.
 */
@Singleton
public class CommentsRemoteSource extends RemoteSource<Comments> {
    private CommentsConverter mConverter;

    @Inject
    public CommentsRemoteSource(CBApiWrapper wrapper, CommentsConverter converter) {
        super(wrapper);
        mConverter = converter;
    }

    @Override
    public Observable<Comments> loadData(int page, String... args) {
        String sid = args[0], sn = args[1];
        return mApiWrapper.getComments(sid, sn)
                .flatMap(new Func1<ResponseBody, Observable<Comments>>() {
                    @Override
                    public Observable<Comments> call(ResponseBody responseBody) {
                        try {
                            return mConverter.toObservable(responseBody.string());
                        } catch (IOException e) {
                            return Observable.error(new ResponseParseException());
                        }
                    }
                })
                .retry(1);
    }
}
