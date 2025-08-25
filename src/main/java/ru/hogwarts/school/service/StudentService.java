package ru.hogwarts.school.service;

import io.micrometer.common.lang.Nullable;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.StudentRepository;

import java.util.*;


@Service
public class StudentService {

    @Autowired
    private final StudentRepository studentRepository;

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

    //изменить студента
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
    @Transactional
    public Student findStudent(long id) {
        String sql = "SELECT * FROM student WHERE id = :id";

        try {
            return (Student) entityManager.createNativeQuery(sql, Student.class)
                    .setParameter("id", id)
                    .getSingleResult();
        } catch (NoResultException e) {
            throw new RuntimeException("Студента с таким ID " + id + " не существует", e);
        }

    }

    //удалить студента по айди
    public void deleteStudent(Long id) {
        studentRepository.deleteById(id);
    }

    // выдать список всех студентов
    @Transactional
    public Collection<Student> getAllStudents() {
        String sql = "SELECT * FROM student";
          return  entityManager.createNativeQuery(sql, Student.class).getResultList();
    }

    //фильтр студентов по возрасту
    public Collection<Student> findStudentsByAge(int age) {
        return studentRepository.findByAge(age);
        
    }
}
