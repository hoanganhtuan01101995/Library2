package com.alticast.viettelottcommons.resource;

import com.alticast.viettelottcommons.util.TextUtils;
import com.alticast.viettelottcommons.util.TimeUtil;
import com.alticast.viettelottcommons.util.WindDataConverter;

import java.util.ArrayList;

public class TopupHistory {
    private com.alticast.viettelottcommons.resource.Wallet wallet = null;
    private int total = 0;
    private ArrayList<HistoryData> data = null;

    public com.alticast.viettelottcommons.resource.Wallet getWallet() {
        return wallet;
    }

    public void setWallet(com.alticast.viettelottcommons.resource.Wallet wallet) {
        this.wallet = wallet;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public ArrayList<HistoryData> getData() {
        return data;
    }

    public void setData(ArrayList<HistoryData> data) {
        this.data = data;
    }

    public class HistoryData {
        private String tx_id = null;
        private Source source = null;
        private com.alticast.viettelottcommons.resource.Wallet wallet = null;
        private String date = null;
        private String request_from = null;

        public String getTx_id() {
            return tx_id;
        }

        public void setTx_id(String tx_id) {
            this.tx_id = tx_id;
        }

        public Source getSource() {
            return source;
        }

        public String getDateDisplay() {
            long time = TimeUtil.getLongTime(date, WindDataConverter.WINDMILL_SERVER_TIME_FORMAT);
            return TextUtils.getDateString(time, WindDataConverter.WINDMIL_TOPUP_DATE_FORMAT);
        }
        public String getHourDisplay() {
            long time = TimeUtil.getLongTime(date, WindDataConverter.WINDMILL_SERVER_TIME_FORMAT);
            return TextUtils.getDateString(time, WindDataConverter.WINDMIL_TOPUP_HOUR_FORMAT);
        }

        public void setSource(Source source) {
            this.source = source;
        }

        public com.alticast.viettelottcommons.resource.Wallet getWallet() {
            return wallet;
        }

        public void setWallet(com.alticast.viettelottcommons.resource.Wallet wallet) {
            this.wallet = wallet;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getRequest_from() {
            return request_from;
        }

        public void setRequest_from(String request_from) {
            this.request_from = request_from;
        }

        @Override
        public String toString() {
            return "HistoryData{" +
                    "tx_id='" + tx_id + '\'' +
                    ", source='" + source + '\'' +
                    ", wallet=" + wallet +
                    ", date='" + date + '\'' +
                    ", request_from='" + request_from + '\'' +
                    '}';
        }


        public class Source {
            private String type = null;
            private String resource = null;
            private String display_name = null;

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }

            public String getResource() {
                return resource;
            }

            public void setResource(String resource) {
                this.resource = resource;
            }

            public String getDisplay_name() {
                return type.equals(WalletTopupMethod.METHOD_SCARD) ? "Thẻ cào" : display_name;
            }

            public void setDisplay_name(String display_name) {
                this.display_name = display_name;
            }

            @Override
            public String toString() {
                return "Source{" +
                        "type='" + type + '\'' +
                        ", resource='" + resource + '\'' +
                        ", display_name='" + display_name + '\'' +
                        '}';
            }
        }
    }

    @Override
    public String toString() {
        return "TopupHistory{" +
                "wallet=" + wallet +
                ", total=" + total +
                ", data=" + data +
                '}';
    }
}