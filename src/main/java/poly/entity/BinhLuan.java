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
    @JoinColumn(name = "MaBaiViet")
    private BaiViet baiViet;

    @ManyToOne
    @JoinColumn(name = "MaNguoiDung")
    private NguoiDung nguoiDung;

    @Column(name = "NoiDung", nullable = false, length = 100)
    private String noiDung;

    @Column(name = "TrangThai", length = 15)
    private String trangThai = "Chờ duyệt";

    @Column(name = "NgayTao")
    private LocalDateTime ngayTao;

    @PrePersist
    protected void onCreate() {
        ngayTao = LocalDateTime.now();
    }
}
