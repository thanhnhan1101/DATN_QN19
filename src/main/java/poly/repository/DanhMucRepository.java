package poly.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import poly.entity.DanhMuc;

public interface DanhMucRepository extends JpaRepository<DanhMuc, Integer> {
    DanhMuc findByDuongDan(String duongDan);
}