package poly.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import poly.entity.NguoiDung;
import poly.entity.ThongBao;
import poly.repository.ThongBaoRepository;
import java.util.List;

@Service
public class ThongBaoService {

    @Autowired
    private ThongBaoRepository thongBaoRepository;

    public void taoThongBao(String tieuDe, String noiDung, NguoiDung nguoiNhan) {
        ThongBao thongBao = new ThongBao();
        thongBao.setTieuDe(tieuDe);
        thongBao.setNoiDung(noiDung);
        thongBao.setNguoiNhan(nguoiNhan);
        thongBaoRepository.save(thongBao);
    }

    public List<ThongBao> layThongBaoNguoiDung(Long maNguoiDung) {
        return thongBaoRepository.findByNguoiNhanMaNguoiDungOrderByNgayGuiDesc(maNguoiDung);
    }

    public long demThongBaoChuaDoc(Long maNguoiDung) {
        return thongBaoRepository.countUnreadNotifications(maNguoiDung);
    }

    @Transactional
    public void danhDauTatCaDaDoc(Long maNguoiDung) {
        thongBaoRepository.markAllAsRead(maNguoiDung);
    }
}