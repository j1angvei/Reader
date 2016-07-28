package cn.j1angvei.cnbetareader.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.j1angvei.cnbetareader.R;
import cn.j1angvei.cnbetareader.activity.BaseActivity;
import cn.j1angvei.cnbetareader.adapter.NewsAdapter;
import cn.j1angvei.cnbetareader.presenter.NewsPresenter;
import cn.j1angvei.cnbetareader.view.NewsView;

/**
 * Created by Wayne on 2016/7/9.
 */
public abstract class NewsFragment<T, VH extends RecyclerView.ViewHolder> extends BaseFragment implements NewsView<T>, SwipeRefreshLayout.OnRefreshListener, View.OnClickListener {
    static final String NEWS_TYPE = "NewsFragment.news_type";
    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;

    @Inject
    LinearLayoutManager mLinearLayoutManager;
    @Inject
    NewsAdapter<T, VH> mAdapter;
    @Inject
    NewsPresenter<T> mPresenter;

    FloatingActionButton mFab;

    private String mType;
    private int mPage = 1;

    void setBundle(String type) {
        Bundle args = new Bundle();
        args.putString(NEWS_TYPE, type);
        setArguments(args);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mType = getArguments().getString(NEWS_TYPE);
        inject(((BaseActivity) getActivity()).getActivityComponent());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news_list, container, false);
        mFab = (FloatingActionButton) getActivity().findViewById(R.id.fab);
        ButterKnife.bind(this, view);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mFab.setOnClickListener(this);
        mPresenter.setView(this);
        onRefresh();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mRecyclerView.setAdapter(null);
        mRecyclerView.setLayoutManager(null);
    }

    @Override
    public void showLoading() {
        mSwipeRefreshLayout.setRefreshing(true);
    }

    @Override
    public void hideLoading() {
        mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void renderItem(T item) {
        mAdapter.add(item);
    }

    @Override
    public void clearItems() {
        mPage = 1;
        mAdapter.clear();
    }

    @Override
    public void onRefresh() {
        clearItems();
        retrieveItem();
    }

    protected void retrieveItem() {
        mPresenter.retrieveNews(mType, mPage++);
    }


}