package com.example.ahorcado;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class GameFragment extends Fragment {

    private TextView wordDisplay;
    private TextView resultMessage;
    private EditText letterInput;
    private Button guessButton;
    private Button playAgainButton;
    private Button viewScoresButton;
    private ImageView hangmanImage;

    private String currentWord;
    private char[] wordDisplayArray;
    private List<String> wordList;
    private Set<String> usedWords;
    private int incorrectGuesses;
    private int playerScore;
    private String playerName;

    private static final int MAX_SCORE = 200;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_game, container, false);
        wordDisplay = view.findViewById(R.id.word_display);
        resultMessage = view.findViewById(R.id.result_message);
        letterInput = view.findViewById(R.id.letter_input);
        guessButton = view.findViewById(R.id.guess_button);
        hangmanImage = view.findViewById(R.id.hangman_image);
        playAgainButton = view.findViewById(R.id.play_again_button);
        viewScoresButton = view.findViewById(R.id.view_scores_button);

        Bundle args = getArguments();
        if (args != null) {
            playerName = args.getString("PLAYER_NAME", "Jugador");
            playerScore = args.getInt("INITIAL_SCORE", 0);
        }

        // palabras para el juego
        wordList = new ArrayList<>(Arrays.asList("computadora", "teclado", "mouse", "lapiz", "pantalla", "monitor", "escritorio", "internet", "software", "hardware", "proyecto", "programacion", "desarrollo", "codigo", "aplicacion", "telefono", "tablet", "impresora", "red", "sistema", "unitec"));
        usedWords = new HashSet<>();
        // Iniciar el juego
        startNewGame();

        guessButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String letter = letterInput.getText().toString().toLowerCase();
                if (!letter.isEmpty() && letter.length() == 1 && (Character.isLetter(letter.charAt(0)) || letter.charAt(0) == 'ñ')) {
                    handleGuess(letter.charAt(0));
                    letterInput.setText("");
                } else {
                    Toast.makeText(getActivity(), "Por favor, ingresa una letra válida.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        playAgainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startNewGame();
            }
        });

        viewScoresButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, new ScoreFragment());
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        return view;
    }

    private void startNewGame() {
        if (usedWords.size() == wordList.size()) {
            usedWords.clear();
        }

        Random random = new Random();
        do {
            currentWord = wordList.get(random.nextInt(wordList.size()));
        } while (usedWords.contains(currentWord));

        usedWords.add(currentWord);
        wordDisplayArray = new char[currentWord.length()];
        Arrays.fill(wordDisplayArray, '_');
        incorrectGuesses = 0;
        resultMessage.setVisibility(View.GONE);
        playAgainButton.setVisibility(View.GONE);
        viewScoresButton.setVisibility(View.GONE);
        letterInput.setEnabled(true);
        guessButton.setEnabled(true);
        updateWordDisplay();
        updateHangmanImage();
    }

    private void handleGuess(char guessedLetter) {
        boolean isCorrect = false;
        for (int i = 0; i < currentWord.length(); i++) {
            if (currentWord.charAt(i) == guessedLetter) {
                wordDisplayArray[i] = guessedLetter;
                isCorrect = true;
            }
        }
        if (!isCorrect) {
            incorrectGuesses++;
        }
        updateWordDisplay();
        updateHangmanImage();
        checkGameOver();
    }

    private void updateWordDisplay() {
        StringBuilder displayString = new StringBuilder();
        for (char c : wordDisplayArray) {
            displayString.append(c).append(' ');
        }
        wordDisplay.setText(displayString.toString().trim());
    }

    private void updateHangmanImage() {
        switch (incorrectGuesses) {
            case 1:
                hangmanImage.setImageResource(R.drawable.error1);
                break;
            case 2:
                hangmanImage.setImageResource(R.drawable.error2);
                break;
            case 3:
                hangmanImage.setImageResource(R.drawable.error3);
                break;
            case 4:
                hangmanImage.setImageResource(R.drawable.error4);
                break;
            case 5:
                hangmanImage.setImageResource(R.drawable.error5);
                break;
            case 6:
                hangmanImage.setImageResource(R.drawable.error6);
                break;
            case 7:
                hangmanImage.setImageResource(R.drawable.error7);
                break;
            default:
                hangmanImage.setImageResource(R.drawable.error1);
        }
    }

    private void checkGameOver() {
        if (incorrectGuesses >= 7) {
            resultMessage.setText("Has perdido. La palabra era: " + currentWord);
            resultMessage.setVisibility(View.VISIBLE);
            playAgainButton.setVisibility(View.VISIBLE);
            viewScoresButton.setVisibility(View.VISIBLE);
            savePlayerScore();
            letterInput.setEnabled(false);
            guessButton.setEnabled(false);
        } else if (new String(wordDisplayArray).equals(currentWord)) {
            resultMessage.setText("¡Felicidades! Has adivinado la palabra: " + currentWord);
            resultMessage.setVisibility(View.VISIBLE);
            playAgainButton.setVisibility(View.VISIBLE);
            viewScoresButton.setVisibility(View.VISIBLE);
            playerScore += 10;
            savePlayerScore();
            letterInput.setEnabled(false);
            guessButton.setEnabled(false);
        }
    }

    private void savePlayerScore() {
        ScoreManager scoreManager = new ScoreManager(getActivity());
        scoreManager.saveScore(playerName, playerScore);
    }

    private int getPlayerScore(String playerName) {
        ScoreManager scoreManager = new ScoreManager(getActivity());
        return scoreManager.getPlayerScore(playerName);
    }
}
