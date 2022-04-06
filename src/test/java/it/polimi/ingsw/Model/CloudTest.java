package it.polimi.ingsw.Model;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CloudTest {
	Cloud cloud;

	@BeforeEach
	public void setUp() {
	}

	@AfterEach
	public void tearDown() {
		cloud = null;
	}

	@Test
	public void MoveStudentTest(){
		int ID = 0;
		cloud = new Cloud(ID);
		ArrayList<Piece> students = new ArrayList<Piece>();
		students.add(Piece.GNOME);
		students.add(Piece.FROG);
		cloud.addStudents(students);
		assertEquals(students,cloud.getStudents());
		assertEquals(ID,cloud.getCloudID());
	}
}