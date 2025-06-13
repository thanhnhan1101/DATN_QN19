package poly.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import poly.entity.LichSuXem;
import poly.repository.LichSuXemRepository;

@Service
public class LichSuXemService {
    @Autowired
    private LichSuXemRepository LichSuXemRepository;

   public List<LichSuXem> getAllLichSuXem() {
    return LichSuXemRepository.findAll();
   }

   public LichSuXem save(LichSuXem lichSuXem) {
    return LichSuXemRepository.save(lichSuXem);
   }

   public LichSuXem findById(Long id) {
    return LichSuXemRepository.findById(id).orElse(null);
   }

   public void deleteById(Long id) {
    LichSuXemRepository.deleteById(id);
   }

   public List<LichSuXem> findByBaiVietId(Integer baiVietId) {
    return LichSuXemRepository.findByBaiViet_MaBaiViet(baiVietId);
   }

   public List<LichSuXem> findByNguoiDungId(Long nguoiDungId) {
    return LichSuXemRepository.findByNguoiDung_MaNguoiDung(nguoiDungId);
   }
}
