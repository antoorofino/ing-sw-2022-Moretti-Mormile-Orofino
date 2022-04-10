package it.polimi.ingsw.Model;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
		assertEquals(false,island.towerIsAlreadyBuild());
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
		assertEquals(null,island.getIslandOwner());
	}

	@Test
	public void getSizeTest(){
		assertEquals(1,island.getSize());
		//TODO quando si fa merge
	}

	//TODO getCountTest calculateInfluenceTest
	/*@Test
	public void getCountTest(){
	}
	@Test
	public void calculateInfluenceTest(){
	}
	*/
	@Test
	public void getIdTest(){
		assertEquals(0,island.getID());
	}

	@Test
	public void getFlagNoInfluenceTest(){
		assertEquals(false,island.getFlagNoInfluence());
		island.setFlagNoInfluence();
		assertEquals(true, island.getFlagNoInfluence());
		island.removeFlagNoInfluence();
		assertEquals(false, island.getFlagNoInfluence());
	}


}
