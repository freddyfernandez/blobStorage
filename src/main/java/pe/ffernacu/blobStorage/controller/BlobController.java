package pe.ffernacu.blobStorage.controller;

import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.WritableResource;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pe.ffernacu.blobStorage.service.IFileDirectory;
import pe.ffernacu.blobStorage.service.IFileService;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;

@RestController
@RequestMapping("blob")
@Slf4j
@AllArgsConstructor
public class BlobController {
    private final IFileDirectory iFileDirectory;
    private final IFileService iFileService;

    /*public BlobController(IFileDirectory iFileDirectory,IFileService iFileService) {
        this.iFileDirectory = iFileDirectory;
        this.iFileService= iFileService;
    }*/

    @GetMapping(value="${aadi.esb.api.read-blob-file.endpoint}")
    public String readBlobFile(@PathVariable String fileName) throws IOException {
        return iFileService.readBlobFileService(fileName);
    }

    @PostMapping("/writeBlobFile")
    public String writeBlobFile(@RequestBody @NotNull String fileName) throws IOException {
        return iFileService.writeBlobFileService(fileName);
    }

    @GetMapping(value = "${aadi.esb.api.storage-download.endpoint}")
    public String downloadBlobFile(@PathVariable String fileName) throws IOException {
        return iFileService.downloadBlobFileService(fileName);
    }

    @PostMapping(path = "${aadi.esb.api.storage-upload.endpoint}")
    public String UploadFile(@RequestParam("file") final MultipartFile file) throws IOException{
        file.getInputStream();
        iFileService.UploadFileService(file);
        iFileDirectory.saveFileDirectory(file.getOriginalFilename());
        return "ok";
    }
}