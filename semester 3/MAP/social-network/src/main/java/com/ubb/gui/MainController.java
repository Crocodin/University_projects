package com.ubb.gui;

import com.ubb.domain.user.User;
import com.ubb.facade.UserFacade;
import com.ubb.service.*;
import com.ubb.service.notifications.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Tab;

public class MainController {

    @FXML
    public Tab profileTab;

    @FXML
    private Tab notificationTab;

    @FXML
    private Tab ducksTab;

    @FXML
    private Tab personsTab;

    @FXML
    private Tab graphTab;

    @FXML
    private Tab eventsTab;

    @FXML
    private Tab lanesTab;

    @FXML
    private Tab flocksTab;

    @FXML
    private Tab friendShipTab;

    @FXML
    private Tab messageTab;

    private User loginUser;

    private final DuckService duckService = new DuckService();
    private final PersonService personService = new PersonService();
    private FriendService friendService;
    private UserFacade userFacade;
    private final LaneService laneService = new LaneService();
    private EventService eventService;
    private FlockService flockService;
    private MessageService messageService;
    private FriendRequestService friendRequestService;
    private NotificationService notificationService;
    private EventNotificationService eventNotificationService;

    public MainController(User loginUser) {
        this.loginUser = loginUser;
    }

    @FXML
    public void initialize() throws Exception {
        userFacade = new UserFacade(duckService, personService);
        friendService = new FriendService(userFacade);
        eventService = new EventService(userFacade, duckService, laneService);
        flockService = new FlockService(duckService);
        friendService = new FriendService(userFacade);
        messageService = new MessageService(userFacade);
        friendRequestService = new FriendRequestService(userFacade);
        eventNotificationService = new EventNotificationService(userFacade, eventService);
        notificationService = new NotificationService(friendRequestService, eventNotificationService, loginUser);

        FXMLLoader loader_duck = new FXMLLoader(getClass().getResource("/com/ubb/gui/duck-tab.fxml"));
        DuckTabController duckController = new DuckTabController(duckService);
        loader_duck.setController(duckController);
        ducksTab.setContent(loader_duck.load());

        FXMLLoader loader_person = new FXMLLoader(getClass().getResource("/com/ubb/gui/person-tab.fxml"));
        PersonTabController personController = new PersonTabController(personService);
        loader_person.setController(personController);
        personsTab.setContent(loader_person.load());

        FXMLLoader loader_graph = new FXMLLoader(getClass().getResource("/com/ubb/gui/graph-tab.fxml"));
        GraphTabController graphController = new GraphTabController(userFacade, friendService);
        loader_graph.setController(graphController);
        graphTab.setContent(loader_graph.load());

        FXMLLoader loader_lane = new FXMLLoader(getClass().getResource("/com/ubb/gui/lane-tab.fxml"));
        LaneTabController laneController = new LaneTabController(laneService);
        loader_lane.setController(laneController);
        lanesTab.setContent(loader_lane.load());

        FXMLLoader loader_flock = new FXMLLoader(getClass().getResource("/com/ubb/gui/flock-tab.fxml"));
        FlockTabController flockController = new FlockTabController(flockService);
        loader_flock.setController(flockController);
        flocksTab.setContent(loader_flock.load());

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/ubb/gui/notification-tab.fxml"));
        notificationTab.setContent(loader.load());
        NotificationController notificationsController = loader.getController();
        notificationsController.setService(notificationService, loginUser, friendService);

        FXMLLoader loader_friendship = new FXMLLoader(getClass().getResource("/com/ubb/gui/friendship-tab.fxml"));
        FriendshipTabController friendshipController = new FriendshipTabController(friendService, userFacade, notificationService);
        loader_friendship.setController(friendshipController);
        friendShipTab.setContent(loader_friendship.load());

        FXMLLoader loader_message = new FXMLLoader(getClass().getResource("/com/ubb/gui/message-tab.fxml"));
        MessageTabController messageController = new MessageTabController(friendService, messageService, loginUser);
        loader_message.setController(messageController);
        messageTab.setContent(loader_message.load());

        FXMLLoader loader_event = new FXMLLoader(getClass().getResource("/com/ubb/gui/event-tab.fxml"));
        EventTabController eventController = new EventTabController(eventService, userFacade, notificationService);
        loader_event.setController(eventController);
        eventsTab.setContent(loader_event.load());

        eventController.addObserver(notificationsController);

        FXMLLoader loader_profile = new FXMLLoader(getClass().getResource("/com/ubb/gui/profile-tab.fxml"));
        profileTab.setContent(loader_profile.load());
        ProfileController profileController = loader_profile.getController();
        profileController.setService(loginUser, userFacade);
    }
}
