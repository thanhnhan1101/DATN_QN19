package poly.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "LichSuDangNhap")
public class LichSuDangNhap {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MaLichSu")
    private Long maLichSu;

    @ManyToOne
    @JoinColumn(name = "MaNguoiDung")
    private NguoiDung nguoiDung;

    @Column(name = "DiaChiIP")
    private String diaChiIP;

    @Column(name = "ThietBi")
    private String thietBi;

    @Column(name = "ThoiGian")
    private LocalDateTime thoiGian;

    @PrePersist
    public void prePersist() {
        if (thoiGian == null) thoiGian = LocalDateTime.now();
    }
}