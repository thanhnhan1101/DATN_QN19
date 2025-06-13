package poly.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "QuangCao")
public class QuangCao {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MaQuangCao")
    private Integer maQuangCao;

    @Column(name = "TenQuangCao", nullable = false, columnDefinition = "NVARCHAR(255)")
    private String tenQuangCao;

    @Column(name = "HinhAnh", length = 255)
    private String hinhAnh;

    @Column(name = "DuongDan", length = 500)
    private String duongDan;

    @Column(name = "ViTri", columnDefinition = "NVARCHAR(50)")
    private String viTri;

    @Column(name = "KichThuoc", columnDefinition = "NVARCHAR(20)")
    private String kichThuoc;

    @Column(name = "NgayBatDau")
    private LocalDateTime ngayBatDau;

    @Column(name = "NgayKetThuc")
    private LocalDateTime ngayKetThuc;

    @Column(name = "LuotClick", columnDefinition = "INT DEFAULT 0")
    private Integer luotClick = 0;

    @Column(name = "DuongDanURL", length = 500)
    private String duongDanURL;

    @Column(name = "TrangThai", columnDefinition = "BIT DEFAULT 1")
    private Boolean trangThai = true;

    @Column(name = "GiaTheoNgay", columnDefinition = "DECIMAL(10,2)")
    private BigDecimal giaTheoNgay;

    @Column(name = "NhaTaiTro", columnDefinition = "NVARCHAR(255)")
    private String nhaTaiTro;

    @Column(name = "NgayTao", columnDefinition = "DATETIME DEFAULT GETDATE()")
    private LocalDateTime ngayTao;

    @PrePersist
    protected void onCreate() {
        ngayTao = LocalDateTime.now();
    }
}
