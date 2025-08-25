package poly.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import poly.entity.LichSuDangNhap;
import poly.entity.NguoiDung;

public interface LichSuDangNhapRepository extends JpaRepository<LichSuDangNhap, Long> {
    List<LichSuDangNhap> findByNguoiDungOrderByThoiGianDesc(NguoiDung nguoiDung);
}