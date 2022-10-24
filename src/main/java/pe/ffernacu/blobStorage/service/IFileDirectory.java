package pe.ffernacu.blobStorage.service;

import pe.ffernacu.blobStorage.dao.IFileDirectoryDAO;
import pe.ffernacu.blobStorage.model.FileDirectory;

public interface IFileDirectory {

    public  FileDirectory saveFileDirectory(String fileDirectory);


}
