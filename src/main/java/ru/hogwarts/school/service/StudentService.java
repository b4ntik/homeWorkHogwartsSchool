package ru.hogwarts.school.service;

import io.micrometer.common.lang.Nullable;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.Objects;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.AvatarRepository;
import ru.hogwarts.school.repository.FacultyRepository;
import ru.hogwarts.school.repository.StudentRepository;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;
//import java.util.logging.Logger;


@Service
public class StudentService {


    @Autowired
    private final StudentRepository studentRepository;
    private final FacultyRepository facultyRepository;
    private final AvatarRepository avatarRepository;
    private static final Logger logger = LoggerFactory.getLogger(StudentService.class);

    public StudentService(StudentRepository studentRepository, FacultyRepository facultyRepository, AvatarRepository avatarRepository) {
        this.studentRepository = studentRepository;
        this.facultyRepository = facultyRepository;
        this.avatarRepository = avatarRepository;

    }

    @PersistenceContext
    private EntityManager entityManager;

    //создание студента
    @Transactional
    public Student createStudent(@Nullable Student student) {
        String methodName = new Throwable().getStackTrace()[0].getMethodName();
        logger.info("Method {} was called", methodName);
        logger.info("Was invoked method for create student");
        student.setId(null);
        return studentRepository.save(student);
    }

    //изменить студента

    public Student editStudent(Student student) {
        if (student.getId() != null) {
            Optional<Student> editedStudent = studentRepository.findById(student.getId());
            if (editedStudent.isPresent()) {
                Student newStudent = editedStudent.get();
                newStudent.setName(student.getName());
                newStudent.setAge(student.getAge());
                studentRepository.save(newStudent);
                String methodName = new Throwable().getStackTrace()[0].getMethodName();
                logger.info("Method {} was called", methodName);
                logger.info("Student {} was edited exists", newStudent.getName());
                return newStudent;
            }
        }
        logger.info("Student {} doesn`t exist", student.getId());
        return null;
    }

    //найти студента по айди
    public Optional<Student> findStudent(Long id) {
        String methodName = new Throwable().getStackTrace()[0].getMethodName();
        logger.info("Method {} was called", methodName);
        logger.info("Student by id {} was found", id);
        return studentRepository.findById(id);
    }

    //удалить студента по айди
    @Transactional
    public void deleteStudent(Long id) {
        avatarRepository.deleteAvatarByStudentId(id);
        String methodName = new Throwable().getStackTrace()[0].getMethodName();
        logger.info("Method {} was called", methodName);
        logger.info("Student by id {} was deleted", id);
        studentRepository.deleteById(id);

    }

    // выдать список всех студентов
    public List<String> getAllStudents() {
        String methodName = new Throwable().getStackTrace()[0].getMethodName();
        logger.info("Method {} was called", methodName);
        logger.info("All students was printed");
        List<String> allNames = studentRepository.findAll().stream()
                .filter(Objects::nonNull)
                .map(Student::getName)
                .map(String::toUpperCase)
                .sorted()
                .collect(Collectors.toList());


        return allNames;

    }

    @Transactional
    public void deleteStudentsByName(String nameStudent) {
        String methodName = new Throwable().getStackTrace()[0].getMethodName();
        logger.info("Method {} was called", methodName);
        logger.info("Student by name {} was deleted", nameStudent);

        studentRepository.deleteStudentsByName(nameStudent);
    }

    //фильтр студентов по возрасту
    public Collection<Student> findStudentsByAge(int age) {
        String methodName = new Throwable().getStackTrace()[0].getMethodName();
        logger.info("Method {} was called", methodName);
        logger.info("Student {} years old was found", age);
        return studentRepository.findByAge(age);

    }

    //поиск студента по имени
    public Collection<Student> findStudentsByName(String nameStudent) {
        String methodName = new Throwable().getStackTrace()[0].getMethodName();
        logger.info("Method {} was called", methodName);
        logger.info("Student by name {} was found", nameStudent);
        return studentRepository.findStudentsByName(nameStudent);

    }

    public Collection<Student> findStudentsByAgeBetween(int min, int max) {
        String methodName = new Throwable().getStackTrace()[0].getMethodName();
        logger.info("Method {} was called", methodName);
        logger.trace("Method 'findStudentsByAge'{} was called", methodName);
        logger.info("Students between {min} and {max}");
        return studentRepository.findStudentsByAgeBetween(min, max);
    }

    public Faculty findFacultyByStudentId(Long studentId) {
        String methodName = new Throwable().getStackTrace()[0].getMethodName();
        logger.info("Method {} was called", methodName);
        logger.info("Faculty {} by studentId", facultyRepository.findFacultyByStudentId(studentId));
        return facultyRepository.findFacultyByStudentId(studentId);
    }

    public OptionalDouble getAverageAge() {
        String methodName = new Throwable().getStackTrace()[0].getMethodName();
        logger.info("Method {} was called", methodName);
        logger.info("All students was printed");
        //List<Integer> ages = Arrays.studentRepository.findAll().getAge();
        OptionalDouble averageAge = studentRepository.findAll().stream()
                .filter(Objects::nonNull)
                .map(Student::getAge)
                .mapToInt(Integer::intValue)
                .average();


        return averageAge;
    }
}