package pe.ffernacu.blobStorage.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.ffernacu.blobStorage.model.FileDirectory;

public interface IFileDirectoryDAO extends JpaRepository<FileDirectory,Integer> {
}
