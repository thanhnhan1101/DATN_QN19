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
    private Integer maBaiViet;
    
    @Column(name = "TieuDe", nullable = false, columnDefinition = "NVARCHAR(255)")
    private String tieuDe;
    
    @Column(name = "DuongDan", nullable = false, length = 255, unique = true)
    private String duongDan;
    
    @Column(name = "TomTat", columnDefinition = "NVARCHAR(500)")
    private String tomTat;
    
    @Column(name = "NoiDung", nullable = false, columnDefinition = "NVARCHAR(MAX)")
    private String noiDung;
    
    @Column(name = "HinhAnh", columnDefinition = "VARCHAR(MAX)")
    private String hinhAnh;
    
    @Column(name = "LuotXem", columnDefinition = "INT DEFAULT 0")
    private Integer luotXem = 0;
    
    @Column(name = "TrangThai", columnDefinition = "NVARCHAR(20) DEFAULT N'Nháp'")
    private String trangThai = "Nháp";
    
    @Column(name = "NgayDang")
    private LocalDateTime ngayDang;
    
    @Column(name = "NgayTao", columnDefinition = "DATETIME DEFAULT GETDATE()")
    private LocalDateTime ngayTao;
    
    @Column(name = "NgaySua")
    private LocalDateTime ngaySua;
    
    @ManyToOne
    @JoinColumn(name = "MaDanhMuc", foreignKey = @ForeignKey(name = "FK_BaiViet_DanhMuc"))
    private DanhMuc danhMuc;
    
    @ManyToOne
    @JoinColumn(name = "MaTacGia", foreignKey = @ForeignKey(name = "FK_BaiViet_TacGia"))
    private NguoiDung tacGia;

    @PrePersist
    protected void onCreate() {
        ngayTao = LocalDateTime.now();
    }
}