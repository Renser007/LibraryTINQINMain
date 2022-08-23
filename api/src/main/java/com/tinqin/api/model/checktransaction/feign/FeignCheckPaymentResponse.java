package com.tinqin.api.model.checktransaction.feign;

import lombok.*;

@Builder
@Getter
@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
public class FeignCheckPaymentResponse {

    private String status;

}
