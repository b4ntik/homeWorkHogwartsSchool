package ru.hogwarts.school;

import ru.hogwarts.school.model.Student;

public class TestStudent {
    private Student testStudent;
    private String name;
    private int age;

   public TestStudent() {
        testStudent = new Student();
        //testStudent.setName("TestStudent");
        //testStudent.setAge(66);
    }
        public Student getStudent() {
            return testStudent;
   }


    public void setName(String testStudent) {
       this.name = name;
    }
    public void setAge(int age){this.age=age;}

    public String getName() { return name;}

    public int getAge(){ return age;}
}
