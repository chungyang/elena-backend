package com.elena.elena.dao;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

public class ElevationData {

    @Setter @Getter private String id;
    @Setter @Getter private float elevation;

    public ElevationData(@NonNull String id, Float elevation){
        this.id = id;
    }

    @Override
    public String toString(){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("(").append(id).append(",").append(elevation)
                .append(")");
        return stringBuilder.toString();
    }
}
