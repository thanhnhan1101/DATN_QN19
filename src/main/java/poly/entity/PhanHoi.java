package poly.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "PhanHoi")
public class PhanHoi {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MaPhanHoi")
    private Long maPhanHoi;

    @ManyToOne
    @JoinColumn(name = "MaNguoiDung", foreignKey = @ForeignKey(name = "FK_PhanHoi_NguoiDung"))
    private NguoiDung nguoiDung;

    @Column(name = "NoiDung", columnDefinition = "NVARCHAR(255)")
    private String noiDung;

    @Column(name = "TrangThai", columnDefinition = "NVARCHAR(20) DEFAULT N'Chưa xử lý'")
    private String trangThai = "Chưa xử lý";

    @Column(name = "NgayGui", columnDefinition = "DATETIME DEFAULT GETDATE()")
    private LocalDateTime ngayGui;

    @PrePersist
    protected void onCreate() {
        ngayGui = LocalDateTime.now();
    }
}