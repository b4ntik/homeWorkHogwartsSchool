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

        return studentRepository.save(student);
    }

    //изменить студента

    public Optional<Student> editStudent(Student student) {
        if (student.getId() != null) {
            Optional<Student> editedStudent = studentRepository.findById(student.getId());
            if(editedStudent.isPresent()){
                Student newStudent = editedStudent.get();
                newStudent.setName(student.getName());
                newStudent.setAge(student.getAge());
                studentRepository.save(newStudent);
                return Optional.of(newStudent);
            }
        }
        return Optional.empty();
    }
        //найти студента по айди
        public Optional<Student> findStudent (Long id){
            return studentRepository.findById(id);
        }

        //удалить студента по айди
        public void deleteStudent (Long id){
            studentRepository.deleteById(id);
        }

        // выдать список всех студентов

        public List<Student> getAllStudents () {

            return studentRepository.findAll();

        }

        //фильтр студентов по возрасту
        public Collection<Student> findStudentsByAge ( int age){
            return studentRepository.findByAge(age);

        }
        public Collection<Student> findStudentsByAgeBetween(int min, int max){

        return studentRepository.findStudentsByAgeBetween(min, max);
        }

    }