package it.polimi.ingsw.model;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class IslandTest {
	Island island;

	@BeforeEach
	public void setUp() {
		island = new Island(1);
	}


	@AfterEach
	public void tearDown() {
		island = null;
	}

	@Test
	public void addStudentTest(){
		Piece student = Piece.GNOME;
		island.addStudent(student);
		island.addStudent(student);
		island.addStudent(student);
		assertEquals(3,island.getNumStudents(Piece.GNOME));
	}

	@Test
	public void towerIsAlreadyBuildTest(){
		assertFalse(island.towerIsAlreadyBuild());
	}

	@Test
	public void getStudentsTest(){
		// no students on island
		for (Piece p:Piece.values()) {
			assertEquals(0,island.getNumStudents(p));
		}

		// add students
		Piece student = Piece.GNOME;
		island.addStudent(student);
		island.addStudent(student);
		student = Piece.UNICORN;
		island.addStudent(student);

		// check students getNumStudents(Piece)
		// check students getStudentsOnIsland()
		for (Piece p:island.getStudentsOnIsland().keySet()) {
			assertEquals(island.getStudentsOnIsland().get(p),island.getNumStudents(p));
		}
	}

	 @Test
	public void getIslandOwnerTest(){
		 assertNull(island.getIslandOwner());
	}

	@Test
	public void getSizeTest(){
		assertEquals(1,island.getSize());
	}

	@Test
	public void getIdTest(){
		assertEquals(1,island.getID());
	}

	@Test
	public void decreaseIDTest(){
		island.decreaseID();
		assertEquals(0,island.getID());
	}

	@Test
	public void increaseSizeTest(){
		island.increaseSize(2);
		assertEquals(3,island.getSize());
	}

	@Test
	public void calculateInfluenceTest(){
		/*
		P1 3 dragon 2 frog 1 fairy
		P2 3 frog 2 unicorn 1 gnome
		P3 4 unicorn 2 fairy
		 */

		ArrayList<Piece> studentsP1 = new ArrayList<>();
		studentsP1.add(Piece.DRAGON);
		studentsP1.add(Piece.DRAGON);
		studentsP1.add(Piece.DRAGON);
		studentsP1.add(Piece.FROG);
		studentsP1.add(Piece.FROG);
		studentsP1.add(Piece.FAIRY);

		ArrayList<Piece> studentsP2 = new ArrayList<>();
		studentsP2.add(Piece.FROG);
		studentsP2.add(Piece.FROG);
		studentsP2.add(Piece.FROG);
		studentsP2.add(Piece.UNICORN);
		studentsP2.add(Piece.UNICORN);
		studentsP2.add(Piece.GNOME);

		ArrayList<Piece> studentsP3 = new ArrayList<>();
		studentsP3.add(Piece.UNICORN);
		studentsP3.add(Piece.FAIRY);
		studentsP3.add(Piece.UNICORN);
		studentsP3.add(Piece.UNICORN);
		studentsP3.add(Piece.UNICORN);
		studentsP3.add(Piece.FAIRY);

		// create player
		Player p1 = new Player("id_p1");
		p1.setNickname("p1");
		Player p2 = new Player("id_p2");
		p2.setNickname("p2");
		Player p3 = new Player("id_p3");
		p3.setNickname("p3");

		p1.getPlayerBoard().addToEntrance(studentsP1);
		p2.getPlayerBoard().addToEntrance(studentsP2);
		p3.getPlayerBoard().addToEntrance(studentsP3);

		PlayersHandler playersHandler = new PlayersHandler(3);
		playersHandler.addPlayer(p1);
		playersHandler.addPlayer(p2);
		playersHandler.addPlayer(p3);

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

		for (Piece p:studentsP3) {
			p3.getPlayerBoard().addStudentToRoom(p);
		}
		teachersHandler.calculateTeacher(playersHandler.getPlayers(),false);

		// no piece on island
		island.calculateInfluence(teachersHandler,true,null, null);
		assertNull(island.getIslandOwner());

		/*
		P1 DRAGON : 6
		P2 FROG GNOME: 4 + 3 = 6
		P3 UNICORN FAIRY: 4 + 2
		Tie P1 and P2
		 */

		island.addStudent(Piece.DRAGON);
		island.addStudent(Piece.DRAGON);
		island.addStudent(Piece.DRAGON);
		island.addStudent(Piece.DRAGON);
		island.addStudent(Piece.DRAGON);
		island.addStudent(Piece.DRAGON);

		island.addStudent(Piece.FROG);
		island.addStudent(Piece.FROG);
		island.addStudent(Piece.FROG);
		island.addStudent(Piece.FROG);

		island.addStudent(Piece.FAIRY);
		island.addStudent(Piece.FAIRY);

		island.addStudent(Piece.GNOME);
		island.addStudent(Piece.GNOME);

		island.addStudent(Piece.UNICORN);
		island.addStudent(Piece.UNICORN);
		island.addStudent(Piece.UNICORN);
		island.addStudent(Piece.UNICORN);

		island.calculateInfluence(teachersHandler,true,null, null);
		assertNull(island.getIslandOwner());

		/*
		P1 DRAGON : 6 + 3 = 9
		P2 FROG GNOME: 4 + 3 = 7
		P3 UNICORN FAIRY: 4 +2
		 */

		island.addStudent(Piece.GNOME);
		island.addStudent(Piece.DRAGON);
		island.addStudent(Piece.DRAGON);
		island.addStudent(Piece.DRAGON);

		island.calculateInfluence(teachersHandler,true,null,null);

		assertEquals(p1,island.getIslandOwner());
		assertTrue(island.towerIsAlreadyBuild());

		island.calculateInfluence(teachersHandler,true,Piece.DRAGON, null);

		assertEquals(p2,island.getIslandOwner());
	}
}
