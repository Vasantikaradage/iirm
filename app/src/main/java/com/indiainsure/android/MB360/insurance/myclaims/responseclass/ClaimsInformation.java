package com.indiainsure.android.MB360.insurance.myclaims.responseclass;

import org.simpleframework.xml.ElementList;

import java.util.ArrayList;
import java.util.List;

public class ClaimsInformation {
    @ElementList(name = "claims", inline = true,required = false)
    public List<Claims> claims = new ArrayList<>();

    public List<Claims> getClaims() {
        return claims;
    }

    public void setClaims(List<Claims> claims) {
        this.claims = claims;
    }

    @Override
    public String toString() {
        return "ClaimsInformation{" +
                "claims=" + claims +
                '}';
    }
}
