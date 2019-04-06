package com.dsciitp.shabd.Setting;

import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.style.ImageSpan;
import android.widget.TextView;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;


public class TextJustification {

    public static void justify(final TextView textView) {


        final AtomicBoolean isJustify = new AtomicBoolean(false);


        final String textString = textView.getText().toString();


        final TextPaint textPaint = textView.getPaint();

        CharSequence textViewText = textView.getText();


        final Spannable builder = textViewText instanceof Spannable ?
                (Spannable) textViewText :
                new SpannableString(textString);


        textView.post(new Runnable() {
            @Override
            public void run() {


                if (!isJustify.get()) {


                    final int lineCount = textView.getLineCount();

                    final int textViewWidth = textView.getWidth();

                    for (int i = 0; i < lineCount; i++) {


                        int lineStart = textView.getLayout().getLineStart(i);
                        int lineEnd = textView.getLayout().getLineEnd(i);

                        String lineString = textString.substring(lineStart, lineEnd);


                        if (i == lineCount - 1) {
                            break;
                        }


                        String trimSpaceText = lineString.trim();
                        String removeSpaceText = lineString.replaceAll(" ", "");

                        float removeSpaceWidth = textPaint.measureText(removeSpaceText);
                        float spaceCount = trimSpaceText.length() - removeSpaceText.length();

                        float eachSpaceWidth = (textViewWidth - removeSpaceWidth) / spaceCount;

                        Set<Integer> endsSpace = spacePositionInEnds(lineString);
                        for (int j = 0; j < lineString.length(); j++) {
                            char c = lineString.charAt(j);

                            Drawable drawable = new ColorDrawable(0x00ffffff);

                            if (c == ' ') {
                                if (endsSpace.contains(j)) {
                                    drawable.setBounds(0, 0, 0, 0);
                                } else {
                                    drawable.setBounds(0, 0, (int) eachSpaceWidth, 0);
                                }
                                ImageSpan span = new ImageSpan(drawable);
                                builder.setSpan(span, lineStart + j, lineStart + j + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                            }
                        }
                    }

                    textView.setText(builder);
                    isJustify.set(true);
                }
            }
        });
    }

    private static Set<Integer> spacePositionInEnds(String string) {
        Set<Integer> result = new HashSet<>();
        for (int i = 0; i < string.length(); i++) {
            char c = string.charAt(i);
            if (c == ' ') {
                result.add(i);
            } else {
                break;
            }
        }

        if (result.size() == string.length()) {
            return result;
        }

        for (int i = string.length() - 1; i > 0; i--) {
            char c = string.charAt(i);
            if (c == ' ') {
                result.add(i);
            } else {
                break;
            }
        }

        return result;
    }
}

