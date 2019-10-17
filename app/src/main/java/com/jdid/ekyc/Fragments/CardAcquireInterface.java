package com.jdid.ekyc.Fragments;

public interface CardAcquireInterface{
    public void updateEventLog(boolean fPass, boolean fShowIcon, String strInformation);
    public void setNextStep();
    public void startCompareFace();
}