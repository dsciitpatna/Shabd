package com.dsciitp.shabd.Learn.Drawing;

import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Display;
import android.view.View;
import android.widget.Button;

import com.dsciitp.shabd.R;
import com.pes.androidmaterialcolorpickerdialog.ColorPicker;

import java.util.Random;

import me.panavtec.drawableview.DrawableView;
import me.panavtec.drawableview.DrawableViewConfig;

public class DrawingActivity extends AppCompatActivity {

    private DrawableView drawableView;
    private DrawableViewConfig config;
    private int selectedColorR;
    private int selectedColorG;
    private int selectedColorB;
    private int selectedColorRGB;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawing2);
        drawableView= findViewById(R.id.paintView);
        Button strokeWidthMinusButton =  findViewById(R.id.strokeWidthMinusButton);
        Button strokeWidthPlusButton = findViewById(R.id.strokeWidthPlusButton);
        Button changeColorButton =  findViewById(R.id.changeColorButton);
        Button undoButton =  findViewById(R.id.undoButton);
        Display display = getWindowManager(). getDefaultDisplay();
        Point size = new Point();
        display. getSize(size);
        int width = size. x;
        int height = size. y-200;
        final ColorPicker cp = new ColorPicker(DrawingActivity.this, 25, 30, 40);

        config= new DrawableViewConfig();
        config.setStrokeColor(getResources().getColor(android.R.color.black));
        config.setShowCanvasBounds(true); // If the view is bigger than canvas, with this the user will see the bounds (Recommended)
        config.setStrokeWidth(20.0f);
        config.setMinZoom(1.0f);
        config.setMaxZoom(1.0f);
        config.setCanvasHeight(height);
        config.setCanvasWidth(width);
        drawableView.setConfig(config);
        strokeWidthPlusButton.setOnClickListener(new View.OnClickListener() {

            @Override public void onClick(View v) {
                config.setStrokeWidth(config.getStrokeWidth() + 10);
            }
        });
        strokeWidthMinusButton.setOnClickListener(new View.OnClickListener() {

            @Override public void onClick(View v) {
                config.setStrokeWidth(config.getStrokeWidth() - 10);
            }
        });
        changeColorButton.setOnClickListener(new View.OnClickListener() {

            @Override public void onClick(View v) {
                Random random = new Random();
                cp.show();
                Button okColor = cp.findViewById(R.id.okColorButton);

                okColor.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        /* You can get single channel (value 0-255) */
                        selectedColorR = cp.getRed();
                        selectedColorG = cp.getGreen();
                        selectedColorB = cp.getBlue();

                        /* Or the android RGB Color (see the android Color class reference) */
                        selectedColorRGB = cp.getColor();
                        config.setStrokeColor(
                                Color.argb(255, selectedColorR, selectedColorG, selectedColorB));
                        cp.dismiss();
                    }
                });

            }
        });
        undoButton.setOnClickListener(new View.OnClickListener() {

            @Override public void onClick(View v) {
                drawableView.undo();
            }
        });
    }
}
