package it.polimi.ingsw.model;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

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
	public void setUpIslandTest(){
		islandHandler.setupIslandsTest();

		// check students (all dragons)
		for (Island i:islandHandler.getIslands()) {
			if(i.getID() != islandHandler.getMotherNature() && i.getID() != (islandHandler.getMotherNature() + 6)%12){
				assertEquals(1,i.getNumStudents(Piece.DRAGON));
			}
		}
	}

	@Test
	public void getIslandsTest(){
		islandHandler.setupIslandsTest();
		assertEquals(12,islandHandler.getIslands().size());
	}

	@Test
	public void mergeIslandsTest(){
		islandHandler.setupIslandsTest();
		/*
		P1 3 dragon 2 frog
		P2 3 frog
		 */

		ArrayList<Piece> studentsP1 = new ArrayList<>();
		studentsP1.add(Piece.DRAGON);
		studentsP1.add(Piece.DRAGON);
		studentsP1.add(Piece.DRAGON);
		studentsP1.add(Piece.FROG);
		studentsP1.add(Piece.FROG);

		ArrayList<Piece> studentsP2 = new ArrayList<>();
		studentsP2.add(Piece.FROG);
		studentsP2.add(Piece.FROG);
		studentsP2.add(Piece.FROG);

		// create player
		Player p1 = new Player("id_p1");
		p1.setNickname("p1");
		Player p2 = new Player("id_p2");
		p2.setNickname("p2");

		p1.getPlayerBoard().addToEntrance(studentsP1);
		p2.getPlayerBoard().addToEntrance(studentsP2);

		PlayersHandler playersHandler = new PlayersHandler(3);
		playersHandler.addPlayer(p1);
		playersHandler.addPlayer(p2);

		TeachersHandler teachersHandler = new TeachersHandler();

		// add students to room
		for (Piece p:studentsP1) {
			p1.getPlayerBoard().addStudentToRoom(p);
		}
		teachersHandler.calculateTeacher(playersHandler.getPlayers(),false);

		for (Piece p:studentsP2) {
			p2.getPlayerBoard().addStudentToRoom(p);
		}
		teachersHandler.calculateTeacher(playersHandler.getPlayers(),false);

		// p1 take the island 0 (at the start no students on mother nature pos)
		islandHandler.getCurrentIsland().addStudent(Piece.DRAGON);
		islandHandler.getCurrentIsland().calculateInfluence(teachersHandler,true,null, null);
		assertEquals(p1,islandHandler.getCurrentIsland().getIslandOwner());

		islandHandler.moveMotherNature(1);
		// p1 take the island 1
		islandHandler.getCurrentIsland().calculateInfluence(teachersHandler,true,null, null);
		assertEquals(p1,islandHandler.getCurrentIsland().getIslandOwner());

		islandHandler.moveMotherNature(10);
		// p1 take the island 11
		islandHandler.getCurrentIsland().calculateInfluence(teachersHandler,true,null, null);
		assertEquals(p1,islandHandler.getCurrentIsland().getIslandOwner());

		// merge island
		islandHandler.mergeIslands();

		assertEquals(10,islandHandler.getIslands().size());
		assertEquals(p1,islandHandler.getCurrentIsland().getIslandOwner());

		// last count merge
		assertEquals(1,islandHandler.getCountsLastMerge());
	}
}
