package com.alticast.viettelottcommons.loader;

import com.alticast.viettelottcommons.WindmillConfiguration;
import com.alticast.viettelottcommons.api.WindmillCallback;
import com.alticast.viettelottcommons.def.PurchasePathway;
import com.alticast.viettelottcommons.manager.AuthManager;
import com.alticast.viettelottcommons.def.entry.EntryPathLogImpl;
import com.alticast.viettelottcommons.resource.ApiError;
import com.alticast.viettelottcommons.resource.CampaignGroupData;
import com.alticast.viettelottcommons.resource.Campaigns;
import com.alticast.viettelottcommons.resource.ChannelProduct;
import com.alticast.viettelottcommons.resource.response.CampaignGroupRes;
import com.alticast.viettelottcommons.resource.response.CampaignsRes;
import com.alticast.viettelottcommons.service.ServiceGenerator;
import com.alticast.viettelottcommons.serviceMethod.acms.campaign.CampaignMethod;
import com.alticast.viettelottcommons.util.ErrorUtil;

import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by mc.kim on 8/11/2016.
 */
public class CampaignLoader {


    public static final String LEFT_CAMPAIGN = "package.campaign://OTT.TABLET.PROMO.LEFT";
    public static final String MAIN_PROMO_PHONE = "package.campaign://OTT.MAIN.PROMO";
    public static final String MAIN_PROMO_TABLET = "package.campaign://OTT.MAIN.PROMO.TABLET";

    public static final String PROMO_TOP = "package.campaign://OTT.MENU.PROMO.TOP";
    public static final String PROMO_MID = "package.campaign://OTT.MENU.PROMO.MID";
    public static final String PROMO_BOT = "package.campaign://OTT.MENU.PROMO.BOT";

    public static final String PROMO_TOP_TABLET = "package.campaign://OTT.MENU.PROMO.TOP.TABLET";
    public static final String PROMO_MID_TABLET = "package.campaign://OTT.MENU.PROMO.MID.TABLET";
    public static final String PROMO_BOT_TABLET = "package.campaign://OTT.MENU.PROMO.BOT.TABLET";

    public static final String PROMO_TOP_STR = "OTT.MENU.PROMO.TOP";
    public static final String PROMO_MID_STR = "OTT.MENU.PROMO.MID";
    public static final String PROMO_BOT_STR = "OTT.MENU.PROMO.BOT";

    public static final String PHONE_PROMO = "campaign://OTT.PROMO.MAIN";
    public static final String TABLET_PROMO = "campaign://OTT.PROMO.MAIN.TABLET";
    //    public static final String NEW_CAMPAIGN = "package.campaign://OTT.PROMO.MAIN";
    public static final String RIGHT_CAMPAIGN = "package.campaign://OTT.TABLET.PROMO.RIGHT";

    public static final String HOTMOVIE_CAMPAIGN = "package.campaign://OTT.MAIN.HOTMOVIE";
    public static final String CLIP_CAMPAIGN = "package.campaign://OTT.MAIN.CLIP";

    private final String ACTION_PROGRAM = "program";
    private final String ACTION_CATEGORY = "category";

    private HashMap<String, ChannelProduct> channelMap = new HashMap<>();

    private CampaignGroupRes campaignGroupResOrigin, campaignGroupResDisplay;

    private String promotionCampaign;

    private static CampaignLoader ourInstance = new CampaignLoader();

    public static CampaignLoader getInstance() {
        return ourInstance;
    }

    public void clearData() {
        campaignGroupResOrigin = null;
        campaignGroupResDisplay = null;
    }

    private CampaignLoader() {
    }

    public String getPromotionCampaign() {
        return WindmillConfiguration.type.equals("phone") ? PHONE_PROMO : TABLET_PROMO;
    }

    public String getMainPromotionCampaign() {
        return WindmillConfiguration.type.equals("phone") ? MAIN_PROMO_PHONE : MAIN_PROMO_TABLET;
    }

    // get TOP_BOT_MID promotion
    public ArrayList<String> getFullPromotion() {
        ArrayList<String> list = new ArrayList<>();
        if (WindmillConfiguration.type.equals("phone")) {
            list.add(PROMO_TOP);
            list.add(PROMO_MID);
            list.add(PROMO_BOT);
        } else {
            list.add(PROMO_TOP_TABLET);
            list.add(PROMO_MID_TABLET);
            list.add(PROMO_BOT_TABLET);
        }
        return list;
    }


    public void getCampaignGroup(ArrayList<String> pids, final WindmillCallback callback) {
        CampaignMethod campaignMethod = ServiceGenerator.getInstance().createSerive(CampaignMethod.class);
        Call<CampaignGroupRes> call = campaignMethod.getCampaigns(AuthManager.getGuestToken(), pids);

        call.enqueue(new Callback<CampaignGroupRes>() {
            @Override
            public void onResponse(Call<CampaignGroupRes> call, Response<CampaignGroupRes> response) {
                if (callback == null) {
                    return;
                }
                if (response.isSuccess()) {
                    CampaignGroupRes campaignGroupRes = response.body();

//                    CampaignGroupData campaignGroupData = new CampaignGroupData();

                    CampaignLoader.getInstance().setCampaignGroupRes(campaignGroupRes);
                    callback.onSuccess(response.body());
                } else {
                    ApiError error = ErrorUtil.parseError(response);
                    callback.onError(error);
                }
            }

            @Override
            public void onFailure(Call<CampaignGroupRes> call, Throwable t) {
                if (callback == null) {
                    return;
                }
                callback.onFailure(call, t);
            }
        });
    }


    /***
     * @param id       campaign ID
     * @param pid      campaign PID for update EntryPath
     * @param callback callback
     */
    public void getCampaignProgram(String id, final String pid, final WindmillCallback callback) {
        CampaignMethod campaignMethod = ServiceGenerator.getInstance().createSerive(CampaignMethod.class);
        String url = "/api1/contents/campaigns/" + id;
        Call<CampaignsRes> call = campaignMethod.getCampaign(url, AuthManager.getAccessToken());
        call.enqueue(new Callback<CampaignsRes>() {
            @Override
            public void onResponse(Call<CampaignsRes> call, Response<CampaignsRes> response) {
                if (callback == null) {
                    return;
                }
                if (response.isSuccess()) {
                    CampaignsRes res = response.body();
                    ArrayList<Campaigns> campaignses = res.getData();
                    int size = campaignses.size();
                    if (size < 1) {
                        ApiError error = new ApiError();
                        error.noData();
                        callback.onError(error);
                    } else {
                        Campaigns campaigns = res.getData(ACTION_PROGRAM);
                        if (campaigns != null) {
                            ProgramLoader.getInstance().getProgram(campaigns.getAction_url(), new WindmillCallback() {
                                @Override
                                public void onSuccess(Object obj) {
                                    EntryPathLogImpl.getInstance().updateLog(PurchasePathway.ENTRY_PROMOTION, pid);
                                    callback.onSuccess(obj);
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
                            ApiError error = new ApiError();
                            error.noCampaignProgram();
                            callback.onError(error);
                        }
                    }
                } else {
                    ApiError error = ErrorUtil.parseError(response);
                    callback.onError(error);
                }
            }

            @Override
            public void onFailure(Call<CampaignsRes> call, Throwable t) {
                if (callback == null) {
                    return;
                }
                callback.onFailure(call, t);
            }
        });
    }

    public CampaignGroupRes getCampaignGroupResOrigin() {
        return campaignGroupResOrigin;
    }

    public CampaignGroupRes getCampaignGroupResDisplay() {
        return campaignGroupResDisplay;
    }

    public void setCampaignGroupRes(CampaignGroupRes campaignGroupRes) {
        this.campaignGroupResOrigin = campaignGroupRes;
        this.campaignGroupResDisplay = new CampaignGroupRes(campaignGroupRes);
    }

    public void refreshCampainGroupDisplay() {
        if (campaignGroupResOrigin != null) {
            campaignGroupResDisplay = new CampaignGroupRes(campaignGroupResOrigin);
        }
    }
}
