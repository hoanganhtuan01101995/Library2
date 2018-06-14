package com.alticast.viettelottcommons.loader;

import com.alticast.viettelottcommons.api.WindmillCallback;
import com.alticast.viettelottcommons.def.PurchasePathway;
import com.alticast.viettelottcommons.def.entry.EntryPathLogImpl;
import com.alticast.viettelottcommons.manager.AuthManager;
import com.alticast.viettelottcommons.manager.UserGradeDataProcessManager;
import com.alticast.viettelottcommons.resource.ApiError;
import com.alticast.viettelottcommons.resource.Path;
import com.alticast.viettelottcommons.resource.PhotoRes;
import com.alticast.viettelottcommons.resource.Program;
import com.alticast.viettelottcommons.resource.RecommendedVod;
import com.alticast.viettelottcommons.resource.Series;
import com.alticast.viettelottcommons.resource.TrailerRes;
import com.alticast.viettelottcommons.resource.Vod;
import com.alticast.viettelottcommons.resource.response.ProgramList;
import com.alticast.viettelottcommons.resource.response.ProgramProductRes;
import com.alticast.viettelottcommons.resource.response.TrailerList;
import com.alticast.viettelottcommons.service.ServiceGenerator;
import com.alticast.viettelottcommons.serviceMethod.acms.program.ProductMethod;
import com.alticast.viettelottcommons.util.ErrorUtil;
import com.alticast.viettelottcommons.util.Logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by mc.kim on 8/10/2016.
 */
public class ProgramLoader {
    public static enum SortOption {
        ABC, Pop, Recent
    }

    private final String FORMAT_SHORT = "short";
    private final String FORMAT_LONG = "long";

//    public static final int LIMIT = 24;
    public static final int LIMIT = 20;
    private final int LIMIT_PREVIEW = 20;
//    private final int LIMIT_PREVIEW = 9;

    private HashMap<String, Object> programMap = new HashMap<>();

    private static ProgramLoader ourInstance = new ProgramLoader();

    public static ProgramLoader getInstance() {
        return ourInstance;
    }

    private ProgramLoader() {
    }


    public void getRecommendedVod(Response<RecommendedVod> response, final String entry, final WindmillCallback callback) {
        ProductMethod productMethod = ServiceGenerator.getInstance().createSerive(ProductMethod.class);
        RecommendedVod.Basis[] basis = response.body().getBasis();

        if (basis == null) {
            callback.onError(null);
            return;
        }
        final Hashtable<String, String> clickLogoTable = new Hashtable<String, String>();
        ArrayList<String> ids = new ArrayList<String>();
        for (int i = 0; i < basis.length; i++) {
            ids.add(basis[i].value);
            clickLogoTable.put(basis[i].value, basis[i].loggingUrl);
        }

        Call<ProgramList> call = productMethod.getPrograms(AuthManager.getAccessToken(),
                ids, UserGradeDataProcessManager.getInstacne().getInclude(), FORMAT_SHORT);

        call.enqueue(new Callback<ProgramList>() {
            @Override
            public void onResponse(Call<ProgramList> call, Response<ProgramList> response) {
                if (callback == null) {
                    return;
                }

                if (response.isSuccess()) {
                    setClickLogForRecommend(response.body(), entry, clickLogoTable);
                    ProgramList programList = response.body();

                    if(programList != null && programList.getData() != null) {
                        for(Vod vod : programList.getData()) {
                            vod.setRelatedVod(true);
                        }
                    }
                    callback.onSuccess(programList);

                } else {
                    ApiError error = ErrorUtil.parseError(response);
                    callback.onError(error);
                }
            }

            @Override
            public void onFailure(Call<ProgramList> call, Throwable t) {
                if (callback == null) {
                    return;
                }

                callback.onFailure(call, t);
            }
        });
    }

    public void getCategoryVod(String categoryId, int offset, SortOption sortOption, final WindmillCallback callback) {
        ProductMethod productMethod = ServiceGenerator.getInstance().createSerive(ProductMethod.class);
        Logger.print(this, "call getCategoryVod");
        String url = "/api1/contents/categories/" + categoryId + "/programs";
        String until = "now";
        Call<ProgramList> call = null;
        boolean pop = false;
        switch (sortOption) {
            case ABC:
                call = productMethod.getCategoryPrograms(url,
                        AuthManager.getAccessToken(), LIMIT, offset,
                        UserGradeDataProcessManager.getInstacne().getIncludeProduct(), pop, FORMAT_SHORT);
                break;
            case Pop:
                pop = true;
                call = productMethod.getCategoryPrograms(url,
                        AuthManager.getAccessToken(), LIMIT, offset,
                        UserGradeDataProcessManager.getInstacne().getIncludeProduct(), pop, FORMAT_SHORT);
                break;

            case Recent:
                call = productMethod.getCategoryPrograms(url,
                        AuthManager.getAccessToken(), until, LIMIT, offset,
                        UserGradeDataProcessManager.getInstacne().getIncludeProduct(), pop, FORMAT_SHORT);
                break;

        }

        call.enqueue(new Callback<ProgramList>() {
            @Override
            public void onResponse(Call<ProgramList> call, Response<ProgramList> response) {
                if (callback == null) {
                    return;
                }

                if (response.isSuccess()) {
                    settingForCategory(response.body());
                    callback.onSuccess(response.body());

                } else {
                    ApiError error = ErrorUtil.parseError(response);
                    callback.onError(error);
                }
            }

            @Override
            public void onFailure(Call<ProgramList> call, Throwable t) {
                if (callback == null) {
                    return;
                }

                callback.onFailure(call, t);
            }
        });
    }

    public void getCategoryVod(String categoryId, int offset, int limit, final WindmillCallback callback) {
        ProductMethod productMethod = ServiceGenerator.getInstance().createSerive(ProductMethod.class);
        Logger.print(this, "call getCategoryVod");
        String url = "/api1/contents/categories/" + categoryId + "/programs";
        String until = "now";
        Call<ProgramList> call = null;
        call = productMethod.getCategoryPrograms(url,
                AuthManager.getAccessToken(), limit, offset, until,
                UserGradeDataProcessManager.getInstacne().getIncludeProduct(), FORMAT_LONG);

        call.enqueue(new Callback<ProgramList>() {
            @Override
            public void onResponse(Call<ProgramList> call, Response<ProgramList> response) {
                if (callback == null) {
                    return;
                }

                if (response.isSuccess()) {
                    settingForCategory(response.body());
                    callback.onSuccess(response.body());

                } else {
                    ApiError error = ErrorUtil.parseError(response);
                    callback.onError(error);
                }
            }

            @Override
            public void onFailure(Call<ProgramList> call, Throwable t) {
                if (callback == null) {
                    return;
                }

                callback.onFailure(call, t);
            }
        });
    }

    public void getCategoryVodPreview(final String categoryId, int offset, SortOption sortOption, final WindmillCallback callback) {
        ProductMethod productMethod = ServiceGenerator.getInstance().createSerive(ProductMethod.class);
        Logger.print(this, "call getCategoryVod");
        String url = "/api1/contents/categories/" + categoryId + "/programs";
        String until = "now";
        Call<ProgramList> call = null;
        boolean pop = false;
        switch (sortOption) {
            case ABC:
                call = productMethod.getCategoryPrograms(url,
                        AuthManager.getAccessToken(), LIMIT_PREVIEW, offset,
                        UserGradeDataProcessManager.getInstacne().getIncludeProduct(), pop, FORMAT_SHORT);
                break;
            case Pop:
                pop = true;
                call = productMethod.getCategoryPrograms(url,
                        AuthManager.getAccessToken(), LIMIT_PREVIEW, offset,
                        UserGradeDataProcessManager.getInstacne().getIncludeProduct(), pop, FORMAT_SHORT);
                break;

            case Recent:
                call = productMethod.getCategoryPrograms(url,
                        AuthManager.getAccessToken(), until, LIMIT_PREVIEW, offset,
                        UserGradeDataProcessManager.getInstacne().getIncludeProduct(), pop, FORMAT_SHORT);
                break;

        }

        call.enqueue(new Callback<ProgramList>() {
            @Override
            public void onResponse(Call<ProgramList> call, Response<ProgramList> response) {
                if (callback == null) {
                    return;
                }

                if (response.isSuccess()) {
                    response.body().setCategoryId(categoryId);
                    settingForCategory(response.body());
                    callback.onSuccess(response.body());

                } else {
                    ApiError error = ErrorUtil.parseError(response);
                    callback.onError(error);
                }
            }

            @Override
            public void onFailure(Call<ProgramList> call, Throwable t) {
                if (callback == null) {
                    return;
                }

                callback.onFailure(call, t);
            }
        });
    }


    public void getSeries(final Vod vod, int offset, final WindmillCallback callback) {

        Program program = vod.getProgram();

        if (program.getSeries() == null) {
            callback.onError(null);
            return;
        }


        String seriesId = program.getSeries().getId();
        ProductMethod productMethod = ServiceGenerator.getInstance().createSerive(ProductMethod.class);
//        Call<ProgramList> call = productMethod.getSeries(AuthManager.getAccessToken(), seriesId, UserGradeDataProcessManager.getInstacne().getInclude(),
//                offset, LIMIT, FORMAT_LONG);
        Call<ProgramList> call = productMethod.getSeries(AuthManager.getAccessToken(), seriesId, UserGradeDataProcessManager.getInstacne().getInclude(),
                offset, FORMAT_LONG);

        call.enqueue(new Callback<ProgramList>() {
            @Override
            public void onResponse(Call<ProgramList> call, Response<ProgramList> response) {
                if (callback == null) {
                    return;
                }

                if (response != null && response.isSuccess()) {
                    settingForSeries(response.body(), vod);
                    callback.onSuccess(response.body());

                } else {
                    ApiError error = ErrorUtil.parseError(response);
                    callback.onError(error);
                }
            }

            @Override
            public void onFailure(Call<ProgramList> call, Throwable t) {
                if (callback == null) {
                    return;
                }

                callback.onFailure(call, t);
            }
        });
    }
    public ProgramList getSeries(final Vod vod, int offset) throws IOException {

        Program program = vod.getProgram();

        if (program.getSeries() == null) {
            return null;
        }


        String seriesId = program.getSeries().getId();
        ProductMethod productMethod = ServiceGenerator.getInstance().createSerive(ProductMethod.class);
        Call<ProgramList> call = productMethod.getSeries(AuthManager.getAccessToken(), seriesId, UserGradeDataProcessManager.getInstacne().getInclude(),
                offset, LIMIT, FORMAT_LONG);
//        Call<ProgramList> call = productMethod.getSeries(AuthManager.getAccessToken(), seriesId, UserGradeDataProcessManager.getInstacne().getInclude(),
//                offset, FORMAT_LONG);

        Response<ProgramList> response = call.execute();
        if (response != null && response.isSuccess()) {
            settingForSeries(response.body(), vod);
            return response.body();

        } else {
            return null;
        }

    }

    public void getSeriesLastWatchedEpisode(final Vod vod, final WindmillCallback callback) {

        Program program = vod.getProgram();

        if (program.getSeries() == null) {
            callback.onError(null);
        }
        String seriesId = program.getSeries().getId();
        String seasonId = program.getSeries().getSeason();
        ProductMethod productMethod = ServiceGenerator.getInstance().createSerive(ProductMethod.class);
        Call<Series> call = productMethod.getSeriesLastWatchEpisode(AuthManager.getAccessToken(), seriesId, seasonId);

        call.enqueue(new Callback<Series>() {
            @Override
            public void onResponse(Call<Series> call, Response<Series> response) {
                if (callback == null) {
                    return;
                }

                if (response != null && response.isSuccess()) {
//                    settingForSeries(response.body(), vod);
                    callback.onSuccess(response.body());

                } else {
                    ApiError error = ErrorUtil.parseError(response);
                    callback.onError(error);
                }
            }

            @Override
            public void onFailure(Call<Series> call, Throwable t) {
                if (callback == null) {
                    return;
                }

                callback.onFailure(call, t);
            }
        });
    }
    public Series getSeriesLastWatchedEpisode(final Vod vod) throws IOException {

        Program program = vod.getProgram();

        if (program.getSeries() == null) {
            return null;
        }
        String seriesId = program.getSeries().getId();
        String seasonId = program.getSeries().getSeason();
        ProductMethod productMethod = ServiceGenerator.getInstance().createSerive(ProductMethod.class);
        Call<Series> call = productMethod.getSeriesLastWatchEpisode(AuthManager.getAccessToken(), seriesId, seasonId);

        Response<Series> response = call.execute();
        if (response != null && response.isSuccess()) {
            return response.body();
        } else {
            return null;
        }
    }


    public void getProgram(String contentsId, final WindmillCallback callback) {
        ProductMethod productMethod = ServiceGenerator.getInstance().createSerive(ProductMethod.class);
        Logger.print(this, "call getProgram");
        String url = "/api1/contents/programs/" + contentsId;
        Call<Vod> call = productMethod.getProgram(url, AuthManager.getAccessToken(),
                UserGradeDataProcessManager.getInstacne().getInclude(), FORMAT_LONG);

        call.enqueue(new Callback<Vod>() {
            @Override
            public void onResponse(Call<Vod> call, Response<Vod> response) {
                if (callback == null) {
                    return;
                }

                if (response != null && response.isSuccess()) {
                    callback.onSuccess(response.body());

                } else {
                    ApiError error = ErrorUtil.parseError(response);
                    callback.onError(error);
                }
            }

            @Override
            public void onFailure(Call<Vod> call, Throwable t) {
                if (callback == null) {
                    return;
                }

                callback.onFailure(call, t);
            }
        });
    }

    public void getPrograms(ArrayList<String> contentsIdList, final WindmillCallback callback) {
        ProductMethod productMethod = ServiceGenerator.getInstance().createSerive(ProductMethod.class);
        Call<ProgramList> call = productMethod.getPrograms(AuthManager.getAccessToken(), contentsIdList,
                UserGradeDataProcessManager.getInstacne().getInclude(), FORMAT_LONG);

        call.enqueue(new Callback<ProgramList>() {
            @Override
            public void onResponse(Call<ProgramList> call, Response<ProgramList> response) {
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
            public void onFailure(Call<ProgramList> call, Throwable t) {
                if (callback == null) {
                    return;
                }

                callback.onFailure(call, t);
            }
        });
    }
    public void getProductPrograms(String productId, final WindmillCallback callback) {
        ProductMethod productMethod = ServiceGenerator.getInstance().createSerive(ProductMethod.class);
        Call<ProgramProductRes> call = productMethod.getProductProgram(AuthManager.getAccessToken(), productId,
                UserGradeDataProcessManager.getInstacne().getInclude(), FORMAT_LONG);

        call.enqueue(new Callback<ProgramProductRes>() {
            @Override
            public void onResponse(Call<ProgramProductRes> call, Response<ProgramProductRes> response) {
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
            public void onFailure(Call<ProgramProductRes> call, Throwable t) {
                if (callback == null) {
                    return;
                }

                callback.onFailure(call, t);
            }
        });
    }


    public void postReviews(String contentsId, int rating, String message, final WindmillCallback callback) {
        ProductMethod productMethod = ServiceGenerator.getInstance().createSerive(ProductMethod.class);
        String url = "/api1/programs/" + contentsId + "/reviews";
        Call<Void> call = productMethod.requestReview(url, AuthManager.getAccessToken(), message, rating);
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
                if (callback == null) {
                    return;
                }

                callback.onFailure(call, t);
            }
        });
    }

    private void settingForSeries(ProgramList programList, Vod selectedVod) {
        int size = programList.getData().size();
        for (int i = 0; i < size; i++) {
            Vod vod = programList.getData().get(i);
            vod.setPath(selectedVod.getPath());
            programList.getData().set(i, vod);
        }
    }

    public void getTrailer(String programID, final WindmillCallback callback) {
        ProductMethod productMethod = ServiceGenerator.getInstance().createSerive(ProductMethod.class);
        String url = "/api1/contents/programs/" + programID + "/videos";
        int offset = 0;
        int limit = 7;
        Call<TrailerRes> call = productMethod.getTrailer(url,AuthManager.getAccessToken(),offset,limit);

        call.enqueue(new Callback<TrailerRes>() {
            @Override
            public void onResponse(Call<TrailerRes> call, Response<TrailerRes> response) {
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
            public void onFailure(Call<TrailerRes> call, Throwable t) {
                if (callback == null) {
                    return;
                }

                callback.onFailure(call, t);
            }
        });
    }

    public void getPhoto(String programID, final WindmillCallback callback) {
        ProductMethod productMethod = ServiceGenerator.getInstance().createSerive(ProductMethod.class);
        String url = "/api1/contents/programs/" + programID + "/photos";
        int offset = 0;
        int limit = 7;
        Call<PhotoRes> call = productMethod.getPhoto(url,AuthManager.getAccessToken(),offset,limit);

        call.enqueue(new Callback<PhotoRes>() {
            @Override
            public void onResponse(Call<PhotoRes> call, Response<PhotoRes> response) {
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
            public void onFailure(Call<PhotoRes> call, Throwable t) {
                if (callback == null) {
                    return;
                }

                callback.onFailure(call, t);
            }
        });
    }


    public void settingForCategory(ProgramList programList) {
        int size = programList.getData().size();
        for (int i = 0; i < size; i++) {
            Vod vod = programList.getData().get(i);
            Path path = new Path(PurchasePathway.ENTRY_MENU, EntryPathLogImpl.getInstance().getPurchaseMenuString(), vod.getProgram().getId());
            vod.setPath(path);
            programList.getData().set(i, vod);
        }
    }

    public void settingPath(ArrayList<Vod> listVod) {
        for (Vod vod : listVod) {
            Path path = new Path(PurchasePathway.ENTRY_MENU, EntryPathLogImpl.getInstance().getPurchaseMenuString(), vod.getProgram().getId());
            vod.setPath(path);
        }
    }


    private void setClickLogForRecommend(ProgramList programList, String entry, Hashtable<String, String> clickLogoTable) {
        int size = programList.getData().size();
        for (int i = 0; i < size; i++) {
            Vod vod = programList.getData().get(i);
            Path path = new Path(PurchasePathway.ENTRY_RECOMMEND, entry, clickLogoTable.get(vod.getProgram().getId()));
            vod.setPath(path);
            programList.getData().set(i, vod);
        }
    }

    public void put(String id, Object object) {
        programMap.put(id, object);
    }

    public Object get(String id) {
        return programMap.get(id);
    }

    public void clearData() {
        if (programMap == null) {
            return;
        }
        programMap.clear();
    }

}
