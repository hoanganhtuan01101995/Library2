package com.alticast.viettelottcommons.loader;

import com.alticast.android.util.Log;
import com.alticast.viettelottcommons.WindmillConfiguration;
import com.alticast.viettelottcommons.api.WindmillCallback;
import com.alticast.viettelottcommons.def.PurchasePathway;
import com.alticast.viettelottcommons.def.entry.EntryPathLogImpl;
import com.alticast.viettelottcommons.manager.AuthManager;
import com.alticast.viettelottcommons.manager.ChannelManager;
import com.alticast.viettelottcommons.manager.MyContentManager;
import com.alticast.viettelottcommons.resource.ApiError;
import com.alticast.viettelottcommons.resource.Channel;
import com.alticast.viettelottcommons.resource.Login;
import com.alticast.viettelottcommons.resource.Path;
import com.alticast.viettelottcommons.resource.Program;
import com.alticast.viettelottcommons.resource.Resume;
import com.alticast.viettelottcommons.resource.Vod;
import com.alticast.viettelottcommons.resource.response.ProgramList;
import com.alticast.viettelottcommons.resource.response.ResumeListRes;
import com.alticast.viettelottcommons.resource.response.STBInfo;
import com.alticast.viettelottcommons.resource.response.UserDataListRes;
import com.alticast.viettelottcommons.resource.response.UserDataRes;
import com.alticast.viettelottcommons.service.ServiceGenerator;
import com.alticast.viettelottcommons.serviceMethod.SendLogMethod;
import com.alticast.viettelottcommons.serviceMethod.upms.UserDataMethod;
import com.alticast.viettelottcommons.util.ErrorUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by mc.kim on 8/11/2016.
 */
public class UserDataLoader {
    public static final String TYPE_CHANNEL = "channel";
    public static final int LIMIT = 24;
    public static final String TYPE_VOD = "vod";
    public static final Log LOG = Log.createLog("UserDataLoader");
    private static UserDataLoader ourInstance = new UserDataLoader();

    public static UserDataLoader getInstance() {
        return ourInstance;
    }

    private UserDataLoader() {
    }


    public void createMyContent(Program program, String group, final WindmillCallback callback) {
        UserDataMethod userDataMethod = ServiceGenerator.getInstance().createSerive(UserDataMethod.class);
        String id = program.getId();
        String name = program.getTitle(WindmillConfiguration.LANGUAGE);
        String type = "program";
        String filter_value = "0";
        boolean auto_overwrite = false;


        Call<UserDataRes> call = userDataMethod.createMyContent(AuthManager.getAccessToken(), id, name, type, filter_value, auto_overwrite, group);
        call.enqueue(new Callback<UserDataRes>() {
            @Override
            public void onResponse(Call<UserDataRes> call, Response<UserDataRes> response) {
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
            public void onFailure(Call<UserDataRes> call, Throwable t) {
                if (callback == null) {
                    return;
                }
                callback.onFailure(call, t);
            }
        });
    }

    public void getMyContents(int offset, String group, String sort, final WindmillCallback callback) {
        UserDataMethod userDataMethod = ServiceGenerator.getInstance().createSerive(UserDataMethod.class);
        String type = "program";
//        String filter_value = "0";
//        String operator = "operator";
//        String operator = "all";
        if (sort == null) {
            sort = MyContentManager.STR_NEWSEST;
        }

        Call<UserDataListRes> call = userDataMethod.getMyContents(AuthManager.getAccessToken(), offset, 200, type, group, sort);
        call.enqueue(new Callback<UserDataListRes>() {
            @Override
            public void onResponse(Call<UserDataListRes> call, Response<UserDataListRes> response) {
                if (callback == null) {
                    return;
                }
                if (response.isSuccess()) {
                    ArrayList<String> ids = new ArrayList<>();
                    ArrayList<UserDataRes> res = response.body().getData();

                    int size = res.size();
                    for (int i = 0; i < size; i++) {
                        UserDataRes userDataRes = res.get(i);
                        ids.add(userDataRes.getId());
                    }

                    if (ids.isEmpty()) {
                        callback.onSuccess(null);
                    }

                    ProgramLoader.getInstance().getPrograms(ids, new WindmillCallback() {
                        @Override
                        public void onSuccess(Object obj) {
                            String host = PurchasePathway.ENTRY_MYCONTENTS;
                            ProgramList programList = (ProgramList) obj;
                            int size = programList.getData().size();
                            for (int i = 0; i < size; i++) {
                                Vod vod = programList.getData().get(i);
                                Path path = new Path(host, vod.getProgram().getTitle(WindmillConfiguration.LANGUAGE), null);
                                vod.setPath(path);
                                programList.getData().set(i, vod);
                            }
                            callback.onSuccess(programList);
                        }

                        @Override
                        public void onFailure(Call call, Throwable t) {
                            callback.onFailure(call, t);
                        }

                        @Override
                        public void onError(ApiError error) {
                            callback.onError(error);
                        }
                    });


                } else {
                    ApiError error = ErrorUtil.parseError(response);
                    callback.onError(error);
                }
            }

            @Override
            public void onFailure(Call<UserDataListRes> call, Throwable t) {
                if (callback == null) {
                    return;
                }
                callback.onFailure(call, t);
            }
        });
    }

    public void deleteMyContents(ArrayList<String> ids, final WindmillCallback callback) {
        UserDataMethod userDataMethod = ServiceGenerator.getInstance().createSerive(UserDataMethod.class);
        Call<Void> call = userDataMethod.deleteMyContent(AuthManager.getAccessToken(), ids);
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


    public void createMyChannel(Channel channel, final WindmillCallback callback) {
        UserDataMethod userDataMethod = ServiceGenerator.getInstance().createSerive(UserDataMethod.class);
        String id = channel.getId();
        String name = channel.getChannelName();
        String type = "program";
        String filter_value = "0";
        boolean auto_overwrite = true;

        Call<UserDataListRes> call = userDataMethod.createMyChannel(AuthManager.getAccessToken(), id, name, type, filter_value, auto_overwrite);
        call.enqueue(new Callback<UserDataListRes>() {
            @Override
            public void onResponse(Call<UserDataListRes> call, Response<UserDataListRes> response) {
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
            public void onFailure(Call<UserDataListRes> call, Throwable t) {
                if (callback == null) {
                    return;
                }
                callback.onFailure(call, t);
            }
        });
    }

    public void getMyChannels(int offset, final WindmillCallback callback) {
        UserDataMethod userDataMethod = ServiceGenerator.getInstance().createSerive(UserDataMethod.class);
        String type = "program";
//        String filter_value = "0";
//        String operator = "operator";
        Call<UserDataListRes> call = userDataMethod.getMyChannels(AuthManager.getAccessToken(), offset, 100, type);
        call.enqueue(new Callback<UserDataListRes>() {
            @Override
            public void onResponse(Call<UserDataListRes> call, Response<UserDataListRes> response) {
                if (callback == null) {
                    return;
                }
                if (response.isSuccess()) {
                    ChannelManager.getInstance().add(response.body());
                    callback.onSuccess(response.body());
                } else {
                    ApiError error = ErrorUtil.parseError(response);
                    callback.onError(error);
                }
            }

            @Override
            public void onFailure(Call<UserDataListRes> call, Throwable t) {
                if (callback == null) {
                    return;
                }
                callback.onFailure(call, t);
            }
        });
    }


    public void deleteMyChannels(ArrayList<String> ids, final WindmillCallback callback) {
        UserDataMethod userDataMethod = ServiceGenerator.getInstance().createSerive(UserDataMethod.class);
        Call<Void> call = userDataMethod.deleteMyChannel(AuthManager.getAccessToken(), ids);
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

    LinkedHashMap<String, Resume> map = new LinkedHashMap<String, Resume>();


    public void getResumeList(int offset, final WindmillCallback callback) {
        UserDataMethod userDataMethod = ServiceGenerator.getInstance().createSerive(UserDataMethod.class);

        Call<ResumeListRes> call = userDataMethod.getResumeList(AuthManager.getAccessToken(), LIMIT, offset, TYPE_VOD);

        call.enqueue(new Callback<ResumeListRes>() {
            @Override
            public void onResponse(Call<ResumeListRes> call, Response<ResumeListRes> response) {
                if (callback == null) {
                    return;
                }
                if (response.isSuccess()) {
                    ArrayList<String> ids = new ArrayList<>();
                    final ArrayList<Resume> resumes = response.body().getData();

                    int size = resumes.size();


                    for (int i = 0; i < size; i++) {
                        Resume resume = resumes.get(i);
                        ids.add(resume.getId());
                        map.put(resume.getId(), resume);
                    }
                    if (ids.size() < 1) {
                        callback.onSuccess(null);
                        return;
                    }

                    ProgramLoader.getInstance().getPrograms(ids, new WindmillCallback() {
                        @Override
                        public void onSuccess(Object obj) {
                            String host = PurchasePathway.ENTRY_MENU;
                            ProgramList programList = (ProgramList) obj;
                            int size = programList.getData().size();
                            for (int i = 0; i < size; i++) {
                                Vod vod = programList.getData().get(i);
                                Resume resume = map.get(vod.getProgram().getId());
                                if (resume != null) {
                                    vod.setTimeOffset(resume.getTime_offset());
                                }
                                Path path = new Path(host, EntryPathLogImpl.getInstance().getPurchaseMenuString(), null);
                                vod.setPath(path);
                                programList.getData().set(i, vod);
                            }

                            callback.onSuccess(programList);
                        }

                        @Override
                        public void onFailure(Call call, Throwable t) {
                            callback.onFailure(call, t);
                        }

                        @Override
                        public void onError(ApiError error) {
                            callback.onError(error);
                        }
                    });


                } else {
                    ApiError error = ErrorUtil.parseError(response);
                    callback.onError(error);
                }
            }

            @Override
            public void onFailure(Call<ResumeListRes> call, Throwable t) {
                if (callback == null) {
                    return;
                }
                callback.onFailure(call, t);
            }
        });
    }

    public void deleteResumeList(String id, final WindmillCallback callback) {
        UserDataMethod userDataMethod = ServiceGenerator.getInstance().createSerive(UserDataMethod.class);
        Call call = userDataMethod.deleteResumeList(AuthManager.getAccessToken(), id);
        call.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
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
            public void onFailure(Call call, Throwable t) {
                if (callback == null) {
                    return;
                }
                callback.onFailure(call, t);
            }
        });
    }

    public void deleteRecentlyWatchedList(String id, final WindmillCallback callback) {
        UserDataMethod userDataMethod = ServiceGenerator.getInstance().createSerive(UserDataMethod.class);
        Call call = userDataMethod.deleteRecentlyWatchedList(AuthManager.getAccessToken(), id);
        call.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
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
            public void onFailure(Call call, Throwable t) {
                if (callback == null) {
                    return;
                }
                callback.onFailure(call, t);
            }
        });
    }

    public void deleteRecentlyWatchedPairList(String id, final WindmillCallback callback) {
        UserDataMethod userDataMethod = ServiceGenerator.getInstance().createSerive(UserDataMethod.class);
        Call call = userDataMethod.deleteRecentlyWatchedPairList(AuthManager.getAccessToken(), id);
        call.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
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
            public void onFailure(Call call, Throwable t) {
                if (callback == null) {
                    return;
                }
                callback.onFailure(call, t);
            }
        });
    }


    public void getSTBIp(String stbUdid, final WindmillCallback callback) {
        UserDataMethod userDataMethod = ServiceGenerator.getInstance().createSerive(UserDataMethod.class);
        String withIp = "device://stb/";
        Call<STBInfo> call = userDataMethod.getSTBInfo(AuthManager.getAccessToken(), withIp + stbUdid);
        call.enqueue(new Callback<STBInfo>() {
            @Override
            public void onResponse(Call<STBInfo> call, Response<STBInfo> response) {
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
            public void onFailure(Call<STBInfo> call, Throwable t) {
                if (callback == null) {
                    return;
                }
                callback.onFailure(call, t);
            }
        });
    }

    public Response<STBInfo> getSTBIp(String stbUdid) {
        UserDataMethod userDataMethod = ServiceGenerator.getInstance().createSerive(UserDataMethod.class);
        String withIp = "device://stb/";
        Call<STBInfo> call = userDataMethod.getSTBInfo(AuthManager.getAccessToken(), withIp + stbUdid);
        try {
            return call.execute();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void getResumeListHome(int offset, int limit, final WindmillCallback callback) {
        UserDataMethod userDataMethod = ServiceGenerator.getInstance().createSerive(UserDataMethod.class);

        Call<ResumeListRes> call = userDataMethod.getResumeList(AuthManager.getAccessToken(), limit, offset, TYPE_VOD);

        call.enqueue(new Callback<ResumeListRes>() {
            @Override
            public void onResponse(Call<ResumeListRes> call, Response<ResumeListRes> response) {
                if (callback == null) {
                    return;
                }
                if (response.isSuccess()) {
                    ArrayList<String> ids = new ArrayList<>();
                    final ArrayList<Resume> resumes = response.body().getData();

                    int size = resumes.size();


                    for (int i = 0; i < size; i++) {
                        Resume resume = resumes.get(i);
                        ids.add(resume.getId());
                    }
                    if (ids.size() < 1) {
                        callback.onSuccess(null);
                        return;
                    }

                    ProgramLoader.getInstance().getPrograms(ids, new WindmillCallback() {
                        @Override
                        public void onSuccess(Object obj) {
                            String host = PurchasePathway.ENTRY_PROMOTION;
                            ProgramList programList = (ProgramList) obj;
                            int size = programList.getData().size();
                            for (int i = 0; i < size; i++) {
                                Vod vod = programList.getData().get(i);
                                vod.setType(Vod.TYPE_RESUME);
                                Path path = new Path(host, EntryPathLogImpl.getInstance().getPurchaseMenuString(), null);
                                vod.setPath(path);
                                programList.getData().set(i, vod);
                            }

                            callback.onSuccess(programList);
                        }

                        @Override
                        public void onFailure(Call call, Throwable t) {
                            callback.onFailure(call, t);
                        }

                        @Override
                        public void onError(ApiError error) {
                            callback.onError(error);
                        }
                    });


                } else {
                    ApiError error = ErrorUtil.parseError(response);
                    callback.onError(error);
                }
            }

            @Override
            public void onFailure(Call<ResumeListRes> call, Throwable t) {
                if (callback == null) {
                    return;
                }
                callback.onFailure(call, t);
            }
        });
    }

    public void getRecentlyWatched(int offset, int limit, final WindmillCallback callback) {
        UserDataMethod userDataMethod = ServiceGenerator.getInstance().createSerive(UserDataMethod.class);

        Call<ResumeListRes> call = userDataMethod.getRecentlyWatched(AuthManager.getAccessToken(), offset, limit);

        call.enqueue(new Callback<ResumeListRes>() {
            @Override
            public void onResponse(Call<ResumeListRes> call, Response<ResumeListRes> response) {
                if (callback == null) {
                    return;
                }
                if (response.isSuccess()) {
                    ArrayList<String> ids = new ArrayList<>();
                    final ArrayList<Resume> resumes = response.body().getData();

                    int size = resumes.size();


                    for (int i = 0; i < size; i++) {
                        Resume resume = resumes.get(i);
                        ids.add(resume.getProgram_id());
                    }
                    if (ids.size() < 1) {
                        callback.onSuccess(null);
                        return;
                    }

                    ProgramLoader.getInstance().getPrograms(ids, new WindmillCallback() {
                        @Override
                        public void onSuccess(Object obj) {
                            String host = PurchasePathway.ENTRY_PROMOTION;
                            ProgramList programList = (ProgramList) obj;
                            int size = programList.getData().size();
                            for (int i = 0; i < size; i++) {
                                Vod vod = programList.getData().get(i);
                                vod.setTimeOffset(resumes.get(i).getTime_offset());
                                vod.setType(Vod.TYPE_RESUME);
                                vod.setWatchedID(resumes.get(i).getId());
                                Path path = new Path(host, EntryPathLogImpl.getInstance().getPurchaseMenuString(), null);
                                vod.setPath(path);
                                programList.getData().set(i, vod);
                            }

                            callback.onSuccess(programList);
                        }

                        @Override
                        public void onFailure(Call call, Throwable t) {
                            callback.onFailure(call, t);
                        }

                        @Override
                        public void onError(ApiError error) {
                            callback.onError(error);
                        }
                    });


                } else {
                    ApiError error = ErrorUtil.parseError(response);
                    callback.onError(error);
                }
            }

            @Override
            public void onFailure(Call<ResumeListRes> call, Throwable t) {
                if (callback == null) {
                    return;
                }
                callback.onFailure(call, t);
            }
        });
    }

    public void getRecentlyWatchedPair(int offset, int limit, final WindmillCallback callback) {
        UserDataMethod userDataMethod = ServiceGenerator.getInstance().createSerive(UserDataMethod.class);

        Call<ResumeListRes> call = userDataMethod.getRecentlyWatchedPair(AuthManager.getAccessToken(), offset, limit);

        call.enqueue(new Callback<ResumeListRes>() {
            @Override
            public void onResponse(Call<ResumeListRes> call, Response<ResumeListRes> response) {
                if (callback == null) {
                    return;
                }
                if (response.isSuccess()) {
                    ArrayList<String> ids = new ArrayList<>();
                    final ArrayList<Resume> resumes = response.body().getData();

                    int size = resumes.size();


                    for (int i = 0; i < size; i++) {
                        Resume resume = resumes.get(i);
                        ids.add(resume.getProgram_id());
                    }
                    if (ids.size() < 1) {
                        callback.onSuccess(null);
                        return;
                    }

                    ProgramLoader.getInstance().getPrograms(ids, new WindmillCallback() {
                        @Override
                        public void onSuccess(Object obj) {
                            String host = PurchasePathway.ENTRY_PROMOTION;
                            ProgramList programList = (ProgramList) obj;
                            int size = programList.getData().size();
                            for (int i = 0; i < size; i++) {
                                Vod vod = programList.getData().get(i);
                                vod.setTimeOffset(resumes.get(i).getTime_offset());
                                vod.setType(Vod.TYPE_RESUME);
                                vod.setWatchedID(resumes.get(i).getId());
                                Path path = new Path(host, EntryPathLogImpl.getInstance().getPurchaseMenuString(), null);
                                vod.setPath(path);
                                programList.getData().set(i, vod);
                            }

                            callback.onSuccess(programList);
                        }

                        @Override
                        public void onFailure(Call call, Throwable t) {
                            callback.onFailure(call, t);
                        }

                        @Override
                        public void onError(ApiError error) {
                            callback.onError(error);
                        }
                    });


                } else {
                    ApiError error = ErrorUtil.parseError(response);
                    callback.onError(error);
                }
            }

            @Override
            public void onFailure(Call<ResumeListRes> call, Throwable t) {
                if (callback == null) {
                    return;
                }
                callback.onFailure(call, t);
            }
        });
    }

    public void sendLog(boolean vietteltv_logger, String phone_number, String log_type, String log_content, String device_type,
                        String version_name, int version_code, String device_code, String brand_name, String key_access,
                        String environment) {
        SendLogMethod sendLogMethod = ServiceGenerator.getSendLogInstance().createSeriveSendLog(SendLogMethod.class);
        Call<Void> call = sendLogMethod.sendLog("" + vietteltv_logger, phone_number, log_type, log_content, device_type, version_name, "" + version_code, device_code, brand_name,
                key_access, environment);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccess()) {
                    LOG.printMsg("isSuccess");
                } else {
                    LOG.printMsg("error:");
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                LOG.printMsg("onFailure");
            }
        });
    }

}
