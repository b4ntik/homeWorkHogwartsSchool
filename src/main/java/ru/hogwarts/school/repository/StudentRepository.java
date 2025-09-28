package ru.hogwarts.school.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.hogwarts.school.model.Student;

import java.util.Collection;
import java.util.List;

public interface StudentRepository extends JpaRepository<Student, Long> {
    Collection<Student> findByAge(int age);
    Collection<Student> findStudentsByAgeBetween(int min, int max);
    Collection<Student> findStudentsByFacultyId(Long facultyId);

    //добавлены методы для проверки и очистки БД после тестов
    void deleteStudentsByName(String nameStudent);
    Collection<Student> findStudentsByName(String nameStudent);
    List<Student> findAll();

}
