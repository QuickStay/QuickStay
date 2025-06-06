package com.project.quickstay.domain.place.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PlaceRegister {

    @NotEmpty
    private String name;
    @NotEmpty
    private String description;
    @NotEmpty
    private String province;
    @NotEmpty
    private String city;
    @NotEmpty
    private String detailAddress;
    @NotEmpty
    private String contact;

}
