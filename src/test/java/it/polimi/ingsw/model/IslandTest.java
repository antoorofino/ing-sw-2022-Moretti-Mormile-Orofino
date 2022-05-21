package it.polimi.ingsw.model;

import it.polimi.ingsw.util.exception.SpecificStudentNotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class IslandTest {
	Island island;

	@BeforeEach
	public void setUp() {
		island = new Island(0);
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
		assertEquals(false, island.towerIsAlreadyBuild());
	}

	@Test
	public void getNumStudentsTest(){
		assertEquals(0,island.getNumStudents(Piece.GNOME));
		assertEquals(0,island.getNumStudents(Piece.FROG));
		assertEquals(0,island.getNumStudents(Piece.DRAGON));
		assertEquals(0,island.getNumStudents(Piece.FAIRY));
		assertEquals(0,island.getNumStudents(Piece.UNICORN));

		Piece student = Piece.GNOME;
		island.addStudent(student);
		island.addStudent(student);
		student = Piece.UNICORN;
		island.addStudent(student);
		assertEquals(2,island.getNumStudents(Piece.GNOME));
		assertEquals(0,island.getNumStudents(Piece.FROG));
		assertEquals(0,island.getNumStudents(Piece.DRAGON));
		assertEquals(0,island.getNumStudents(Piece.FAIRY));
		assertEquals(1,island.getNumStudents(Piece.UNICORN));

	}

	 @Test
	public void getIslandOwnerTest(){
		 assertNull(island.getIslandOwner());
	}

	@Test
	public void getSizeTest(){
		assertEquals(1,island.getSize());
		//TODO: size > 1 after a merge
	}

	@Test
	public void getIdTest(){
		assertEquals(0,island.getID());
	}

	@Test
	public void getFlagNoInfluenceTest(){
		assertEquals(0,island.getFlagNoInfluence());
		island.addFlagNoInfluence();
		assertEquals(1, island.getFlagNoInfluence());
		island.addFlagNoInfluence();
		assertEquals(2, island.getFlagNoInfluence());
		island.removeFlagNoInfluence();
		assertEquals(1, island.getFlagNoInfluence());
		island.removeFlagNoInfluence();
		assertEquals(0, island.getFlagNoInfluence());
	}


	@Test
	public void calculateInfluenceTest(){

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

		/*
		P1 3 dragon 2 frog 1 fairy
		P2 3 frog 2 unicorn 1 gnome
		P3 4 unicorn 2 fairy
		 */

		Player p1 = new Player("id_p1");
		p1.setNickname("p1");
		Player p2 = new Player("id_p2");
		p2.setNickname("p2");
		Player p3 = new Player("id_p3");
		p3.setNickname("p3");

		p1.getPlayerBoard().addToEntrance(studentsP1);
		p2.getPlayerBoard().addToEntrance(studentsP2);
		p3.getPlayerBoard().addToEntrance(studentsP3);

		try {
			p1.getPlayerBoard().addStudentToRoom(Piece.DRAGON);
		} catch (SpecificStudentNotFoundException ignored) {

		}
		try {
			p1.getPlayerBoard().addStudentToRoom(Piece.DRAGON);
		} catch (SpecificStudentNotFoundException ignored) {

		}
		try {
			p1.getPlayerBoard().addStudentToRoom(Piece.DRAGON);
		} catch (SpecificStudentNotFoundException ignored) {

		}
		try {
			p1.getPlayerBoard().addStudentToRoom(Piece.FROG);
		} catch (SpecificStudentNotFoundException ignored) {

		}
		try {
			p1.getPlayerBoard().addStudentToRoom(Piece.FROG);
		} catch (SpecificStudentNotFoundException ignored) {

		}
		try {
			p1.getPlayerBoard().addStudentToRoom(Piece.FAIRY);
		} catch (SpecificStudentNotFoundException ignored) {

		}

		try {
			p2.getPlayerBoard().addStudentToRoom(Piece.UNICORN);
		} catch (SpecificStudentNotFoundException ignored) {

		}
		try {
			p2.getPlayerBoard().addStudentToRoom(Piece.UNICORN);
		} catch (SpecificStudentNotFoundException ignored) {

		}
		try {
			p2.getPlayerBoard().addStudentToRoom(Piece.FROG);
		} catch (SpecificStudentNotFoundException ignored) {

		}
		try {
			p2.getPlayerBoard().addStudentToRoom(Piece.FROG);
		} catch (SpecificStudentNotFoundException ignored) {

		}
		try {
			p2.getPlayerBoard().addStudentToRoom(Piece.FROG);
		} catch (SpecificStudentNotFoundException ignored) {

		}
		try {
			p2.getPlayerBoard().addStudentToRoom(Piece.GNOME);
		} catch (SpecificStudentNotFoundException ignored) {

		}


		try {
			p3.getPlayerBoard().addStudentToRoom(Piece.FAIRY);
		} catch (SpecificStudentNotFoundException ignored) {

		}
		try {
			p3.getPlayerBoard().addStudentToRoom(Piece.FAIRY);
		} catch (SpecificStudentNotFoundException ignored) {

		}
		try {
			p3.getPlayerBoard().addStudentToRoom(Piece.UNICORN);
		} catch (SpecificStudentNotFoundException ignored) {

		}
		try {
			p3.getPlayerBoard().addStudentToRoom(Piece.UNICORN);
		} catch (SpecificStudentNotFoundException ignored) {

		}
		try {
			p3.getPlayerBoard().addStudentToRoom(Piece.UNICORN);
		} catch (SpecificStudentNotFoundException ignored) {

		}
		try {
			p3.getPlayerBoard().addStudentToRoom(Piece.UNICORN);
		} catch (SpecificStudentNotFoundException ignored) {

		}



		PlayersHandler playersHandler = new PlayersHandler();
		playersHandler.addPlayer(p1);
		playersHandler.addPlayer(p2);
		playersHandler.addPlayer(p3);

		TeachersHandler teachersHandler = new TeachersHandler();
		teachersHandler.calculateTeacher(playersHandler.getPlayers(),false);
		/*
		P1 DRAGON : 6
		P2 FROG GNOME: 4 + 3 = 7
		P3 UNICORN FAIRY: 4 +2
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
		island.addStudent(Piece.GNOME);

		island.addStudent(Piece.UNICORN);
		island.addStudent(Piece.UNICORN);
		island.addStudent(Piece.UNICORN);
		island.addStudent(Piece.UNICORN);


		island.calculateInfluence(teachersHandler,true,null, new Player("id"));
		assertEquals(p2,island.getIslandOwner());

		island.addStudent(Piece.DRAGON);
		island.addStudent(Piece.DRAGON);
		island.addStudent(Piece.DRAGON);
		/*
		P1 DRAGON : 6 + 3
		P2 FROG GNOME: 4 + 3 = 7
		P3 UNICORN FAIRY: 4 +2
		 */
		island.calculateInfluence(teachersHandler,true,null, new Player("id"));
		assertEquals(p1,island.getIslandOwner());

		island.calculateInfluence(teachersHandler,true,Piece.DRAGON, new Player("id"));
		assertEquals(p2,island.getIslandOwner());

	}


}
