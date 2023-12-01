package com.indiainsure.android.MB360.insurance.hospitalnetwork.reponseclass;

import org.simpleframework.xml.Element;


public class HospitalsCount {
    @Element(name = "V_COUNT",required = false)
    public int V_COUNT;

    public int getV_COUNT() {
        return V_COUNT;
    }

    public void setV_COUNT(int v_COUNT) {
        V_COUNT = v_COUNT;
    }

    @Override
    public String toString() {
        return "HospitalsCount{" +
                "V_COUNT=" + V_COUNT +
                '}';
    }
}
