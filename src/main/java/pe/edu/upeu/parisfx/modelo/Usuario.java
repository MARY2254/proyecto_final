package pe.edu.upeu.parisfx.modelo;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "upeu_usuario")
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario")
    private Long idUsuario;

    @Column(name = "nombre", nullable = false)
    @Size(max = 30)
    private String nombre;

    @Column(name = "apellido", nullable = false)
    @Size(max = 30)
    private String apellido;

    @Size(max = 8)
    @Column(name = "dni",nullable = false)
    private String dni;

    @Column(name = "user", nullable = false, unique = true, length = 20)
    private String user;

    @Size(max = 9)
    @Column(name = "telefono",nullable = false)
    private String telf;


    @Column(name = "clave", nullable = false, length = 100)
    private String clave;

    @JoinColumn(name = "id_perfil", referencedColumnName = "id_perfil")
    @ManyToOne(optional = false)
    private Perfil Perfil;
}

