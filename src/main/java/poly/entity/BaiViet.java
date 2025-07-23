package poly.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "BaiViet")
public class BaiViet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MaBaiViet")
    private Long maBaiViet;

    @Column(name = "TieuDe", nullable = false, columnDefinition = "NVARCHAR(100)")
    private String tieuDe;

    @Column(name = "TomTat", columnDefinition = "NVARCHAR(500)")
    private String tomTat;

    @Column(name = "NoiDung", nullable = false, columnDefinition = "NVARCHAR(MAX)")
    private String noiDung;

    @Column(name = "DuongDanAnh1", columnDefinition = "NVARCHAR(MAX)")
    private String duongDanAnh1;

    @Column(name = "DuongDanAnh2", columnDefinition = "NVARCHAR(MAX)")
    private String duongDanAnh2;

    @Column(name = "DuongDanAnh3", columnDefinition = "NVARCHAR(MAX)")
    private String duongDanAnh3;

    @ManyToOne
    @JoinColumn(name = "MaDanhMuc", foreignKey = @ForeignKey(name = "FK_BaiViet_DanhMuc"))
    private DanhMuc danhMuc;

    @ManyToOne
    @JoinColumn(name = "MaTacGia", foreignKey = @ForeignKey(name = "FK_BaiViet_TacGia"))
    private NguoiDung tacGia;

    @Column(name = "TrangThai", columnDefinition = "NVARCHAR(15) DEFAULT N'Nháp'")
    private String trangThai = "Nháp";

    @Column(name = "NgayXuatBan")
    private LocalDateTime ngayXuatBan;

    @Column(name = "LuotXem", columnDefinition = "INT DEFAULT 0")
    private Integer luotXem = 0;

    @Column(name = "NgayTao", columnDefinition = "DATETIME DEFAULT GETDATE()")
    private LocalDateTime ngayTao;

    @Column(name = "NgayCapNhat", columnDefinition = "DATETIME DEFAULT GETDATE()")
    private LocalDateTime ngayCapNhat;

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