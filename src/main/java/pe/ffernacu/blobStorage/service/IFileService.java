package pe.ffernacu.blobStorage.service;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface IFileService {
    public String readBlobFileService(String fileName) throws IOException;
    public String writeBlobFileService(String fileName) throws IOException;
    public String downloadBlobFileService(String fileName) throws IOException;
    public void UploadFileService(MultipartFile file) throws IOException;

}
