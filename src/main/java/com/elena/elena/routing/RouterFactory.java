package com.elena.elena.routing;

import org.springframework.stereotype.Component;

@Component
public class RouterFactory  {

    public static AbstractRouter getRouter(Algorithm algorithm, int percentage) {

        switch(algorithm){

            case A_STAR:
                return new AstarRouter();

            case DIJKSTRA:
                return new DijkstraRouter();

            case A_STAR_YEN:
                return new YenRouter(1, RouterFactory.getRouter(Algorithm.A_STAR));

            default:
                return new YenRouter(1, RouterFactory.getRouter(Algorithm.DIJKSTRA));

        }
    }


    public static AbstractRouter getRouter(Algorithm algorithm) {

        switch(algorithm){

            case A_STAR:
                return new AstarRouter();

            default:
                return new DijkstraRouter();

        }
    }
}
