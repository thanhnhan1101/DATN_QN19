package poly.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "ThichBinhLuan")
public class ThichBinhLuan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MaThich")
    private Long maThich;
    
    @ManyToOne
    @JoinColumn(name = "MaNguoiDung")
    private NguoiDung nguoiDung;
    
    @ManyToOne
    @JoinColumn(name = "MaBinhLuan")
    private BinhLuan binhLuan;
    
    @Column(name = "NgayThich", columnDefinition = "DATETIME DEFAULT GETDATE()")
    private LocalDateTime ngayThich;

    @PrePersist
    protected void onCreate() {
        ngayThich = LocalDateTime.now();
    }
}
