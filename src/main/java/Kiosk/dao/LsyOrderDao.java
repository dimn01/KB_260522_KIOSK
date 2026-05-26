package Kiosk.dao;

import Kiosk.domain.LsyOrder;
import java.util.List;

public interface LsyOrderDao {
    void save(LsyOrder order);
    List<LsyOrder> findByMemberId(String memberId);
}
