package ru.hogwarts.school.service;

import jakarta.annotation.Nullable;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.repository.FacultyRepository;

import java.util.Collection;

@Service
public class FacultyService {
    @Autowired
    private final FacultyRepository facultyRepository;

    public FacultyService(FacultyRepository facultyRepository){ this.facultyRepository = facultyRepository;}

    @PersistenceContext
    private EntityManager entityManager;

    //создание факультета
    @Transactional
    public Faculty createFaculty(@Nullable Faculty faculty) {
        faculty.setId(null);
        Faculty newFaculty = entityManager.merge(faculty);
        entityManager.flush();
        return newFaculty;
    }

    //изменение факультета
    @Transactional
    public Faculty editFaculty(Faculty faculty) {
        if(faculty.getId() == null){
            throw new IllegalArgumentException("ID не может быть нулевым");
        }
        Faculty editedFaculty = entityManager.merge(faculty);
        entityManager.flush();
        return editedFaculty;
    }

    //удаление факультета
    public void deleteFaculty(Long id) {
        facultyRepository.deleteById(id);
    }

    //найти факультет по айди
    public Faculty findFaculty(long id) {
        String sql = "SELECT * FROM faculty WHERE id = :id";

        try {
            return (Faculty) entityManager.createNativeQuery(sql, Faculty.class)
                    .setParameter("id", id)
                    .getSingleResult();
        } catch (NoResultException e) {
            throw new RuntimeException("Факультета с таким ID " + id + " не существует", e);
        }

    }

    //выдать все факультеты
    @Transactional
    public Collection<Faculty> getAllFaculties() {
        String sql = "SELECT * FROM faculty";
        return  entityManager.createNativeQuery(sql, Faculty.class).getResultList();
    }
    //фильтр факультетов по цвету
    public Collection<Faculty> findFacultiesByColor(String color) {

        return facultyRepository.findByColor(color);

    }
}
