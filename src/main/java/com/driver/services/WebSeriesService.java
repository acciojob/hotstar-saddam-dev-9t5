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

        Optional<WebSeries> optionalWebSeries = Optional.ofNullable(webSeriesRepository.findBySeriesName(webSeriesEntryDto.getSeriesName()));
        if(optionalWebSeries.isEmpty()) {
            throw new Exception("Series is already present");
        }

        Optional<ProductionHouse> optionalProductionHouse = productionHouseRepository.findById(webSeriesEntryDto.getProductionHouseId());
        if (optionalProductionHouse.isEmpty()) {
            throw new Exception("Production House not available");
        }

        ProductionHouse productionHouse = optionalProductionHouse.get();

        List<WebSeries> webSeriesList = webSeriesRepository.findAllByProductionHouse(productionHouse);
        int numberOfWebSeries = webSeriesList.size();

        productionHouse.setRatings((productionHouse.getRatings()*numberOfWebSeries+webSeriesEntryDto.getRating())/(numberOfWebSeries+1));

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
