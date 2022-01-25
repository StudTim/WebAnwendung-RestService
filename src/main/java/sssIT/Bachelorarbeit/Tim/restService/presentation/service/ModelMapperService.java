package sssIT.Bachelorarbeit.Tim.restService.presentation.service;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
public class ModelMapperService {

    public ModelMapper getModelMapper() {
        return new ModelMapper();
    }

}
