package poly.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "ThongBao")
public class ThongBao {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MaThongBao")
    private Long maThongBao;

    @Column(name = "TieuDe", columnDefinition = "NVARCHAR(255)")
    private String tieuDe;

    @Column(name = "NoiDung", columnDefinition = "NVARCHAR(100)")
    private String noiDung;

    @ManyToOne
    @JoinColumn(name = "MaNguoiNhan", foreignKey = @ForeignKey(name = "FK_ThongBao_NguoiDung"))
    private NguoiDung nguoiNhan;

    @Column(name = "TrangThai", columnDefinition = "NVARCHAR(20) DEFAULT N'Chưa đọc'")
    private String trangThai = "Chưa đọc";

    @Column(name = "NgayGui", columnDefinition = "DATETIME DEFAULT GETDATE()")
    private LocalDateTime ngayGui;

    @PrePersist
    protected void onCreate() {
        ngayGui = LocalDateTime.now();
    }
}