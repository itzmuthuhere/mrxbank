package com.mrxbank.accounts.service;

import com.mrxbank.accounts.dto.CustomerDto;

public interface IAccountsService {

    void createAccount(CustomerDto customerDto);

    CustomerDto fetchAccount(String mobileNumber);

    boolean updateAccount(CustomerDto customerDto);

    boolean deleteAccount(String mobileNumber);


}
