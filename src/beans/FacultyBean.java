package beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import dto.Faculty;

public class FacultyBean implements Serializable{
	private List<Faculty> faculties = new ArrayList<>();

	public List<Faculty> getFaculties() {
		return faculties;
	}

	public void setFaculties(List<Faculty> faculties) {
		this.faculties = faculties;
	}
	
	
}
