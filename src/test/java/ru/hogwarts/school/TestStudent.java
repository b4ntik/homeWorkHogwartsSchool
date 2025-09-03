package ru.hogwarts.school;

import ru.hogwarts.school.model.Student;

public class TestStudent {
    private Student testStudent;

   public TestStudent() {
        testStudent = new Student();
        testStudent.setName("TestStudent");
        testStudent.setAge(66);
    }
        public Student getStudent() {
            return testStudent;
   }


}
