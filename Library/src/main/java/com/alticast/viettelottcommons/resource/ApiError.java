package com.alticast.viettelottcommons.resource;

import com.alticast.viettelottcommons.R;
import com.alticast.viettelottcommons.def.ErrorCode;

import java.net.HttpURLConnection;
import java.util.HashMap;

/**
 * Created by mc.kim on 3/11/2016.
 */
public class ApiError {
    private Error error = null;

    public Error getError() {
        return error;
    }

    public ApiError() {

    }
    public ApiError(String code, String message) {
        error = new Error(code, message);
    }

    public class Error {
        private int status = 0;
        private String code = null;
        private String message = null;
        private String developerMessage = null;

        public Error() {
        }

        public Error(String code, String message) {
            this.code = code;
            this.message = message;
        }

        public int getStatus() {
            return status;
        }

        public String getCode() {
            return code;
        }

        public String getMessage() {
            return message;
        }


        public void setStatus(int status) {
            this.status = status;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public void setDeveloperMessage(String developerMessage) {
            this.developerMessage = developerMessage;
        }

        public String getDeveloperMessage() {
            return developerMessage;
        }

        @Override
        public String toString() {
            return "Error{" +
                    "status=" + status +
                    ", code='" + code + '\'' +
                    ", message='" + message + '\'' +
                    ", developerMessage='" + developerMessage + '\'' +
                    '}';
        }
    }


    @Override
    public String toString() {
        return "ApiError{" +
                "error=" + error +
                '}';
    }

    public void tokenExpired() {
        error = new Error();
        error.status = HttpURLConnection.HTTP_UNAUTHORIZED;

    }

    public void noData() {
        error = new Error();
        error.status = ErrorCode.STATE_NO_DATA;
    }

    public void noCampaignProgram() {
        error = new Error();
        error.status = ErrorCode.STATE_NO_CAMPAIGN_PROGRAM;
    }

    // pn.duy
    private static final HashMap<Integer, String> sErrorCodeMap = new HashMap<>();
    private static final HashMap<String, Integer> sErrorMessageMap = new HashMap<>();

    static {
        sErrorCodeMap.put(401, "H1401");
        sErrorCodeMap.put(404, "H1404");
        sErrorCodeMap.put(500, "H1500");
        sErrorCodeMap.put(502, "H1502");
        sErrorCodeMap.put(503, "H1503");
        sErrorCodeMap.put(-0x63, "H0400");
        sErrorCodeMap.put(-0x0100, "H0401");
        sErrorCodeMap.put(-0x0200, "H0402");
        sErrorCodeMap.put(-0x0300, "H0403");
        sErrorCodeMap.put(-0x0400, "H0404");
        sErrorCodeMap.put(-0x1000, "H0410");
        sErrorCodeMap.put(-0x1100, "H0411");
        sErrorCodeMap.put(-0x2000, "H0420");
        sErrorCodeMap.put(-0x3000, "H0430");
        sErrorCodeMap.put(-0x4000, "H0440");
        sErrorCodeMap.put(-0x5000, "H0450");
        sErrorCodeMap.put(-0x6000, "H0460");
        sErrorCodeMap.put(-0x6100, "H0461");
        sErrorCodeMap.put(-20481, "H9001");
        sErrorCodeMap.put(-20482, "H9002");
        sErrorCodeMap.put(-20483, "H9003");
        sErrorCodeMap.put(-20484, "H9004");
        sErrorCodeMap.put(-20485, "H9005");
        sErrorCodeMap.put(-20486, "H9006");
        sErrorCodeMap.put(-20487, "H9007");
        sErrorCodeMap.put(-20488, "H9008");
        sErrorCodeMap.put(-20489, "H9009");
        sErrorCodeMap.put(-20490, "H9010");
        sErrorCodeMap.put(-20491, "H9011");
        sErrorCodeMap.put(-20492, "H9012");
        sErrorCodeMap.put(-20493, "H9013");
        sErrorCodeMap.put(-20494, "H9014");
        sErrorCodeMap.put(-20495, "H9015");
        sErrorCodeMap.put(-20496, "H9016");
        sErrorCodeMap.put(-20497, "H9017");
        sErrorCodeMap.put(-20498, "H9018");
        sErrorCodeMap.put(-20499, "H9019");
        sErrorCodeMap.put(-20500, "H9020");
        sErrorCodeMap.put(-20501, "H9021");
        sErrorCodeMap.put(-20502, "H9022");
        sErrorCodeMap.put(-20503, "H9023");
        sErrorCodeMap.put(-20504, "H9024");
        sErrorCodeMap.put(-20505, "H9025");
        sErrorCodeMap.put(-20506, "H9026");
        sErrorCodeMap.put(-20507, "H9027");
        sErrorCodeMap.put(-20508, "H9028");
        sErrorCodeMap.put(-20509, "H9029");
        sErrorCodeMap.put(-20510, "H9030");
        sErrorCodeMap.put(-20511, "H9031");
        sErrorCodeMap.put(-20512, "H9032");
        sErrorCodeMap.put(-20513, "H9033");
        sErrorCodeMap.put(-20514, "H9034");
        sErrorCodeMap.put(-20515, "H9035");
        sErrorCodeMap.put(-20516, "H9036");
        sErrorCodeMap.put(-20517, "H9037");
        sErrorCodeMap.put(-20518, "H9038");
        sErrorCodeMap.put(-20519, "H9039");
        sErrorCodeMap.put(-20520, "H9040");
        sErrorCodeMap.put(-20521, "H9041");
        sErrorCodeMap.put(-20522, "H9042");
        sErrorCodeMap.put(-20523, "H9043");
        sErrorCodeMap.put(-20524, "H9044");
        sErrorCodeMap.put(-20525, "H9045");
        sErrorCodeMap.put(-20526, "H9046");
        sErrorCodeMap.put(-20527, "H9047");
        sErrorCodeMap.put(-20528, "H9048");
        sErrorCodeMap.put(-20529, "H9049");
        sErrorCodeMap.put(-201, "H0201");
        sErrorCodeMap.put(-202, "H0202");
        sErrorCodeMap.put(-203, "H0203");
        sErrorCodeMap.put(-204, "H0204");
        sErrorCodeMap.put(-205, "H0205");
        sErrorCodeMap.put(-211, "H0211");
        sErrorCodeMap.put(-212, "H0212");
        sErrorCodeMap.put(-213, "H0213");
        sErrorCodeMap.put(-214, "H0214");
        sErrorCodeMap.put(-215, "H0215");
        sErrorCodeMap.put(-216, "H0216");
        sErrorCodeMap.put(-217, "H0217");

        sErrorMessageMap.put("H1000", R.string.error_h1000);
        sErrorMessageMap.put("H1001", R.string.error_h1001);
        sErrorMessageMap.put("H1002", R.string.error_h1002);
        sErrorMessageMap.put("H1401", R.string.error_h1401);
        sErrorMessageMap.put("H1404", R.string.error_h1404);
        sErrorMessageMap.put("H1500", R.string.error_h1500);
        sErrorMessageMap.put("H1502", R.string.error_h1502);
        sErrorMessageMap.put("H1503", R.string.error_h1503);

        sErrorMessageMap.put("H0400", R.string.error_h0400);
        sErrorMessageMap.put("H0401", R.string.error_h0401);
        sErrorMessageMap.put("H0402", R.string.error_h0402);
        sErrorMessageMap.put("H0403", R.string.error_h0403);
        sErrorMessageMap.put("H0404", R.string.error_h0404);
        sErrorMessageMap.put("H0410", R.string.error_h0410);
        sErrorMessageMap.put("H0411", R.string.error_h0411);
        sErrorMessageMap.put("H0420", R.string.error_h0420);
        sErrorMessageMap.put("H0430", R.string.error_h0430);
        sErrorMessageMap.put("H0440", R.string.error_h0440);
        sErrorMessageMap.put("H0450", R.string.error_h0450);
        sErrorMessageMap.put("H0460", R.string.error_h0460);
        sErrorMessageMap.put("H0461", R.string.error_h0461);

        sErrorMessageMap.put("H9001", R.string.error_drm);
        sErrorMessageMap.put("H9002", R.string.error_drm);
        sErrorMessageMap.put("H9003", R.string.error_drm);
        sErrorMessageMap.put("H9004", R.string.error_drm);
        sErrorMessageMap.put("H9005", R.string.error_drm);
        sErrorMessageMap.put("H9006", R.string.error_drm);
        sErrorMessageMap.put("H9007", R.string.error_drm);
        sErrorMessageMap.put("H9008", R.string.error_drm);
        sErrorMessageMap.put("H9010", R.string.error_drm);
        sErrorMessageMap.put("H9011", R.string.error_drm);
        sErrorMessageMap.put("H9012", R.string.error_drm);
        sErrorMessageMap.put("H9013", R.string.error_drm);
        sErrorMessageMap.put("H9014", R.string.error_drm);
        sErrorMessageMap.put("H9015", R.string.error_drm);
        sErrorMessageMap.put("H9016", R.string.error_drm);
        sErrorMessageMap.put("H9017", R.string.error_drm);
        sErrorMessageMap.put("H9018", R.string.error_drm);
        sErrorMessageMap.put("H9019", R.string.error_drm);
        sErrorMessageMap.put("H9020", R.string.error_drm);
        sErrorMessageMap.put("H9021", R.string.error_drm);
        sErrorMessageMap.put("H9022", R.string.error_drm);
        sErrorMessageMap.put("H9023", R.string.error_drm);
        sErrorMessageMap.put("H9024", R.string.error_drm);
        sErrorMessageMap.put("H9025", R.string.error_drm);
        sErrorMessageMap.put("H9026", R.string.error_drm);
        sErrorMessageMap.put("H9027", R.string.error_drm);
        sErrorMessageMap.put("H9028", R.string.error_drm);
        sErrorMessageMap.put("H9029", R.string.error_drm);
        sErrorMessageMap.put("H9030", R.string.error_drm);
        sErrorMessageMap.put("H9031", R.string.error_drm);
        sErrorMessageMap.put("H9032", R.string.error_drm);
        sErrorMessageMap.put("H9033", R.string.error_drm);
        sErrorMessageMap.put("H9034", R.string.error_drm);
        sErrorMessageMap.put("H9035", R.string.error_drm);
        sErrorMessageMap.put("H9036", R.string.error_drm);
        sErrorMessageMap.put("H9037", R.string.error_drm);
        sErrorMessageMap.put("H9038", R.string.error_drm);
        sErrorMessageMap.put("H9039", R.string.error_drm);
        sErrorMessageMap.put("H9040", R.string.error_drm);
        sErrorMessageMap.put("H9041", R.string.error_drm);
        sErrorMessageMap.put("H9042", R.string.error_drm);
        sErrorMessageMap.put("H9043", R.string.error_drm);
        sErrorMessageMap.put("H9044", R.string.error_drm);
        sErrorMessageMap.put("H9045", R.string.error_drm);
        sErrorMessageMap.put("H9046", R.string.error_drm);
        sErrorMessageMap.put("H9047", R.string.error_drm);
        sErrorMessageMap.put("H9048", R.string.error_drm);
        sErrorMessageMap.put("H9049", R.string.error_drm);

        sErrorMessageMap.put("H0201", R.string.error_h0201);
        sErrorMessageMap.put("H0202", R.string.error_h0202);
        sErrorMessageMap.put("H0203", R.string.error_h0203);
        sErrorMessageMap.put("H0204", R.string.error_h0204);
        sErrorMessageMap.put("H0205", R.string.error_h0205);
        sErrorMessageMap.put("H0211", R.string.error_h0211);
        sErrorMessageMap.put("H0212", R.string.error_h0212);
        sErrorMessageMap.put("H0213", R.string.error_h0213);
        sErrorMessageMap.put("H0214", R.string.error_h0214);
        sErrorMessageMap.put("H0215", R.string.error_h0215);
        sErrorMessageMap.put("H0216", R.string.error_h0216);
        sErrorMessageMap.put("H0217", R.string.error_h0217);

    }

//    201 알수없는 TV Sync시 에러가 난경우
//    202 단말기기가 등록되지않았을 경우
//    203 입력데이터값이 잘못 요청된경우
//    204 STB과 연결시 기기등록에 실패한경우
//    205 STB과 연결이 실패한 경우
//    STB에서 받은 채널이 OTT에서 재생할수 없는 채널인 경우
//    STB에서 받은 VOD가 OTT에서 재생할수 없는 상품인 경우
//    STB에서 받은 캐치업 프로그램이 OTT에서 재생할수 없는 경우

    public ApiError(int statusCode, String errorCode, String message) {
        if(this.error == null) {
            this.error = new Error();
        }

        this.error.setStatus(statusCode);
        this.error.setCode(errorCode);
        this.error.setMessage(message);
    }

    public int getStatusCode() {
        return error != null ? error.getStatus() : -1;
    }

    public String getErrorCode() {
        return error != null ? error.getCode() : null;
    }


    public static String getErrorCode(int statusCode) {
        if(statusCode==-1){
            return sErrorCodeMap.containsKey(statusCode)?sErrorCodeMap.get(statusCode):"H9999";
        }{
            return sErrorCodeMap.containsKey(statusCode)?sErrorCodeMap.get(statusCode):"H0400";
        }
    }

    public static int getErrerString(String errorCode) {
//        Logger.d("ApiError", "sErrorMessageMap:" + sErrorMessageMap);
        return sErrorMessageMap.containsKey(errorCode) ? sErrorMessageMap.get(errorCode) : R.string.error_h1000;
    }

}
