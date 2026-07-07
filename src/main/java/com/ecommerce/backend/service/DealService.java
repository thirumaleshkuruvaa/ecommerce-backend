package com.ecommerce.backend.service;

import java.util.List;

import com.ecommerce.backend.exceptions.DealException;
import com.ecommerce.backend.model.Deal;

public interface DealService {

    List<Deal> getDeal();

    Deal createDeal(Deal deal) throws DealException;

    Deal updateDeal(Deal deal, Long id) throws DealException;

    void deleteDeal(Long id) throws DealException;
}