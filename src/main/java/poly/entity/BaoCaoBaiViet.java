package poly.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "BaoCaoBaiViet")
public class BaoCaoBaiViet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MaBaoCao")
    private Long maBaoCao;

    @ManyToOne
    @JoinColumn(name = "MaBaiViet", foreignKey = @ForeignKey(name = "FK_BaoCaoBaiViet_BaiViet"))
    private BaiViet baiViet;

    @ManyToOne
    @JoinColumn(name = "MaNguoiBaoCao", foreignKey = @ForeignKey(name = "FK_BaoCaoBaiViet_NguoiDung"))
    private NguoiDung nguoiBaoCao;

    @Column(name = "LyDo", columnDefinition = "NVARCHAR(200)")
    private String lyDo;

    @Column(name = "NgayBaoCao", columnDefinition = "DATETIME DEFAULT GETDATE()")
    private LocalDateTime ngayBaoCao;

    @Column(name = "TrangThai", columnDefinition = "NVARCHAR(20) DEFAULT N'Chưa xử lý'")
    private String trangThai = "Chưa xử lý";

    @PrePersist
    protected void onCreate() {
        ngayBaoCao = LocalDateTime.now();
    }
}
