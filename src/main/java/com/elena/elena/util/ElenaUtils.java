package com.elena.elena.util;

import com.elena.elena.model.AbstractElenaPath;
import com.elena.elena.routing.ElevationMode;
import com.elena.elena.routing.WeightType;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URIBuilder;

import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

public class ElenaUtils {

    public static InputStream getFileAsInputStream(String fileName) {
        InputStream res = ElenaUtils.class.getResourceAsStream("/" + fileName);

        return res;
    }

    public static AbstractElenaPath selectPath(ElevationMode mode, List<AbstractElenaPath> paths, int percentage){

        float margin = paths.get(0).getPathWeights().get(WeightType.DISTANCE) * percentage / 100;
        AbstractElenaPath selectedPath = paths.get(0);

        for(AbstractElenaPath path : paths){

            float pathDistance = path.getPathWeights().get(WeightType.DISTANCE);

            if(pathDistance < margin && compareElevation(selectedPath, path, mode)){
                selectedPath = path;
            }
        }

        return selectedPath;
    }

    public static boolean compareElevation(AbstractElenaPath firstPath, AbstractElenaPath secondPath, ElevationMode mode){

        switch (mode){
            case MAX:
                return firstPath.getPathWeights().get(WeightType.ELEVATION) > secondPath.getPathWeights().get(WeightType.ELEVATION);
            default:
                return firstPath.getPathWeights().get(WeightType.ELEVATION) < secondPath.getPathWeights().get(WeightType.ELEVATION);
        }
    }


    public static Optional<URI> getURL(String hostName, String searchPath, String scheme, NameValuePair... searchParameters){

        URI uri = null;
        try {
            uri = new URIBuilder()
                    .setScheme(scheme)
                    .setHost(hostName)
                    .setPath("/" + searchPath)
                    .setParameters(searchParameters).build();

        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        Optional<URI> optionalURI = Optional.ofNullable(uri);

        return optionalURI;
    }

}
