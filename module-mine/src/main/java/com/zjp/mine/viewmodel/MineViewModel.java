package com.zjp.mine.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.zjp.base.viewmodel.BaseViewModel;
import com.zjp.common.bean.ArticleEntity;
import com.zjp.common.bean.UserInfo;
import com.zjp.mine.api.MineService;
import com.zjp.mine.bean.Leaderboard;
import com.zjp.mine.bean.UserCenter;
import com.zjp.mine.cache.CacheUtil;
import com.zjp.network.bean.BaseResponse;
import com.zjp.network.https.RetrofitHelper;
import com.zjp.network.observer.NetCallback;
import com.zjp.network.observer.NetHelperObserver;
import com.zjp.network.scheduler.IoMainScheduler;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Created by zjp on 2020/07/07 15:55
 */
public class MineViewModel extends BaseViewModel {

    public MutableLiveData<UserInfo> mIntegralLiveData = new MutableLiveData<>();
    public MutableLiveData<String> mCacheSizeLiveData = new MutableLiveData<>();
    public MutableLiveData<BaseResponse> loginoutLiveData = new MutableLiveData<>();
    public MutableLiveData<List<UserInfo>> leaderBoardLiveData = new MutableLiveData<>();
    public MutableLiveData<ArticleEntity> articleLiveData = new MutableLiveData<>();
    public MutableLiveData<UserCenter> userCenterLiveData = new MutableLiveData<>();
    public MutableLiveData<BaseResponse> mUnCollectMutable = new MutableLiveData<>();
    public MutableLiveData<BaseResponse> mCollectMutable = new MutableLiveData<>();
    public MutableLiveData<BaseResponse> mShareArticleMutable = new MutableLiveData<>();
    public MutableLiveData<Leaderboard> mLeaderBoardMuTable = new MutableLiveData<>();

    public MineViewModel(@NonNull Application application) {
        super(application);
    }

    public void getIntegral() {
        RetrofitHelper.getInstance().create(MineService.class)
                .getIntegral()
                .compose(new IoMainScheduler<>())
                .doOnSubscribe(this)
                .subscribe(new NetHelperObserver<>(new NetCallback<BaseResponse<UserInfo>>() {
                    @Override
                    public void success(BaseResponse<UserInfo> response) {
                        mIntegralLiveData.postValue(response.getData());
                    }

                    @Override
                    public void error(String msg) {

                    }
                }));
    }

    public void getCacheSize() {
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                String cacheSize = CacheUtil.getTotalCacheSize();
                if (!emitter.isDisposed()) {
                    emitter.onNext(cacheSize);
                }
            }
        }).compose(new IoMainScheduler<>())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        addDisposable(d);
                    }

                    @Override
                    public void onNext(String s) {
                        mCacheSizeLiveData.postValue(s);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    public void clearCache() {
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                CacheUtil.clearAllCache();
                String size = CacheUtil.getTotalCacheSize();
                if (!emitter.isDisposed()) {
                    emitter.onNext(size);
                }
            }
        }).compose(new IoMainScheduler<>())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        addDisposable(d);
                    }

                    @Override
                    public void onNext(String size) {
                        mCacheSizeLiveData.postValue(size);
                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }

    public void loginout() {
        RetrofitHelper.getInstance().create(MineService.class)
                .logout()
                .compose(new IoMainScheduler<>())
                .doOnSubscribe(this)
                .subscribe(new NetHelperObserver<>(new NetCallback<BaseResponse>() {
                    @Override
                    public void success(BaseResponse response) {
                        if (response.getErrorCode() == 0) {
                            loginoutLiveData.postValue(response);
                        }
                    }

                    @Override
                    public void error(String msg) {

                    }
                }));
    }

    public void getRankList(int page) {
        RetrofitHelper.getInstance().create(MineService.class)
                .getRankList(page)
                .compose(new IoMainScheduler<>())
                .doOnSubscribe(this)
                .subscribe(new NetHelperObserver<>(new NetCallback<BaseResponse<Leaderboard>>() {
                    @Override
                    public void success(BaseResponse<Leaderboard> response) {
                        List<UserInfo> datas = response.getData().getDatas();
                        if (null != datas && datas.size() > 0) {
                            leaderBoardLiveData.postValue(datas);
                        }

                    }

                    @Override
                    public void error(String msg) {
                    }
                }));
    }

    public void getCollectArticleList(int page) {
        RetrofitHelper.getInstance().create(MineService.class)
                .getCollectArticleList(page)
                .compose(new IoMainScheduler<>())
                .doOnSubscribe(this)
                .subscribe(new NetHelperObserver<>(new NetCallback<BaseResponse<ArticleEntity>>() {
                    @Override
                    public void success(BaseResponse<ArticleEntity> response) {
                        articleLiveData.postValue(response.getData());
                    }

                    @Override
                    public void error(String msg) {

                    }
                }));
    }

    public void getUserCenter(int userId, int page) {
        RetrofitHelper.getInstance().create(MineService.class)
                .getUserCenter(userId, page)
                .compose(new IoMainScheduler<>())
                .doOnSubscribe(this)
                .subscribe(new NetHelperObserver<>(new NetCallback<BaseResponse<UserCenter>>() {
                    @Override
                    public void success(BaseResponse<UserCenter> response) {
                        userCenterLiveData.postValue(response.getData());
                    }

                    @Override
                    public void error(String msg) {

                    }
                }));
    }

    public void collect(int id) {
        RetrofitHelper.getInstance().create(MineService.class)
                .collect(id)
                .compose(new IoMainScheduler<>())
                .doOnSubscribe(this)
                .subscribe(new NetHelperObserver<>(new NetCallback<BaseResponse>() {
                    @Override
                    public void success(BaseResponse response) {
                        mCollectMutable.postValue(response);
                    }

                    @Override
                    public void error(String msg) {

                    }
                }));
    }

    public void unCollect(int id) {
        RetrofitHelper.getInstance().create(MineService.class)
                .unCollect(id)
                .compose(new IoMainScheduler<>())
                .doOnSubscribe(this)
                .subscribe(new NetHelperObserver<>(new NetCallback<BaseResponse>() {
                    @Override
                    public void success(BaseResponse response) {
                        mUnCollectMutable.postValue(response);
                    }

                    @Override
                    public void error(String msg) {

                    }
                }));
    }

    public void myShare(int pageNum) {
        RetrofitHelper.getInstance().create(MineService.class)
                .myShare(pageNum)
                .compose(new IoMainScheduler<>())
                .doOnSubscribe(this)
                .subscribe(new NetHelperObserver<>(new NetCallback<BaseResponse<UserCenter>>() {
                    @Override
                    public void success(BaseResponse<UserCenter> response) {
                        userCenterLiveData.postValue(response.getData());
                    }

                    @Override
                    public void error(String msg) {

                    }
                }));
    }

    public void shareArticle(String title, String link) {
        RetrofitHelper.getInstance().create(MineService.class)
                .shareArticle(title, link)
                .compose(new IoMainScheduler<>())
                .doOnSubscribe(this)
                .subscribe(new NetHelperObserver<>(new NetCallback<BaseResponse>() {
                    @Override
                    public void success(BaseResponse response) {
                        mShareArticleMutable.postValue(response);
                    }

                    @Override
                    public void error(String msg) {

                    }
                }));
    }

    public void getIntegralRecord(int pageNum) {
        RetrofitHelper.getInstance().create(MineService.class)
                .getIntegralRecord(pageNum)
                .compose(new IoMainScheduler<>())
                .doOnSubscribe(this)
                .subscribe(new NetHelperObserver<>(new NetCallback<BaseResponse<Leaderboard>>() {
                    @Override
                    public void success(BaseResponse<Leaderboard> response) {
                        mLeaderBoardMuTable.postValue(response.getData());
                    }

                    @Override
                    public void error(String msg) {

                    }
                }));
    }

}
