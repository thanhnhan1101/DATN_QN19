package poly.entity;

import jakarta.persistence.*;
import lombok.Data;
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
    private String trangThai = "Chờ duyệt";

    @Column(name = "NgayTao")
    private LocalDateTime ngayTao;

    @PrePersist
    protected void onCreate() {
        if (ngayTao == null) {
            ngayTao = LocalDateTime.now();
        }
    }
}
