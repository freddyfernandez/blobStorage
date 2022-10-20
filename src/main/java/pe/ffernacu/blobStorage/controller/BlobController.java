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
import pe.ffernacu.blobStorage.model.FileDirectory;
import pe.ffernacu.blobStorage.service.IFileDirectory;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("blob")
@Slf4j
public class BlobController {
    @Value("${aadi.cloud.api.storage.endpoint}")
    private String uriStorage;
    @Value("${aadi.cloud.api.storage.container.name}")
    private String containerName;
    @Value("${aadi.cloud.api.storage.blob.endpoint}")
    private String uriFile;
    @Value("${aadi.cloud.api.storage.connection-string}")
    private String myConnectionString;
    private ResourceLoader resourceLoader;
    private Resource blobFile;

    private final IFileDirectory iFileDirectory;

    public BlobController(IFileDirectory iFileDirectory) {
        this.iFileDirectory = iFileDirectory;
    }

    @GetMapping(value="${aadi.esb.api.read-blob-file.endpoint}")
    public String readBlobFile(@PathVariable String fileName) throws IOException {
        this.blobFile=resourceLoader.getResource(uriFile+fileName);
        return StreamUtils.copyToString(
                this.blobFile.getInputStream(),
                Charset.defaultCharset());
    }

    @PostMapping("/writeBlobFile")
    public String writeBlobFile(@RequestBody @NotNull String data) throws IOException {
        try (OutputStream os = ((WritableResource) this.blobFile).getOutputStream()) {
            os.write(data.getBytes());
        }
        return "file was updated";
    }

    @GetMapping(value = "${aadi.esb.api.storage-download.endpoint}")
    public String downloadBlobFile(@PathVariable String fileName) throws IOException {
        BlobServiceClient storageClient = new BlobServiceClientBuilder()
                .endpoint(uriStorage)
                .buildClient();
        BlobClient blobClient = storageClient.getBlobContainerClient(this.containerName).getBlobClient(fileName);
        if (blobClient != null) {
                String filePath="C:\\temp\\" + blobClient.getBlobName();
                try {
                    FileUtils.forceMkdirParent(new File(filePath));
                    blobClient.downloadToFile(filePath);
                }
                catch (Exception e){
                    log.info("{} Archivo existente ", blobClient.getBlobName());
                    return "Archivo existente";
                }
        }
        return "descarga exitosa";
    }

    @PostMapping(path = "${aadi.esb.api.storage-upload.endpoint}")
    public String UploadFile(@RequestParam("file") MultipartFile file) throws IOException{
        BlobServiceClient storageClient = new BlobServiceClientBuilder()
                .endpoint(uriStorage).connectionString(myConnectionString)
                .buildClient();
        BlobClient blobClient = storageClient.getBlobContainerClient(this.containerName).getBlobClient(file.getOriginalFilename());//convierte tipo multipart a string
        blobClient.upload(file.getInputStream(),file.getSize(),true);//si ya existe, se sobreescribe los segmentos modificados
        iFileDirectory.saveFileDirectory(uriFile+file.getOriginalFilename());
        return "ok";
    }

}