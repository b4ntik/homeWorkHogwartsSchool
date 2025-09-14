package ru.hogwarts.school.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.ResponseEntity;
import ru.hogwarts.school.model.Avatar;

import java.util.List;

public interface AvatarRepository extends JpaRepository<Avatar, Long>{
    void deleteAvatarByStudentId(Long id);

}
