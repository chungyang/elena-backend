package com.elena.elena.routing;

import org.springframework.stereotype.Component;

@Component
public class RouterFactory  {

    public static AbstractRouter getRouter(Algorithm algorithm, int percentage) {

        switch(algorithm){

            case A_STAR_MULTIROUTES:
                int numberOfRoutes = Math.max(20, percentage / 20);
                return new MultiRoutesAstarRouter(numberOfRoutes);

            default:
                return RouterFactory.getRouter(algorithm);
        }
    }


    public static AbstractRouter getRouter(Algorithm algorithm) {

        switch(algorithm){

            case A_STAR:
                return new AstarRouter(null);

            case DIJKSTRA:
                return new DijkstraRouter();

            case A_STAR_YEN:
                return new YenRouter(10, RouterFactory.getRouter(Algorithm.A_STAR));

            default:
                return new YenRouter(5, RouterFactory.getRouter(Algorithm.DIJKSTRA));

        }
    }
}
