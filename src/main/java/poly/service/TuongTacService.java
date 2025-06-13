package poly.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import poly.entity.TuongTac;
import poly.repository.TuongTacRepository;

@Service
public class TuongTacService {
    @Autowired
    private TuongTacRepository tuongTacRepository;

    public List<TuongTac> getAllTuongTac() {
        return tuongTacRepository.findAll();
    }

    public TuongTac save(TuongTac tuongTac) {
        return tuongTacRepository.save(tuongTac);
    }

    public TuongTac findById(Long id) {
        return tuongTacRepository.findById(id).orElse(null);
    }

    public void deleteById(Long id) {
        tuongTacRepository.deleteById(id);
    }

    public List<TuongTac> findByNguoiDungId(Long nguoiDungId) {
        return tuongTacRepository.findByNguoiDung_MaNguoiDung(nguoiDungId);
    }

    public List<TuongTac> findByBaiVietId(Integer baiVietId) {
        return tuongTacRepository.findByBaiViet_MaBaiViet(baiVietId);
    }
}
