package com.elena.elena.model;

import lombok.Getter;

import java.util.Objects;

public class Coordinate {

    @Getter private float lat;
    @Getter private float lon;

    public Coordinate(float latittude, float longitutde){
        this.lat = latittude;
        this.lon = longitutde;
    }

    @Override
    public String toString() {
        return String.format("[ %s, %s ]", this.lat, this.lon);
    }

    @Override
    public int hashCode() {
        return Objects.hash(lat, lon);
    }

    @Override
    public boolean equals(Object obj) {

        if(!(obj instanceof Coordinate)){
            return false;
        }

        if(obj == this){
            return true;
        }
        Coordinate coordinate = (Coordinate) obj;
        return this.lon == coordinate.lon && this.lat == coordinate.lat;
    }
}
