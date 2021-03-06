package cn.j1angvei.cbnews.newslist.headline;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import cn.j1angvei.cbnews.R;
import cn.j1angvei.cbnews.bean.Headline;
import cn.j1angvei.cbnews.bean.News;
import cn.j1angvei.cbnews.newslist.NewsAdapter;
import cn.j1angvei.cbnews.util.Navigator;

/**
 * Created by Wayne on 2016/7/5.
 * get headline news from repository
 */
public class HeadlineRvAdapter extends NewsAdapter<Headline, HeadlineRvAdapter.ViewHolder> {
    private static final String TAG = "HeadlineRvAdapter";
    private ArrayList<Headline> mHeadlines;
    private Fragment mView;

    public HeadlineRvAdapter(Fragment fragment) {
        mView = fragment;
        mHeadlines = new ArrayList<>();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_headline, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Context context = holder.itemView.getContext();
        final Headline headline = mHeadlines.get(position);
        holder.tvTitle.setText(headline.getTitle());
        holder.tvDescription.setText(headline.getSummary());
        holder.tvDescription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigator.toContent(holder.getAdapterPosition(), mHeadlines, mView.getActivity());
            }
        });
        //relate items
        final ArrayList<News> newsList = (ArrayList<News>) headline.getRelatedNews();
        if (newsList.size() <= 0) {
            holder.llRelateContainer.setVisibility(View.GONE);
        } else {
            holder.llRelateContainer.setVisibility(View.VISIBLE);
            Glide.with(context).load(headline.getThumb()).into(holder.ivThumb);
            for (int i = 0; i < newsList.size() && i < 3; i++) {
                TextView tv = holder.tvRelate.get(i);
                final News item = headline.getRelatedNews().get(i);
                tv.setVisibility(View.VISIBLE);
                tv.setText(item.getTitle());
                final int finalI = i;
                tv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Navigator.toContent(finalI, newsList, mView.getActivity());
                    }
                });
            }
        }
    }

    @Override
    public int getItemCount() {
        return mHeadlines.size();
    }

    @Override
    public void clear() {
        int size = mHeadlines.size();
        mHeadlines.clear();
        notifyItemRangeRemoved(0, size);
    }

    @Override
    public void add(Headline item) {
        mHeadlines.add(item);
        notifyItemInserted(mHeadlines.size() - 1);
    }

    @Override
    public void add(List<Headline> items) {
        mHeadlines.addAll(items);
        notifyDataSetChanged();
    }

    @Override
    public ArrayList<String> getSids() {
        ArrayList<String> allSid = new ArrayList<>();
        for (Headline headline : mHeadlines) {
            allSid.add(headline.getSid());
        }
        return allSid;
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_headline_title)
        TextView tvTitle;
        @BindView(R.id.tv_headline_description)
        TextView tvDescription;
        @BindView(R.id.iv_headline_thumb)
        ImageView ivThumb;
        @BindViews({R.id.tv_headline_relate_1, R.id.tv_headline_relate_2, R.id.tv_headline_relate_3})
        List<TextView> tvRelate;

        LinearLayout llRelateContainer;
        View itemView;

        public ViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            llRelateContainer = (LinearLayout) itemView.findViewById(R.id.ll_headline_relate_container);
            ButterKnife.bind(this, itemView);
        }
    }
}
