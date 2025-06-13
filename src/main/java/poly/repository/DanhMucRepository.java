package poly.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import poly.entity.DanhMuc;
import java.util.List;

public interface DanhMucRepository extends JpaRepository<DanhMuc, Integer> {
    List<DanhMuc> findByDanhMucChaIsNullOrderByTenDanhMuc();
    DanhMuc findByDuongDan(String duongDan);
}