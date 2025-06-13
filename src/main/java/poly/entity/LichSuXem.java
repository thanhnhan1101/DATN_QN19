package poly.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "LichSuXem")
public class LichSuXem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MaLichSu")
    private Long maLichSu;
    
    @ManyToOne
    @JoinColumn(name = "MaNguoiDung", foreignKey = @ForeignKey(name = "FK_LichSuXem_NguoiDung"))
    private NguoiDung nguoiDung;
    
    @ManyToOne
    @JoinColumn(name = "MaBaiViet", foreignKey = @ForeignKey(name = "FK_LichSuXem_BaiViet"))
    private BaiViet baiViet;
    
    @Column(name = "ThoiGianXem", columnDefinition = "DATETIME DEFAULT GETDATE()")
    private LocalDateTime thoiGianXem;

    @PrePersist
    protected void onCreate() {
        thoiGianXem = LocalDateTime.now();
    }
}