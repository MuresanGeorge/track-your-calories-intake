package com.george.tracker.controller;

import com.george.tracker.service.ConsumptionService;
import com.george.tracker.transport.ConsumptionDto;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/intakes")
public class ConsumptionController {

    private final ConsumptionService consumptionService;

    private final ModelMapper mapper;

    public ConsumptionController(ConsumptionService consumptionService, ModelMapper modelMapper) {
        this.consumptionService = consumptionService;
        this.mapper = modelMapper;
    }


    @PostMapping
    @ApiOperation(value = "Create a new daily intake", notes = "Returns 201 Created if the daily intake is successfully created")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Success"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 401, message = "Not authenticated"),
            @ApiResponse(code = 404, message = "Data not found"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    @ResponseStatus(HttpStatus.CREATED)
    public void createConsumption(@Valid @RequestBody ConsumptionDto consumptionDto) {

    }
}
