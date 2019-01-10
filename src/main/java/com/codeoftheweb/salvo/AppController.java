package com.codeoftheweb.salvo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;


@RestController

@RequestMapping("/api")
public class AppController {

    @Autowired
    private PlayerRepository playerRepo;
    @Autowired
    private GameRepository gameRepo;
    @Autowired
    private GamePlayerRepository gamePlayerRepo;

    @RequestMapping("/players")
    public List<Player> getPlayer() {
        return playerRepo.findAll();
    }

    @RequestMapping("/games")
    public List<Object> getGame() {
        return gameRepo.findAll().stream().map(Game -> gameDTO(Game)).collect(toList());
    }

    private Map<String, Object> gameDTO(Game game) {
        Map<String, Object> dto = new LinkedHashMap<String, Object>();
        dto.put("id", game.getId());
        dto.put("created", game.getDate());
        dto.put("gamePlayers", game.getGamePlayers()
                .stream()
                .map(gamePlayer -> gamePlayerDTO(gamePlayer))
                .collect(toList()));
        return dto;
    }

    private Map<String, Object> playerDTO(Player player) {
        Map<String, Object> dto = new LinkedHashMap<String, Object>();
        dto.put("id", player.getId());
        dto.put("userName", player.getUserName());
        dto.put("email", player.getEmail());
        return dto;
    }

    private Map<String, Object> gamePlayerDTO(GamePlayer gamePlayer) {
        Map<String, Object> dto = new LinkedHashMap<String, Object>();
        dto.put("id", gamePlayer.getId());
        dto.put("player", playerDTO(gamePlayer.getPlayer()));

        return dto;
    }

    @RequestMapping("/gamePlayers")
    public List<GamePlayer> getGamePlayer() {
        return gamePlayerRepo.findAll();
    }

    @RequestMapping("/game_view/{gameId}")
    public Map<String, Object> gameViewDTO(@PathVariable Long gameId) {
        GamePlayer gamePlayer = gamePlayerRepo.getOne(gameId);

        Map<String, Object> dto = new LinkedHashMap<String, Object>();
        dto.put("id", gamePlayer.getGame().getId());
        dto.put("created", gamePlayer.getGame().getDate());
        dto.put("GamePlayers", gamePlayer.getGame().getGamePlayers().stream().map(gamePlayer1 -> gamePlayerDTO(gamePlayer1)).collect(Collectors.toList()));
        dto.put("Ships",gamePlayer.getShips().stream().map(ship -> shipDTO(ship)).collect(Collectors.toList()));

        return dto;
    }

    @RequestMapping("/ships")
    public Map<String, Object> shipDTO(Ship ship) {

        Map<String, Object> dto = new LinkedHashMap<String, Object>();
        dto.put("Ship_Id", ship.getId());
        dto.put("Type", ship.getType());
        dto.put("Ship_Location", ship.getMyLocation());

        return dto;
    }
}


