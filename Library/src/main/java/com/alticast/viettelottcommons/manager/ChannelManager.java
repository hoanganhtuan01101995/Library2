package com.alticast.viettelottcommons.manager;

import android.util.Log;
import android.util.Pair;

import com.alticast.viettelottcommons.WindmillConfiguration;
import com.alticast.viettelottcommons.api.WindmillCallback;
import com.alticast.viettelottcommons.loader.ChannelLoader;
import com.alticast.viettelottcommons.loader.UserDataLoader;
import com.alticast.viettelottcommons.resource.ApiError;
import com.alticast.viettelottcommons.resource.Channel;
import com.alticast.viettelottcommons.resource.ChannelProduct;
import com.alticast.viettelottcommons.resource.Schedule;
import com.alticast.viettelottcommons.resource.response.ChannelRes;
import com.alticast.viettelottcommons.resource.response.ScheduleListRes;
import com.alticast.viettelottcommons.resource.response.UserDataListRes;
import com.alticast.viettelottcommons.resource.response.UserDataRes;
import com.alticast.viettelottcommons.service.ServiceGenerator;
import com.alticast.viettelottcommons.serviceMethod.acms.program.ProductMethod;
import com.alticast.viettelottcommons.util.ErrorUtil;
import com.alticast.viettelottcommons.util.Logger;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.alticast.viettelottcommons.GlobalInfo.listSchedule;

/**
 * Created by mc.kim on 8/12/2016.
 */
public class ChannelManager {
    public static final int SORT_ASC = 0;//오름차순
    public static final int SORT_DESC = 1;//내림차순
    public static final String NONE_CHANNEL = "-1";

    public static final String UPDATE_CHANNEL_WATCH_NOW = "UPDATE_CHANNEL_WATCH_NOW";

    public String currentChannelPlayingID;

    private static ChannelManager ourInstance = new ChannelManager();

    public static ChannelManager getInstance() {
        return ourInstance;
    }

    private ChannelManager() {
    }

    /***
     * 1st : Id in Channel Object
     * 2st : channelProduct
     */
    private HashMap<String, ChannelProduct> channelMap = new HashMap<>();
    private Map<String, String> mIdMap = new LinkedHashMap<>();


    private ArrayList<ChannelProduct> channelList = new ArrayList<>();


    public String getCurrentChannelId() {
        return currentChannelId;
    }

    public void setCurrentChannelId(String currentChannelId) {
        this.currentChannelId = currentChannelId;
    }

    private String currentChannelId = null;

    public void clearData() {
        if (channelList != null) {
            channelList.clear();
        }

        if (channelMap != null) {
            channelMap.clear();
        }

        if (mIdMap != null) {
            mIdMap.clear();
        }
    }

    public int getChannelSize() {

        return channelList.size();
    }

    public void initChannelMap(ArrayList<ChannelProduct> list) {
        channelList = list;
        int size = list.size();
        for (int i = 0; i < size; i++) {
            ChannelProduct channelProduct = list.get(i);
            channelMap.put(channelProduct.getChannel().getId(), channelProduct);
        }

    }

    public void initChannelMap(ArrayList<ChannelProduct> list, List<String> listChannelIdsAvai) {
//        channelList = list;
        if (list == null || list.isEmpty()) return;

        ArrayList<ChannelProduct> listChannels = new ArrayList<>();


        int size = list.size();
        for (int i = 0; i < size; i++) {
            ChannelProduct channelProduct = list.get(i);
            if (channelProduct.getChannel() == null || channelProduct.getService_id() == null) {
                continue;
            }

//            channelMap.put(channelProduct.getChannel().getId(), channelProduct);
            if (listChannelIdsAvai == null || listChannelIdsAvai.contains(channelProduct.getService_id())) {
                listChannels.add(channelProduct);
                channelMap.put(channelProduct.getChannel().getId(), channelProduct);
            }
        }

        if (listChannels.isEmpty()) {
            channelList = list;
            for (ChannelProduct channelProduct : channelList) {
                if (channelProduct.getChannel() == null || channelProduct.getChannel().getId() == null) {
                    continue;
                }
                channelMap.put(channelProduct.getChannel().getId(), channelProduct);
            }

        } else {
            channelList = listChannels;
        }
    }

    public boolean checkData() {
        if (channelList == null || channelList.isEmpty()) {
            return false;
        }

        if (channelMap == null || channelMap.isEmpty()) {
            return false;
        }

        return true;
    }

    public void clearChannelMap() {
        if (channelList != null) {
            channelList.clear();
        }

        if (channelMap != null) {
            channelMap.clear();
        }
    }

    public boolean contains(String id) {
        synchronized (this) {
            return mIdMap.containsKey(id);
        }
    }

    public int countContent(String type) {
        int count = 0;
        for (Map.Entry<String, String> entry : mIdMap.entrySet()) {
            if (entry.getValue().equals(type)) {
                count++;
            }
        }

        return count;
    }


    public void add(UserDataListRes userDataListRes) {
        ArrayList<UserDataRes> data = userDataListRes.getData();

        int size = data.size();

        for (int i = 0; i < size; i++) {
            if (!mIdMap.containsKey(data.get(i).getId())) {
                mIdMap.put(data.get(i).getId(), UserDataLoader.TYPE_CHANNEL);
            }
        }
    }

    public void add(UserDataRes userDataRes) {
        if (mIdMap != null && !mIdMap.containsKey(userDataRes.getId())) {
            mIdMap.put(userDataRes.getId(), UserDataLoader.TYPE_CHANNEL);
        }
    }

    public void add(String id) {
        if (mIdMap != null && !mIdMap.containsKey(id)) {
            mIdMap.put(id, UserDataLoader.TYPE_CHANNEL);
        }
    }

    public void add(String id, String type) {
        if (mIdMap != null && !mIdMap.containsKey(id)) {
            mIdMap.put(id, type);
        }
    }

    public void delete(ArrayList<String> ids) {
        for (String id : ids) {
            if (mIdMap.containsKey(id)) {
                mIdMap.remove(id);
            }
        }
    }

    public ArrayList<Schedule> getMyScheduleList(ArrayList<Schedule> list) {
        ArrayList<Schedule> scheduleList = new ArrayList<>();
        for (Schedule schedule : list) {
            if (mIdMap.containsKey(schedule.getChannel().getId())) {
                scheduleList.add(schedule);
            }
        }
        return scheduleList;
    }

    private HashMap<String, String> hashGenre = null;

    public HashMap<String, String> getHashGenre() {
        if (hashGenre == null) {
            hashGenre = new HashMap<>();
            hashGenre.put("1:1:0:0", "Tin tức thời sự");
            hashGenre.put("1:2:0:0", "Phim truyện");
            hashGenre.put("1:3:0:0", "Thể thao");
            hashGenre.put("1:4:0:0", "Giải trí tổng hợp");
            hashGenre.put("1:5:0:0", "Thanh thiếu niên");
            hashGenre.put("1:6:0:0", "Kênh tỉnh");
            hashGenre.put("1:7:0:0", "Du lịch khám phá");
            hashGenre.put("1:8:0:0", "Gói kênh K+");
            hashGenre.put("1:9:0:0", "Gói kênh HD1");
            hashGenre.put("1:10:0:0", "Gói kênh HD2");
            hashGenre.put("1:11:0:0", "VTV Cab");
            hashGenre.put("1:12:0:0", "Golf Channels");
        }
        return hashGenre;
    }

    public void intChannelList(final WindmillCallback callback) {


        ChannelLoader.getInstance().getChannels(new WindmillCallback() {
            @Override
            public void onSuccess(Object obj) {
                final ChannelRes channelRes = (ChannelRes) obj;
                final ArrayList<ChannelProduct> list = channelRes.getData();
//                initChannelMap(list);
//                if (callback != null) {
//                    callback.onSuccess(obj);
//                }

                ChannelLoader.getInstance().getChannelIds(new WindmillCallback() {
                    @Override
                    public void onSuccess(Object obj) {
                        initChannelMap(list, (List<String>) obj);
                        if (callback != null) {
                            callback.onSuccess(channelRes);
                        }
                    }

                    @Override
                    public void onFailure(Call call, Throwable t) {
                        initChannelMap(list, null);
                        if (callback != null) {
                            callback.onSuccess(channelRes);
                        }
                    }

                    @Override
                    public void onError(ApiError error) {
                        initChannelMap(list, null);
                        if (callback != null) {
                            callback.onSuccess(channelRes);
                        }
                    }
                });

            }

            @Override
            public void onFailure(Call call, Throwable t) {
                if (callback != null) {
                    callback.onFailure(call, t);
                }
            }

            @Override
            public void onError(ApiError error) {
                if (callback != null) {
                    callback.onError(error);
                }
            }
        });
    }

    public ArrayList<ChannelProduct> getChannelList() {
        return channelList;
    }

    public boolean isHasData() {
        return channelList != null && !channelList.isEmpty();
    }

    public HashMap<ChannelProduct, Schedule> getHashMap() {
        return hashMap;
    }

    public void setHashMap(HashMap<ChannelProduct, Schedule> hashMap) {
        this.hashMap = new HashMap<>(hashMap);
    }

    private HashMap<ChannelProduct, Schedule> hashMap;

    public ArrayList<Schedule> getChannelListFav(ArrayList<Schedule> schedulesList) {
        ArrayList<Schedule> list = new ArrayList<>();
        for (Schedule schedule : schedulesList) {
            if (mIdMap.containsKey(schedule.getChannel().getId()))
                list.add(schedule);
        }
        return list;
    }

    public void setChannelList(ArrayList<ChannelProduct> channelList) {
        this.channelList = channelList;
    }

    public void getCatchUpChannels(final OnChannelCallback callback) {
        if (callback == null) {
            return;
        }


        if (channelList.isEmpty()) {
            callback.needLoading(true);

            intChannelList(new WindmillCallback() {
                @Override
                public void onSuccess(Object obj) {
                    ChannelRes channelRes = (ChannelRes) obj;

                    callback.onSuccess(getCatchupChannel(channelRes.getData()));
                }

                @Override
                public void onFailure(Call call, Throwable t) {
                    callback.onFail(0);
                }

                @Override
                public void onError(ApiError error) {
                    callback.onError(error);
                }
            });


        } else {
            callback.onSuccess(getCatchupChannel(channelList));
        }

    }

    private ArrayList<ChannelProduct> getCatchupChannel(ArrayList<ChannelProduct> channelProducts) {
        ArrayList<ChannelProduct> channels = new ArrayList<>();
        int size = channelProducts.size();
        Logger.print(this, "channelProduct size : " + size);

        for (int i = 0; i < size; i++) {
            ChannelProduct eachProducts = channelProducts.get(i);
            Logger.print(this, "idx : " + i + ", getChannelDuration : " + eachProducts.getCatchUpDuration());
            if (eachProducts.getCatchUpDuration() > 0) {
                channels.add(eachProducts);
            }
        }

        return channels;
    }


    public void getChannels(final OnChannelCallback callback) {
        if (callback == null) {
            return;
        }

        if (channelList.isEmpty()) {
            callback.needLoading(true);

            intChannelList(new WindmillCallback() {
                @Override
                public void onSuccess(Object obj) {
                    ChannelRes channelRes = (ChannelRes) obj;
                    callback.onSuccess(channelRes.getData());
                }

                @Override
                public void onFailure(Call call, Throwable t) {
                    callback.onFail(0);
                }

                @Override
                public void onError(ApiError error) {
                    callback.onError(error);
                }
            });


        } else {
            callback.onSuccess(channelList);
        }

    }

    public void getCurrentScheduleList(final WindmillCallback callback) {
        if (callback == null) {
            return;
        }

        if (ChannelManager.getInstance().checkData()) {
            getCurrentScheduleList(channelList.size(), callback);
        } else {
            ChannelManager.getInstance().getChannel(new WindmillCallback() {
                @Override
                public void onSuccess(Object obj) {
                    if (ChannelManager.getInstance().checkData()) {
                        getCurrentScheduleList(channelList.size(), callback);
                    }
                }

                @Override
                public void onFailure(Call call, Throwable t) {

                }

                @Override
                public void onError(ApiError error) {

                }
            });
        }


    }

    public void getChannelSchedule(final String channelId, final WindmillCallback callback) {
        if (channelId.equalsIgnoreCase("0")) return;
        final Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);

        cal.add(Calendar.DAY_OF_YEAR, -7);
        long start = cal.getTimeInMillis();
        cal.add(Calendar.DAY_OF_YEAR, 10);
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        cal.set(Calendar.MILLISECOND, 59);
        long end = cal.getTimeInMillis();

        ChannelLoader.getInstance().getScheduleList(channelId, start, end, 10000, new WindmillCallback() {
            @Override
            public void onSuccess(Object obj) {
                callback.onSuccess(obj);
            }

            @Override
            public void onFailure(Call call, Throwable t) {
            }

            @Override
            public void onError(ApiError error) {
            }
        });
    }

    private void getCurrentScheduleList(int size, final WindmillCallback callback) {
        ChannelLoader.getInstance().getCurrentScheduleList(size, new WindmillCallback() {
            @Override
            public void onSuccess(Object obj) {
                ScheduleListRes scheduleListRes = (ScheduleListRes) obj;
                ArrayList<Schedule> scheduleList = scheduleListRes.getData();
                int size = scheduleList.size();

                for (int i = 0; i < size; i++) {
                    Schedule schedule = scheduleList.get(i);
                    String channelId = schedule.getChannel().getId();
                    ChannelProduct channelProduct = channelMap.get(channelId);
                    if (channelProduct != null) {
                        schedule.setChannel(channelProduct.getChannel());
                    }
                    scheduleList.set(i, schedule);
                }

                try {
                    Comparator<Schedule> comparator = new Comparator<Schedule>() {
                        @Override
                        public int compare(Schedule lhs, Schedule rhs) {
                            ChannelProduct channelProductlhs = channelMap.get(lhs.getChannel().getId());
                            ChannelProduct channelProductrhs = channelMap.get(rhs.getChannel().getId());
                            if (channelProductlhs == null || channelProductrhs == null) {
                                return 0;
                            }
                            return channelProductlhs.getNumber() - channelProductrhs.getNumber();
                        }
                    };
                    Collections.sort(scheduleList, comparator);
                } catch (Exception ex) {
                    Log.e("ChannelException", "ChannelException " + ex.getMessage());
                }

                scheduleListRes.setData(scheduleList);
                callback.onSuccess(scheduleListRes);

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
    }

    public void getCurrentScheduleList(ArrayList<String> ids, final WindmillCallback callback) {
        if (callback == null) {
            return;
        }

        ChannelLoader.getInstance().getCurrentScheduleList(ids, new WindmillCallback() {
            @Override
            public void onSuccess(Object obj) {
                ScheduleListRes scheduleListRes = (ScheduleListRes) obj;
                ArrayList<Schedule> scheduleList = scheduleListRes.getData();
                int size = scheduleList.size();

                for (int i = 0; i < size; i++) {
                    Schedule schedule = scheduleList.get(i);
                    String channelId = schedule.getChannel().getId();
                    ChannelProduct channelProduct = channelMap.get(channelId);
                    schedule.setChannel(channelProduct.getChannel());
                    scheduleList.set(i, schedule);
                }
                scheduleListRes.setData(scheduleList);
                callback.onSuccess(scheduleListRes);
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
    }

    private final String REGION_OTT = "OTT";
    private final String FORMAT_SHORT = "short";
    private final String FORMAT_LONG = "long";
    private final int LIMIT = 2000;
    private final int OFFSET = 0;

    public void getChannel(final WindmillCallback callback) {
        ProductMethod productMethod = ServiceGenerator.getInstance().createSerive(ProductMethod.class);

        String region = REGION_OTT;
        if (WindmillConfiguration.isAlacaterVersion && AuthManager.currentLevel() == AuthManager.UserLevel.LEVEL3) {
            region = HandheldAuthorization.getInstance().getCurrentUser().getSo_id();
        }

        Call<ChannelRes> call = productMethod.getChannels(AuthManager.getAccessToken(),
                OFFSET, region, LIMIT, UserGradeDataProcessManager.getInstacne().getIncludeProduct(), FORMAT_SHORT);

        call.enqueue(new Callback<ChannelRes>() {
            @Override
            public void onResponse(Call<ChannelRes> call,final Response<ChannelRes> channelRes) {
                if (callback == null) {
                    return;
                }
                if (channelRes.isSuccess()) {
                    final ArrayList<ChannelProduct> list = channelRes.body().getData();
                    Logger.print(this, "initChannel() : " + list.size());
//                    ChannelManager.getInstance().initChannelMap(list);

                    ChannelLoader.getInstance().getChannelIds(new WindmillCallback() {
                        @Override
                        public void onSuccess(Object obj) {
                            initChannelMap(list, (List<String>) obj);
                            if (callback != null) {
                                callback.onSuccess(channelRes);
                            }
                        }

                        @Override
                        public void onFailure(Call call, Throwable t) {
                            initChannelMap(list, null);
                            if (callback != null) {
                                callback.onSuccess(channelRes);
                            }
                        }

                        @Override
                        public void onError(ApiError error) {
                            initChannelMap(list, null);
                            if (callback != null) {
                                callback.onSuccess(channelRes);
                            }
                        }
                    });

                    callback.onSuccess(null);
                } else {
                    ApiError error = ErrorUtil.parseError(channelRes);
                    callback.onError(error);
                }

            }

            @Override
            public void onFailure(Call<ChannelRes> call, Throwable t) {
                if (callback == null) {
                    return;
                }
                callback.onFailure(call, t);
            }
        });
    }

    public ChannelProduct getChannel(String pid) {

        int size = channelList.size();

        for (int i = 0; i < size; i++) {
            if (channelList.get(i).getChannel().getId().equalsIgnoreCase(pid)) {
                return channelList.get(i);
            }
        }

        return null;
    }

    public ChannelProduct getChannelThroughServiceId(String id) {
        int size = channelList.size();

        for (int i = 0; i < size; i++) {
            if (channelList.get(i).getService_id().equalsIgnoreCase(id)) {
                return channelList.get(i);
            }
        }

        return null;
    }

    public boolean isChannelListInitted() {
        if (channelList.size() > 0) return true;
        else return false;
    }

    public interface OnChannelCallback {
        public void needLoading(boolean needLoading);

        public void onSuccess(ArrayList<ChannelProduct> channels);

        public void onFail(int why);

        public void onError(ApiError error);
    }

    public String getCurrentChannelPlayingID() {
        return currentChannelPlayingID;
    }

    public void setCurrentChannelPlayingID(String currentChannelPlayingID) {
        currentChannelPlayingID = currentChannelPlayingID;
    }

}
