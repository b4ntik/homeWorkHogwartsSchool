package ru.hogwarts.school.service;

import io.micrometer.common.lang.Nullable;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.AvatarRepository;
import ru.hogwarts.school.repository.FacultyRepository;
import ru.hogwarts.school.repository.StudentRepository;

import java.util.*;


@Service
public class StudentService {

    @Autowired
    private final StudentRepository studentRepository;
    private final FacultyRepository facultyRepository;
    private final AvatarRepository avatarRepository;

    public StudentService(StudentRepository studentRepository, FacultyRepository facultyRepository, AvatarRepository avatarRepository){ this.studentRepository = studentRepository;
        this.facultyRepository = facultyRepository;
        this.avatarRepository = avatarRepository;
    }

    @PersistenceContext
    private EntityManager entityManager;

    //создание студента
    @Transactional
    public Student createStudent(@Nullable Student student) {
        student.setId(null);

        return studentRepository.save(student);
    }

    //изменить студента

    public Student editStudent(Student student) {
        if (student.getId() != null) {
            Optional<Student> editedStudent = studentRepository.findById(student.getId());
            if(editedStudent.isPresent()){
                Student newStudent = editedStudent.get();
                newStudent.setName(student.getName());
                newStudent.setAge(student.getAge());
                studentRepository.save(newStudent);
                return newStudent;
            }
        }
        return null;
    }
        //найти студента по айди
        public Optional<Student> findStudent (Long id){
            return studentRepository.findById(id);
        }

        //удалить студента по айди
        @Transactional
        public void deleteStudent (Long id){
        avatarRepository.deleteAvatarByStudentId(id);
        studentRepository.deleteById(id);

        }

        // выдать список всех студентов
        public List<Student> getAllStudents () {

            return studentRepository.findAll();

        }
        @Transactional
        public void deleteStudentsByName(String nameStudent){
        studentRepository.deleteStudentsByName(nameStudent);
        }

        //фильтр студентов по возрасту
        public Collection<Student> findStudentsByAge ( int age){
            return studentRepository.findByAge(age);

        }
    //фильтр студентов по возрасту
    public Collection<Student> findStudentsByName (String nameStudent){
        return studentRepository.findStudentsByName(nameStudent);

    }
        public Collection<Student> findStudentsByAgeBetween(int min, int max){

        return studentRepository.findStudentsByAgeBetween(min, max);
        }

         public Faculty findFacultyByStudentId(Long studentId) {

        return facultyRepository.findFacultyByStudentId(studentId);
    }

    public int findCountStudents() { return studentRepository.findCountStudents();
    }

    public int findAverageAgeOfStudents() { return studentRepository.findAverageAgeOfStudents();
    }

    public List<Student> findLastFiveStudents() { return studentRepository.findLastFiveStudents();
    }
}