package poly.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import poly.entity.DanhMuc;
import poly.repository.DanhMucRepository;
import java.util.List;

@Service
public class DanhMucService {
    @Autowired
    private DanhMucRepository danhMucRepository;
    
    public List<DanhMuc> getAllDanhMuc() {
        return danhMucRepository.findAll();
    }
    
    public DanhMuc save(DanhMuc danhMuc) {
        return danhMucRepository.save(danhMuc);
    }
    
    public DanhMuc findById(Integer id) {
        return danhMucRepository.findById(id).orElse(null);
    }
    
    public void deleteById(Integer id) {
        danhMucRepository.deleteById(id);
    }
    
    public DanhMuc findByDuongDan(String duongDan) {
        return danhMucRepository.findByDuongDan(duongDan);
    }

    // Thêm phương thức đếm tổng số danh mục
    public long countAll() {
        return danhMucRepository.count();
    }
}