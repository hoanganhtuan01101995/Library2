package com.alticast.viettelottcommons.manager;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.alticast.viettelottcommons.WindmillConfiguration;
import com.alticast.viettelottcommons.api.WindmillCallback;
import com.alticast.viettelottcommons.loader.UserDataLoader;
import com.alticast.viettelottcommons.resource.ApiError;
import com.alticast.viettelottcommons.resource.CampaignData;
import com.alticast.viettelottcommons.resource.Campaigns;
import com.alticast.viettelottcommons.resource.Channel;
import com.alticast.viettelottcommons.resource.Program;
import com.alticast.viettelottcommons.resource.Reservation;
import com.alticast.viettelottcommons.resource.Schedule;
import com.alticast.viettelottcommons.resource.Vod;
import com.alticast.viettelottcommons.resource.ads.Placement;
import com.alticast.viettelottcommons.resource.response.ProgramList;
import com.alticast.viettelottcommons.util.Logger;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;

/**
 * Created by duyuno on 10/16/16.
 */
public class MyContentManager {

    public static final String GROUP_KIDS = "KIDS";
    public static final String GROUP_MUSIC = "MUSIC";
    public static final String GROUP_CLIP = "CLIP";
    public static final String GROUP_OTHERS = "OTHERS";

    public static final String STR_A2Z = "char_asc";
    public static final String STR_Z2A = "char_desc";
    public static final String STR_NEWSEST = "newest";
    public static final String STR_OLDEST = "oldest";

    public static final int TOP_WIDTH = 720;
    public static final int TOP_HEIGHT = 435;
    public static final int MID_WIDTH = 667;
    public static final int MID_HEIGHT = 158;

    public static final int TOP_TABLET_WIDTH = 1024;
    public static final int TOP_TABLET_HEIGHT = 392;
    public static final int MID_TABLET_WIDTH = 1024;
    public static final int MID_TABLET_HEIGHT = 130;

    public static final int START_PROMOTION_NUMBER = 3;

    private int startPromotionBanner;

    public static final int limitPromotion = 5;

    public static String myContentType = "myContentType";
    private static MyContentManager ourInstance = new MyContentManager();

    public static MyContentManager getInstance() {
        return ourInstance;
    }

    private ArrayList<Vod> listPromotionVod = new ArrayList<>();
    private ArrayList<CampaignData> listPromotionCampaign = new ArrayList<>();
    private ArrayList<Vod> listResumview = new ArrayList<>();
    private ArrayList<Vod> listRecentlyWatched = new ArrayList<>();

    private ArrayList<Vod> listWatching = new ArrayList<>();

    private ArrayList<Campaigns> listCampains = new ArrayList<>();

    public ArrayList<Placement> getListPromotionPlacement() {
        return listPromotionPlacement;
    }

    public Placement getListIframePlacement() {
        return listIframePlacement;
    }

    public void setListIframePlacement(Placement listIframePlacement) {
        this.listIframePlacement = listIframePlacement;
    }

    public void setListPromotionPlacement(ArrayList<Placement> listPromotionPlacement) {
        this.listPromotionPlacement = listPromotionPlacement;
        if (listPromotionPlacement == null || listPromotionPlacement.size() == 0) {
            WindmillConfiguration.isUseCastisPromotion = false;
            return;
        }

        listHomeTopPlacement = initListCastisHomeTOP();
        listHomeMidPlacement = initListCastisHomeMID();
        listHomeBotPlacement = initListCastisHomeBOT();

        listSubTopPlacement = initListCastisSubTOP();
        listSubMidPlacement = initListCastisSubMID();
        listSubBotPlacement = initListCastisSubBOT();

        listIframePlacement = initListCastisIFrame();

        if (isHasCastisPromotionTOP() || isHasCastisPromotionMID() || isHasCastisPromotionBOT()
                || isHasCastisSubPromotionTOP() || isHasCastisSubPromotionMID() || isHasCastisSubPromotionBOT()) {

        } else {
            WindmillConfiguration.isUseCastisPromotion = false;
        }
//        initHashHomePromotion();
//        initHashSubPromotion();
    }

    public ArrayList<Placement> getlistHomePromotion() {
        if (listPromotionPlacement == null || listPromotionPlacement.size() == 0) return null;
        ArrayList<Placement> list = new ArrayList<>();
        for (Placement placement : listPromotionPlacement) {
            if (placement.getAdSpaceId().contains(Placement.Mobile_HOME)) {
                list.add(placement);
            }
        }
        return list;
    }

    public int getSizeHomePromotion() {
        if (listPromotionPlacement == null || listPromotionPlacement.size() == 0) return 0;
        ArrayList<Placement> list = new ArrayList<>();
        for (Placement placement : listPromotionPlacement) {
            if (placement.getAdSpaceId().equalsIgnoreCase(Placement.Mobile_HOME)) {
                list.add(placement);
            }
        }
        return list.size();
    }

    public ArrayList<Placement> getlistTopPromotion() {
        if (listPromotionPlacement == null || listPromotionPlacement.size() == 0) return null;
        ArrayList<Placement> list = new ArrayList<>();
        for (Placement placement : listPromotionPlacement) {
            if (placement.getAdSpaceId().equalsIgnoreCase(Placement.Mobile_HOME_TOP)) {
                list.add(placement);
            }
        }
        return list;
    }

    public ArrayList<Placement> getlistMidPromotion() {
        if (listPromotionPlacement == null || listPromotionPlacement.size() == 0) return null;
        ArrayList<Placement> list = new ArrayList<>();
        for (Placement placement : listPromotionPlacement) {
            if (placement.getAdSpaceId().equalsIgnoreCase(Placement.Mobile_HOME_MID)) {
                list.add(placement);
            }
        }
        return list;
    }

    public ArrayList<Placement> getlistBottomPromotion() {
        if (listPromotionPlacement == null || listPromotionPlacement.size() == 0) return null;
        ArrayList<Placement> list = new ArrayList<>();
        for (Placement placement : listPromotionPlacement) {
            if (placement.getAdSpaceId().equalsIgnoreCase(Placement.Mobile_HOME_BOTTOM)) {
                list.add(placement);
            }
        }
        return list;
    }

    private ArrayList<Placement> listPromotionPlacement = new ArrayList<>();

    private ArrayList<Campaigns> listCampainsTOP = new ArrayList<>();
    private ArrayList<Campaigns> listCampainsMID = new ArrayList<>();
    private ArrayList<Campaigns> listCampainsBOT = new ArrayList<>();

    private ArrayList<Vod> listGeneral = new ArrayList<>();
    private ArrayList<Vod> listKid = new ArrayList<>();
    private ArrayList<Vod> listMusic = new ArrayList<>();
    private ArrayList<Vod> listClip = new ArrayList<>();

    private Reservation[] listReservation;

    private ArrayList<Schedule> listFavouriteChannel = new ArrayList<>();
    private Vod currentPlayVod;


    public void setListPromotionVod(ArrayList<Vod> listPromotionVod) {
        this.listPromotionVod = listPromotionVod;
    }

    public void setListPromotionCampaignData(ArrayList<CampaignData> listPromotionCampaign) {
        this.listPromotionCampaign = listPromotionCampaign;
    }

    public void setListPromotionCampaign(ArrayList<Campaigns> listCampaigns) {
        this.listCampains = listCampaigns;
    }

    public ArrayList<Campaigns> getListCampainsTOP() {
        return listCampainsTOP;
    }

    public boolean isHasCampaignTop() {
        if (listCampainsTOP == null || listCampainsTOP.size() == 0) return false;
        else return true;
    }

    public int getNumberOfTOP() {
        return listCampainsTOP == null ? 0 : listCampainsTOP.size();
    }

    public void setListCampainsTOP(ArrayList<Campaigns> listCampainsTOP) {
        this.listCampainsTOP = listCampainsTOP;
    }

    public ArrayList<Campaigns> getListCampainsMID() {
        return listCampainsMID;
    }

    public void setListCampainsMID(ArrayList<Campaigns> listCampainsMID) {
        this.listCampainsMID = listCampainsMID;
    }

    public ArrayList<Campaigns> getListCampainsBOT() {
        return listCampainsBOT;
    }

    public void setListCampainsBOT(ArrayList<Campaigns> listCampainsBOT) {
        this.listCampainsBOT = listCampainsBOT;
    }

    public ArrayList<Vod> getListPromotionVod() {
        return listPromotionVod;
    }

    public int getTotalPromotionVod() {
        if (listPromotionVod != null)
            return listPromotionVod.size();
        else return 0;
    }

    public ArrayList<CampaignData> getListPromotionCampaign() {
        return listPromotionCampaign;
    }

    public ArrayList<Campaigns> getListCampaigns() {
        return listCampains;
    }

    public int getTotalCampaigns() {
        if (listCampains != null)
            return listCampains.size();
        else return 0;
    }

    public int getTotalCampaignData() {
        if (listPromotionCampaign != null)
            return listPromotionCampaign.size();
        else return 0;
    }

    public ArrayList<Vod> getListResumview() {
        return listResumview;
    }

    public boolean isHasPromotion() {
//        if(WindmillConfiguration.LIVE) {
//            return listPromotionCampaign != null && !listPromotionCampaign.isEmpty();
//        } else {
//            return listPromotionVod != null && !listPromotionVod.isEmpty();
//        }
        return (listPromotionCampaign != null && !listPromotionCampaign.isEmpty())
                || (listPromotionVod != null && !listPromotionVod.isEmpty() || listCampains != null && !listCampains.isEmpty()

                || WindmillConfiguration.isUseCastisPromotion && listPromotionPlacement != null && listPromotionPlacement.size() > 0);
    }

    public boolean isHasPomotionVod() {
        return listPromotionVod != null && !listPromotionVod.isEmpty();
    }

    public boolean isHasPomotionCampaign() {
        return listPromotionCampaign != null && !listPromotionCampaign.isEmpty();
    }

    public ArrayList<Vod> getListRecentlyWatched() {
        return listRecentlyWatched;
    }

    public ArrayList<String> getListProgramIds() {
        ArrayList<String> list = new ArrayList<>();
        if (listRecentlyWatched != null && listRecentlyWatched.size() > 0) {
            for (Vod vod : listRecentlyWatched)
                list.add(vod.getProgram().getId());

        }
        return list;
    }

    public void setListRecentlyWatched(ArrayList<Vod> listRecentlyWatched) {
        this.listRecentlyWatched = listRecentlyWatched;
    }

    public void clearData() {
//        if(listPromotionVod != null) {
//            listPromotionVod.clear();
//        }

        if (listResumview != null) {
            listResumview.clear();
        }
        if (listRecentlyWatched != null) {
            listRecentlyWatched.clear();
        }

//        if(listPromotionCampaign != null) {
//            listPromotionCampaign.clear();
//        }
    }

    public boolean isLoadedMyContent() {
        if (ChannelManager.getInstance().countContent(UserDataLoader.TYPE_VOD) == 0) {
            return false;
        } else return true;
    }

    public void getMyContents(String group, String sort, final WindmillCallback windmillCallback) {
//        if (listMycontents != null) {
//            listMycontents.clear();
//        } else {
//            listMycontents = new ArrayList<>();
//        }

        UserDataLoader.getInstance().getMyContents(0, group, sort, new WindmillCallback() {
            @Override
            public void onSuccess(Object obj) {
                ProgramList list = (ProgramList) obj;
                if (list == null) return;
                for (Vod vod : list.getData()) {
//                    listMycontents.add(vod.getProgram())
                    ChannelManager.getInstance().add(vod.getProgram().getId(), UserDataLoader.TYPE_VOD);
                }
                windmillCallback.onSuccess(obj);
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                windmillCallback.onFailure(call, t);
            }

            @Override
            public void onError(ApiError error) {
                windmillCallback.onError(error);
            }
        });
    }

    public void addMyContent(final Program program, final WindmillCallback windmillCallback) {

        int count = ChannelManager.getInstance().countContent(UserDataLoader.TYPE_VOD);

//        if (count >= 40) {
//            String message = WindmillConfiguration.LANGUAGE.equals("eng") ? "You can only add limit 40 Vods"
//                    : "Bạn chỉ có thể thêm tối đa 40 Vod trong 1 playlist";
//            ApiError apiError = new ApiError(null, message);
//            windmillCallback.onError(apiError);
//            return;
//        }
        String group = MyContentManager.getInstance().myContentType;
        UserDataLoader.getInstance().createMyContent(program, group, new WindmillCallback() {
            @Override
            public void onSuccess(Object obj) {
                ChannelManager.getInstance().add(program.getId(), UserDataLoader.TYPE_VOD);
                if (windmillCallback != null)
                    windmillCallback.onSuccess(obj);
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                if (windmillCallback != null)
                    windmillCallback.onFailure(call, t);
            }

            @Override
            public void onError(ApiError error) {
                if (error.getStatusCode() == 409) {
                    String message = WindmillConfiguration.LANGUAGE.equals("eng") ? "You can only add limit 40 Vods"
                            : "Bạn chỉ có thể thêm tối đa 40 Vod trong 1 playlist";
                    ApiError apiError = new ApiError(null, message);
                    windmillCallback.onError(apiError);
                } else {
                    if (windmillCallback != null)
                        windmillCallback.onError(error);
                }
            }
        });
    }

    public void deleteMycontents(final ArrayList<String> ids, final WindmillCallback windmillCallback) {
        UserDataLoader.getInstance().deleteMyContents(ids, new WindmillCallback() {
            @Override
            public void onSuccess(Object obj) {

                ChannelManager.getInstance().delete(ids);
                windmillCallback.onSuccess(obj);
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                windmillCallback.onFailure(call, t);
            }

            @Override
            public void onError(ApiError error) {
                windmillCallback.onError(error);
            }
        });
    }

    public boolean isMyContent(Program program) {

        return ChannelManager.getInstance().contains(program.getId());
    }

    public void addMyChannel(final Channel channel, final WindmillCallback windmillCallback) {

        int count = ChannelManager.getInstance().countContent(UserDataLoader.TYPE_CHANNEL);
        if (count >= 60) {
            String message = WindmillConfiguration.LANGUAGE.equals("eng") ? "You can only add limit 60 Channels"
                    : "Bạn chỉ có thể thêm tối đa 60 kênh";
            ApiError apiError = new ApiError(null, message);
            windmillCallback.onError(apiError);
            return;
        }

        UserDataLoader.getInstance().createMyChannel(channel, new WindmillCallback() {
            @Override
            public void onSuccess(Object obj) {

                ChannelManager.getInstance().add(channel.getId());

                windmillCallback.onSuccess(obj);
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                windmillCallback.onFailure(call, t);
            }

            @Override
            public void onError(ApiError error) {
                windmillCallback.onError(error);
            }
        });
    }

    public void deleteMyChannels(final ArrayList<String> ids, final WindmillCallback windmillCallback) {
        UserDataLoader.getInstance().deleteMyChannels(ids, new WindmillCallback() {
            @Override
            public void onSuccess(Object obj) {
                ChannelManager.getInstance().delete(ids);
                windmillCallback.onSuccess(obj);
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                windmillCallback.onFailure(call, t);
            }

            @Override
            public void onError(ApiError error) {
                windmillCallback.onError(error);
            }
        });
    }

    public void getMyResuming(final WindmillCallback windmillCallback) {
//        if(listResumview != null) {
//            listResumview.clear();
//        }
        if (HandheldAuthorization.getInstance().isLogIn()) {
            UserDataLoader.getInstance().getResumeListHome(0, 100, new WindmillCallback() {
                @Override
                public void onSuccess(Object obj) {
                    if (listResumview == null) {
                        listResumview = new ArrayList<Vod>();
                    }
                    ProgramList programList = (ProgramList) obj;
                    if (programList != null && programList.getData() != null && !programList.getData().isEmpty()) {
                        for (Vod vod : programList.getData()) {
                            vod.setResumeVod(true);
                        }
                        listResumview.clear();
                        listResumview.addAll(programList.getData());
                    }
                    windmillCallback.onSuccess(null);
                }

                @Override
                public void onFailure(Call call, Throwable t) {
                    windmillCallback.onSuccess(null);
                }

                @Override
                public void onError(ApiError error) {
                    windmillCallback.onSuccess(null);
                }
            });
        } else {
//            currentItem.setVisibility(View.GONE);
            windmillCallback.onSuccess(null);
        }
    }

    public void getMyRecentlyWatched(final WindmillCallback windmillCallback) {
        if (HandheldAuthorization.getInstance().isLogIn()) {
            if (AuthManager.currentLevel() == AuthManager.UserLevel.LEVEL2) {
                UserDataLoader.getInstance().getRecentlyWatched(0, 100, new WindmillCallback() {
                    @Override
                    public void onSuccess(Object obj) {
                        if (listRecentlyWatched == null) {
                            listRecentlyWatched = new ArrayList<>();
                        }
                        ProgramList programList = (ProgramList) obj;
                        if (programList != null && programList.getData() != null && !programList.getData().isEmpty()) {
                            for (Vod vod : programList.getData()) {
                                vod.setResumeVod(true);
                            }
                            listRecentlyWatched.clear();
                            listRecentlyWatched.addAll(programList.getData());
//                            MyContentManager.getInstance().setListRecentlyWatched(listRecentlyWatched);
                        }
                        windmillCallback.onSuccess(listRecentlyWatched);
                    }

                    @Override
                    public void onFailure(Call call, Throwable t) {
                        windmillCallback.onSuccess(null);
                    }

                    @Override
                    public void onError(ApiError error) {
                        windmillCallback.onSuccess(null);
                    }
                });
            } else if (AuthManager.currentLevel() == AuthManager.UserLevel.LEVEL3) {
                UserDataLoader.getInstance().getRecentlyWatchedPair(0, 100, new WindmillCallback() {
                    @Override
                    public void onSuccess(Object obj) {
                        if (listRecentlyWatched == null) {
                            listRecentlyWatched = new ArrayList<>();
                        }
                        ProgramList programList = (ProgramList) obj;
                        if (programList != null && programList.getData() != null && !programList.getData().isEmpty()) {
                            for (Vod vod : programList.getData()) {
                                vod.setResumeVod(true);
                            }
                            listRecentlyWatched.clear();
                            listRecentlyWatched.addAll(programList.getData());
//                            MyContentManager.getInstance().setListRecentlyWatched(listRecentlyWatched);
                        }
                        windmillCallback.onSuccess(null);
                    }

                    @Override
                    public void onFailure(Call call, Throwable t) {
                        windmillCallback.onSuccess(null);
                    }

                    @Override
                    public void onError(ApiError error) {
                        windmillCallback.onSuccess(null);
                    }
                });
            } else {
                windmillCallback.onSuccess(null);
            }
        }
    }

    public void clearResumeView() {
        if (listResumview != null) listResumview.clear();
    }

    public boolean isHasResumingContent() {
        return listResumview != null && !listResumview.isEmpty();
    }

    public boolean isHasRecentlyWatchedContent() {
        return listRecentlyWatched != null && !listRecentlyWatched.isEmpty();
    }

    public boolean isMyChannel(Channel channel) {
        return ChannelManager.getInstance().contains(channel.getId());
    }

    public ArrayList<Vod> getListWatching() {
        return listWatching;
    }

    public void setListWatching(ArrayList<Vod> listWatching) {
        this.listWatching = listWatching;
    }

    public Vod getCurrentPlayVod() {
        return currentPlayVod;
    }

    public void setCurrentPlayVod(Vod currentPlayVod) {
        this.currentPlayVod = currentPlayVod;
    }

    public void loadPlayListNumber() {
        UserDataLoader.getInstance().getMyContents(0, MyContentManager.GROUP_OTHERS, null, new WindmillCallback() {
            @Override
            public void onSuccess(Object obj) {
                if (obj != null)
                    listGeneral = ((ProgramList) obj).getData();
                else listGeneral.clear();
            }

            @Override
            public void onFailure(Call call, Throwable t) {
            }

            @Override
            public void onError(ApiError error) {
            }
        });
        UserDataLoader.getInstance().getMyContents(0, MyContentManager.GROUP_KIDS, null, new WindmillCallback() {
            @Override
            public void onSuccess(Object obj) {
                if (obj != null)
                    listKid = ((ProgramList) obj).getData();
                else listKid.clear();
            }

            @Override
            public void onFailure(Call call, Throwable t) {
            }

            @Override
            public void onError(ApiError error) {
            }
        });
        UserDataLoader.getInstance().getMyContents(0, MyContentManager.GROUP_MUSIC, null, new WindmillCallback() {
            @Override
            public void onSuccess(Object obj) {
                if (obj != null)
                    listMusic = ((ProgramList) obj).getData();
                else listMusic.clear();
            }

            @Override
            public void onFailure(Call call, Throwable t) {
            }

            @Override
            public void onError(ApiError error) {
            }
        });
        UserDataLoader.getInstance().getMyContents(0, MyContentManager.GROUP_CLIP, null, new WindmillCallback() {
            @Override
            public void onSuccess(Object obj) {
                if (obj != null)
                    listClip = ((ProgramList) obj).getData();
                else listClip.clear();
            }

            @Override
            public void onFailure(Call call, Throwable t) {
            }

            @Override
            public void onError(ApiError error) {
            }
        });
    }

    public ArrayList<Vod> getListGeneral() {
        return listGeneral;
    }

    public void setListGeneral(ArrayList<Vod> listGeneral) {
        this.listGeneral = listGeneral;
    }

    public ArrayList<Vod> getListKid() {
        return listKid;
    }

    public void setListKid(ArrayList<Vod> listKid) {
        this.listKid = listKid;
    }

    public ArrayList<Vod> getListMusic() {
        return listMusic;
    }

    public void setListMusic(ArrayList<Vod> listMusic) {
        this.listMusic = listMusic;
    }

    public ArrayList<Vod> getListClip() {
        return listClip;
    }

    public void setListClip(ArrayList<Vod> listClip) {
        this.listClip = listClip;
    }

    public Reservation[] getListReservation() {
        return listReservation;
    }

    public void setListReservation(Reservation[] listReservation) {
        this.listReservation = listReservation;
    }

    public boolean isExistReservationLayout() {
        if (listReservation == null) return false;
        if (listReservation.length > 0) return true;
        else return false;
    }

    public boolean isExistRecentWatchedLayout() {
        if (listRecentlyWatched.size() > 0) return true;
        else return false;
    }

    public ArrayList<Schedule> getListFavouriteChannel() {
        return listFavouriteChannel;
    }

    public void setListFavouriteChannel(ArrayList<Schedule> listFavouriteChannel) {
        this.listFavouriteChannel = listFavouriteChannel;
    }

    public boolean isExistFavouriteChannelLayout() {
        if (listFavouriteChannel.size() > 0) return true;
        else return false;
    }

    public boolean isAddedToPlaylist(Vod vod) {
        for (Vod mVod : listGeneral) {
            if (vod.getProgram().getId().equalsIgnoreCase(mVod.getProgram().getId())) {
                return true;
            }
        }
        for (Vod mVod : listKid) {
            if (vod.getProgram().getId().equalsIgnoreCase(mVod.getProgram().getId())) {
                return true;
            }
        }
        for (Vod mVod : listMusic) {
            if (vod.getProgram().getId().equalsIgnoreCase(mVod.getProgram().getId())) {
                return true;
            }
        }
        for (Vod mVod : listClip) {
            if (vod.getProgram().getId().equalsIgnoreCase(mVod.getProgram().getId())) {
                return true;
            }
        }
        return false;
    }

    // Castis Promotion

    private ArrayList<Placement> listHomeTopPlacement = new ArrayList<>();
    private ArrayList<Placement> listHomeMidPlacement = new ArrayList<>();
    private ArrayList<Placement> listHomeBotPlacement = new ArrayList<>();

    private ArrayList<Placement> listSubTopPlacement = new ArrayList<>();
    private ArrayList<Placement> listSubMidPlacement = new ArrayList<>();

    private ArrayList<Placement> listSubBotPlacement = new ArrayList<>();

    private Placement listIframePlacement = null;
    private Bitmap iFrameBitmap = null;
    private String iFrameUrl;

    public Bitmap getiFrameBitmap() {
        return iFrameBitmap;
    }

    public String getiFrameUrl() {
        return iFrameUrl;
    }

    public ArrayList<Placement> initListCastisHOME() {
        if (listPromotionPlacement == null || listPromotionPlacement.size() == 0) return null;
        ArrayList<Placement> list = new ArrayList<>();
        for (Placement placement : listPromotionPlacement) {
            if (placement.getAdSpaceId().contains(Placement.Mobile_HOME)) {
                list.add(placement);
            }
        }
        return list;
    }

    public ArrayList<Placement> initListCastisHomeTOP() {
        if (listPromotionPlacement == null || listPromotionPlacement.size() == 0) return null;
        ArrayList<Placement> list = new ArrayList<>();
        for (Placement placement : listPromotionPlacement) {
            if (placement.getAdSpaceId().contains(Placement.Mobile_HOME_TOP)) {
                list.add(placement);
            }
        }
        return list;
    }

    public ArrayList<Placement> initListCastisSubTOP() {
        if (listPromotionPlacement == null || listPromotionPlacement.size() == 0) return null;
        ArrayList<Placement> list = new ArrayList<>();
        for (Placement placement : listPromotionPlacement) {
            if (placement.getAdSpaceId().contains(Placement.Mobile_SUB_TOP)) {
                list.add(placement);
            }
        }
        return list;
    }

    public ArrayList<Placement> initListCastisSubMID() {
        if (listPromotionPlacement == null || listPromotionPlacement.size() == 0) return null;
        ArrayList<Placement> list = new ArrayList<>();
        for (Placement placement : listPromotionPlacement) {
            if (placement.getAdSpaceId().contains(Placement.Mobile_SUB_MID)) {
                list.add(placement);
            }
        }
        return list;
    }

    public ArrayList<Placement> initListCastisSubBOT() {
        if (listPromotionPlacement == null || listPromotionPlacement.size() == 0) return null;
        ArrayList<Placement> list = new ArrayList<>();
        for (Placement placement : listPromotionPlacement) {
            if (placement.getAdSpaceId().contains(Placement.Mobile_SUB_BOTTOM)) {
                list.add(placement);
            }
        }
        return list;
    }

    public ArrayList<Placement> initListCastisHomeMID() {
        if (listPromotionPlacement == null || listPromotionPlacement.size() == 0) return null;
        ArrayList<Placement> list = new ArrayList<>();
        for (Placement placement : listPromotionPlacement) {
            if (placement.getAdSpaceId().contains(Placement.Mobile_HOME_MID)) {
                list.add(placement);
            }
        }
        return list;
    }

    public ArrayList<Placement> initListCastisHomeBOT() {
        if (listPromotionPlacement == null || listPromotionPlacement.size() == 0) return null;
        ArrayList<Placement> list = new ArrayList<>();
        for (Placement placement : listPromotionPlacement) {
            if (placement.getAdSpaceId().contains(Placement.Mobile_HOME_BOTTOM)) {
                list.add(placement);
            }
        }
        return list;
    }

    public Placement initListCastisIFrame() {
        if (listPromotionPlacement == null || listPromotionPlacement.size() == 0) return null;
        ArrayList<Placement> list = new ArrayList<>();
        for (Placement placement : listPromotionPlacement) {
            if (placement.getAdSpaceId().contains(Placement.Mobile_IFRAME)) {
                list.add(placement);
                getIframeBitmap(placement);
                return placement;
            }
        }
        return null;
    }

    public void getIframeBitmap(Placement placement) {
        if (placement == null) return;
        String link = placement.getAdContentUrl();
        iFrameUrl = link;
        if (link == null) return;

        try {
            URL url = new URL(link);
            iFrameBitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream());
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    public ArrayList<Placement> getListCastisPromotionHomeTOP() {
        return listHomeTopPlacement;
    }

    public String getBannerHomeImpression(int position) {
        String impression = null;
        if (listHomeTopPlacement == null || listHomeTopPlacement.isEmpty()) {
            return null;
        }
        int size = listHomeTopPlacement.size();
        if (size <= position) return null;

        impression = listHomeTopPlacement.get(position).getImpression();
        return impression;
    }

    public String getBannerSubImpression(int position) {
        String impression = null;
        if (listSubTopPlacement == null || listSubTopPlacement.isEmpty()) {
            return null;
        }
        int size = listSubTopPlacement.size();
        if (size <= position) return null;

        impression = listSubTopPlacement.get(position).getImpression();
        return impression;
    }


    public HashMap<String, Boolean> getHashImpression() {
        return hashImpression;
    }

    public void setHashImpression(HashMap<String, Boolean> hashImpression) {
        this.hashImpression = hashImpression;
    }

    private HashMap<String, Boolean> hashImpression = new HashMap<>();

    public boolean isSendLogHomeCastisEnable() {
        return isSendLogHomeCastisEnable;
    }

    public void enableSendLogHomeFragment(boolean sendLogHomeCastisEnable) {
        this.isSendLogHomeCastisEnable = sendLogHomeCastisEnable;
        if (!isSendLogHomeCastisEnable) {
            resetImpressionLogBanner();
        }
    }

    private boolean isSendLogHomeCastisEnable;

    public boolean isSendLogSubCastisEnable() {
        return isSendLogSubCastisEnable;
    }

    public void enableSendLogSubFragment(boolean sendLogSubCastisEnable) {
        this.isSendLogSubCastisEnable = sendLogSubCastisEnable;
        if (!isSendLogSubCastisEnable) {
            resetImpressionLogBanner();
        }
    }

    private boolean isSendLogSubCastisEnable;

    public HashMap<String, Boolean> getHashMidAndBotImpression() {
        return hashMidAndBotImpression;
    }

    public void resetImpressionLogBanner() {
        hashMidAndBotImpression.clear();
    }

    private HashMap<String, Boolean> hashMidAndBotImpression = new HashMap<>();

    public boolean isHasCastisPromotionTOP() {
//        return false;
        if (listHomeTopPlacement != null && listHomeTopPlacement.size() > 0)
            return true;
        return false;
    }

    public boolean isHasCastisSubPromotionTOP() {
//        return false;
        if (listSubTopPlacement != null && listSubTopPlacement.size() > 0)
            return true;
        return false;
    }

    public boolean isHasCastisPromotionBOT() {
//        return false;
        if (listHomeBotPlacement != null && listHomeBotPlacement.size() > 0)
            return true;
        return false;
    }

    public boolean isHasCastisSubPromotionBOT() {
//        return false;
        if (listSubBotPlacement != null && listSubBotPlacement.size() > 0)
            return true;
        return false;
    }

    public boolean isHasCastisPromotionMID() {
        if (listHomeMidPlacement != null && listHomeMidPlacement.size() > 0)
            return true;
        return false;
    }

    public boolean isHasCastisSubPromotionMID() {
        if (listSubMidPlacement != null && listSubMidPlacement.size() > 0)
            return true;
        return false;
    }


    public ArrayList<Placement> getListCastisPromotionHomeMID() {
        return listHomeMidPlacement;
    }

    public ArrayList<Placement> getListCastisPromotionSubMID() {
        return listSubMidPlacement;
    }

    public ArrayList<Placement> getListCastisPromotionHomeBOT() {
        return listHomeBotPlacement;
    }

    public ArrayList<Placement> getListCastisPromotionSubBOT() {
        return listSubBotPlacement;
    }

    public void setListSubBotPlacement(ArrayList<Placement> listSubBotPlacement) {
        this.listSubBotPlacement = listSubBotPlacement;
    }

    public ArrayList<Placement> getListCastisPromotionSubTOP() {
        return listSubTopPlacement;
    }

    public void setListSubTopPlacement(ArrayList<Placement> listSubTopPlacement) {
        this.listSubTopPlacement = listSubTopPlacement;
    }

    public HashMap<Integer, Placement> getHashHomePromotion() {
        return hashHomePromotion;
    }

    public void setHashHomePromotion(HashMap<Integer, Placement> hashHomePromotion) {
        this.hashHomePromotion = hashHomePromotion;
    }

    private HashMap<Integer, Placement> hashHomePromotion = new HashMap<>();// for mid banner

    public HashMap<Integer, Placement> getHashSubPromotion() {
        return hashSubPromotion;
    }

    public void setHashSubPromotion(HashMap<Integer, Placement> hashSubPromotion) {
        this.hashSubPromotion = hashSubPromotion;
    }

    private HashMap<Integer, Placement> hashSubPromotion = new HashMap<>();// for mid banner

    public void initHashHomePromotion() {
        if (hashHomePromotion != null) {
            hashHomePromotion.clear();
            hashHomePromotion = new HashMap<>();
        }
        if (listHomeMidPlacement == null) return;
        int sizeMid = listHomeMidPlacement.size();
        int count = MyContentManager.getInstance().getStartPromotionBanner() + 1;
        for (int i = 0; i < sizeMid; i++) {
            Logger.print("TAG", "TAG init initHashHomePromotion i " + i + " placement " + listHomeMidPlacement.get(i).getAdSpaceId());
            hashHomePromotion.put(count, listHomeMidPlacement.get(i));
            count += 2;
        }
    }

    public void initHashSubPromotion() {
        if (hashSubPromotion != null) {
            hashSubPromotion.clear();
            hashSubPromotion = new HashMap<>();
        }
        if (listSubMidPlacement == null) return;
        int sizeMid = listSubMidPlacement.size();
        int count = MyContentManager.getInstance().getStartPromotionBanner() + 1;
        for (int i = 0; i < sizeMid; i++) {
            hashSubPromotion.put(count, listSubMidPlacement.get(i));
            count += 2;
        }
    }

    public int getStartPromotionBanner() {
        return startPromotionBanner;
    }

    public void setStartPromotionBanner(int startPromotionBanner) {
//        Logger.print("TAG", "TAG init initHashHomePromotion setStartPromotionBanner " + startPromotionBanner);
        this.startPromotionBanner = startPromotionBanner;
    }
}
