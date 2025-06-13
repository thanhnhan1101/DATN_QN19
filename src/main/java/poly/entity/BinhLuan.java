package poly.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "BinhLuan")
public class BinhLuan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MaBinhLuan")
    private Long maBinhLuan;
    
    @ManyToOne
    @JoinColumn(name = "MaNguoiDung")
    private NguoiDung nguoiDung;
    
    @ManyToOne
    @JoinColumn(name = "MaBaiViet")
    private BaiViet baiViet;
    
    @Column(name = "NoiDung", columnDefinition = "NVARCHAR(MAX)")
    private String noiDung;
    
    @ManyToOne
    @JoinColumn(name = "MaBinhLuanCha", foreignKey = @ForeignKey(name = "FK_BinhLuan_Cha"))
    private BinhLuan binhLuanCha;
    
    @Column(name = "NgayBinhLuan", columnDefinition = "DATETIME DEFAULT GETDATE()")
    private LocalDateTime ngayBinhLuan;

    @PrePersist
    protected void onCreate() {
        ngayBinhLuan = LocalDateTime.now();
    }
}
