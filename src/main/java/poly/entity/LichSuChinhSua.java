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
    @JoinColumn(name = "MaBaiViet")
    private BaiViet baiViet;

    @ManyToOne
    @JoinColumn(name = "MaNguoiSua")
    private NguoiDung nguoiSua;

    @Column(name = "NgayChinhSua")
    private LocalDateTime ngayChinhSua = LocalDateTime.now();

    @Column(name = "NoiDungCu", columnDefinition = "NVARCHAR(MAX)")
    private String noiDungCu;
}