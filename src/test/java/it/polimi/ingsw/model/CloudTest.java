package it.polimi.ingsw.model;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CloudTest {
	Cloud cloud;

	@AfterEach
	public void tearDown() {
		cloud = null;
	}

	@Test
	public void cloudTest(){
		int ID = 0;
		cloud = new Cloud(ID);
		// check id
		assertEquals(ID,cloud.getCloudID());
		// add students to cloud
		ArrayList<Piece> students = new ArrayList<>();
		students.add(Piece.GNOME);
		students.add(Piece.FROG);
		cloud.addStudents(students);
		// check students
		assertEquals(students,cloud.getStudents());
	}
}