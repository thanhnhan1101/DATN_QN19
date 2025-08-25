package poly.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "QuangCao")
public class QuangCao {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MaQuangCao")
    private Long maQuangCao;

    @ManyToOne
    @JoinColumn(name = "MaNguoiDung")
    private NguoiDung nguoiDung;

    @Column(name = "TieuDe", length = 255)
    private String tieuDe;

    @Lob
    @Column(name = "DuongDanAnh")
    private String duongDanAnh;

    @Lob
    @Column(name = "DuongDanLienKet")
    private String duongDanLienKet;

    @Column(name = "ViTri", length = 20)
    private String viTri;

    @Column(name = "NgayBatDau")
    private LocalDate ngayBatDau;

    @Column(name = "NgayKetThuc")
    private LocalDate ngayKetThuc;

    @Column(name = "TrangThai", length = 20)
    private String trangThai;

    @Column(name = "NgayTao")
    private LocalDateTime ngayTao;

    @Column(name = "TenDoanhNghiep", length = 255)
    private String tenDoanhNghiep;

    @Column(name = "Email", length = 100)
    private String email;

    @Column(name = "SoDienThoai", length = 15)
    private String soDienThoai;

    @Column(name = "KichThuocAnh", length = 20)
    private String kichThuocAnh;

    @Column(name = "ChiPhi", precision = 18, scale = 2)
    private BigDecimal chiPhi;

    @Column(name = "LuotHienThi")
    private Integer luotHienThi;

    @Column(name = "LuotClick")
    private Integer luotClick;

    @Column(name = "LyDoTuChoi", length = 255)
    private String lyDoTuChoi;

    @Column(name = "NgayCapNhat")
    private LocalDateTime ngayCapNhat;

    @Lob
    @Column(name = "HoaDon", columnDefinition = "NVARCHAR(MAX)")
    private String hoaDon;

    @PrePersist
    protected void onCreate() {
        ngayTao = LocalDateTime.now();
        ngayCapNhat = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        ngayCapNhat = LocalDateTime.now();
    }
}