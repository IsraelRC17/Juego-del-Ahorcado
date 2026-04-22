package com.example.ahorcado;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class GameOverFragment extends Fragment {

    private TextView gameOverMessage;
    private Button playAgainButton;
    private Button viewScoresButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_game_over, container, false);
        gameOverMessage = view.findViewById(R.id.game_over_message);
        playAgainButton = view.findViewById(R.id.play_again_button);
        viewScoresButton = view.findViewById(R.id.view_scores_button);

        Bundle args = getArguments();
        if (args != null) {
            String message = args.getString("GAME_OVER_MESSAGE");
            gameOverMessage.setText(message);
        }

        playAgainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, new PlayerNameFragment());
                transaction.addToBackStack(null);
                transaction.commit();
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
}
