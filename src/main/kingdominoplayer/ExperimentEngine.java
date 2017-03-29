package kingdominoplayer;

import kingdominoplayer.naiverepresentation.datastructures.GameState;
import kingdominoplayer.tinyrepresentation.gamestrategies.TinyStrategyID;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

/**
 * Copyright 2017 Tomologic AB<br>
 * User: gedda<br>
 * Date: 2017-03-06<br>
 * Time: 19:47<br><br>
 */
public class ExperimentEngine
{

    public static void main(final String[] args) throws IOException
    {
        final String usage = "Usage: java -jar kdom-exp <playerStrategy> <opponentStrategy> <outputFile>";

        String playerStrategy = "";
        String opponentStrategy = "";
        String outputFile = "";

        if (args.length == 3)
        {
            playerStrategy = args[0];
            opponentStrategy = args[1];
            outputFile = args[2];
        }
        else
        {
            System.err.println(usage);
            System.exit(0);
        }

        final Game game = GameServer.startGame(4);

        final String opponent1Name = "Opponent1";
        final String opponent2Name = "Opponent2";
        final String opponent3Name = "Opponent3";
        final String playerName = "Player";

        final TinyStrategyID opponentStrategyID = TinyStrategyID.valueOf(opponentStrategy);
        final TinyStrategyID playerStrategyID = TinyStrategyID.valueOf(playerStrategy);

        final String opponent1UUID = game.addPlayer(opponent1Name);
        final String opponent2UUID = game.addPlayer(opponent2Name);
        final String opponent3UUID = game.addPlayer(opponent3Name);

        final String playerUUID = game.addPlayer(playerName);
        final TinyPlayer player = new TinyPlayer(playerUUID, playerName, playerStrategyID, false);

        final ArrayList<Player> players = new ArrayList<>(4);
        players.add(new TinyPlayer(opponent1UUID, opponent1Name, opponentStrategyID, false));
        players.add(new TinyPlayer(opponent2UUID, opponent2Name, opponentStrategyID, false));
        players.add(new TinyPlayer(opponent3UUID, opponent3Name, opponentStrategyID, false));
        players.add(player);

        final GameState gameState = game.play(players);

        game.printResult();

        final ExperimentResult experimentResult = new ExperimentResult(gameState, player);
        experimentResult.appendToFile(outputFile);
    }


    private static class ExperimentResult
    {

        private final GameState iGameState;
        private final Player iPlayer;

        public ExperimentResult(final GameState gameState, final Player player)
        {
            iGameState = gameState;
            iPlayer = player;
        }

        private int getPlayerWinValue(final String playerName, final GameState gameState)
        {
            final Map<String, Integer> scores = gameState.getScores();

            final int playerScore = scores.get(playerName);

            scores.remove(playerName);

            final Collection<Integer> opponentScores = scores.values();

            int maxOpponentScore = 0;
            for (final int opponentScore : opponentScores)
            {
                if (opponentScore > maxOpponentScore)
                {
                    maxOpponentScore = opponentScore;
                }
            }

            return playerScore > maxOpponentScore ? 1 : maxOpponentScore > playerScore ? -1 : 0;
        }

        @Override
        public String toString()
        {
            final int playerWinValue = getPlayerWinValue(iPlayer.getName(), iGameState);
            final int playerScore = iGameState.getScore(iPlayer.getName());
            final int numPlayers = iGameState.getNumPlayers();

            String numAvailableMovesString = "";
            final int[] numAvailableMovesArray = iPlayer.getNumAvailableMoves();
            for (int i = 0; i < numAvailableMovesArray.length; ++i)
            {
                final int numAvailableMoves = numAvailableMovesArray[i];
                numAvailableMovesString = i == numAvailableMovesArray.length - 1
                        ? numAvailableMovesString.concat(Integer.toString(numAvailableMoves))
                        : numAvailableMovesString.concat(Integer.toString(numAvailableMoves) + ", ");
            }

            String numAvailableDraftString = "";
            final int[] numAvailableDraftArray = iPlayer.getNumAvailableDraft();
            for (int i = 0; i < numAvailableDraftArray.length; ++i)
            {
                final int numAvailableDraft = numAvailableDraftArray[i];
                numAvailableDraftString = i == numAvailableDraftArray.length - 1
                        ? numAvailableDraftString.concat(Integer.toString(numAvailableDraft))
                        : numAvailableDraftString.concat(Integer.toString(numAvailableDraft) + ", ");
            }

            String chosenDraftPositionString = "";
            final int[] chosenDraftPositions = iPlayer.getChosenDraftPositions();
            for (int i = 0; i < chosenDraftPositions.length; ++i)
            {
                final int chosenDraftPosition = chosenDraftPositions[i];
                chosenDraftPositionString = i == chosenDraftPositions.length - 1
                        ? chosenDraftPositionString.concat(Integer.toString(chosenDraftPosition))
                        : chosenDraftPositionString.concat(Integer.toString(chosenDraftPosition) + ", ");
            }

            String numPlayoutsPerSecondString = "";
            final double[] numPlayoutsPerSecondArray = iPlayer.getNumPlayoutsPerSecond();
            for (int i = 0; i < numPlayoutsPerSecondArray.length; ++i)
            {
                final double numPlayoutsPerSecond = numPlayoutsPerSecondArray[i];
                numPlayoutsPerSecondString = i == numPlayoutsPerSecondArray.length - 1
                        ? numPlayoutsPerSecondString.concat(String.format("%.1f", numPlayoutsPerSecond))
                        : numPlayoutsPerSecondString.concat(String.format("%.1f", numPlayoutsPerSecond) + ", ");
            }

            String scoresString = "";
            final int[] scores = iPlayer.getScores();
            for (int i = 0; i < scores.length; ++i)
            {
                final int score = scores[i];
                scoresString = i == scores.length - 1
                        ? scoresString.concat(Integer.toString(playerScore))
                        : scoresString.concat(Integer.toString(score) + ", ");
            }

            final double scoreDifference = getPlayerScoreDifferenceToBestOpponent();


            String opponentScoresString = "";
            final int[] opponentScores = getOpponentScoresAsFixedArray();
            for (int i = 0; i < opponentScores.length; ++i)
            {
                opponentScoresString = i == opponentScores.length - 1
                        ? opponentScoresString.concat(Integer.toString(opponentScores[i]))
                        : opponentScoresString.concat(Integer.toString(opponentScores[i]) + ", ");
            }


            return Integer.toString(playerWinValue) + ", "
                    + Integer.toString(playerScore) + ", "
                    + Integer.toString(numPlayers) + ", "
                    + numAvailableMovesString + ", "
                    + numAvailableDraftString + ", "
                    + chosenDraftPositionString + ", "
                    + numPlayoutsPerSecondString + ", "
                    + scoresString + ", "
                    + String.format("%.0f", scoreDifference) + ", "
                    + opponentScoresString
                    + ";\n";
        }

        private int[] getOpponentScoresAsFixedArray()
        {
            final int[] opponentScores = {-1, -1, -1};
            int counter = 0;

            for (int opponentScore : getOpponentScores())
            {
                opponentScores[counter] = opponentScore;
                counter++;
            }
            return opponentScores;
        }


        private double getPlayerScoreDifferenceToBestOpponent()
        {
            final int playerScore = iGameState.getScore(iPlayer.getName());
            final Collection<Integer> opponentScores = getOpponentScores();

            final ArrayList<Integer> opponentScoresSorted = new ArrayList<>(opponentScores.size());
            opponentScoresSorted.addAll(opponentScores);
            opponentScoresSorted.sort((Integer s1, Integer s2) ->
            {
                //noinspection CodeBlock2Expr
                return s1 > s2 ? -1 : s2 > s1 ? 1 : 0;
            });

            final Integer topOpponentScore = opponentScoresSorted.get(0);

            return (double)(playerScore - topOpponentScore);
        }

        private Collection<Integer> getOpponentScores()
        {
            final Map<String, Integer> opponentScores = iGameState.getScores();
            opponentScores.remove(iPlayer.getName());
            return opponentScores.values();
        }

        private void appendToFile(final String filename)
        {
            try
            {
                final File outputFile = new File(filename);

                if (!outputFile.exists())
                {
                    //noinspection ResultOfMethodCallIgnored
                    outputFile.createNewFile();
                    Files.write(Paths.get(filename), "# win, score, num_players, num_available_moves(13), num_available_draft(13), chosen_draft_position(13)\n".getBytes(), StandardOpenOption.APPEND);
                }

                final String resultString = toString();
                Files.write(Paths.get(filename), resultString.getBytes(), StandardOpenOption.APPEND);
            }
            catch (IOException e)
            {
                System.err.print(e);
                System.exit(2);
            }
        }
    }
}
