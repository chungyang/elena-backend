package com.elena.elena.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Created by chungyang on 3/25/20.
 */
@RestController
public class RouteController {

    @RequestMapping(method= RequestMethod.GET, value="/{from}/{to}")
    @CrossOrigin("http://localhost:3000")
    public String getRouteCoordinates(@PathVariable String from, @PathVariable String to){

        System.out.println(from);
        System.out.println(to);
        String body = "{\"values\": [ [42.704202, -71.502017], [42.7036844, -71.5020453] ," +
                "[42.7035846, -71.5020392]]}";

        return body;
    }

}
