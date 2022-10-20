package pe.ffernacu.blobStorage.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import pe.ffernacu.blobStorage.config.AsyncExecutorConfig;
import pe.ffernacu.blobStorage.dao.IFileDirectoryDAO;
import pe.ffernacu.blobStorage.model.FileDirectory;
import pe.ffernacu.blobStorage.service.IFileDirectory;

@Service
@AllArgsConstructor
public class FileDirectoryImpl implements IFileDirectory {

    private final IFileDirectoryDAO iFileDirectoryDAO;
    //withcontructor = @AllArgsConstructor
    /*public FileDirectoryImpl(IFileDirectoryDAO iFileDirectoryDAO) {
        this.iFileDirectoryDAO = iFileDirectoryDAO;
    }*/
    @Async(AsyncExecutorConfig.APP_ASYNC_EXECUTOR)
    @Override
    public FileDirectory saveFileDirectory(String fileDirectory) {
        return iFileDirectoryDAO.save(
                FileDirectory.builder()
                        .uri(fileDirectory)
                        .build()
        );
    }
}
