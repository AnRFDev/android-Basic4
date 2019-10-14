package com.rustfisher.basic4.entity;

public class PingNetEntity {
    /*
     * 进行ping操作的ip
     */
    private String ip;

    /*
     进行ping操作的次数
     */
    private int pingCount;

    /*
     ping操作超时时间
     */

    private int pingWtime;

    /*
     存储ping操作后得到的数据
     */
    private StringBuffer resultBuffer;

    /*
     ping ip花费的时间
     */
    private String pingTime;

    /*
     进行ping操作后的结果
     */
    private boolean result;

    public PingNetEntity(String ip, int pingCount, int pingWtime, StringBuffer resultBuffer) {
        this.ip = ip;
        this.pingWtime = pingWtime;
        this.pingCount = pingCount;
        this.resultBuffer = resultBuffer;
    }

    public String getPingTime() {
        return pingTime;
    }

    public void setPingTime(String pingTime) {
        this.pingTime = pingTime;
    }

    public StringBuffer getResultBuffer() {
        return resultBuffer;
    }

    public void setResultBuffer(StringBuffer resultBuffer) {
        this.resultBuffer = resultBuffer;
    }

    public int getPingCount() {
        return pingCount;
    }

    public void setPingCount(int pingCount) {
        this.pingCount = pingCount;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public int getPingWtime() {
        return pingWtime;
    }

    public void setPingWtime(int pingWtime) {
        this.pingWtime = pingWtime;
    }
}
