package com.example.cse110_wwr_team2.Invitation;

public class Invitation {
    private String fromGmail;
    private String toGmail;
    private String toName;
    private String fromName;
    private String deviceID;
    private int status;

    public static final int PENDING = 0;
    public static final int ACCEPTED = 1;
    public static final int DECLINED = 2;

    public Invitation(String fromGmail, String toGmail, int status, String toName, String fromName, String deviceID) {
        if(status != PENDING && status != ACCEPTED && status != DECLINED) {
            throw new IllegalArgumentException("wrong status");
        }
        this.fromGmail = fromGmail;
        this.toGmail = toGmail;
        this.toName = toName;
        this.fromName = fromName;
        this.deviceID = deviceID;
        this.status = status;
    }

    public Invitation(String fromGmail, String toGmail, String toName, String fromName, String deviceID) {
        this(fromGmail, toGmail, Invitation.PENDING, toName, fromName, deviceID);
    }

    public int getStatus() {
        return this.status;
    }

    public void setStatus(int status) {
        if(status != PENDING && status != ACCEPTED && status != DECLINED) {
            throw new IllegalArgumentException("wrong status");
        }
        this.status = status;
    }

    public String getFromGmail() {
        return this.fromGmail;
    }

    public String getToGmail() {
        return this.toGmail;
    }

    public String getToName() {
        return this.toName;
    }

    public String getFromName() {
        return this.fromName;
    }

    public String getDeviceID() {
        return this.deviceID;
    }
}
