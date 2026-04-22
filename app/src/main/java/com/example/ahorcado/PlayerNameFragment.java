package com.example.ahorcado;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class PlayerNameFragment extends Fragment {

    private EditText playerNameEditText;
    private Button startGameButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_player_name, container, false);
        playerNameEditText = view.findViewById(R.id.player_name);
        startGameButton = view.findViewById(R.id.start_game_button);

        startGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String playerName = playerNameEditText.getText().toString().trim();
                if (!playerName.isEmpty()) {
                    handlePlayerName(playerName);
                }
            }
        });

        return view;
    }

    private void handlePlayerName(String playerName) {
        ScoreManager scoreManager = new ScoreManager(getActivity());
        int existingScore = scoreManager.getPlayerScore(playerName);

        if (existingScore > 0) {
            showContinueOrRestartDialog(playerName, existingScore);
        } else {
            startGame(playerName, 0);
        }
    }

    private void showContinueOrRestartDialog(final String playerName, int existingScore) {
        new AlertDialog.Builder(getActivity())
                .setTitle("Jugador Existente")
                .setMessage("El nombre ya existe con una puntuación de " + existingScore + ". ¿Quieres continuar con tu puntuación o empezar de nuevo?")
                .setPositiveButton("Continuar", (dialog, which) -> startGame(playerName, existingScore))
                .setNegativeButton("Empezar de Nuevo", (dialog, which) -> {
                    ScoreManager scoreManager = new ScoreManager(getActivity());
                    scoreManager.resetScore(playerName);
                    startGame(playerName, 0);
                })
                .show();
    }

    private void startGame(String playerName, int initialScore) {
        GameFragment gameFragment = new GameFragment();
        Bundle args = new Bundle();
        args.putString("PLAYER_NAME", playerName);
        args.putInt("INITIAL_SCORE", initialScore);
        gameFragment.setArguments(args);

        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, gameFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}

