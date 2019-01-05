package com.bytecubed.team.provisioning;

import com.bytecubed.commons.models.Player;
import com.bytecubed.team.repository.TeamRegistry;
import com.bytecubed.team.web.NFLResponse;
import com.bytecubed.team.web.TeamController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TeamRosterLoader {

    private TeamController teamController;
    private Logger logger = LoggerFactory.getLogger(TeamRosterLoader.class);

    public TeamRosterLoader(TeamController teamController) {
        this.teamController = teamController;
    }

    public List<Player> load() {

        NFLResponse response = new RestTemplate().getForObject("http://api.fantasy.nfl.com/v1/players/stats?statType=seasonStats&season=2018&week=1&format=json", NFLResponse.class);
        logger.debug( "Player Response size:  " + response.getPlayers().size());

        return response.getPlayers().stream()
                .map(n->n.toPlayer(new TeamRegistry()))
                .collect(Collectors.toList());
    }

}
