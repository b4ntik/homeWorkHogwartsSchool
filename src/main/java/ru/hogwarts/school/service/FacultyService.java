package ru.hogwarts.school.service;

import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.stream.Collectors;

@Service
public class FacultyService {
    private HashMap<Long, Faculty> faculties;

    //создание факультета
    public Faculty createFaculty(Faculty faculty) {
        faculty.setId();
        faculties.put(faculty.getId(), faculty);
        return faculty;
    }

    //изменение факультета. в данной ситуации replace выглядит логичнее put, иначе будет дублирование create
    //и replace сам вернет null, если совпадений не найдено
    public Faculty editFaculty(Faculty faculty) {
        faculties.replace(faculty.getId(), faculty);
        return faculty;
    }

    //удаление факультета
    public Faculty deleteFaculty(long id) {
        return faculties.remove(id);
    }

    //найти факультет по айди
    public Faculty findFaculty(long id) {
        return faculties.get(id);
    }

    //выдать все факультеты
    public Collection<Faculty> getAllFaculties() {
        return faculties.values();
    }
    //фильтр факультетов по цвету
    public Collection<Faculty> findFacultiesByColor(String color) {
        Collection<Faculty> facultiesForFilter = getAllFaculties();
        if (facultiesForFilter.isEmpty()) {
            return Collections.emptyList();
        }
        Collection<Faculty> result = facultiesForFilter.stream()
                .filter(s -> s.getColor().toLowerCase().contains(color.toLowerCase()))
                .collect(Collectors.toList());
        return result;

    }
}
