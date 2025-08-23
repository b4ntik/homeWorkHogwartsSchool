package ru.hogwarts.school.service;

import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Student;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class StudentService {
    private HashMap<Long, Student> students = new HashMap<>();
    private long id = 0;

    //создание студента
    public Student createStudent(Student student) {
        student.setId(++id);
        students.put(id, student);
        return student;
    }

    //изменить студента. в данной ситуации replace выглядит логичнее put, иначе будет дублирование create
    //и replace сам вернет null, если совпадений не найдено
    public Student editStudent(Student student) {
        students.replace(student.getId(), student);
        return student;
    }

    //найти студента по айди
    public Student findStudent(long id) {
        return students.get(id);
    }

    //удалить студента по айди
    public Student deleteStudent(long id) {
        return students.remove(id);
    }

    // выдать список всех студентов
    public Collection<Student> getAllStudents() {
        return students.values();
    }

    //фильтр студентов по возрасту
    public Collection<Student> findStudentsByAge(int age) {
        Collection<Student> studentsForFilter = getAllStudents();
        if (studentsForFilter.isEmpty()) {
            return Collections.emptyList();
        }
        Collection<Student> result = studentsForFilter.stream()
                .filter(s -> s.getAge() == age)
                .collect(Collectors.toList());
        return result;

    }
}
