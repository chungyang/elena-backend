package com.elena.elena.routing;

import org.springframework.stereotype.Component;

@Component
public class RouterFactory  {

    public static AbstractRouter getRouter(Algorithm algorithm, ElevationMode elevationMode, int percentage) {

        switch(algorithm){

            case A_STAR:
                return new AstarRouter(); //Fill in the constructor parameters later

            case DIJKSTRA:
                return new DijkstraRouter(); //Fill in the constructor parameters later

            default:
                return new YenRouter(); //Fill in the constructor parameters later

        }
    }
}
