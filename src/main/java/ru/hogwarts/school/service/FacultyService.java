package ru.hogwarts.school.service;

import jakarta.annotation.Nullable;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.FacultyRepository;
import ru.hogwarts.school.repository.StudentRepository;

import java.util.Collection;
import java.util.Optional;

@Service
public class FacultyService {
    @Autowired
    private final FacultyRepository facultyRepository;
    private final StudentRepository studentRepository;
    private static final Logger logger = LoggerFactory.getLogger(FacultyService.class);

    public FacultyService(FacultyRepository facultyRepository, StudentRepository studentRepository) {
        this.facultyRepository = facultyRepository;
        this.studentRepository = studentRepository;
    }

    //создание факультета
    @Transactional
    public Faculty createFaculty(@Nullable Faculty faculty) {
        faculty.setId(null);
        //логируем выполнение метода
        String methodName = new Throwable().getStackTrace()[0].getMethodName();
        logger.info("Was invoked method {} for create Faculty", methodName);
        return facultyRepository.save(faculty);

    }

    //изменение факультета
    @Transactional
    public Faculty editFaculty(Faculty faculty) {
        if (faculty.getId() != null) {
            Optional<Faculty> editedFaculty = facultyRepository.findById(faculty.getId());
            if (editedFaculty.isPresent()) {
                Faculty newFaculty = editedFaculty.get();
                newFaculty.setName(faculty.getName());
                newFaculty.setColor(faculty.getColor());
                facultyRepository.save(newFaculty);
                //логируем выполнение метода
                String methodName = new Throwable().getStackTrace()[0].getMethodName();
                logger.info("Was invoked method {} for edit Faculty", methodName);
                return newFaculty;
            }
        }
        //логируем выполнение метода
        String methodName = new Throwable().getStackTrace()[0].getMethodName();
        logger.info("Failed edit Faculty by {}", methodName);
        return null;
    }

    //удаление факультета
    public void deleteFaculty(Long id) {
        //логируем выполнение метода
        String methodName = new Throwable().getStackTrace()[0].getMethodName();
        logger.info("Was invoked method {} for delete Faculty", methodName);

        facultyRepository.deleteById(id);
    }

    //найти факультет по айди
    public Optional<Faculty> findFaculty(long id) {
        //логируем выполнение метода
        String methodName = new Throwable().getStackTrace()[0].getMethodName();
        logger.info("Was invoked method {} for search Faculty", methodName);
        return facultyRepository.findById(id);
    }

    //выдать все факультеты

    public Collection<Faculty> getAllFaculties() {
        //логируем выполнение метода
        String methodName = new Throwable().getStackTrace()[0].getMethodName();
        logger.info("Was invoked method {} for search all faculties", methodName);

        return facultyRepository.findAll();

    }

    //фильтр факультетов по цвету
    public Collection<Faculty> findFacultiesByColor(String color) {
        //логируем выполнение метода
        String methodName = new Throwable().getStackTrace()[0].getMethodName();
        logger.info("Was invoked method {} for search Faculty", methodName);
        return facultyRepository.findByColor(color);

    }

    public Collection<Faculty> findFacultyByColorOrNameIgnoreCase(String color, String name) {
        //логируем выполнение метода
        String methodName = new Throwable().getStackTrace()[0].getMethodName();
        logger.info("Was invoked method {} for search Faculty", methodName);

        return facultyRepository.findByColorOrNameIgnoreCase(color, name);
    }

    public Collection<Student> findStudentByFacultyId(Long id) {
        //логируем выполнение метода
        String methodName = new Throwable().getStackTrace()[0].getMethodName();
        logger.info("Was invoked method {} for search Faculty", methodName);
        return studentRepository.findStudentsByFacultyId(id);
    }
}
