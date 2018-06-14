package com.alticast.viettelottcommons.serviceMethod.acms.platform;


import com.alticast.viettelottcommons.resource.Mail;
import com.alticast.viettelottcommons.resource.response.MailListRes;
import com.alticast.viettelottcommons.resource.response.PlatformRes;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.Url;

/**
 * Created by mc.kim on 8/10/2016.
 */
public interface PlatformMethod {
    final String ACCESS_TOKEN = "access_token";

    @GET("/api1/contents/platform")
    Call<PlatformRes> getPlatform(@Query(ACCESS_TOKEN) String accessToken);

    @GET("/api1/contents/mails")
    Call<MailListRes> getMailList(@Query(ACCESS_TOKEN) String accessToken,
                                  @Query("type") String type,
                                  @Query("since") String since,
                                  @Query("until") String until,
                                  @Query("offset") int offset,
                                  @Query("limit") int limit,
                                  @Query("format") String format,
                                  @Query("from") String from);

    @GET("/api1/contents/mails")
    Call<MailListRes> getPairingGuide(@Query(ACCESS_TOKEN) String accessToken,
                                      @Query("type") String type,
                                      @Query("format") String format,
                                      @Query("from") String from);

    @GET
    Call<Mail> getMailContent(@Url String url,
                        @Query(ACCESS_TOKEN) String accessToken);
}
