package pe.ffernacu.blobStorage.model;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@Entity
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class FileDirectory {
    @Id
    @SequenceGenerator(
            name = "file_directory_id_sequence",
            sequenceName = "file_directory_id_sequence"
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "file_directory_id_sequence"
    )
    private Integer id;
    private String uri;


}
