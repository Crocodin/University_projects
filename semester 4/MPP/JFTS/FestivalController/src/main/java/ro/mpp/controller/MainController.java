package ro.mpp.controller;

import lombok.Setter;
import ro.mpp.domain.User;
import ro.mpp.service.IFestivalService;

public class MainController {
    @Setter private IFestivalService festivalService;
    @Setter private User user;
}
