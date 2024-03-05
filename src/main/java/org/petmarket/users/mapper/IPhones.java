package org.petmarket.users.mapper;

import org.petmarket.users.dto.UserPhoneDto;

import java.util.Set;

public interface IPhones {

    void setMainPhone(String number);

    Set<UserPhoneDto> getPhones();
}
