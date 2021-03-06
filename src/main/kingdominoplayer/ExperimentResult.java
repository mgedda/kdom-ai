package kingdominoplayer;

/*
 * Copyright (c) 2018 Magnus Gedda<br>
 * User: gedda<br>
 * Date: 2018-01-04<br>
 * Time: 23:05<br><br>
 */

import kingdominoplayer.naiverepresentation.datastructures.GameState;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

public class ExperimentResult
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
        final String separator = " ";

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
                    : numAvailableMovesString.concat(Integer.toString(numAvailableMoves) + separator);
        }

        String numAvailableDraftString = "";
        final int[] numAvailableDraftArray = iPlayer.getNumAvailableDraft();
        for (int i = 0; i < numAvailableDraftArray.length; ++i)
        {
            final int numAvailableDraft = numAvailableDraftArray[i];
            numAvailableDraftString = i == numAvailableDraftArray.length - 1
                    ? numAvailableDraftString.concat(Integer.toString(numAvailableDraft))
                    : numAvailableDraftString.concat(Integer.toString(numAvailableDraft) + separator);
        }

        String chosenDraftPositionString = "";
        final int[] chosenDraftPositions = iPlayer.getChosenDraftPositions();
        for (int i = 0; i < chosenDraftPositions.length; ++i)
        {
            final int chosenDraftPosition = chosenDraftPositions[i];
            chosenDraftPositionString = i == chosenDraftPositions.length - 1
                    ? chosenDraftPositionString.concat(Integer.toString(chosenDraftPosition))
                    : chosenDraftPositionString.concat(Integer.toString(chosenDraftPosition) + separator);
        }

        String numPlayoutsPerSecondString = "";
        final double[] numPlayoutsPerSecondArray = iPlayer.getNumPlayoutsPerSecond();
        for (int i = 0; i < numPlayoutsPerSecondArray.length; ++i)
        {
            final double numPlayoutsPerSecond = numPlayoutsPerSecondArray[i];
            numPlayoutsPerSecondString = i == numPlayoutsPerSecondArray.length - 1
                    ? numPlayoutsPerSecondString.concat(String.format("%.1f", numPlayoutsPerSecond))
                    : numPlayoutsPerSecondString.concat(String.format("%.1f", numPlayoutsPerSecond) + separator);
        }

        String scoresString = "";
        final int[] scores = iPlayer.getScores();
        for (int i = 0; i < scores.length; ++i)
        {
            final int score = scores[i];
            scoresString = i == scores.length - 1
                    ? scoresString.concat(Integer.toString(playerScore))
                    : scoresString.concat(Integer.toString(score) + separator);
        }

        final double scoreDifference = getPlayerScoreDifferenceToBestOpponent();


        String opponentScoresString = "";
        final int[] opponentScores = getOpponentScoresAsFixedArray();
        for (int i = 0; i < opponentScores.length; ++i)
        {
            opponentScoresString = i == opponentScores.length - 1
                    ? opponentScoresString.concat(Integer.toString(opponentScores[i]))
                    : opponentScoresString.concat(Integer.toString(opponentScores[i]) + separator);
        }


        return Integer.toString(playerWinValue) + separator
                + Integer.toString(playerScore) + separator
                + Integer.toString(numPlayers) + separator
                + numAvailableMovesString + separator
                + numAvailableDraftString + separator
                + chosenDraftPositionString + separator
                + numPlayoutsPerSecondString + separator
                + scoresString + separator
                + String.format("%.0f", scoreDifference) + separator
                + opponentScoresString
                + "\n";
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

    public void appendToFile(final String filename)
    {
        try
        {
            final File outputFile = new File(filename);

            if (!outputFile.exists())
            {
                //noinspection ResultOfMethodCallIgnored
                outputFile.createNewFile();
                //Files.write(Paths.get(filename), "# win, score, num_players, num_available_moves(13), num_available_draft(13), chosen_draft_position(13)\n".getBytes(), StandardOpenOption.APPEND);
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
