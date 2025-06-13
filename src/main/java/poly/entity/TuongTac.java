package poly.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "TuongTac")
public class TuongTac {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MaTuongTac")
    private Long maTuongTac;
    
    @ManyToOne
    @JoinColumn(name = "MaNguoiDung")
    private NguoiDung nguoiDung;
    
    @ManyToOne
    @JoinColumn(name = "MaBaiViet")
    private BaiViet baiViet;
    
    @Column(name = "LoaiTuongTac", columnDefinition = "NVARCHAR(20)")
    private String loaiTuongTac;  // 'like', 'dislike', 'share_email', 'share_fb'
    
    @Column(name = "ThoiGian", columnDefinition = "DATETIME DEFAULT GETDATE()")
    private LocalDateTime thoiGian;

    @PrePersist
    protected void onCreate() {
        thoiGian = LocalDateTime.now();
    }
}
