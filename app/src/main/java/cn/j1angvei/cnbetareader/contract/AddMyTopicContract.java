package cn.j1angvei.cnbetareader.contract;

import android.content.Context;

import java.util.List;

import cn.j1angvei.cnbetareader.bean.Topic;

/**
 * Created by Wayne on 2016/8/28.
 */

public interface AddMyTopicContract {
    interface View extends BaseView {
        Context getViewContext();

        void renderTopics(int groupPosition, List<Topic> topics);

        void onAddMyTopicsSuccess();
    }

    interface Presenter extends BasePresenter<View> {
        void retrieveTopics(int groupPosition);

        void addToMyTopics(List<Topic> topics);

    }
}