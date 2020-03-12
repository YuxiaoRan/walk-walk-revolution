package com.example.cse110_wwr_team2.Team;

import java.util.Comparator;

public class TeamMember {
    public String nameOfMember;
    public String teamID;
    public boolean pendingInvite;

    public TeamMember() {}

    public TeamMember(String name, String teamID, boolean pInvite) {
        this.nameOfMember = name;
        this.teamID = teamID;
        this.pendingInvite = pInvite;
    }

    public static Comparator<TeamMember> memberNameComparator = new Comparator<TeamMember>() {
        @Override
        public int compare(TeamMember o1, TeamMember o2) {
            String memberName1 = o1.nameOfMember.toUpperCase();
            String memberName2 = o2.nameOfMember.toUpperCase();

            return memberName1.compareTo(memberName2);
        }
    };
}
