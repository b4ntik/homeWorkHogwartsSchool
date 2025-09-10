package ru.hogwarts.school;
import org.springframework.core.io.ByteArrayResource;

public class MultipartByteArrayResource extends ByteArrayResource {
    private final String filename;

    public MultipartByteArrayResource(byte[] byteArray, String filename) {
        super(byteArray);
        this.filename = filename;
    }

    @Override
    public String getFilename() {
        return filename;
    }
}