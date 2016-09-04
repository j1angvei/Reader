package cn.j1angvei.cnbetareader.activity;

import android.support.v4.view.ViewPager;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.j1angvei.cnbetareader.R;
import cn.j1angvei.cnbetareader.adapter.ImagePagerAdapter;
import cn.j1angvei.cnbetareader.util.MessageUtil;

/**
 * Created by Wayne on 2016/9/2.
 * show image in news content
 */

public class ContentImageActivity extends BaseActivity {
    public static final String CUR_POS = "ContentImageActivity.cur_pos";
    public static final String IMG_URLS = "ContentImageActivity.img_urls";
    @BindView(R.id.viewpager)
    ViewPager mViewPager;

    private int mCurPos;
    private String[] mUrls;

    @Override
    protected void parseIntent() {
        mCurPos = getIntent().getIntExtra(CUR_POS, 0);
        mUrls = getIntent().getStringArrayExtra(IMG_URLS);
    }

    @Override
    protected void doInjection() {

    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_content_image);
        ButterKnife.bind(this);

        ImagePagerAdapter adapter = new ImagePagerAdapter(getSupportFragmentManager(), mUrls);
        mViewPager.setAdapter(adapter);
        mViewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                if (position >= 0 && position < mUrls.length) {
                    MessageUtil.toast(position + "", getApplicationContext());
                }
            }
        });
        mViewPager.setCurrentItem(mCurPos);
    }
}
