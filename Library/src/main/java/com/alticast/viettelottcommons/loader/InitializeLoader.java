package com.alticast.viettelottcommons.loader;

import android.os.AsyncTask;

import com.alticast.viettelottcommons.WindmillConfiguration;
import com.alticast.viettelottcommons.api.WindmillCallback;
import com.alticast.viettelottcommons.def.PurchasePathway;
import com.alticast.viettelottcommons.def.entry.EntryPathLogImpl;
import com.alticast.viettelottcommons.manager.AuthManager;
import com.alticast.viettelottcommons.manager.ChannelManager;
import com.alticast.viettelottcommons.manager.HandheldAuthorization;
import com.alticast.viettelottcommons.manager.MenuManager;
import com.alticast.viettelottcommons.manager.MyContentManager;
import com.alticast.viettelottcommons.manager.UserGradeDataProcessManager;
import com.alticast.viettelottcommons.resource.ApiError;
import com.alticast.viettelottcommons.resource.CampaignData;
import com.alticast.viettelottcommons.resource.CampaignGroupData;
import com.alticast.viettelottcommons.resource.Campaigns;
import com.alticast.viettelottcommons.resource.ChannelProduct;
import com.alticast.viettelottcommons.resource.Path;
import com.alticast.viettelottcommons.resource.RegisterTagDevice;
import com.alticast.viettelottcommons.resource.RequestTagDevice;
import com.alticast.viettelottcommons.resource.ResultRes;
import com.alticast.viettelottcommons.resource.Vod;
import com.alticast.viettelottcommons.resource.ads.Placement;
import com.alticast.viettelottcommons.resource.ads.PlacementDecision;
import com.alticast.viettelottcommons.resource.response.AdDecisionRes;
import com.alticast.viettelottcommons.resource.response.CampaignGroupRes;
import com.alticast.viettelottcommons.resource.response.CampaignsRes;
import com.alticast.viettelottcommons.resource.response.ChannelRes;
import com.alticast.viettelottcommons.resource.response.ConfigListRes;
import com.alticast.viettelottcommons.resource.response.MenuListRes;
import com.alticast.viettelottcommons.resource.response.ProgramList;
import com.alticast.viettelottcommons.service.ServiceGenerator;
import com.alticast.viettelottcommons.serviceMethod.GetChannelIdsMethod;
import com.alticast.viettelottcommons.serviceMethod.acms.campaign.CampaignMethod;
import com.alticast.viettelottcommons.serviceMethod.acms.category.MenuMethod;
import com.alticast.viettelottcommons.serviceMethod.acms.platform.ConfigMethod;
import com.alticast.viettelottcommons.serviceMethod.acms.program.ProductMethod;
import com.alticast.viettelottcommons.util.ErrorUtil;
import com.alticast.viettelottcommons.util.Logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by mc.kim on 9/25/2016.
 */
public class InitializeLoader {
    CountDownLatch latch = new CountDownLatch(1);
    private final String REGION_OTT = "OTT";
    private final String FORMAT_SHORT = "short";
    private final String FORMAT_LONG = "long";
    private final int LIMIT = 2000;
    private final int OFFSET = 0;
    private InitCallback mInitCallback = null;

    private static InitializeLoader ourInstance = new InitializeLoader();

    public static InitializeLoader getInstance() {
        return ourInstance;
    }


    private InitializeLoader() {
    }


    public void initData(InitCallback initCallback) {
//        InitTask thread = new InitTask();
//        thread.start();
        this.mInitCallback = initCallback;
        new InitAsyncTask().execute();

    }

    private class InitAsyncTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            try {
//                initChannel();
                initMenu();
                initConfig();
//                if (WindmillConfiguration.isUseCastisPromotion) {
//                    if (WindmillConfiguration.type.equalsIgnoreCase("phone")) {
//                        initPromotionPhoneCastis();
//                    } else {
//                        initPromotionTabletCastis();
//                    }
//                    initPromotion();
//                    initFullPromotion();
//                } else {
//                    initPromotion();
//                    initFullPromotion();
//                }

            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            latch.countDown();
            Logger.print(this, "다끈남");
            if (mInitCallback != null) {
                mInitCallback.initializeFinished();
            }
        }
    }

    private class InitTask extends Thread {
        @Override
        public void run() {
            super.run();
            try {
                initChannel();
                initMenu();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }


    private void initChannel() throws IOException {
        ProductMethod productMethod = ServiceGenerator.getInstance().createSerive(ProductMethod.class);
        Call<ChannelRes> call = productMethod.getChannels(AuthManager.getAccessToken(),
                OFFSET, REGION_OTT, LIMIT, UserGradeDataProcessManager.getInstacne().getIncludeProduct(), FORMAT_SHORT);
        Response<ChannelRes> channelRes = call.execute();
        if (channelRes.isSuccess()) {
            ArrayList<ChannelProduct> list = channelRes.body().getData();
            Logger.print(this, "initChannel() : " + list.size());
//            ChannelManager.getInstance().initChannelMap(list);

            GetChannelIdsMethod getChannelIdsMethod = ServiceGenerator.getChannelIdsInstance().createGetChannelIds(GetChannelIdsMethod.class);
            Call<List<String>> callChannels = getChannelIdsMethod.getChannelIds();
            Response<List<String>> channelIdRes = callChannels.execute();

            ChannelManager.getInstance().initChannelMap(list, channelIdRes.body());
        }
    }

    private void initMenu() throws IOException {
        MenuMethod menuMethod = ServiceGenerator.getInstance().createSerive(MenuMethod.class);
        Call<MenuListRes> call = menuMethod.getMenus(AuthManager.getGuestToken(), "all", WindmillConfiguration.MENUS_VERSION);
        Response<MenuListRes> menuRes = call.execute();
        if (menuRes.isSuccess()) {
            Logger.print(this, "initMenu() : " + menuRes.body().getData().size());
            MenuManager.getInstance().saveMenuData(menuRes.body());
        }
    }

    private void initConfig() throws IOException {
        ConfigMethod platformMethod = ServiceGenerator.getInstance().createSerive(ConfigMethod.class);
        Call<ConfigListRes> call = platformMethod.getConfig(AuthManager.getGuestToken(), "user.terms");

        Response<ConfigListRes> menuRes = call.execute();
        if (menuRes.isSuccess()) {
            ConfigLoader.getInstance().initMap(menuRes.body());
        }
    }

    public void initPromotionPhoneCastis(WindmillCallback callback) throws IOException {
        CampaignMethod campaignMethod = ServiceGenerator.getInstance().createSerive(CampaignMethod.class);
        String url = WindmillConfiguration.baseADDS + "ADDSse/DisplayAdDecisionRequest/1.0";//?messageId=&userId=user342&advPlatformType=HANDHELD&sceneId=Mobile.HOME";
//        String url ="ADDSse/DisplayAdDecisionRequest/1.0";//?messageId=&userId=user342&advPlatformType=HANDHELD&sceneId=Mobile.HOME";
        String messageId, userId, advPlatformType, sceneId, regionId;
        messageId = WindmillConfiguration.deviceId + System.currentTimeMillis();
        userId = getUserId();
        regionId = getRegionId();
        Logger.print("TAG", "TAG userId phone " + userId);
        advPlatformType = "HANDHELD";
        sceneId = "Mobile.PHONE";

        Call<AdDecisionRes> call = campaignMethod.getPromotionBanner(url, messageId, userId, advPlatformType, sceneId, regionId);

        Response<AdDecisionRes> response = call.execute();
        if (response.isSuccess()) {
            AdDecisionRes adDecisionRes = response.body();

            ArrayList<Placement> listDecisions = adDecisionRes.getDisplayAdDecision().get(0).getDisplayAd();
            MyContentManager.getInstance().setListPromotionPlacement(listDecisions);
            callback.onSuccess(null);
        } else {
            WindmillConfiguration.isUseCastisPromotion = false;
            callback.onSuccess(null);
        }
    }

    private String getUserId() {
        String userId = "GUEST";
        userId = HandheldAuthorization.getInstance().getCurrentId();
        if (userId == null) {
            userId = "GUEST";
        }
        return userId;
    }

    private String getRegionId() {
        String regionId = "GUEST";
        if (AuthManager.currentLevel() == AuthManager.UserLevel.LEVEL3) {
            regionId = HandheldAuthorization.getInstance().getCurrentUser().getSo_id();
        }
        return regionId;
    }

    public void initPromotionTabletCastis(WindmillCallback callback) throws IOException {
        CampaignMethod campaignMethod = ServiceGenerator.getInstance().createSerive(CampaignMethod.class);
        String url = WindmillConfiguration.baseADDS + "ADDSse/DisplayAdDecisionRequest/1.0";//?messageId=&userId=user342&advPlatformType=HANDHELD&sceneId=Mobile.HOME";
//        String url = "ADDSse/DisplayAdDecisionRequest/1.0";//?messageId=&userId=user342&advPlatformType=HANDHELD&sceneId=Mobile.HOME";
        String messageId, userId, advPlatformType, sceneId, regionId;
        messageId = WindmillConfiguration.deviceId + System.currentTimeMillis();
        userId = getUserId();
        regionId = getRegionId();
        Logger.print("TAG", "TAG userId phone " + userId);
        advPlatformType = "HANDHELD";
        sceneId = "Mobile.TABLET";

        Call<AdDecisionRes> call = campaignMethod.getPromotionBanner(url, messageId, userId, advPlatformType, sceneId, regionId);

        Response<AdDecisionRes> response = call.execute();
        if (response.isSuccess()) {
            AdDecisionRes adDecisionRes = response.body();

            ArrayList<Placement> listDecisions = adDecisionRes.getDisplayAdDecision().get(0).getDisplayAd();
            MyContentManager.getInstance().setListPromotionPlacement(listDecisions);
            callback.onSuccess(null);
        } else {
            WindmillConfiguration.isUseCastisPromotion = false;
            callback.onSuccess(null);
        }
    }

    public void initPushwoodTag(RequestTagDevice requestTagDevice) throws IOException {
        CampaignMethod campaignMethod = ServiceGenerator.getInstance().createSerive(CampaignMethod.class);
        String url = "https://cp.pushwoosh.com/json/1.3/setTags";
        Call<ResultRes> call = campaignMethod.registerPushWoodTag(url, requestTagDevice);

        call.enqueue(new Callback<ResultRes>() {
            @Override
            public void onResponse(Call<ResultRes> call, Response<ResultRes> response) {
            }

            @Override
            public void onFailure(Call<ResultRes> call, Throwable t) {
            }
        });
    }

    public void initPromotion() throws IOException {
        ArrayList<String> pids = new ArrayList<String>();
        pids.add(CampaignLoader.getInstance().getMainPromotionCampaign());
        CampaignMethod campaignMethod = ServiceGenerator.getInstance().createSerive(CampaignMethod.class);
        Call<CampaignGroupRes> call = campaignMethod.getCampaigns(AuthManager.getGuestToken(), pids);
        Response<CampaignGroupRes> campains = call.execute();

        CampaignGroupRes campaignGroupRes = campains.body();

        if (campaignGroupRes == null) return;

        CampaignGroupData campaignGroupData = campaignGroupRes.getData(CampaignLoader.getInstance().getMainPromotionCampaign());
        if (campaignGroupData == null) return;
        ArrayList<CampaignData> listData = campaignGroupData.getData();

        if (campains.isSuccess()) {

            if (true) {

                for (CampaignData data : listData) {
                    if (data.getPid().equals(CampaignLoader.getInstance().getPromotionCampaign())) {
                        MyContentManager.getInstance().setListPromotionCampaign(data.getCampaigns());
                    }
                }
                return;
            }

            CampaignData campaignData = listData.get(0);

            if (campaignData.getAction() == null) {
                for (CampaignData data : listData) {
                    if (data.getPid().contains("OTT.PROMO.MAIN")) {
                        MyContentManager.getInstance().setListPromotionCampaign(data.getCampaigns());
                    }
                }
            } else {
                for (CampaignData cp : listData) {
                    if (cp.getAction() != null) {
                        campaignData = cp;
                        break;
                    }
                }

                if (campaignData == null) {
                    return;
                }

                String categoryId = campaignData.getAction_url();
                String action = campaignData.getAction();
                if (action != null && action.equalsIgnoreCase("category")) {

                    ProductMethod productMethod = ServiceGenerator.getInstance().createSerive(ProductMethod.class);
                    Logger.print(this, "call getCategoryVod");
                    String url = "/api1/contents/categories/" + categoryId + "/programs";
                    String until = "now";
                    Call<ProgramList> call1 = productMethod.getCategoryPrograms(url,
                            AuthManager.getAccessToken(), LIMIT, 0,
                            UserGradeDataProcessManager.getInstacne().getIncludeProduct(), true, FORMAT_SHORT);
                    Response<ProgramList> listVod = call1.execute();
                    if (listVod.isSuccess()) {
                        ProgramList programList = listVod.body();

                        ArrayList<Vod> list = new ArrayList<>();

                        for (Vod vod : programList.getData()) {
                            Path path = new Path(PurchasePathway.ENTRY_PROMOTION, EntryPathLogImpl.getInstance().getPurchaseMenuString(), vod.getProgram().getId());
                            vod.setPath(path);
                        }

                        list.add(programList.getData().get(0));

                        MyContentManager.getInstance().setListPromotionVod(list);
                    }
                }
            }
        }
    }

    // promotion TOP_MID_BOT
    public void initFullPromotion() throws IOException {
        ArrayList<String> pids = CampaignLoader.getInstance().getFullPromotion();
        CampaignMethod campaignMethod = ServiceGenerator.getInstance().createSerive(CampaignMethod.class);
        Call<CampaignGroupRes> call = campaignMethod.getCampaigns(AuthManager.getGuestToken(), pids);

        Response<CampaignGroupRes> campains = call.execute();

        CampaignGroupRes campaignGroupRes = campains.body();
        if (campaignGroupRes == null) return;
        ArrayList<CampaignGroupData> listCampaignGroupData = campaignGroupRes.getData();
        if (listCampaignGroupData == null) return;
        for (CampaignGroupData campaignGroupData : listCampaignGroupData) {
            ArrayList<CampaignData> listData = campaignGroupData.getData();
            if (listData == null) return;
            for (CampaignData campaignData : listData) {
                if (campaignData.getPid().contains(CampaignLoader.PROMO_TOP_STR)) {
                    MyContentManager.getInstance().setListCampainsTOP(campaignData.getCampaigns());
                } else if (campaignData.getPid().contains(CampaignLoader.PROMO_MID_STR)) {
                    MyContentManager.getInstance().setListCampainsMID(campaignData.getCampaigns());
                } else if (campaignData.getPid().contains(CampaignLoader.PROMO_BOT_STR)) {
                    MyContentManager.getInstance().setListCampainsBOT(campaignData.getCampaigns());
                }
            }
        }
    }


    private void initResumeData() throws IOException {

    }

    public interface InitCallback {
        void initializeFinished();
    }


}
