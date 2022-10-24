
package pe.ffernacu.blobStorage.service.impl;

import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.WritableResource;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;
import org.springframework.web.multipart.MultipartFile;
import pe.ffernacu.blobStorage.config.AsyncExecutorConfig;
import pe.ffernacu.blobStorage.config.BlobStorageConfig;
import pe.ffernacu.blobStorage.service.IFileService;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
@Service
@Slf4j
@AllArgsConstructor// no puede instanciar constructores para variables primitivas ni extends en metodos implement ni controller
public class FileServiceImpl implements IFileService {
    private final ResourceLoader resourceLoader;
    private final BlobStorageConfig blobStorageConfig;
    @Override
    public String readBlobFileService(String fileName) throws IOException {
        //blobFile no se puede instanciar como constructor por ser tipo extend
        Resource blobFile = resourceLoader.getResource(blobStorageConfig.getUriFile() + fileName);
        return StreamUtils.copyToString(
                blobFile.getInputStream(),
                Charset.defaultCharset());
    }

    @Override
    public String writeBlobFileService(String fileName) throws IOException {
        Resource blobFile = resourceLoader.getResource(blobStorageConfig.getUriFile() + fileName);
        try (OutputStream os = ((WritableResource) blobFile).getOutputStream()) {
            os.write(fileName.getBytes());
        }
        return "file was updated";
    }
    @Override
    public String downloadBlobFileService(String fileName) throws IOException {
        BlobServiceClient storageClient = new BlobServiceClientBuilder()
                .endpoint(blobStorageConfig.getUriStorage())
                .buildClient();
        BlobClient blobClient = storageClient.getBlobContainerClient(blobStorageConfig.getContainerName()).getBlobClient(fileName);
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
    @Async(AsyncExecutorConfig.APP_ASYNC_EXECUTOR)
    @Override
    public void UploadFileService(MultipartFile file) throws IOException {
        BlobServiceClient storageClient = new BlobServiceClientBuilder()
                .endpoint(blobStorageConfig.getUriStorage()).connectionString(blobStorageConfig.getMyConnectionString())
                .buildClient();
        BlobClient blobClient = storageClient.getBlobContainerClient(blobStorageConfig.getContainerName()).getBlobClient(file.getOriginalFilename());//convierte tipo multipart a string
        blobClient.upload(file.getInputStream(),file.getSize(),true);//si ya existe, se sobreescribe los segmentos modificados
    }
}
