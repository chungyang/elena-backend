package com.elena.elena.routing;


public abstract class AbstractRouterFactory {

    public abstract Router getRouter(Algorithm algorithm, ElevationMode elevationMode,int percentage );
}
