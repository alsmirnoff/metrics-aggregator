package com.learning.metrics_aggregator.service;

import org.springframework.stereotype.Service;

import com.learning.metrics_aggregator.dao.ApplicationRepository;
import com.learning.metrics_aggregator.entity.Application;
import com.learning.metrics_aggregator.exception.ApplicationAlreadyExistException;

@Service
public class ApplicationService {

    private final ApplicationRepository applicationRepository;

    public ApplicationService(ApplicationRepository applicationRepository){
        this.applicationRepository = applicationRepository;
    }

    public Application create(Application application){
        if(applicationRepository.existsByName(application.getName())){
            throw new ApplicationAlreadyExistException("Application '" + application.getName() + "' already exist");
        }
        return applicationRepository.save(application);
    }
}
