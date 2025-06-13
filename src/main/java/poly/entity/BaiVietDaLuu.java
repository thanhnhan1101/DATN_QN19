package poly.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "BaiVietDaLuu")
public class BaiVietDaLuu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MaLuu")
    private Long maLuu;
    
    @ManyToOne
    @JoinColumn(name = "MaNguoiDung")
    private NguoiDung nguoiDung;
    
    @ManyToOne
    @JoinColumn(name = "MaBaiViet")
    private BaiViet baiViet;
    
    @Column(name = "NgayLuu", columnDefinition = "DATETIME DEFAULT GETDATE()")
    private LocalDateTime ngayLuu;

    @PrePersist
    protected void onCreate() {
        ngayLuu = LocalDateTime.now();
    }
}
