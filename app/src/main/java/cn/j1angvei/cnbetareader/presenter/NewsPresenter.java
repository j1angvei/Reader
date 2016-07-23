package cn.j1angvei.cnbetareader.presenter;

import cn.j1angvei.cnbetareader.data.repository.NewsRepository;
import cn.j1angvei.cnbetareader.view.NewsView;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Wayne on 2016/7/9.
 */
public class NewsPresenter<T> implements BasePresenter<NewsView<T>> {
    NewsView<T> mView;
    NewsRepository<T> mRepository;

    public NewsPresenter(NewsRepository<T> repository) {
        mRepository = repository;
    }

    public void setView(NewsView<T> newsView) {
        mView = newsView;
    }

    public void retrieveNews(String type, int page) {
        mView.showLoading();
        mRepository.get(page, type)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<T>() {
                    @Override
                    public void onCompleted() {
                        mView.hideLoading();
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(T t) {
                        mView.renderItem(t);
                    }
                });
    }

}
