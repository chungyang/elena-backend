package com.elena.elena.routing;


public enum ElevationMode {
    MIN(1), MAX(-1);

    protected float sign;

    private ElevationMode(float sign){
        this.sign = sign;
    }
}
