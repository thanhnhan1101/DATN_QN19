package poly.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "LichSuChinhSua")
public class LichSuChinhSua {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MaLichSu")
    private Long maLichSu;

    @ManyToOne
    @JoinColumn(name = "MaBaiViet", foreignKey = @ForeignKey(name = "FK_LichSuChinhSua_BaiViet"))
    private BaiViet baiViet;

    @ManyToOne
    @JoinColumn(name = "MaNguoiSua", foreignKey = @ForeignKey(name = "FK_LichSuChinhSua_NguoiDung"))
    private NguoiDung nguoiSua;

    @Column(name = "NgayChinhSua", columnDefinition = "DATETIME DEFAULT GETDATE()")
    private LocalDateTime ngayChinhSua;

    @Column(name = "NoiDungCu", columnDefinition = "NVARCHAR(MAX)")
    private String noiDungCu;

    @PrePersist
    protected void onCreate() {
        if (ngayChinhSua == null) {
            ngayChinhSua = LocalDateTime.now();
        }
    }
}