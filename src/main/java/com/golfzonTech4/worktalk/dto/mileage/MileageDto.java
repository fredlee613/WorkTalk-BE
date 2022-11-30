package com.golfzonTech4.worktalk.dto.mileage;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.golfzonTech4.worktalk.domain.Mileage_status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class MileageDto{

    private Long mileageId;

    private Long payId;

    private Long memberId;

    private Mileage_status status;

    private int mileageAmount;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private LocalDate mileageDate;
}
