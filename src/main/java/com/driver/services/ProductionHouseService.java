package com.driver.services;


import com.driver.EntryDto.ProductionHouseEntryDto;
import com.driver.model.ProductionHouse;
import com.driver.repository.ProductionHouseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductionHouseService {

    @Autowired
    ProductionHouseRepository productionHouseRepository;

    public Integer addProductionHouseToDb(ProductionHouseEntryDto productionHouseEntryDto){

        ProductionHouse newProductionHouse = new ProductionHouse();
        newProductionHouse.setName(productionHouseEntryDto.getName());
        newProductionHouse.setRatings(0);
        newProductionHouse = productionHouseRepository.save(newProductionHouse);
        return newProductionHouse.getId();
    }



}
