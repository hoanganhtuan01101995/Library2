package com.alticast.viettelottcommons.resource.ads;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by user on 2016-05-17.
 */
public class Result implements Parcelable {

    private static final String CODE_SUCCESS="100";

    /**
     *
     Ad in result
     응답 결과 code 성공: "100", 실패:"2xx"
     210 : 필수 요청파라미터 누락
     211 : DB 장애
     212 : 광고 결정서버(ADDSfe) 접속장애

     placementDecision in result
     응답 결과 code 성공: "100", 실패:"2xx"
     200 : 알수없는 오류
     201 : 유효한 인벤토리가 아님
     202 : CSIS서버 통신실패
     203 : 본편 컨텐트정보 찾을 수 없음

     */
    private String code = null;
    /**응답 결과 메시지*/
    private String message = null;

    @Override
    public String toString() {
        return "Result{" +
                "code='" + code + '\'' +
                ", message='" + message + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    protected Result(Parcel in) {
        code = in.readString();
        message = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(code);
        dest.writeString(message);
    }


    public boolean isSuccess() {
        return code != null&& code.equals(CODE_SUCCESS);
    }

    @SuppressWarnings("unused")
    public static final Creator<Result> CREATOR = new Creator<Result>() {
        @Override
        public Result createFromParcel(Parcel in) {
            return new Result(in);
        }

        @Override
        public Result[] newArray(int size) {
            return new Result[size];
        }
    };
}
