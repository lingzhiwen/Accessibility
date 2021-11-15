package com.zjp.mine.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;
import com.zjp.base.activity.BaseActivity;
import com.zjp.common.bean.UserInfo;
import com.zjp.common.bean.page.PageInfo;
import com.zjp.mine.R;
import com.zjp.mine.adapter.LeaderboardAdapter;
import com.zjp.mine.databinding.ActivityLeaderBoardBinding;
import com.zjp.mine.viewmodel.MineViewModel;

/**
 * Created by zjp on 2020/7/15 22:15.
 */
public class LeaderboardActivity extends BaseActivity<ActivityLeaderBoardBinding, MineViewModel> implements OnRefreshLoadMoreListener {

    private PageInfo pageInfo;
    private LeaderboardAdapter leaderboardAdapter;
    private UserInfo userInfo;

    public static void start(Context context, UserInfo userInfo) {
        Intent intent = new Intent(context, LeaderboardActivity.class);
        intent.putExtra("userInfo", userInfo);
        context.startActivity(intent);
    }

    @Override
    protected void initImmersionBar() {
        super.initImmersionBar();
        mImmersionBar
                .statusBarDarkFont(true)
                .init();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_leader_board;
    }

    @Override
    protected void initView() {
        super.initView();
        mViewDataBinding.titleview.setTitle("积分排行榜");
        pageInfo = new PageInfo();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mViewDataBinding.titleview.setElevation(10f);
        }

        Intent intent = getIntent();
        if (intent != null) {
            userInfo = intent.getParcelableExtra("userInfo");
            mViewDataBinding.setMyselfIntergral(userInfo);
        }
        setLoadSir(mViewDataBinding.clContent);
        loadData();

        mViewDataBinding.includeRefresh.recy.setLayoutManager(new LinearLayoutManager(this));
        mViewDataBinding.includeRefresh.recy.setAdapter(leaderboardAdapter = new LeaderboardAdapter());
        mViewDataBinding.includeRefresh.refresh.setOnRefreshLoadMoreListener(this);
    }

    @Override
    protected void initData() {
        super.initData();
        mViewModel.leaderBoardLiveData.observe(this, leaderboards -> {
            if (mViewDataBinding.includeRefresh.refresh.getState().isOpening) {
                mViewDataBinding.includeRefresh.refresh.finishRefresh();
                mViewDataBinding.includeRefresh.refresh.finishLoadMore();
            }
            if (leaderboards != null && leaderboards.size() > 0) {
                if (pageInfo.isFirstPage()) {
                    showContent();
                    leaderboardAdapter.setList(leaderboards);
                } else {
                    leaderboardAdapter.addData(leaderboards);
                }
                pageInfo.nextPage();
            } else {
                if (pageInfo.isFirstPage()) {
                    showEmpty();
                } else {
                    leaderboardAdapter.addData(leaderboards);
                    mViewDataBinding.includeRefresh.refresh.finishLoadMoreWithNoMoreData();
                }
            }
        });

        leaderboardAdapter.setOnItemClickListener((adapter, view, position) -> UserCenterActivity.start(LeaderboardActivity.this, leaderboardAdapter.getData().get(position).getUserId()));
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        loadData();
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        pageInfo.reset();
        loadData();
    }

    private void loadData() {
        mViewModel.getRankList(pageInfo.page);
    }
}
