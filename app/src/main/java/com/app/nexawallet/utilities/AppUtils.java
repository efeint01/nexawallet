package com.app.nexawallet.utilities;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.app.nexawallet.R;

public class AppUtils {

    // Copy text to the clipboard
    public static void copyToClipboard(Context context, String textToCopy) {
        ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("copied_text", textToCopy);
        clipboard.setPrimaryClip(clip);
    }

    // Paste text from the clipboard
    public static String pasteFromClipboard(Context context) {
        ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = clipboard.getPrimaryClip();
        if (clip != null && clip.getItemCount() > 0) {
            CharSequence text = clip.getItemAt(0).getText();
            if (text != null) {
                Toast.makeText(context, R.string.pasted_from_clipboard, Toast.LENGTH_SHORT).show();
                return text.toString();
            }
        }
        return "";
    }

    public static void shareText(Context context, String textToShare) {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, textToShare);

        Intent chooser = Intent.createChooser(shareIntent, context.getString(R.string.share_via));

        if (shareIntent.resolveActivity(context.getPackageManager()) != null) {
            context.startActivity(chooser);
        } else {
            // Handle the case where no app can handle the share action
            Toast.makeText(context, R.string.no_app_can_handle_this_action, Toast.LENGTH_SHORT).show();
        }
    }


}
