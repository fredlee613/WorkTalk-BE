package com.golfzonTech4.worktalk.dto.mileage;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.golfzonTech4.worktalk.domain.Mileage_status;
import com.querydsl.core.annotations.QueryProjection;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
public class MileageFindDto {

    private Long mileageId;
    private String roomName;
    private Long payId;
    private Mileage_status status;
    private int mileageAmount;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private LocalDate mileageDate;

    @QueryProjection
    public MileageFindDto(Long mileageId, String roomName, Long payId, Mileage_status status, int mileageAmount, LocalDate mileageDate) {
        this.mileageId = mileageId;
        this.roomName = roomName;
        this.payId = payId;
        this.status = status;
        this.mileageAmount = mileageAmount;
        this.mileageDate = mileageDate;
    }
}
