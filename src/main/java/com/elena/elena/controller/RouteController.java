package com.elena.elena.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Created by chungyang on 3/25/20.
 */
@RestController
public class RouteController {

    @RequestMapping(method= RequestMethod.GET, value="search")
    @CrossOrigin("http://localhost:3000")
    @ResponseBody
    public ResponseEntity<String> getRouteCoordinates(@RequestParam("from") String from,
                                                      @RequestParam("to") String to,
                                                      @RequestParam("algorithm") String algorithm,
                                                      @RequestParam("elemode") String elevationMode,
                                                      @RequestParam("percentage") int percentage){

        String body = "{\"values\": [ [42.704202, -71.502017], [42.7036844, -71.5020453] ," +
                "[42.7035846, -71.5020392]]}";
        ResponseEntity<String> responseEntity = new ResponseEntity<String>(body, HttpStatus.OK);


        return responseEntity;
    }

}
