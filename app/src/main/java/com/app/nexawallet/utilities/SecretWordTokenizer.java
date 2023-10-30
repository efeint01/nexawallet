package com.app.nexawallet.utilities;

import android.widget.MultiAutoCompleteTextView;

public class SecretWordTokenizer implements MultiAutoCompleteTextView.Tokenizer {
    private char separator;

    public SecretWordTokenizer(char separator) {
        this.separator = separator;
    }

    @Override
    public int findTokenEnd(CharSequence cs, int pos) {
        if (cs == null) return pos;
        int curPos = pos;
        while (curPos < cs.length()) {
            if (cs.charAt(curPos) == separator) return curPos - 1;
            curPos++;
        }
        return curPos - 1;
    }

    @Override
    public int findTokenStart(CharSequence cs, int pos) {
        if (cs == null) return pos;
        int curPos = pos - 1;
        while (curPos > 0) {
            if (cs.charAt(curPos) == separator) return curPos + 1;
            curPos--;
        }
        return 0;
    }

    @Override
    public CharSequence terminateToken(CharSequence p) {
        return p.toString();
    }
}
