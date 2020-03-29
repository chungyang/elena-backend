package com.elena.elena.controller;

import com.elena.elena.model.Node;
import com.elena.elena.routing.AbstractRouterFactory;
import com.elena.elena.routing.Algorithm;
import com.elena.elena.routing.ElevationMode;
import com.elena.elena.routing.Router;
import com.elena.elena.util.Parser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
public class RouteController {


    @Autowired
    AbstractRouterFactory routerFactory;

    @Autowired
    Parser coordinateParser;

    @RequestMapping(method= RequestMethod.GET, value="search")
    @CrossOrigin("http://localhost:3000")
    @ResponseBody
    public ResponseEntity<String> getRouteCoordinates(@RequestParam("from") String from,
                                                      @RequestParam("to") String to,
                                                      @RequestParam("algorithm") String algorithmName,
                                                      @RequestParam("elemode") String elevationMode,
                                                      @RequestParam("percentage") int percentage){

//        String body = "{\"values\": [ [42.704202, -71.502017], [42.7036844, -71.5020453] ," +
//                "[42.7035846, -71.5020392]]}";

        Algorithm algorithm = Algorithm.getMatchingAlogrithm(algorithmName);
        ElevationMode eleMode = ElevationMode.getElevationMode(elevationMode);
        Router router = routerFactory.getRouter(algorithm, eleMode, percentage);
        Node originNode = new Node();
        Node destinatioNode = new Node();
        String responseBody = coordinateParser.coordinates2string(router.getRoute(originNode, destinatioNode));
        ResponseEntity<String> responseEntity = new ResponseEntity<>(responseBody, HttpStatus.OK);


        return responseEntity;
    }

}
