package ru.hogwarts.school.service;

import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Student;

import java.util.Collection;
import java.util.HashMap;

@Service
public class StudentService {
    private HashMap<Long, Student> students = new HashMap<>();
    private long id;

    //создание студента
    public Student createStudent(Student student){
        student.setId();
        students.put(id, student);
        return student;
    }
    //изменить студента. в данной ситуации replace выглядит логичнее put, иначе будет дублирование create
    //и replace сам вернет null, если совпадений не найдено
    public Student editStudent(Student student){
            students.replace(student.getId(), student);
        return student;
    }
    //найти студента по айди
    public Student findStudent(long id){
        return students.get(id);
    }
    //удалить студента по айди
    public Student deleteStudent(long id){
        return students.remove(id);
    }
    // выдать список всех студентов
    public Collection<Student> getAlStudents(){
        return students.values();
    }
}
