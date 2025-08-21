package poly.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import poly.entity.DanhMuc;

import java.util.List;

@Repository
public interface DanhMucRepository extends JpaRepository<DanhMuc, Integer> {
    DanhMuc findByDuongDan(String duongDan);
    
    @Query("SELECT MAX(d.thuTuHienThi) FROM DanhMuc d")
    Long findMaxThuTuHienThi();
    
    List<DanhMuc> findByThuTuHienThiGreaterThanEqual(Integer thuTuHienThi);
    
    @Modifying
    @Query("UPDATE DanhMuc d SET d.thuTuHienThi = d.thuTuHienThi + 1 " +
           "WHERE d.thuTuHienThi >= :start AND d.thuTuHienThi <= :end")
    void increaseOrderForRange(@Param("start") Integer start, @Param("end") Integer end);
    
    @Modifying
    @Query("UPDATE DanhMuc d SET d.thuTuHienThi = d.thuTuHienThi - 1 " +
           "WHERE d.thuTuHienThi >= :start AND d.thuTuHienThi <= :end")
    void decreaseOrderForRange(@Param("start") Integer start, @Param("end") Integer end);
}