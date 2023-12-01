package com.indiainsure.android.MB360.insurance.ecards.reponseclass;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name = "DocumentElement")
public class EcardResponse {
    @Element(name = "status")
    public String status;
    @Element(name = "EcardInformation")
    public String EcardInformation;

    public String getStatus() {
        return status;
    }

    public String getEcardInformation() {
        return EcardInformation;
    }


    @Override
    public String toString() {
        return "EcardResponse{" +
                "status='" + status + '\'' +
                ", EcardInformation='" + EcardInformation + '\'' +
                '}';
    }
}

