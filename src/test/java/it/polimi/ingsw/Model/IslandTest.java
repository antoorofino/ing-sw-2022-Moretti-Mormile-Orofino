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


}
