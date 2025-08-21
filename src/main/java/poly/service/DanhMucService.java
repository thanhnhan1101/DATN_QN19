package poly.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import poly.entity.DanhMuc;
import poly.repository.DanhMucRepository;
import java.util.List;

@Service
public class DanhMucService {
    @Autowired
    private DanhMucRepository danhMucRepository;
    
    public List<DanhMuc> getAllDanhMuc() {
        // Sắp xếp theo thuTuHienThi tăng dần
        return danhMucRepository.findAll(Sort.by(Sort.Direction.ASC, "thuTuHienThi"));
    }
    
    private String chuanHoaDuongDan(String duongDan) {
        if (duongDan == null) return "";
        
        // Chuyển về chữ thường
        String result = duongDan.toLowerCase();
        
        // Thay thế dấu tiếng Việt
        result = result.replaceAll("[áàảãạâấầẩẫậăắằẳẵặ]", "a")
                      .replaceAll("[éèẻẽẹêếềểễệ]", "e")
                      .replaceAll("[íìỉĩị]", "i")
                      .replaceAll("[óòỏõọôốồổỗộơớờởỡợ]", "o")
                      .replaceAll("[úùủũụưứừửữự]", "u")
                      .replaceAll("[ýỳỷỹỵ]", "y")
                      .replaceAll("đ", "d");
        
        // Thay thế ký tự đặc biệt bằng dấu gạch ngang
        result = result.replaceAll("[^a-z0-9\\s-]", "");
        
        // Thay khoảng trắng bằng dấu gạch ngang
        result = result.replaceAll("\\s+", "-");
        
        // Xóa các dấu gạch ngang liên tiếp
        result = result.replaceAll("-+", "-");
        
        // Xóa dấu gạch ngang ở đầu và cuối
        result = result.replaceAll("^-|-$", "");
        
        return result;
    }

    public DanhMuc save(DanhMuc danhMuc) {
        // Nếu là thêm mới và có thứ tự hiển thị
        if (danhMuc.getMaDanhMuc() == null && danhMuc.getThuTuHienThi() != null) {
            // Lấy danh sách các danh mục có thứ tự >= thứ tự mới
            List<DanhMuc> affectedCategories = danhMucRepository
                .findByThuTuHienThiGreaterThanEqual(danhMuc.getThuTuHienThi());

            // Dịch chuyển các danh mục này lên 1 bậc
            for (DanhMuc cat : affectedCategories) {
                cat.setThuTuHienThi(cat.getThuTuHienThi() + 1);
                danhMucRepository.save(cat);
            }
        }

        // Nếu là cập nhật và thay đổi thứ tự
        if (danhMuc.getMaDanhMuc() != null) {
            DanhMuc oldCategory = danhMucRepository.findById(danhMuc.getMaDanhMuc()).orElse(null);
            if (oldCategory != null && !oldCategory.getThuTuHienThi().equals(danhMuc.getThuTuHienThi())) {
                // Di chuyển lên
                if (oldCategory.getThuTuHienThi() > danhMuc.getThuTuHienThi()) {
                    danhMucRepository.increaseOrderForRange(
                        danhMuc.getThuTuHienThi(), 
                        oldCategory.getThuTuHienThi() - 1
                    );
                }
                // Di chuyển xuống
                else {
                    danhMucRepository.decreaseOrderForRange(
                        oldCategory.getThuTuHienThi() + 1,
                        danhMuc.getThuTuHienThi()
                    );
                }
            }
        }

        // Chuẩn hóa đường dẫn trước khi lưu
        String duongDanChuanHoa = chuanHoaDuongDan(danhMuc.getDuongDan());
        danhMuc.setDuongDan(duongDanChuanHoa);
        
        // Kiểm tra trùng đường dẫn
        DanhMuc existing = danhMucRepository.findByDuongDan(duongDanChuanHoa);
        if (existing != null && !existing.getMaDanhMuc().equals(danhMuc.getMaDanhMuc())) {
            throw new RuntimeException("Đường dẫn đã tồn tại, vui lòng chọn đường dẫn khác!");
        }
        
        // Nếu không nhập thứ tự, tự động set thứ tự cuối cùng
        if (danhMuc.getThuTuHienThi() == null) {
            Long maxOrder = danhMucRepository.findMaxThuTuHienThi();
            danhMuc.setThuTuHienThi(maxOrder != null ? maxOrder.intValue() + 1 : 1);
        }
        
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

    public List<DanhMuc> findAll() {
        return danhMucRepository.findAll();
    }
    
    public long count() {
        return danhMucRepository.count();
    }
}