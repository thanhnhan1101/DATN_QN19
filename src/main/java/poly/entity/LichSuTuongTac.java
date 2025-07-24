package poly.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "LichSuTuongTac")
public class LichSuTuongTac {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MaTuongTac")
    private Long maTuongTac;

    @ManyToOne
    @JoinColumn(name = "MaNguoiDung", foreignKey = @ForeignKey(name = "FK_LichSuTuongTac_NguoiDung"))
    private NguoiDung nguoiDung;

    @ManyToOne
    @JoinColumn(name = "MaBaiViet", foreignKey = @ForeignKey(name = "FK_LichSuTuongTac_BaiViet"))
    private BaiViet baiViet;

    @Column(name = "LoaiTuongTac", columnDefinition = "NVARCHAR(15)")
    private String loaiTuongTac; // 'Xem', 'Thích', 'Bình luận'

    @Column(name = "NgayTuongTac", columnDefinition = "DATETIME DEFAULT GETDATE()")
    private LocalDateTime ngayTuongTac;

    @PrePersist
    protected void onCreate() {
        if (ngayTuongTac == null) {
            ngayTuongTac = LocalDateTime.now();
        }
    }
}