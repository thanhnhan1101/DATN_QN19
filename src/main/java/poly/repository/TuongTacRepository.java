package poly.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import poly.entity.TuongTac;

public interface TuongTacRepository extends JpaRepository<TuongTac, Long> {
    List<TuongTac> findByNguoiDung_MaNguoiDung(Long nguoiDungId);
    List<TuongTac> findByBaiViet_MaBaiViet(Integer baiVietId);
}
