package com.exadel.frs.controller;

import static com.exadel.frs.system.global.Constants.GUID_EXAMPLE;
import com.exadel.frs.dto.ui.ModelCreateDto;
import com.exadel.frs.dto.ui.ModelResponseDto;
import com.exadel.frs.dto.ui.ModelUpdateDto;
import com.exadel.frs.helpers.SecurityUtils;
import com.exadel.frs.mapper.MlModelMapper;
import com.exadel.frs.service.ModelService;
import com.exadel.frs.system.statistics.CallStatistics;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/org/{orgGuid}/app/{appGuid}")
@RequiredArgsConstructor
public class ModelController {

    private final ModelService modelService;
    private final MlModelMapper modelMapper;

    @CallStatistics
    @GetMapping("/model/{guid}")
    @ApiOperation(value = "Get model")
    public ModelResponseDto getModel(
            @ApiParam(value = "GUID of organization", required = true, example = GUID_EXAMPLE)
            @PathVariable
            final String orgGuid,
            @ApiParam(value = "GUID of application", required = true, example = GUID_EXAMPLE)
            @PathVariable
            final String appGuid,
            @ApiParam(value = "GUID of model to return", required = true, example = GUID_EXAMPLE)
            @PathVariable
            final String guid
    ) {
        return modelMapper.toResponseDto(
                modelService.getModel(guid, SecurityUtils.getPrincipalId()),
                appGuid
        );
    }

    @GetMapping("/models")
    @ApiOperation(value = "Get all models in application")
    public List<ModelResponseDto> getModels(
            @ApiParam(value = "GUID of organization", required = true, example = GUID_EXAMPLE)
            @PathVariable
            final String orgGuid,
            @ApiParam(value = "GUID of application", required = true, example = GUID_EXAMPLE)
            @PathVariable
            final String appGuid
    ) {
        return modelMapper.toResponseDto(
                modelService.getModels(appGuid, SecurityUtils.getPrincipalId()),
                appGuid
        );
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/model")
    @ApiOperation(value = "Create model")
    @ApiResponses({
            @ApiResponse(code = 400, message = "Model name is required | Application access type to model is not correct")
    })
    public ModelResponseDto createModel(
            @ApiParam(value = "GUID of organization", required = true, example = GUID_EXAMPLE)
            @PathVariable
            final String orgGuid,
            @ApiParam(value = "GUID of application", required = true, example = GUID_EXAMPLE)
            @PathVariable
            final String appGuid,
            @ApiParam(value = "Model object that needs to be created", required = true)
            @Valid
            @RequestBody
            final ModelCreateDto modelCreateDto) {
        return modelMapper.toResponseDto(
                modelService.createModel(modelCreateDto, orgGuid, appGuid, SecurityUtils.getPrincipalId()),
                appGuid
        );
    }

    @PutMapping("/model/{guid}")
    @ApiOperation(value = "Update model data")
    @ApiResponses({
            @ApiResponse(code = 400, message = "Application access type to model is not correct")
    })
    public ModelResponseDto updateModel(
            @ApiParam(value = "GUID of organization", required = true, example = GUID_EXAMPLE)
            @PathVariable
            final String orgGuid,
            @ApiParam(value = "GUID of application", required = true, example = GUID_EXAMPLE)
            @PathVariable
            final String appGuid,
            @ApiParam(value = "GUID of model that needs to be updated", required = true, example = GUID_EXAMPLE)
            @PathVariable
            final String guid,
            @ApiParam(value = "Model data", required = true)
            @Valid
            @RequestBody
            final ModelUpdateDto modelUpdateDto
    ) {
        var updatedModel = modelService.updateModel(modelUpdateDto, guid, SecurityUtils.getPrincipalId());

        return modelMapper.toResponseDto(updatedModel, appGuid);
    }

    @PutMapping("/model/{guid}/apikey")
    @ApiOperation(value = "Generate new api-key for model")
    public void regenerateApiKey(
            @ApiParam(value = "GUID of organization", required = true, example = GUID_EXAMPLE)
            @PathVariable
            final String orgGuid,
            @ApiParam(value = "GUID of application", required = true, example = GUID_EXAMPLE)
            @PathVariable
            final String appGuid,
            @ApiParam(value = "GUID of the model which GUID needs to be regenerated", required = true, example = GUID_EXAMPLE)
            @PathVariable
            final String guid
    ) {
        modelService.regenerateApiKey(guid, SecurityUtils.getPrincipalId());
    }

    @DeleteMapping("/model/{guid}")
    @ApiOperation(value = "Delete model")
    public void deleteModel(
            @ApiParam(value = "GUID of organization", required = true, example = GUID_EXAMPLE)
            @PathVariable
            final String orgGuid,
            @ApiParam(value = "GUID of application", required = true, example = GUID_EXAMPLE)
            @PathVariable
            final String appGuid,
            @ApiParam(value = "GUID of the model that needs to be deleted", required = true, example = GUID_EXAMPLE)
            @PathVariable
            final String guid
    ) {
        modelService.deleteModel(guid, SecurityUtils.getPrincipalId());
    }
}