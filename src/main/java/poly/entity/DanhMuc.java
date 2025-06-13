package poly.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "DanhMuc")
public class DanhMuc {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MaDanhMuc")
    private Integer maDanhMuc;
    
    @Column(name = "TenDanhMuc", nullable = false, columnDefinition = "NVARCHAR(100)")
    private String tenDanhMuc;
    
    @Column(name = "DuongDan", nullable = false, columnDefinition = "VARCHAR(100)")
    private String duongDan;
    
    @Column(name = "MoTa", columnDefinition = "NVARCHAR(500)")
    private String moTa;
    
    @ManyToOne
    @JoinColumn(name = "MaDanhMucCha", foreignKey = @ForeignKey(name = "FK_DanhMuc_Cha"))
    private DanhMuc danhMucCha;
    
    @Column(name = "NgayTao", columnDefinition = "DATETIME DEFAULT GETDATE()")
    private LocalDateTime ngayTao;

    @PrePersist
    protected void onCreate() {
        ngayTao = LocalDateTime.now();
    }
}