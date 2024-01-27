package com.driver.services;

import com.driver.EntryDto.WebSeriesEntryDto;
import com.driver.model.ProductionHouse;
import com.driver.model.WebSeries;
import com.driver.repository.ProductionHouseRepository;
import com.driver.repository.WebSeriesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class WebSeriesService {

    @Autowired
    WebSeriesRepository webSeriesRepository;

    @Autowired
    ProductionHouseRepository productionHouseRepository;

    public Integer addWebSeries(WebSeriesEntryDto webSeriesEntryDto)throws  Exception{

        //Add a webSeries to the database and update the ratings of the productionHouse
        //Incase the seriesName is already present in the Db throw Exception("Series is already present")
        //use function written in Repository Layer for the same
        //Dont forget to save the production and webseries Repo

        String seriseName = webSeriesEntryDto.getSeriesName();
        List<WebSeries> webSeriesList = webSeriesRepository.findAll();
        for(WebSeries webSeries: webSeriesList) {
            if(seriseName.equals(webSeries.getSeriesName())) {
                throw new Exception("Series is already present");
            }
        }

        Optional<ProductionHouse> optionalProductionHouse = productionHouseRepository.findById(webSeriesEntryDto.getProductionHouseId());
        if (optionalProductionHouse.isEmpty()) {
            throw new Exception("Invalid productionHouseId");
        }
        ProductionHouse productionHouse = optionalProductionHouse.get();

        int numberOfWebSeries = 0;
        for(WebSeries webSeries: webSeriesList) {
            if(webSeries.getProductionHouse().getId() == productionHouse.getId()) numberOfWebSeries++;
        }
        double productionRating = productionHouse.getRatings();
        double totalRating = productionRating*numberOfWebSeries;

        double newRating = totalRating+webSeriesEntryDto.getRating();
        newRating = newRating/(numberOfWebSeries+1);

        productionHouse.setRatings(newRating);
        productionHouseRepository.save(productionHouse);

        WebSeries newWebSeries = new WebSeries();
        newWebSeries.setSeriesName(webSeriesEntryDto.getSeriesName());
        newWebSeries.setAgeLimit(webSeriesEntryDto.getAgeLimit());
        newWebSeries.setRating(webSeriesEntryDto.getRating());
        newWebSeries.setSubscriptionType(webSeriesEntryDto.getSubscriptionType());
        newWebSeries.setProductionHouse(productionHouse);

        newWebSeries = webSeriesRepository.save(newWebSeries);
        return newWebSeries.getId();
    }

}
