package ru.hogwarts.school.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.hogwarts.school.model.Avatar;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.AvatarRepository;
import ru.hogwarts.school.repository.StudentRepository;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageInputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static java.nio.file.StandardOpenOption.CREATE_NEW;

@Service
@Transactional
public class AvatarService {
    @Value("${path.to.avatars.folder}")
    private String avatarsDir;

    @Autowired
    private final AvatarRepository avatarRepository;
    private final StudentRepository studentRepository;
    private final StudentService studentService;

    public AvatarService(AvatarRepository avatarRepository, StudentRepository studentRepository, StudentService studentService) {
        this.avatarRepository = avatarRepository;
        this.studentRepository = studentRepository;
        this.studentService = studentService;
    }
    @Transactional
    public void uploadAvatar(Long studentId, MultipartFile avatarFile) throws IOException {
        Student student = studentRepository.getById(studentId);
        Path filePath = Path.of(avatarsDir, student + "." + getExtensions(Objects.requireNonNull(avatarFile.getOriginalFilename())));
        Files.createDirectories(filePath.getParent());
        Files.deleteIfExists(filePath);
        try (
                InputStream is = avatarFile.getInputStream();
                OutputStream os = Files.newOutputStream(filePath, CREATE_NEW);
                BufferedInputStream bis = new BufferedInputStream(is, 1024);
                BufferedOutputStream bos = new BufferedOutputStream(os, 1024);
        ) {
            bis.transferTo(bos);
        }

        Avatar avatar = avatarRepository.findById(studentId).orElseGet(() -> {
            Avatar  newAvatar = new Avatar();
            newAvatar.setStudent(student);
            return newAvatar;
        });
        avatar.setStudent(student);
        avatar.setFilePath(filePath.toString());
        avatar.setFileSize(avatarFile.getSize());
        avatar.setMediaType(avatarFile.getContentType());
        try {
            avatar.setData(avatarFile.getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        avatarRepository.save(avatar);

    }

    public Avatar findAvatar(Long studentId) {
        return avatarRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Аватар отсутствует по этому идентификатору"));
    }


    private String getExtensions(String fileName) {
        return fileName.substring(fileName.lastIndexOf(".") + 1);

    }

    public void deleteAvatarByStudentId(Long id) {
        avatarRepository.deleteAvatarByStudentId(id);
    }

    public List<byte[]> findAllAvatars(Integer pageNumber, Integer size) throws IOException {
        PageRequest pageRequest = PageRequest.of(pageNumber - 1, size);
        List<Avatar> avatars = avatarRepository.findAll(pageRequest).getContent();
        List<byte[]> previews = new ArrayList<>();
            for(Avatar avatar : avatars){
                //byte[] previewBytes = createPreview(avatar.getData());
                BufferedImage previewImage = createPreview(avatar.getData());
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ImageIO.write(previewImage, "jpg", baos);
                byte[] previewBytes = baos.toByteArray();

                previews.add(previewBytes);
            }

       return previews;
    };



    public BufferedImage createPreview(byte[] avatar) throws IOException {

                //byte[] originalAvatar = avatar.getData();
                ByteArrayInputStream bais = new ByteArrayInputStream(avatar);
                BufferedImage avatarImage = ImageIO.read(bais);
                BufferedImage preview = new BufferedImage(100, 100, avatarImage.getType());
                Graphics2D g2d = preview.createGraphics();
                g2d.drawImage(avatarImage, 0, 0, 100, 100, null);
                g2d.dispose();
               // ByteArrayOutputStream baos = new ByteArrayOutputStream();

                //ImageIO.write(preview, "jpg", baos);

                //previews.add(baos.toByteArray());


        return preview;
    }
}

