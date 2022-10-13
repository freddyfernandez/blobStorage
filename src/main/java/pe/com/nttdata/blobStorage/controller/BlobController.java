package pe.com.nttdata.blobStorage.controller;

import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.WritableResource;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;

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
    @Autowired
    private ResourceLoader resourceLoader;
    private Resource blobFile;

    @GetMapping(value="${aadi.esb.api.read-blob-file.endpoint}")
    public String readBlobFile(@PathVariable String fileName) throws IOException {
        this.blobFile=resourceLoader.getResource(uriFile+fileName);
        return StreamUtils.copyToString(
                this.blobFile.getInputStream(),
                Charset.defaultCharset());
    }

    @PostMapping("/writeBlobFile")
    public String writeBlobFile(@RequestBody String data) throws IOException {
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
}