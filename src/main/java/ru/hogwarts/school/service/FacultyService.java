package ru.hogwarts.school.service;

import ru.hogwarts.school.model.Faculty;

import java.util.Collection;
import java.util.HashMap;

public class FacultyService {
    private HashMap<Long, Faculty> faculties;
    //создание факультета
    public Faculty createFaculty(Faculty faculty){
        faculty.setId();
        faculties.put(faculty.getId(), faculty);
        return faculty;
    }

    //изменение факультета. в данной ситуации replace выглядит логичнее put, иначе будет дублирование create
    //и replace сам вернет null, если совпадений не найдено
    public Faculty editFaculty(Faculty faculty){
        faculties.replace(faculty.getId(), faculty);
        return faculty;
    }

    //удаление факультета
    public Faculty deleteFaculty(long id){
        return faculties.remove(id);
    }

    //найти факультет по айди
    public Faculty findFaculty(long id){
        return faculties.get(id);
    }
    //выдать все факультеты
    public Collection<Faculty> 
}
