package com.example.cse110_wwr_team2.Invitation;

public class Invitation {
    private String fromGmail;
    private String toGmail;
    private int status;

    public static final int PENDING = 0;
    public static final int ACCEPTED = 1;
    public static final int DECLINED = 2;

    public Invitation(String fromGmail, String toGmail, int status) {
        if(status != PENDING && status != ACCEPTED && status != DECLINED) {
            throw new IllegalArgumentException("wrong status");
        }
        this.fromGmail = fromGmail;
        this.toGmail = toGmail;
        this.status = status;
    }

    public Invitation(String fromGmail, String toGmail) {
        this(fromGmail, toGmail, Invitation.PENDING);
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
}
