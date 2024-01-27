package com.driver.services;


import com.driver.EntryDto.SubscriptionEntryDto;
import com.driver.model.Subscription;
import com.driver.model.SubscriptionType;
import com.driver.model.User;
import com.driver.repository.SubscriptionRepository;
import com.driver.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class SubscriptionService {

    @Autowired
    SubscriptionRepository subscriptionRepository;

    @Autowired
    UserRepository userRepository;

    public Integer buySubscription(SubscriptionEntryDto subscriptionEntryDto){

        //Save The subscription Object into the Db and return the total Amount that user has to pay
        int userId = subscriptionEntryDto.getUserId();
        Optional<User> optionalUser = userRepository.findById(userId);
        if(optionalUser.isEmpty()) {
            return -1;
        }
        User user = optionalUser.get();
        Subscription newSubscription = new Subscription();
        newSubscription.setSubscriptionType(subscriptionEntryDto.getSubscriptionType());

        int totalAmountPaid = 0;
        int noOfScreen = 0;
        switch (subscriptionEntryDto.getSubscriptionType()) {
            case BASIC:
                totalAmountPaid = 500;
                noOfScreen = 200;
                break;
            case PRO:
                totalAmountPaid = 800;
                noOfScreen = 250;
                break;
            case ELITE:
                totalAmountPaid = 1000;
                noOfScreen = 350;
                break;
        }

        newSubscription.setNoOfScreensSubscribed(noOfScreen);
        newSubscription.setTotalAmountPaid(totalAmountPaid);
        newSubscription.setStartSubscriptionDate(new Date());
        newSubscription.setUser(user);
        subscriptionRepository.save(newSubscription);
        return totalAmountPaid;
    }

    public Integer upgradeSubscription(Integer userId)throws Exception{

        //If you are already at an ElITE subscription : then throw Exception ("Already the best Subscription")
        //In all other cases just try to upgrade the subscription and tell the difference of price that user has to pay
        //update the subscription in the repository
        int diffAmount = 0;
        Optional<User> optionalUser = userRepository.findById(userId);
        if(optionalUser.isEmpty()) throw new Exception("Invalid userId");

        User user = optionalUser.get();

        Optional<Subscription> optionalSubscription = subscriptionRepository.findByUser(user);
        if(optionalSubscription.isEmpty()) throw new Exception("Invalid userId");

        List<Subscription> subscriptionList = subscriptionRepository.findAll();
        for (Subscription subscription: subscriptionList) {
            if(subscription.getUser().getId() == userId) {
                if(subscription.getSubscriptionType() == SubscriptionType.ELITE) {
                    throw new Exception("Already the best Subscription");
                }else {
                    if(subscription.getSubscriptionType() == SubscriptionType.BASIC) {
                        diffAmount = 500;
                    }else {
                        diffAmount = 200;
                    }
                    subscription.setSubscriptionType(SubscriptionType.ELITE);
                    subscriptionRepository.save(subscription);
                    return diffAmount;
                }
            }
        }

        return diffAmount;

    }

    public Integer calculateTotalRevenueOfHotstar(){

        //We need to find out total Revenue of hotstar : from all the subscriptions combined
        //Hint is to use findAll function from the SubscriptionDb
        int totalRevenue = 0;
        List<Subscription> subscriptionList = subscriptionRepository.findAll();
        for (Subscription subscription: subscriptionList) {
            if(subscription.getSubscriptionType() == SubscriptionType.BASIC) totalRevenue += 500;
            else if(subscription.getSubscriptionType() == SubscriptionType.PRO) totalRevenue += 800;
            else totalRevenue += 1000;
        }
        return totalRevenue;
    }

}
