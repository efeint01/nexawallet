package com.app.nexawallet.fragments;

import static bitcoinunlimited.libbitcoincash.Bip39Kt.Bip39InvalidWords;
import static bitcoinunlimited.libbitcoincash.Bip39Kt.getEnglishWordList;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.app.nexawallet.R;
import com.app.nexawallet.activities.MainActivity;
import com.app.nexawallet.databinding.FragmentRestoreWalletBinding;
import com.app.nexawallet.utilities.WalletManager;
import com.app.nexawallet.utilities.SecretWordTokenizer;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;


public class RestoreWalletFragment extends Fragment {

    FragmentRestoreWalletBinding binding;
    String TAG = RestoreWalletFragment.class.getSimpleName();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentRestoreWalletBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews();
    }


    private void initViews() {
        binding.toolbar.setNavigationOnClickListener(v -> getParentFragmentManager().popBackStack());

        //Set auto complete textviews bip39 word list

        SecretWordTokenizer secretWordTokenizer = new SecretWordTokenizer(' ');
        ArrayAdapter<String> wordsAdapter = new ArrayAdapter<>(getContext(), R.layout.spinner_recovery_phrase_selection, getEnglishWordList());

        binding.secretWordTw.setAdapter(wordsAdapter);
        binding.secretWordTw2.setAdapter(wordsAdapter);
        binding.secretWordTw3.setAdapter(wordsAdapter);
        binding.secretWordTw4.setAdapter(wordsAdapter);
        binding.secretWordTw5.setAdapter(wordsAdapter);
        binding.secretWordTw6.setAdapter(wordsAdapter);
        binding.secretWordTw7.setAdapter(wordsAdapter);
        binding.secretWordTw8.setAdapter(wordsAdapter);
        binding.secretWordTw9.setAdapter(wordsAdapter);
        binding.secretWordTw10.setAdapter(wordsAdapter);
        binding.secretWordTw11.setAdapter(wordsAdapter);
        binding.secretWordTw12.setAdapter(wordsAdapter);

        binding.secretWordTw.setTokenizer(secretWordTokenizer);
        binding.secretWordTw2.setTokenizer(secretWordTokenizer);
        binding.secretWordTw3.setTokenizer(secretWordTokenizer);
        binding.secretWordTw4.setTokenizer(secretWordTokenizer);
        binding.secretWordTw5.setTokenizer(secretWordTokenizer);
        binding.secretWordTw6.setTokenizer(secretWordTokenizer);
        binding.secretWordTw7.setTokenizer(secretWordTokenizer);
        binding.secretWordTw8.setTokenizer(secretWordTokenizer);
        binding.secretWordTw9.setTokenizer(secretWordTokenizer);
        binding.secretWordTw10.setTokenizer(secretWordTokenizer);
        binding.secretWordTw11.setTokenizer(secretWordTokenizer);
        binding.secretWordTw12.setTokenizer(secretWordTokenizer);

        binding.secretWordTw.addTextChangedListener(secretWordWatcher);
        binding.secretWordTw2.addTextChangedListener(secretWordWatcher);
        binding.secretWordTw3.addTextChangedListener(secretWordWatcher);
        binding.secretWordTw4.addTextChangedListener(secretWordWatcher);
        binding.secretWordTw5.addTextChangedListener(secretWordWatcher);
        binding.secretWordTw6.addTextChangedListener(secretWordWatcher);
        binding.secretWordTw7.addTextChangedListener(secretWordWatcher);
        binding.secretWordTw8.addTextChangedListener(secretWordWatcher);
        binding.secretWordTw9.addTextChangedListener(secretWordWatcher);
        binding.secretWordTw10.addTextChangedListener(secretWordWatcher);
        binding.secretWordTw11.addTextChangedListener(secretWordWatcher);
        binding.secretWordTw12.addTextChangedListener(secretWordWatcher);

        setBackspaceListener(binding.secretWordTw, binding.secretWordTw2);
        setBackspaceListener(binding.secretWordTw2, binding.secretWordTw3);
        setBackspaceListener(binding.secretWordTw3, binding.secretWordTw4);
        setBackspaceListener(binding.secretWordTw4, binding.secretWordTw5);
        setBackspaceListener(binding.secretWordTw5, binding.secretWordTw6);
        setBackspaceListener(binding.secretWordTw6, binding.secretWordTw7);
        setBackspaceListener(binding.secretWordTw7, binding.secretWordTw8);
        setBackspaceListener(binding.secretWordTw8, binding.secretWordTw9);
        setBackspaceListener(binding.secretWordTw9, binding.secretWordTw10);
        setBackspaceListener(binding.secretWordTw10, binding.secretWordTw11);
        setBackspaceListener(binding.secretWordTw11, binding.secretWordTw12);

        pasteMnemonicFromClipboard();


        binding.doneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String secretWords = getSecretWordsText();
                WalletManager walletManager = new WalletManager(getContext());

                Executor executor = Executors.newSingleThreadExecutor();
                executor.execute(new Runnable() {
                    @Override
                    public void run() {
                        walletManager.recoverWallet(secretWords);
                        startActivity(new Intent(getActivity(), MainActivity.class));
                    }
                });


            }
        });

    }

    private String getSecretWordsText() {
        // Get text from your EditText fields and concatenate it into a single string
        String secretWords = binding.secretWordTw.getText().toString().trim() + " " +
                binding.secretWordTw2.getText().toString().trim() + " " +
                binding.secretWordTw3.getText().toString().trim() + " " +
                binding.secretWordTw4.getText().toString().trim() + " " +
                binding.secretWordTw5.getText().toString().trim() + " " +
                binding.secretWordTw6.getText().toString().trim() + " " +
                binding.secretWordTw7.getText().toString().trim() + " " +
                binding.secretWordTw8.getText().toString().trim() + " " +
                binding.secretWordTw9.getText().toString().trim() + " " +
                binding.secretWordTw10.getText().toString().trim() + " " +
                binding.secretWordTw11.getText().toString().trim() + " " +
                binding.secretWordTw12.getText().toString().trim() + " ";
        return secretWords.trim();
    }


    public void setBackspaceListener(final EditText previousEditText, final EditText currentEditText) {
        currentEditText.setOnKeyListener((v, keyCode, event) -> {
            if (keyCode == KeyEvent.KEYCODE_DEL && event.getAction() == KeyEvent.ACTION_DOWN) {
                // Check if the current EditText is empty
                if (currentEditText.getText().toString().isEmpty()) {
                    // Move focus to the previous EditText
                    previousEditText.requestFocus();
                    return true; // Consume the event
                }
            }
            return false; // Allow default backspace behavior
        });
    }


    TextWatcher secretWordWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            enableButtonIfValidWords();
        }
    };

    private void enableButtonIfValidWords() {
        boolean allFieldsFilled = checkAllFieldsFilled();
        // Enable the button if all fields are filled and words are valid
        binding.doneBtn.setEnabled(allFieldsFilled && isValidWords(getAllEditTextValues()));
        if (allFieldsFilled) {
            binding.invalidWordsTw.setVisibility(isValidWords(getAllEditTextValues()) ? View.GONE : View.VISIBLE);
        }

    }

    private boolean checkAllFieldsFilled() {
        // Check if all secret word EditText fields are not empty
        return !TextUtils.isEmpty(binding.invalidWordsTw.getText()) &&
                !TextUtils.isEmpty(binding.secretWordTw2.getText()) &&
                !TextUtils.isEmpty(binding.secretWordTw3.getText()) &&
                !TextUtils.isEmpty(binding.secretWordTw4.getText()) &&
                !TextUtils.isEmpty(binding.secretWordTw5.getText()) &&
                !TextUtils.isEmpty(binding.secretWordTw6.getText()) &&
                !TextUtils.isEmpty(binding.secretWordTw7.getText()) &&
                !TextUtils.isEmpty(binding.secretWordTw8.getText()) &&
                !TextUtils.isEmpty(binding.secretWordTw9.getText()) &&
                !TextUtils.isEmpty(binding.secretWordTw10.getText()) &&
                !TextUtils.isEmpty(binding.secretWordTw11.getText()) &&
                !TextUtils.isEmpty(binding.secretWordTw12.getText());
    }

    private List<String> getAllEditTextValues() {
        // Get all the secret word EditText values and store them in a list
        List<String> editTextValues = new ArrayList<>();
        editTextValues.add(binding.secretWordTw.getText().toString());
        editTextValues.add(binding.secretWordTw2.getText().toString());
        editTextValues.add(binding.secretWordTw3.getText().toString());
        editTextValues.add(binding.secretWordTw4.getText().toString());
        editTextValues.add(binding.secretWordTw5.getText().toString());
        editTextValues.add(binding.secretWordTw6.getText().toString());
        editTextValues.add(binding.secretWordTw7.getText().toString());
        editTextValues.add(binding.secretWordTw8.getText().toString());
        editTextValues.add(binding.secretWordTw9.getText().toString());
        editTextValues.add(binding.secretWordTw10.getText().toString());
        editTextValues.add(binding.secretWordTw11.getText().toString());
        editTextValues.add(binding.secretWordTw12.getText().toString());

        return editTextValues;
    }

    private boolean isValidWords(List<String> words) {
        List<String> wordsList = Bip39InvalidWords(words);
        return wordsList.isEmpty();
    }

    private void pasteMnemonicFromClipboard() {
        // Get the clipboard manager
        ClipboardManager clipboard = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
        if (clipboard != null) {
            if (clipboard.hasPrimaryClip()) {
                ClipData clipData = clipboard.getPrimaryClip();

                if (clipData != null && clipData.getItemCount() >= 1) {
                    // Get the first item from the clipboard
                    CharSequence clipboardText = clipData.getItemAt(0).getText().toString().trim();

                    // Split the clipboard text into words (assuming it's space-separated)
                    String[] clipboardWords = clipboardText.toString().split(" ");

                    // Check if the clipboard contains exactly 12 words
                    if (clipboardWords.length == 12) {
                        // Set the words in your EditText fields
                        binding.secretWordTw.setText(clipboardWords[0]);
                        binding.secretWordTw2.setText(clipboardWords[1]);
                        binding.secretWordTw3.setText(clipboardWords[2]);
                        binding.secretWordTw4.setText(clipboardWords[3]);
                        binding.secretWordTw5.setText(clipboardWords[4]);
                        binding.secretWordTw6.setText(clipboardWords[5]);
                        binding.secretWordTw7.setText(clipboardWords[6]);
                        binding.secretWordTw8.setText(clipboardWords[7]);
                        binding.secretWordTw9.setText(clipboardWords[8]);
                        binding.secretWordTw10.setText(clipboardWords[9]);
                        binding.secretWordTw11.setText(clipboardWords[10]);
                        binding.secretWordTw12.setText(clipboardWords[11]);

                        enableButtonIfValidWords();

                    } else {
                        // Handle the case where there are not exactly 12 words
                    }
                }

            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}