package io.bootify.compu_word.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import java.time.OffsetDateTime;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;


@Entity
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
public class Empleado {

    @Id
    @Column(nullable = false, updatable = false)
    private String nombre;

    @Column
    private Integer id;

    @Column
    private String correoElectronico;

    @Column
    private String departament;

    @OneToOne(mappedBy = "permanente", fetch = FetchType.LAZY)
    private Permanente empleado;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "empleado1id")
    private Empleado empleado1;

    @OneToMany(mappedBy = "empleado1")
    private Set<Empleado> departamento;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "empleado2id", nullable = false, unique = true)
    private ReporteDesempeno empleado2;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private OffsetDateTime dateCreated;

    @LastModifiedDate
    @Column(nullable = false)
    private OffsetDateTime lastUpdated;

}
