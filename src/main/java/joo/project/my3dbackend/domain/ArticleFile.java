package joo.project.my3dbackend.domain;

import io.hypersistence.utils.hibernate.type.json.JsonBinaryType;
import lombok.*;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.springframework.data.domain.Persistable;

import javax.persistence.*;
import java.util.Objects;

@Getter
@ToString(callSuper = true)
@Table(name = "article_file")
@Entity
@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ArticleFile implements Persistable<Long> {
    @Id
    @SequenceGenerator(name = "SEQ_GENERATOR", sequenceName = "seq_article_file", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_GENERATOR")
    @EqualsAndHashCode.Include
    private Long id;

    @Column(nullable = false)
    private Long byteSize; // 파일 용량

    @Column(nullable = false)
    private String originalFileName; // 사용자가 업로드한 파일명 (확장자 포함)

    @Column(nullable = false)
    private String fileName; // 서버에 저장한 파일명 (확장자 포함)

    @Column(nullable = false)
    private String fileExtension; // 파일 확장자 (e.g. stp)

    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb")
    private DimensionOption dimensionOption; // 치수 정보

    @ToString.Exclude
    @OneToOne(mappedBy = "articleFile")
    private Article article;

    public ArticleFile(
            Long byteSize,
            String originalFileName,
            String fileName,
            String fileExtension,
            DimensionOption dimensionOption) {
        this.byteSize = byteSize;
        this.originalFileName = originalFileName;
        this.fileName = fileName;
        this.fileExtension = fileExtension;
        this.dimensionOption = dimensionOption;
    }

    public static ArticleFile of(
            Long byteSize,
            String originalFileName,
            String fileName,
            String fileExtension,
            DimensionOption dimensionOption) {
        return new ArticleFile(byteSize, originalFileName, fileName, fileExtension, dimensionOption);
    }

    @Override
    public boolean isNew() {
        return Objects.isNull(this.id);
    }
}
