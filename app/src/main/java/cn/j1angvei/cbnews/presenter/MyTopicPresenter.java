package cn.j1angvei.cbnews.presenter;

import android.util.Log;

import java.util.List;

import javax.inject.Inject;

import cn.j1angvei.cbnews.bean.Topic;
import cn.j1angvei.cbnews.contract.MyTopicContract;
import cn.j1angvei.cbnews.data.repository.Repository;
import cn.j1angvei.cbnews.di.qualifier.QTopic;
import cn.j1angvei.cbnews.di.scope.PerFragment;
import cn.j1angvei.cbnews.util.AppUtil;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;

/**
 * Created by Wayne on 2016/8/19.
 */
@PerFragment
public class MyTopicPresenter implements MyTopicContract.Presenter {
    private static final String TAG = "MyTopicPresenter";
    private Repository<Topic> mRepository;
    private AppUtil mAppUtil;
    private MyTopicContract.View mView;

    @Inject
    public MyTopicPresenter(@QTopic Repository<Topic> repository, AppUtil appUtil) {
        mRepository = repository;
        mAppUtil = appUtil;
    }

    @Override
    public void setView(MyTopicContract.View view) {
        mView = view;
    }

    @Override
    public void retrieveMyTopics() {
        Observable.from(mAppUtil.getMyTopicIds())
                .concatMap(new Func1<String, Observable<Topic>>() {
                    @Override
                    public Observable<Topic> call(String id) {
                        Log.d(TAG, "call: " + id);
                        return mRepository.getData(0, id, null);
                    }
                })
                .toList()
                .subscribe(new Subscriber<List<Topic>>() {
                    @Override
                    public void onCompleted() {
                        Log.d(TAG, "onCompleted: ");
                        mView.hideLoading();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "onError: ", e);
                        mView.onMyTopicsIsEmpty();
                    }

                    @Override
                    public void onNext(List<Topic> topics) {
                        Log.d(TAG, "onNext: " + topics);
                        mView.renderMyTopics(topics);
                    }
                });

    }

    @Override
    public void storeMyTopics() {

    }

}
