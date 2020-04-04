package com.elena.elena.util;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URIBuilder;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Map;
import java.util.Optional;

public class ElenaUtils {

    public static String getFilePath(String fileName) {
        URL res = ElenaUtils.class.getClassLoader().getResource(fileName);
        return res.getPath();
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
