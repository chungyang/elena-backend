package routing;

import com.elena.elena.model.*;
import com.elena.elena.dao.*;
import com.elena.elena.routing.*;
import com.elena.elena.util.*;

import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import java.io.IOException;
import java.util.Set;
import java.util.List;
import java.util.ArrayList;

public class RouterTest {
	
	private AbstractElenaGraph graph;
	private AbstractRouter dijkstra_router;
	private AbstractRouter yen_router;
	
	// Set up graph and router
	@Before
	public void setUp() {
		try {
			// Mock ElevationDao class
			ElevationDao mockedElevationDao = mock(HttpDao.class);
			List<ElevationData> emptyList = new ArrayList();
			when(mockedElevationDao.get(any(Set.class))).thenReturn(emptyList);
			// Create a graph with mocked ElevationDao
			graph = new ElenaGraph("/Users/patrick/desktop/Theory_and_Practice_of_Software_Engineering/Final Project/elena-backend/src/main/resources/manual.graphml", mockedElevationDao);
			// Create a Dijkstra router
			dijkstra_router = new DijkstraRouter();
			// Create a Yen's router with Dijkstra base
			Algorithm algorithm = Algorithm.getAlgorithmByName("dijkstra_yen");
			yen_router = new YenRouter(3, algorithm);
		}
		catch(IOException ioException) {
			ioException.printStackTrace();
		}
	}
	
	// Test Dijkstra algorithm
	@Test
	public void dijkstraTest() {
		List<AbstractElenaPath> shortestPaths = dijkstra_router.getRoute(graph.getNode("n0").get(), graph.getNode("n7").get(), graph);
		Weight distance = Weight.getWeightByName("distance");
		Float expected = (float) 3;
		Float actual = shortestPaths.get(0).getPathWeights().get(distance);
		assertEquals(expected, actual);
	}
	
	// Test Yen's algorithm
	@Test
	public void yenTest() {
		List<AbstractElenaPath> shortestPaths = yen_router.getRoute(graph.getNode("n0").get(), graph.getNode("n7").get(), graph);
		Weight distance = Weight.getWeightByName("distance");
		Float expected = (float) 9;
		Float actual = shortestPaths.get(2).getPathWeights().get(distance);
		assertEquals(expected, actual);
	}
}