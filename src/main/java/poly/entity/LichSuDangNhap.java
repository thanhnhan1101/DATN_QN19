package poly.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "LichSuDangNhap")
public class LichSuDangNhap {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MaLichSu")
    private Long maLichSu;

    @ManyToOne
    @JoinColumn(name = "MaNguoiDung", foreignKey = @ForeignKey(name = "FK_LichSuDangNhap_NguoiDung"))
    private NguoiDung nguoiDung;

    @Column(name = "DiaChiIP", columnDefinition = "NVARCHAR(50)")
    private String diaChiIP;

    @Column(name = "ThietBi", columnDefinition = "NVARCHAR(100)")
    private String thietBi;

    @Column(name = "ThoiGian", columnDefinition = "DATETIME DEFAULT GETDATE()")
    private LocalDateTime thoiGian;

    @PrePersist
    protected void onCreate() {
        thoiGian = LocalDateTime.now();
    }
}