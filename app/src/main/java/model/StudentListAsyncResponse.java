package model;

import java.util.ArrayList;

public interface StudentListAsyncResponse {
       void processFinished(ArrayList<Student> studentArrayList);
       void processFinishedStudent(Student studentObj);
}
