package cn.j1angvei.cnbetareader.presenter;

import javax.inject.Inject;

import cn.j1angvei.cnbetareader.bean.Comments;
import cn.j1angvei.cnbetareader.contract.CommentsContract;
import cn.j1angvei.cnbetareader.data.repository.CommentsRepository;
import cn.j1angvei.cnbetareader.di.scope.PerActivity;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Wayne on 2016/7/28.
 * presenter in mvp
 */
@PerActivity
public class CommentsPresenter implements CommentsContract.Presenter {
    private final CommentsRepository mRepository;
    private CommentsContract.View mView;

    @Inject
    public CommentsPresenter(CommentsRepository repository) {
        mRepository = repository;
    }

    @Override
    public void setView(CommentsContract.View view) {
        mView = view;
    }

    @Override
    public void retrieveComments(String token, String op) {
        mView.showLoading();
        mRepository.get(0, token, op)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Comments>() {
                    @Override
                    public void onCompleted() {
                        mView.hideLoading();
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(Comments comments) {
                        mView.showComments(comments);
                    }
                });
    }

    @Override
    public void operateComment(final int position, String... param) {
        mRepository.operateComment(param)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Boolean>() {
                    @Override
                    public void onCompleted() {
                        //change value in adapter, like support add 1
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(Boolean aBoolean) {
                        if (aBoolean) {
                            mView.afterOperateSuccess(position);
                        } else mView.afterOperateFail();
                    }
                });
    }

}
