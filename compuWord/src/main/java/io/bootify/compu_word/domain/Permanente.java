package io.bootify.compu_word.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;


@Entity
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
public class Permanente {

    @Id
    @Column(nullable = false, updatable = false)
    private String contratoAnual;

    @Column
    private Integer sueldo;

    @Column
    private LocalDateTime fechaIngreso;

    @Column
    private String cargo;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "permanente_id", nullable = false, unique = true)
    private Empleado permanente;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private OffsetDateTime dateCreated;

    @LastModifiedDate
    @Column(nullable = false)
    private OffsetDateTime lastUpdated;

}
