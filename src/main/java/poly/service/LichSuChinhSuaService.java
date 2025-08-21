package poly.service;

import java.util.List;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import poly.entity.LichSuChinhSua;
import poly.entity.BaiViet;
import poly.entity.NguoiDung;
import poly.repository.LichSuChinhSuaRepository;

@Service
public class LichSuChinhSuaService {
    @Autowired
    private LichSuChinhSuaRepository lichSuChinhSuaRepository;

    public void luuLichSu(BaiViet baiViet, NguoiDung nguoiSua, String noiDungCu) {
        LichSuChinhSua lichSu = new LichSuChinhSua();
        lichSu.setBaiViet(baiViet);
        lichSu.setNguoiSua(nguoiSua);
        lichSu.setNgayChinhSua(LocalDateTime.now());
        lichSu.setNoiDungCu(noiDungCu);
        lichSuChinhSuaRepository.save(lichSu);
    }

    public List<LichSuChinhSua> getLichSuByBaiViet(BaiViet baiViet) {
        return lichSuChinhSuaRepository.findByBaiVietOrderByNgayChinhSuaDesc(baiViet);
    }
}