package com.bytecubed.nlp.web;

import com.bytecubed.commons.Formation;
import com.bytecubed.commons.PlayCard;
import com.bytecubed.commons.models.Placement;
import com.bytecubed.commons.models.PlayerMarker;
import com.bytecubed.commons.models.movement.CustomMoveDescriptor;
import com.bytecubed.commons.models.movement.CustomRoute;
import com.bytecubed.nlp.models.PlayCardCommand;
import com.bytecubed.nlp.models.PlayCardInstruction;
import com.bytecubed.nlp.models.RouteInstruction;
import com.bytecubed.nlp.parsing.InstructionParser;
import com.bytecubed.nlp.repository.FormationRepository;
import com.bytecubed.nlp.repository.PlayRepository;
import com.bytecubed.nlp.repository.RouteRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.io.BaseEncoding;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.http.*;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.client.RestTemplate;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static java.util.Arrays.asList;
import static java.util.UUID.randomUUID;
import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class CoachControllerTest {

    public static final String TAG = "X";
    public static final String PLAY_NAME = "ByteCubed";
    private PlayRepository playRepository;
    private InstructionParser instructionParser;
    private FormationRepository formationRepository;
    private RouteRepository routeRepository;
    private UUID TEST_FORMATION_ID = randomUUID();
    private UUID TEST_ROUTE_ID = randomUUID();
    private Formation formation;

    @Before
    public void setup() {
        playRepository = mock(PlayRepository.class);
        instructionParser = mock(InstructionParser.class);
        formationRepository = formationRepository();
        routeRepository = routeRepository();
    }

    @Test
    public void shouldReturnNewPlayCardBasedOnOnePlayerOneRoute() {
        CoachController controller = new CoachController(
                playRepository,
                instructionParser,
                formationRepository,
                routeRepository);


        PlayCardInstruction instruction = new PlayCardInstruction(TEST_FORMATION_ID,
                asList(new RouteInstruction(TAG, TEST_ROUTE_ID)));

        PlayCard card = controller.postScript(instruction).getBody();
        System.out.println(card.getId());

        Formation formation = card.getFormation();
        assertThat(formation.getPlayerMarkers()).hasSize(1);

        PlayerMarker playerMarker = formation.getPlayerMarkers().get(0);
        assertThat(playerMarker.getRoutes()).hasSize(1);

        CustomMoveDescriptor movements = (CustomMoveDescriptor) playerMarker.getRoutes().get(0).getMoveDescriptors().get(0);
        assertThat(movements.getStart()).isEqualTo(new Placement(10d, 10d));
        assertThat(movements.getEnd()).isEqualTo(new Placement(10d, 05d));
    }

    @Test
    @Ignore
    public void shouldGenerateWithIFormation() {
        UUID iFormationId = UUID.fromString("2a6009a0-b66d-440a-b141-7cb64054e217");
//        UUID routeID = UUID.fromString("d2c38c13-ece6-40fe-acab-f72aaacf737b");
        UUID routeID = UUID.fromString("d2c38c13-ece6-40fe-acab-f72aaacf737b");

        PlayCardInstruction instruction = new PlayCardInstruction(iFormationId,
                asList(new RouteInstruction("X", routeID)));

        RestTemplate template = new RestTemplate();
        String response = template.postForEntity("http://nlp.immersivesports.ai/coach/playcards/script", instruction, String.class).getBody();

        System.out.println("Response:  " + response);
    }

    @Test
    @Ignore
    public void shouldGenerateFromSpeech() {
        UUID iFormationId = UUID.fromString("2a6009a0-b66d-440a-b141-7cb64054e217");
//        UUID routeID = UUID.fromString("d2c38c13-ece6-40fe-acab-f72aaacf737b");

        PlayCardCommand command = new PlayCardCommand(iFormationId, "x has a basic");

        RestTemplate template = new RestTemplate();
        String response = template.postForEntity("http://nlp.immersivesports.ai/coach/playcards/text", command, String.class).getBody();

        System.out.println("Response:  " + response);
    }

    @Test
    @Ignore
    public void shouldAddAllRoutes() {
        RestTemplate template = new RestTemplate();

//        CustomRoute route = new CustomRoute(asList(
//                new CustomMoveDescriptor(new Placement(0d,0d), new Placement(0d,-30d)),
//                new CustomMoveDescriptor(new Placement(0d,-30d), new Placement(15d,-30d))
//        ), "X");
//
//        String response = template.postForEntity( "http://localhost:8080/coach/routes",
//                 new CustomRoute(randomUUID(), "dig", route), String.class).getBody();
//        System.out.println(response);
//
//        route = new CustomRoute(asList(
//                new CustomMoveDescriptor(new Placement(0d, 0d), new Placement(0d, -25d)),
//                new CustomMoveDescriptor(new Placement(0d, -25d), new Placement(15d, -25d))
//        ), "X");
//
//        response = template.postForEntity("http://localhost:8080/coach/routes",
//                new CustomRoute(randomUUID(), "basic", route), String.class).getBody();
//        System.out.println(response);
//
//        route = new CustomRoute(asList(
//                new CustomMoveDescriptor(new Placement(0d,0d), new Placement(0d,-15d)),
//                new CustomMoveDescriptor(new Placement(0d,-15d), new Placement(3d,-12d))
//        ), "X");
//
//        response = template.postForEntity( "http://localhost:8080/coach/routes",
//                new CustomRoute(randomUUID(), "stick", route), String.class).getBody();
//        System.out.println(response);

//        route = new CustomRoute(asList(
//                new CustomMoveDescriptor(new Placement(0d,0d), new Placement(0d,-50d))
//        ), "X");
//
//        response = template.postForEntity( "http://nlp.immersivesports.ai/coach/routes",
//                new CustomRoute(randomUUID(), "go", route), String.class).getBody();
//        System.out.println(response);

        CustomRoute route = new CustomRoute(asList(
                new CustomMoveDescriptor(new Placement(0d, 0d), new Placement(0d, -12d)),
                new CustomMoveDescriptor(new Placement(0d, -12d), new Placement(80d, -92d))
        ), "X");

        String response = template.postForEntity("http://nlp.immersivesports.ai/coach/routes",
                new CustomRoute(randomUUID(), "slant", route), String.class).getBody();
        System.out.println(response);
    }

    @Test
    @Ignore
    public void shouldBeGentleInTheEventNothingIsParsed() {
        UUID iFormationId = UUID.fromString("2a6009a0-b66d-440a-b141-7cb64054e217");
//        UUID routeID = UUID.fromString("d2c38c13-ece6-40fe-acab-f72aaacf737b");

        PlayCardCommand command = new PlayCardCommand(iFormationId, "this is nothing");

        RestTemplate template = new RestTemplate();
        String response = template.postForEntity("http://localhost:8080/coach/playcards/text", command, String.class).getBody();

        System.out.println("Response:  " + response);
    }

    @Test
    @Ignore
    public void shouldSaveRealRoute() {
        CustomRoute route = new CustomRoute(asList(new CustomMoveDescriptor(
                new Placement(0, 0),
                new Placement(0, -25))), "X");

        RestTemplate template = new RestTemplate();
        String response = template.postForEntity("http://nlp.immersivesports.ai/coach/routes", route, String.class).getBody();
        System.out.println(response);
    }

    @Test
    @Ignore
    public void shouldSetNamesOfKnownRoutes() {
        RestTemplate template = new RestTemplate();
        template.postForEntity(
                "http://localhost:8080/coach/route/192e3ecd-54ab-42ed-80e2-75323d2bd7bc/name",
                "hook", String.class).getBody();

        template.postForEntity(
                "http://localhost:8080/coach/route/f272a07e-0b21-43ed-9721-377ea6bbd4ce/name",
                "cross", String.class).getBody();

        template.postForEntity(
                "http://localhost:8080/coach/route/1cdce2b6-b192-4312-b6dc-1193603d614a/name",
                "flat", String.class).getBody();

        template.postForEntity(
                "http://localhost:8080/coach/route/cfb88033-bdbc-4dcf-823c-4b334d865cfe/name",
                "book", String.class).getBody();
    }

    @Test
    public void shouldGenerateAndSaveRoute() throws Exception {
        CustomRoute route = new CustomRoute(asList(new CustomMoveDescriptor(
                new Placement(0, 0),
                new Placement(0, -5))), "X");

        MockMvc controller = MockMvcBuilders.standaloneSetup(
                new CoachController(playRepository, instructionParser,
                        formationRepository, routeRepository)).build();
        controller.perform(
                post("/coach/routes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(route)))
                .andExpect(status().isOk());

        ArgumentCaptor<CustomRoute> savedRouteCaptor = ArgumentCaptor.forClass(CustomRoute.class);
        verify(routeRepository).save(savedRouteCaptor.capture());
        CustomRoute savedRoute = savedRouteCaptor.getValue();

        CustomMoveDescriptor descriptor = (CustomMoveDescriptor) savedRoute.getMoveDescriptors().get(0);
        assertThat(descriptor.getStart()).isEqualTo(((CustomMoveDescriptor) route.getMoveDescriptors().get(0)).getStart());
        assertThat(descriptor.getEnd()).isEqualTo(((CustomMoveDescriptor) route.getMoveDescriptors().get(0)).getEnd());
    }

    private FormationRepository formationRepository() {
        FormationRepository formationRepository = mock(FormationRepository.class);
        formation = new Formation(TEST_FORMATION_ID, PLAY_NAME, asList(new PlayerMarker(
                new Placement(10, 10), "wr", TAG
        )));

        when(formationRepository.findById(TEST_FORMATION_ID))
                .thenReturn(Optional.of(formation));

        return formationRepository;
    }

    private RouteRepository routeRepository() {
        RouteRepository routeRepository = mock(RouteRepository.class);
        CustomRoute customRoute = new CustomRoute(asList(
                new CustomMoveDescriptor(new Placement(0d, 0d),
                        new Placement(0d, -5d))
        ), TAG);

        when(routeRepository.findById(TEST_ROUTE_ID)).thenReturn(Optional.of(customRoute));

        return routeRepository;
    }


    @Test
    @Ignore
    public void shouldIngestNGSData() throws NoSuchAlgorithmException {
        List<NFLGameResponse> games = new ArrayList();
        games.addAll(Arrays.asList(getNflGameResponsesByYear(2012)));
        games.addAll(Arrays.asList(getNflGameResponsesByYear(2013)));
        games.addAll(Arrays.asList(getNflGameResponsesByYear(2014)));
        games.addAll(Arrays.asList(getNflGameResponsesByYear(2015)));
        games.addAll(Arrays.asList(getNflGameResponsesByYear(2016)));
        games.addAll(Arrays.asList(getNflGameResponsesByYear(2017)));
        games.addAll(Arrays.asList(getNflGameResponsesByYear(2018)));

        games = filterOnlyBaltimoreGames(games);
        cachePlays(games);

        for (NFLGameResponse game : games) {
            System.out.println(game.getGameId() + " " + game.getGameKey()
                    + " " + game.getVisitorTeamAbbr() + " vs. " + game.getHomeTeamAbbr());
        }
    }

    private void cachePlays(List<NFLGameResponse> games) {
        String authorizationToken = generateAuthorizationToken();
        RestTemplate template = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        System.out.println( "Token:  " + authorizationToken );
        headers.add("Authorization", authorizationToken);

        HttpEntity entity = new HttpEntity("parameters", headers);
        for (NFLGameResponse game : games) {
            String url = "http://localhost:6569/submittedPlays/game?gameId="
                    + game.getGameId() + "&gameKey=" + game.getGameKey();
            System.out.println(url);

            try {
                GamePlay[] plays = template.exchange(url, HttpMethod.GET, entity, GamePlay[].class).getBody();
                System.out.println("Play count:  " + plays.length);
                for (GamePlay play : plays) {
                    String playUrl = "http://localhost:6569/tracking/game/play?gameId=" + game.getGameId() +
                            "&gameKey=" + game.getGameKey() + "&playId=" + play.getPlayId();
                    System.out.println(playUrl);
                    try {
                        String response = template.exchange(playUrl, HttpMethod.GET, entity, String.class).getBody();
                        System.out.println(response);

                    } catch (Exception e) {
                    }
                }
            }catch(Exception e ){}

        }

    }

    private List<NFLGameResponse> filterOnlyBaltimoreGames(List<NFLGameResponse> games) {
        return games.stream()
                .filter(f -> f.getHomeTeamAbbr().equals("BAL") )//|| f.getVisitorTeamAbbr().equals("BAL"))
                .collect(toList());
    }

    private NFLGameResponse[] getNflGameResponsesByYear(int year) {
        String authorizationToken = generateAuthorizationToken();
        RestTemplate template = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.add("Authorization", authorizationToken);

        return template.exchange("http://localhost:6569/league/schedule?season=" + year,
                HttpMethod.GET, null, NFLGameResponse[].class).getBody();
    }

    private String generateAuthorizationToken() {
        String HMAC_SHA1_ALGORITHM = "HmacSHA1";

        String username = "ravens_3rdparty_bytecubed_api";
        String password = "xKymIZL6fzgMksN4";
        String secretKey = "BHEUc/k6+ZyEEgBmQV7QxlQKYj3xMVbZon9QA4ZY";
        String accessToken = "AKIAJ67I5VLKQUHOZBVQ";
        String concatenatedString = "";

        /*
        date = datetime.now(tz=pytz.utc).astimezone(pytz.timezone('US/Pacific')).strftime('%m%d%Y')
        string_to_sign = username + password + access_token + date
         */

        LocalDate pst = LocalDate.now(ZoneId.of("America/Los_Angeles"));
        String date = pst.format(DateTimeFormatter.ofPattern("MMddyyyy"));
        concatenatedString = username + password + accessToken + date;

        String hmacSha1 = null;
        SecretKeySpec signingKey = new SecretKeySpec(secretKey.getBytes(), HMAC_SHA1_ALGORITHM);
        Mac mac;
        try {
            mac = Mac.getInstance(HMAC_SHA1_ALGORITHM);
            mac.init(signingKey);
            byte[] rawHmac = mac.doFinal(concatenatedString.getBytes());
            System.out.println(rawHmac);
            hmacSha1 = BaseEncoding.base64().encode(rawHmac);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
        return "NGS " + accessToken + ":" + hmacSha1;
    }


}