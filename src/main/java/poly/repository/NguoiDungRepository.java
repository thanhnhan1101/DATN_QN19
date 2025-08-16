package poly.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import poly.entity.NguoiDung;

import java.util.List;
import java.util.Optional;

@Repository
public interface NguoiDungRepository extends JpaRepository<NguoiDung, Long> {
    Optional<NguoiDung> findByEmail(String email);
    boolean existsByEmail(String email);
    List<NguoiDung> findByVaiTro(String vaiTro);
    
    NguoiDung findByEmailAndMatKhau(String email, String matKhau);
    
}