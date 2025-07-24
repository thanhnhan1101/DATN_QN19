package poly.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "DanhMuc")
public class DanhMuc {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MaDanhMuc")
    private Integer maDanhMuc;

    @Column(name = "TenDanhMuc", nullable = false, length = 25)
    private String tenDanhMuc;

    @Column(name = "MoTa", length = 255)
    private String moTa;

    @Column(name = "ThuTuHienThi")
    private Integer thuTuHienThi = 0;

    @Column(name = "DuongDan", length = 10, nullable = false)
    private String duongDan = "";
}