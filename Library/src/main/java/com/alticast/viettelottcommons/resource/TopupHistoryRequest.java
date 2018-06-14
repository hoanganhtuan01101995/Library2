package com.alticast.viettelottcommons.resource;


public class TopupHistoryRequest {
    private String since = null;
    private String until = null;
    private String offset = null;
    private String limit = null;

    public TopupHistoryRequest(String since, String until, String offset, String limit) {
        this.since = since;
        this.until = until;
        this.offset = offset;
        this.limit = limit;
    }

    public String getSince() {
        return since;
    }

    public void setSince(String since) {
        this.since = since;
    }

    public String getUntil() {
        return until;
    }

    public void setUntil(String until) {
        this.until = until;
    }

    public String getOffset() {
        return offset;
    }

    public void setOffset(String offset) {
        this.offset = offset;
    }

    public String getLimit() {
        return limit;
    }

    public void setLimit(String limit) {
        this.limit = limit;
    }

    @Override
    public String toString() {
        return "TopupHistoryRequest{" +
                "limit='" + limit + '\'' +
                ", offset='" + offset + '\'' +
                ", until='" + until + '\'' +
                ", since='" + since + '\'' +
                '}';
    }
}
