package com.elena.elena.routing;

import com.elena.elena.TestConfiguration;
import com.elena.elena.model.*;
import com.elena.elena.dao.*;

import org.junit.Assert;
import org.junit.Test;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import java.io.IOException;
import java.util.Set;
import java.util.List;
import java.util.ArrayList;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfiguration.class)
public class RouterTest {

	@Autowired
	@Qualifier("simple.graphml")
	private AbstractElenaGraph graph;

	@Autowired
	@Qualifier("simple.elevation.graphml")
	private AbstractElenaGraph elevationGraph;
	private AbstractRouter dijkstra_router;
	private AbstractRouter yen_router;
	
	// Set up graph and router
	@Before
	public void setUp() {
		dijkstra_router = RouterFactory.getRouter(Algorithm.DIJKSTRA);
		// Create a Yen's router with Dijkstra base
		yen_router = RouterFactory.getRouter( Algorithm.DIJKSTRA_YEN, 100);
	}
	
	// Test Dijkstra algorithm
	@Test
	public void dijkstraTest() {
		List<AbstractElenaPath> shortestPaths = dijkstra_router.getRoute(graph.getNode("n0").get(), graph.getNode("n3").get(), graph);
		Float expected = 3f;
		Float actual = shortestPaths.get(0).getPathWeights().get(WeightType.DISTANCE);
		assertEquals(expected, actual);
	}
	
	// Test Yen's algorithm
	@Test
	public void yenTest() {
		List<AbstractElenaPath> shortestPaths = yen_router.getRoute(graph.getNode("n0").get(), graph.getNode("n3").get(), graph);
		Float expected = 5f;
		Float actual = shortestPaths.get(2).getPathWeights().get(WeightType.DISTANCE);
		assertEquals(expected, actual);
	}

	@Test
	public void dijkstraMinElevationTest(){
		List<AbstractElenaPath> minElevation = RouterFactory.getRouter(Algorithm.DIJKSTRA_ELEVATION,300,
				ElevationMode.MIN).getRoute(elevationGraph.getNode("n0").get(), elevationGraph.getNode("n7").get(), elevationGraph);
		Float expectedElevation = 0f;
		Float actualElevation = minElevation.get(0).getPathWeights().get(WeightType.ELEVATION);
		assertEquals(expectedElevation, actualElevation);

		Float pathDistance = minElevation.get(0).getPathWeights().get(WeightType.DISTANCE);
		Assert.assertTrue(pathDistance <= 7);
	}

	@Test
	public void dijkstraMinElevationTest2(){
		List<AbstractElenaPath> minElevation = RouterFactory.getRouter(Algorithm.DIJKSTRA_ELEVATION,100,
				ElevationMode.MIN).getRoute(elevationGraph.getNode("n0").get(), elevationGraph.getNode("n7").get(), elevationGraph);
		Float expectedElevation = 3f;
		Float actualElevation = minElevation.get(0).getPathWeights().get(WeightType.ELEVATION);
		assertEquals(expectedElevation, actualElevation);

		Float pathDistance = minElevation.get(0).getPathWeights().get(WeightType.DISTANCE);
		Assert.assertTrue(pathDistance <= 3);
	}


}