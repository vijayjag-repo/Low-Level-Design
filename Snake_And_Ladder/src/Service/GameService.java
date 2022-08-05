package Service;

import Constants.Constants;
import Model.Board;
import Model.Ladder;
import Model.Player;
import Model.Snake;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

public class GameService {
    private Board snakeLadderBoard;
    private int numberOfPlayers;
    private Queue<Player> players;
    private boolean isGameCompleted;

    public void setPlayers(List<Player> players){
        this.players = new LinkedList<Player>();
        this.numberOfPlayers = players.size();
        Map<String, Integer> playerPieces = new HashMap<String, Integer>();
        for (Player player: players){
            this.players.add(player);
            playerPieces.put(player.getId(), 0);
        }
        snakeLadderBoard.setPlayerPieces(playerPieces);
    }

    public void setSnakes(List<Snake> snakes){
        snakeLadderBoard.setSnakes(snakes);
    }

    public void setLadders(List<Ladder> ladders){
        snakeLadderBoard.setLadders(ladders);
    }

    public void startGame() {
        while (!isGameCompleted) {
            int diceValue = DiceService.roll();
            Player currentPlayer = players.poll();

            movePlayer(currentPlayer, diceValue);
            if (hasPlayerWon(currentPlayer)) {
                System.out.println(currentPlayer.getName() + " won the game.");
                snakeLadderBoard.getPlayerPieces().remove(currentPlayer.getId());
            } else {
                players.add(currentPlayer);
            }

        }
    }

    private void movePlayer(Player player, int diceValue) {
        int oldPosition = snakeLadderBoard.getPlayerPieces().get(player.getId());
        int newPosition = oldPosition + diceValue;

        int boardSize = snakeLadderBoard.getSize();

        if (newPosition > boardSize) {
            newPosition = oldPosition;
        } else {
            newPosition = getNewPositionAfterMoving(newPosition);
        }

        snakeLadderBoard.getPlayerPieces().put(player.getId(), newPosition);

        System.out.println("Player " + player.getName() + " rolled a " + diceValue + " and moved from " + oldPosition + " to " + newPosition);
    }

    private int getNewPositionAfterMoving(int newPosition) {
        int temporaryPosition;

        do {
            temporaryPosition = newPosition;
            for (Snake snake : snakeLadderBoard.getSnakes()) {
                if (snake.getStart() == newPosition) {
                    newPosition = snake.getEnd();
                }
            }

            for (Ladder ladder : snakeLadderBoard.getLadders()) {
                if (ladder.getStart() == newPosition) {
                    newPosition = ladder.getEnd();
                }
            }
        } while (newPosition != temporaryPosition);

        return newPosition;
    }

    private boolean hasPlayerWon(Player player) {
        int playerPosition = snakeLadderBoard.getPlayerPieces().get(player.getId());
        return playerPosition == Constants.BOARD_SIZE;
    }
}
