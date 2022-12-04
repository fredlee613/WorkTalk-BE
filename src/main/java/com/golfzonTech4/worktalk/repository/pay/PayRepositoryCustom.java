package com.golfzonTech4.worktalk.repository.pay;

import com.golfzonTech4.worktalk.dto.pay.PayInsertDto;

import java.util.List;
import java.util.Optional;

public interface PayRepositoryCustom {
    public List<PayInsertDto> findAlByReserveId(Long reserveId);

    public Optional<PayInsertDto> findByCUid(String customerUid);

}
