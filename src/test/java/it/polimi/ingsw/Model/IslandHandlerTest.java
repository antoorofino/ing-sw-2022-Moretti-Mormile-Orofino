package it.polimi.ingsw.Model;

import it.polimi.ingsw.Exception.SpecificCloudNotFoundException;
import it.polimi.ingsw.Exception.SpecificIslandNotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class IslandHandlerTest {
	IslandsHandler islandHandler;

	@BeforeEach
	public void setUp() {
		islandHandler = new IslandsHandler();
	}


	@AfterEach
	public void tearDown() {
		islandHandler = null;
	}

	@Test
	public void setIslandsTest() throws SpecificCloudNotFoundException, SpecificIslandNotFoundException {
		ArrayList<Island> islands = new ArrayList<Island>();
		Island island1 = new Island(0);
		Island island2 = new Island(1);
		islands.add(island1);
		islands.add(island2);
		islandHandler.setIslands(islands);
		assertEquals(island1,islandHandler.getIslandByID(0));
		assertEquals(islands,islandHandler.getIslands());
		try{
			islandHandler.getIslandByID(2);
		}catch (Exception e){}

	}

	/*@Test
	public void setMotherTest() throws SpecificCloudNotFoundException {
		int mother = 0;
		islandHandler.setMotherNature(mother);
		mother++;
		islandHandler.setMotherNature(mother);
		assertEquals(mother,islandHandler.getMotherNature());
	}*/
}
