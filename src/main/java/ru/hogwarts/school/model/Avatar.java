package ru.hogwarts.school.model;

import jakarta.persistence.*;

@Entity
public class Avatar {
    @Id
    @Column
    private Long id;
    @Column
    private String filePath;
    @Column
    private long fileSize;
    @Column
    private String mediaType;

    @Lob
    @Column(name = "data")
    private byte[] data;


    @OneToOne
    @MapsId
    @JoinColumn(name = "student_id")
    private Student student;


    public void setStudent(Student student) {
        this.student = student;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }

    public void setMediaType(String mediaType) {
        this.mediaType =mediaType;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public Long getId() {
        return id;
    }

    public String getFilePath() {
        return filePath;
    }

    public long getFileSize() {
        return fileSize;
    }

    public String getMediaType() {
        return mediaType;
    }

    public byte[] getData() {
        return data;
    }

    public Student getStudent() {
        return student;
    }
}
