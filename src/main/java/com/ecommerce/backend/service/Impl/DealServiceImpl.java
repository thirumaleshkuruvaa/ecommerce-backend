package com.ecommerce.backend.service.Impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ecommerce.backend.exceptions.DealException;
import com.ecommerce.backend.model.Deal;
import com.ecommerce.backend.model.HomeCategory;
import com.ecommerce.backend.repository.DealRepository;
import com.ecommerce.backend.repository.HomeCategoryRepository;
import com.ecommerce.backend.service.DealService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class DealServiceImpl implements DealService {

        private final HomeCategoryRepository homeCategoryRepository;
        private final DealRepository dealRepository;

        public DealServiceImpl(
                        HomeCategoryRepository homeCategoryRepository,
                        DealRepository dealRepository) {

                this.homeCategoryRepository = homeCategoryRepository;
                this.dealRepository = dealRepository;
        }

        // GET ALL DEALS

        @Override
        public List<Deal> getDeal() {

                log.info("Fetching all deals");

                return dealRepository.findAll();
        }

        // CREATE DEAL

        @Override
        public Deal createDeal(Deal deal)
                        throws DealException {

                log.info("Creating new deal");

                HomeCategory homeCategory = homeCategoryRepository

                                .findById(deal.getCategory().getId())

                                .orElseThrow(() -> {

                                        log.error("Home category not found with id {}",
                                                        deal.getCategory().getId());

                                        return new DealException(
                                                        "Home category not found");
                                });
                Deal newDeal = new Deal();

                newDeal.setCategory(homeCategory);

                newDeal.setDiscount(deal.getDiscount());

                log.debug("Saving deal with discount {}",
                                deal.getDiscount());

                Deal savedDeal = dealRepository.save(newDeal);

                log.info("Deal created successfully");

                return savedDeal;
        }

        // UPDATE DEAL

        @Override
        public Deal updateDeal(
                        Deal deal,
                        Long id)
                        throws DealException {
                log.info("Updating deal with id {}",
                                id);

                Deal existingDeal = dealRepository

                                .findById(id)

                                .orElseThrow(() -> {

                                        log.error("Deal not found with id {}",
                                                        id);

                                        return new DealException(
                                                        "Deal not found");
                                });

                // UPDATE DISCOUNT

                if (deal.getDiscount() != null) {

                        log.debug("Updating deal discount");

                        existingDeal.setDiscount(
                                        deal.getDiscount());
                }

                // UPDATE CATEGORY

                if (deal.getCategory() != null) {

                        HomeCategory homeCategory = homeCategoryRepository

                                        .findById(deal.getCategory().getId())

                                        .orElseThrow(() -> {

                                                log.error("Home category not found with id {}",
                                                                deal.getCategory().getId());

                                                return new DealException(
                                                                "Home category not found");
                                        });
                        existingDeal.setCategory(homeCategory);
                }

                Deal updatedDeal = dealRepository.save(existingDeal);

                log.info("Deal updated successfully with id {}",
                                id);

                return updatedDeal;
        }

        // DELETE DEAL

        @Override
        public void deleteDeal(Long id)
                        throws DealException {

                log.info("Deleting deal with id {}", id);

                Deal deal = dealRepository

                                .findById(id)

                                .orElseThrow(() -> {

                                        log.error("Deal not found with id {}",
                                                        id);

                                        return new DealException(
                                                        "Deal not found");
                                });

                dealRepository.delete(deal);

                log.info("Deal deleted successfully with id {}",
                                id);
        }

}