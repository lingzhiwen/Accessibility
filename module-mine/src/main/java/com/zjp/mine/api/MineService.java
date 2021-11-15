package com.zjp.mine.api;

import com.zjp.common.bean.ArticleEntity;
import com.zjp.common.bean.UserInfo;
import com.zjp.mine.bean.Leaderboard;
import com.zjp.mine.bean.UserCenter;
import com.zjp.network.bean.BaseResponse;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by zjp on 2020/07/07 15:56
 */
public interface MineService {

    //获取个人积分
    @GET("lg/coin/userinfo/json")
    Observable<BaseResponse<UserInfo>> getIntegral();

    //退出登录
    @GET("user/logout/json")
    Observable<BaseResponse> logout();

    //积分排行榜接口
    @GET("coin/rank/{page}/json")
    Observable<BaseResponse<Leaderboard>> getRankList(@Path("page") int page);

    //获取收藏文章数据
    @GET("lg/collect/list/{page}/json")
    Observable<BaseResponse<ArticleEntity>> getCollectArticleList(@Path("page") int pageNo);

    //用户中心
    @GET("user/{userId}/share_articles/{page}/json")
    Observable<BaseResponse<UserCenter>> getUserCenter(@Path("userId") int userId,
                                                       @Path("page") int page);

    //取消收藏
    @POST("lg/uncollect_originId/{id}/json")
    Observable<BaseResponse> unCollect(@Path("id") int id);

    //收藏
    @POST("lg/collect/{id}/json")
    Observable<BaseResponse> collect(@Path("id") int id);

    //我的分享
    @GET("user/lg/private_articles/{pageNum}/json")
    Observable<BaseResponse<UserCenter>> myShare(@Path("pageNum") int pageNum);

    /**
     * 分享文章
     * 注意需要登录后查看，如果为CSDN，简书等链接会直接通过审核，在对外的分享文章列表中展示。
     */
    @FormUrlEncoded
    @POST("lg/user_article/add/json")
    Observable<BaseResponse> shareArticle(@Field("title") String title,
                                          @Field("link") String link);

    //我的积分记录
    @GET("/lg/coin/list/{pageNum}/json")
    Observable<BaseResponse<Leaderboard>> getIntegralRecord(@Path("pageNum") int pageNum);

}
