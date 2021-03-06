package cn.j1angvei.cbnews.newscontent;

import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.j1angvei.cbnews.R;
import cn.j1angvei.cbnews.base.BaseActivity;
import cn.j1angvei.cbnews.bean.News;
import cn.j1angvei.cbnews.di.component.DaggerActivityComponent;
import cn.j1angvei.cbnews.di.module.ActivityModule;
import cn.j1angvei.cbnews.util.Navigator;

/**
 * Created by Wayne on 2016/7/5.
 * display news content
 */
public class ContentActivity extends BaseActivity {
    private static final String TAG = "ContentActivity";
    public static final String NEWS_LIST = "ContentActivity.news_source";
    public static final String NEWS_POSITION = "ContentActivity.news_position";
    @BindView(R.id.viewpager)
    ViewPager mViewPager;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.fab)
    FloatingActionButton mFab;
    ContentPagerAdapter mAdapter;
    private List<News> mNewses;
    private int mInitPos;

    @Override
    protected void parseIntent() {
        mInitPos = getIntent().getIntExtra(NEWS_POSITION, 0);
        mNewses = getIntent().getParcelableArrayListExtra(NEWS_LIST);
    }

    @Override
    protected void doInjection() {
        mActivityComponent = DaggerActivityComponent.builder()
                .applicationComponent(getApplicationComponent())
                .activityModule(new ActivityModule(this))
                .build();
        mActivityComponent.inject(this);
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_news_content);
        ButterKnife.bind(this);
        //toolbar
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
//        actionBar.setTitle(R.string.title_activity_content);
        //fab
        mFab.setImageResource(R.drawable.ic_group_comments_white);
        //viewpager
        mAdapter = new ContentPagerAdapter(getSupportFragmentManager(), mNewses);
        mViewPager.setAdapter(mAdapter);
        ViewPager.SimpleOnPageChangeListener listener = new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                final News news = mNewses.get(position);
                setTitle(news.getTitle());
                mFab.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Navigator.toComments(news.getSid(), news.getTitle(), v.getContext());
                    }
                });
            }
        };
        mViewPager.addOnPageChangeListener(listener);
        mViewPager.setCurrentItem(mInitPos);
        if (mInitPos == 0) {
            listener.onPageSelected(mInitPos);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
