package com.kane.auth.Mapper;

import com.kane.auth.dto.request.SignUpRequest;
import com.kane.auth.model.UserAccount;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserAccountMapper {
    UserAccountMapper INSTANCE = Mappers.getMapper(UserAccountMapper.class);

    @Mapping(source = "nameProfile", target = "profile.name")
    UserAccount toUserAccount(final SignUpRequest userAccount);
}
