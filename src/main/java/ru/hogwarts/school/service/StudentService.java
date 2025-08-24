package ru.hogwarts.school.service;

import io.micrometer.common.lang.Nullable;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.StudentRepository;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class StudentService {

    @Autowired
    private StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository){ this.studentRepository = studentRepository;}

    @PersistenceContext
    private EntityManager entityManager;

    //создание студента
    @Transactional
    public Student createStudent(@Nullable Student student) {
        student.setId(null);
        Student newStudent = entityManager.merge(student);
        entityManager.flush();
        return newStudent;
    }

    //изменить студента. в данной ситуации replace выглядит логичнее put, иначе будет дублирование create
    //и replace сам вернет null, если совпадений не найдено
    @Transactional
    public Student editStudent(Student student) {
        if(student.getId() == null){
            throw new IllegalArgumentException("ID не может быть нулевым");
        }
        Student editedStudent = entityManager.merge(student);
        entityManager.flush();
        return editedStudent;
    }

    //найти студента по айди
    public Student findStudent(Long id) {
        return studentRepository.findById(id).get();
    }

    //удалить студента по айди
    public void deleteStudent(Long id) {
        studentRepository.deleteById(id);
    }

    // выдать список всех студентов
    public Collection<Student> getAllStudents() {
        studentRepository.findAll();
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
