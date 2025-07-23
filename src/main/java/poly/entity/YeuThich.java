package poly.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(
    name = "YeuThich",
    uniqueConstraints = @UniqueConstraint(columnNames = {"MaNguoiDung", "MaBaiViet"})
)
public class YeuThich {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MaYeuThich")
    private Long maYeuThich;

    @ManyToOne
    @JoinColumn(name = "MaNguoiDung", foreignKey = @ForeignKey(name = "FK_YeuThich_NguoiDung"))
    private NguoiDung nguoiDung;

    @ManyToOne
    @JoinColumn(name = "MaBaiViet", foreignKey = @ForeignKey(name = "FK_YeuThich_BaiViet"))
    private BaiViet baiViet;

    @Column(name = "TrangThai", columnDefinition = "NVARCHAR(20) DEFAULT N'Đã thích'")
    private String trangThai = "Đã thích";

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
