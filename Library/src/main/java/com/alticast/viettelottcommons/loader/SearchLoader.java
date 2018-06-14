package com.alticast.viettelottcommons.loader;

import com.alticast.viettelottcommons.api.WindmillCallback;
import com.alticast.viettelottcommons.manager.AuthManager;
import com.alticast.viettelottcommons.resource.ApiError;
import com.alticast.viettelottcommons.resource.response.SearchCompletionListRes;
import com.alticast.viettelottcommons.resource.response.SearchKeywordHistoryListRes;
import com.alticast.viettelottcommons.resource.response.SearchKeywordListRes;
import com.alticast.viettelottcommons.resource.response.SearchResultRes;
import com.alticast.viettelottcommons.service.ServiceGenerator;
import com.alticast.viettelottcommons.serviceMethod.acms.search.SearchMethod;
import com.alticast.viettelottcommons.util.ErrorUtil;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by mc.kim on 8/12/2016.
 */
public class SearchLoader {
    public static final String KEY_CATEGORY = "category";

    public static final String CATEGORY_MOVIE = "movie";
    public static final String CATEGORY_VOD = "vod";
    public static final String CATEGORY_TVSHOW = "tvshow";
    public static final String CATEGORY_SERIES = "vgroup";
    public static final String CATEGORY_VSERIES = "vseries";
    public static final String CATEGORY_CHANNEL = "channel";
    public static final String CATEGORY_PROGRAM = "schedule";
    public static final String CATEGORY_CATCHUP = "catchup";

    private String filter = "vgroup:series";
    private String vsuppress = "series:0";
    private String region = "OTT";


    public static final String CATEGORY_ALL = "movie,vgroup,tvshow,schedule,channel,catchup";
    //    public static final String CATEGORY_ALL = "vod,movie,vgroup,vseries,schedule,channel,catchup";
    public static final String CATEGORY_VOD_TYPE = "movie,vgroup,tvshow";
    //    public static final String CATEGORY_VOD_TYPE = "vod,vgroup";
    private static SearchLoader ourInstance = new SearchLoader();

    public static SearchLoader getInstance() {
        return ourInstance;
    }

    private SearchLoader() {
    }

    public static final int SEARCH_LIMIT = 5;

    private final int SEARCH_PAGE_LIMIT = 24;

    private Call currentCall = null;


    public void getKeywordSearchRcmd(final WindmillCallback callback) {
        SearchMethod searchMethod = ServiceGenerator.getInstance().createSerive(SearchMethod.class);
        Call<SearchKeywordListRes> call = searchMethod.getSearchKeywordRcmd(AuthManager.getAccessToken(), AuthManager.getLicense(), SEARCH_LIMIT);
        currentCall = call;
        call.enqueue(new Callback<SearchKeywordListRes>() {
            @Override
            public void onResponse(Call<SearchKeywordListRes> call, Response<SearchKeywordListRes> response) {
                currentCall = null;
                if (callback == null) {
                    return;
                }
                if (response.isSuccess()) {
                    callback.onSuccess(response.body());
                } else {
                    ApiError error = ErrorUtil.parseError(response);
                    callback.onError(error);
                }
            }

            @Override
            public void onFailure(Call<SearchKeywordListRes> call, Throwable t) {
                currentCall = null;
                if (callback == null) {
                    return;
                }
                callback.onFailure(call, t);
            }
        });
    }

    public void getKeywordSearchHistory(final WindmillCallback callback) {
        SearchMethod searchMethod = ServiceGenerator.getInstance().createSerive(SearchMethod.class);
        Call<SearchKeywordHistoryListRes> call = searchMethod.getSearchKeywordHistory(AuthManager.getAccessToken(), AuthManager.getLicense(), SEARCH_LIMIT);
//        currentCall = call;
        call.enqueue(new Callback<SearchKeywordHistoryListRes>() {
            @Override
            public void onResponse(Call<SearchKeywordHistoryListRes> call, Response<SearchKeywordHistoryListRes> response) {
//                currentCall = null;
                if (callback == null) {
                    return;
                }
                if (response.isSuccess()) {
                    callback.onSuccess(response.body());
                } else {
                    ApiError error = ErrorUtil.parseError(response);
                    callback.onError(error);
                }
            }

            @Override
            public void onFailure(Call<SearchKeywordHistoryListRes> call, Throwable t) {
//                currentCall = null;
                if (callback == null) {
                    return;
                }
                callback.onFailure(call, t);
            }
        });
    }

    public void getKeywordSearchPop(final WindmillCallback callback) {
        SearchMethod searchMethod = ServiceGenerator.getInstance().createSerive(SearchMethod.class);
        Call<SearchKeywordListRes> call = searchMethod.getSearchKeywordPop(AuthManager.getAccessToken(), AuthManager.getLicense(), SEARCH_LIMIT);
//        currentCall = call;
        call.enqueue(new Callback<SearchKeywordListRes>() {
            @Override
            public void onResponse(Call<SearchKeywordListRes> call, Response<SearchKeywordListRes> response) {
//                currentCall = null;
                if (callback == null) {
                    return;
                }
                if (response.isSuccess()) {
                    callback.onSuccess(response.body());
                } else {
                    ApiError error = ErrorUtil.parseError(response);
                    callback.onError(error);
                }
            }

            @Override
            public void onFailure(Call<SearchKeywordListRes> call, Throwable t) {
                currentCall = null;
                if (callback == null) {
                    return;
                }
                callback.onFailure(call, t);
            }
        });
    }

    public void getSearchCompletion(String q, final WindmillCallback callback) {
        SearchMethod searchMethod = ServiceGenerator.getInstance().createSerive(SearchMethod.class);
        Call<SearchCompletionListRes> call = searchMethod.getSearchComplete(AuthManager.getAccessToken(), AuthManager.getLicense(), q, SEARCH_LIMIT);
        currentCall = call;
        call.enqueue(new Callback<SearchCompletionListRes>() {
            @Override
            public void onResponse(Call<SearchCompletionListRes> call, Response<SearchCompletionListRes> response) {
                currentCall = null;
                if (callback == null) {
                    return;
                }
                if (response.isSuccess()) {
                    callback.onSuccess(response.body());
                } else {
                    ApiError error = ErrorUtil.parseError(response);
                    callback.onError(error);
                }
            }

            @Override
            public void onFailure(Call<SearchCompletionListRes> call, Throwable t) {
                currentCall = null;
                if (callback == null) {
                    return;
                }
                callback.onFailure(call, t);
            }
        });
    }

    public void getSearchResult(String q, String category, String inputRoute, int offset, final WindmillCallback callback) {
        SearchMethod searchMethod = ServiceGenerator.getInstance().createSerive(SearchMethod.class);
        Call<SearchResultRes> call = searchMethod.getSearchResult(AuthManager.getAccessToken(), AuthManager.getLicense(),q, category, SEARCH_PAGE_LIMIT, offset, filter, vsuppress, region, inputRoute);
        currentCall = call;
        call.enqueue(new Callback<SearchResultRes>() {
            @Override
            public void onResponse(Call<SearchResultRes> call, Response<SearchResultRes> response) {
                currentCall = null;
                if (callback == null) {
                    return;
                }
                if (response.isSuccess()) {
                    callback.onSuccess(response.body());
                } else {
                    ApiError error = ErrorUtil.parseError(response);
                    callback.onError(error);
                }
            }

            @Override
            public void onFailure(Call<SearchResultRes> call, Throwable t) {
                currentCall = null;
                if (callback == null) {
                    return;
                }
                callback.onFailure(call, t);
            }
        });
    }

    public void getSearchResultForCheck(String q, String inputRoute, final WindmillCallback callback) {
        SearchMethod searchMethod = ServiceGenerator.getInstance().createSerive(SearchMethod.class);
        Call<SearchResultRes> call = searchMethod.getSearchResult(AuthManager.getAccessToken(),AuthManager.getLicense(), q, CATEGORY_ALL, SEARCH_PAGE_LIMIT, 0, filter, vsuppress, region, inputRoute);
        currentCall = call;
        call.enqueue(new Callback<SearchResultRes>() {
            @Override
            public void onResponse(Call<SearchResultRes> call, Response<SearchResultRes> response) {
                currentCall = null;
                if (callback == null) {
                    return;
                }
                if (response.isSuccess()) {
                    callback.onSuccess(response.body());
                } else {
                    ApiError error = ErrorUtil.parseError(response);
                    callback.onError(error);
                }
            }

            @Override
            public void onFailure(Call<SearchResultRes> call, Throwable t) {
                currentCall = null;
                if (callback == null) {
                    return;
                }
                callback.onFailure(call, t);
            }
        });
    }

    public void checkSearchResultForCheck(final String q, String inputRoute, final WindmillCallback callback) {
        SearchMethod searchMethod = ServiceGenerator.getInstance().createSerive(SearchMethod.class);
        Call<SearchResultRes> call = searchMethod.getSearchResult(AuthManager.getAccessToken(),AuthManager.getLicense(), q, CATEGORY_ALL, SEARCH_PAGE_LIMIT, 0, filter, vsuppress, region, inputRoute);
        currentCall = call;
        call.enqueue(new Callback<SearchResultRes>() {
            @Override
            public void onResponse(Call<SearchResultRes> call, Response<SearchResultRes> response) {
                currentCall = null;
                if (callback == null) {
                    return;
                }

                if (response.isSuccess()) {
                    SearchResultRes searchResultRes = response.body();
                    if (searchResultRes.isHasSearchResult()) {
                        callback.onSuccess(q);
                    } else {
                        callback.onError(null);
                    }

                } else {
                    ApiError error = ErrorUtil.parseError(response);
                    callback.onError(error);
                }
            }

            @Override
            public void onFailure(Call<SearchResultRes> call, Throwable t) {
                currentCall = null;
                if (callback == null) {
                    return;
                }
                callback.onFailure(call, t);
            }
        });
    }


    public void cancel() {
        if (currentCall != null) {
            currentCall.cancel();
        }
    }

    public void deleteKeywordSearchHistory(final WindmillCallback callback) {
        SearchMethod searchMethod = ServiceGenerator.getInstance().createSerive(SearchMethod.class);
        Call<Void> call = searchMethod.deleteSearchKeywordHistory(AuthManager.getAccessToken(), "DELETE");
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (callback == null) {
                    return;
                }
                if (response.isSuccess()) {
                    callback.onSuccess(response.body());
                } else {
                    ApiError error = ErrorUtil.parseError(response);
                    callback.onError(error);
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {

            }
        });
    }

}
