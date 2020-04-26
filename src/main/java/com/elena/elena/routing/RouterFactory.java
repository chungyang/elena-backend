package com.elena.elena.routing;

import org.springframework.stereotype.Component;

@Component
public class RouterFactory  {

    public static AbstractRouter getRouter(Algorithm algorithm, int percentage, ElevationMode elevationMode){

        switch (algorithm){

            case DIJKSTRA_ELEVATION:
                return new DijkstraElevationRouter(percentage, elevationMode);
            default:
                return RouterFactory.getRouter(algorithm, percentage);
        }
    }

    public static AbstractRouter getRouter(Algorithm algorithm, int percentage) {

        switch(algorithm){

            case A_STAR_YEN:
                return new YenRouter(3, RouterFactory.getRouter(Algorithm.A_STAR));

            case DIJKSTRA_YEN:
                return new YenRouter(3, RouterFactory.getRouter(Algorithm.DIJKSTRA));

            default:
                return RouterFactory.getRouter(algorithm);

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
