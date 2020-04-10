package com.elena.elena.controller;

import com.elena.elena.dao.ElevationDao;
import com.elena.elena.model.AbstractElenaGraph;
import com.elena.elena.model.ElenaGraph;
import com.elena.elena.routing.AbstractRouter;
import com.elena.elena.routing.Algorithm;
import com.elena.elena.routing.ElevationMode;
import com.elena.elena.routing.RouterFactory;
import com.elena.elena.util.Parser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.IOException;


@RestController
public class RouteController {


    @Autowired
    private Parser coordinateParser;

    @Autowired
    private ApplicationContext appContext;

    @Autowired
    @Qualifier("sqliteDao")
    private ElevationDao elevationDao;

    private AbstractElenaGraph graph;



    @RequestMapping(method= RequestMethod.GET, value="search")
    @CrossOrigin("http://localhost:3000")
    @ResponseBody
    public ResponseEntity<String> getRouteCoordinates(@RequestParam("from") String from,
                                                      @RequestParam("to") String to,
                                                      @RequestParam("algorithm") String algorithmName,
                                                      @RequestParam("elemode") String elevationMode,
                                                      @RequestParam("percentage") int percentage) throws IOException {

        String body = "{\"values\": [ [42.704202, -71.502017], [42.7036844, -71.5020453] ," +
                "[42.7035846, -71.5020392]]}";

        Algorithm algorithm = Algorithm.getAlgorithmByName(algorithmName);
        ElevationMode eleMode = ElevationMode.getElevationMode(elevationMode);
        AbstractRouter router = RouterFactory.getRouter(algorithm, eleMode, percentage);

        if(!graph.getNode(from).isPresent() || !graph.getNode(to).isPresent()){
            //Throw an error response back
        }

        String responseBody = coordinateParser.path2String(router.getRoute(graph.getNode(from).get(), graph.getNode(to).get(), graph).get(0));
        ResponseEntity<String> responseEntity = new ResponseEntity<>(body, HttpStatus.OK);


        return responseEntity;
    }

    @RequestMapping(method= RequestMethod.GET, value="autocomplete")
    @CrossOrigin("http://localhost:3000")
    @ResponseBody
    public ResponseEntity<String> getAutocompleteList(@RequestParam("from") String from,
                                                      @RequestParam("to") String to) {

        return null;
    }

    @PostConstruct
    private void init() throws IOException {
        long s = System.currentTimeMillis();
        graph =  new ElenaGraph("berkeley.graphml", elevationDao);
        long e = System.currentTimeMillis();
        System.out.println(e - s);
        int i = 0;
    }

    @PreDestroy
    private void cleanup(){
        graph.cleanup();
    }
}
