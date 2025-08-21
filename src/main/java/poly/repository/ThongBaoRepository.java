package poly.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import poly.entity.ThongBao;
import java.util.List;

public interface ThongBaoRepository extends JpaRepository<ThongBao, Long> {
    
    List<ThongBao> findByNguoiNhanMaNguoiDungOrderByNgayGuiDesc(Long maNguoiDung);
    
    @Query("SELECT COUNT(t) FROM ThongBao t WHERE t.nguoiNhan.maNguoiDung = ?1 AND t.trangThai = 'Chưa đọc'")
    long countUnreadNotifications(Long maNguoiDung);
    
    @Modifying
    @Query("UPDATE ThongBao t SET t.trangThai = 'Đã đọc' WHERE t.nguoiNhan.maNguoiDung = ?1")
    void markAllAsRead(Long maNguoiDung);
}