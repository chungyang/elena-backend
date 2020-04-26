package com.elena.elena.routing;

import com.elena.elena.TestConfiguration;
import com.elena.elena.model.AbstractElenaGraph;
import com.elena.elena.model.AbstractElenaPath;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfiguration.class)
public class RouterTest {

	@Autowired
	@Qualifier("simple.graphml")
	private AbstractElenaGraph graph;
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
		Float expected = (float) 3;
		Float actual = shortestPaths.get(0).getPathWeights().get(WeightType.DISTANCE);
		assertEquals(expected, actual);
	}
	
	// Test Yen's algorithm
	@Test
	public void yenTest() {
		List<AbstractElenaPath> shortestPaths = yen_router.getRoute(graph.getNode("n0").get(), graph.getNode("n3").get(), graph);
		Float expected = (float) 5;
		Float actual = shortestPaths.get(2).getPathWeights().get(WeightType.DISTANCE);
		assertEquals(expected, actual);
	}
}