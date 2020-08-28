package com.oauth.clients;

import com.model.remotes.UserRemote;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "user")
public interface UserClient extends UserRemote {

}
