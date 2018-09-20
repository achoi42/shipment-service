package com.solstice.client;

import com.solstice.model.info.AddressInfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.hateoas.Resource;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient("account-service")
@Component
public interface AccountClient {

  @GetMapping("/accounts/{acctId}/address/{addrId}")
  Resource<AddressInfo> readSingleAddress(@PathVariable(value = "acctId") long accountId, @PathVariable(value = "addrId") long addressId);
}
