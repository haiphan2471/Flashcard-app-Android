package com.example.flashcard_mine;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;

import org.w3c.dom.Text;

import java.util.List;


public class MainActivity extends AppCompatActivity {

    FlashcardDatabase flashcardDatabase;
    List<Flashcard> allFlashcards;
    int current_card_displayed_index = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        findViewById(R.id.show_button).setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                findViewById(R.id.hide_button).setVisibility(View.VISIBLE);
                findViewById(R.id.show_button).setVisibility(View.INVISIBLE);

                // get the center for the clipping circle
                int cx = findViewById(R.id.option1).getWidth() / 2;
                int cy = findViewById(R.id.option1).getHeight() / 2;

                // get the final radius for the clipping circle
                float finalRadius = (float) Math.hypot(cx, cy);

                // create the animator for this view (the start radius is zero)
                //0f to final radius
                Animator anim1 = ViewAnimationUtils.createCircularReveal(findViewById(R.id.option1), cx, cy, 0f, finalRadius);
                Animator anim2 = ViewAnimationUtils.createCircularReveal(findViewById(R.id.option2), cx, cy, 0f, finalRadius);
                Animator anim3 = ViewAnimationUtils.createCircularReveal(findViewById(R.id.option3), cx, cy, 0f, finalRadius);

                findViewById(R.id.option1).setVisibility(View.VISIBLE);
                findViewById(R.id.option2).setVisibility(View.VISIBLE);
                findViewById(R.id.option3).setVisibility(View.VISIBLE);

                anim1.setDuration(300);
                anim1.start();
                anim2.setDuration(300);
                anim2.start();
                anim3.setDuration(300);
                anim3.start();
            }
        });


        findViewById(R.id.hide_button).setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                findViewById(R.id.hide_button).setVisibility(View.INVISIBLE);
                findViewById(R.id.show_button).setVisibility(View.VISIBLE);

                int cx = findViewById(R.id.option1).getWidth() / 2;
                int cy = findViewById(R.id.option1).getHeight() / 2;

                float initialRadius = (float) Math.hypot(cx, cy);

                //initial radius to 0f
                Animator anim1 = ViewAnimationUtils.createCircularReveal(findViewById(R.id.option1), cx, cy, initialRadius, 0f );
                Animator anim2 = ViewAnimationUtils.createCircularReveal(findViewById(R.id.option2), cx, cy, initialRadius, 0f );
                Animator anim3 = ViewAnimationUtils.createCircularReveal(findViewById(R.id.option3), cx, cy, initialRadius, 0f );

                findViewById(R.id.option1).setVisibility(View.INVISIBLE);
                findViewById(R.id.option2).setVisibility(View.INVISIBLE);
                findViewById(R.id.option3).setVisibility(View.INVISIBLE);

                anim1.setDuration(300);
                anim1.start();
                anim2.setDuration(300);
                anim2.start();
                anim3.setDuration(300);
                anim3.start();

            }
        });


        findViewById(R.id.add_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddCardActivity.class);
                startActivityForResult(intent, 100);  //MainActivity.this.startActivityForResult(intent, 100);
                overridePendingTransition(R.anim.right_in, R.anim.left_out);
            }
        });


        findViewById(R.id.edit_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*
                //Edit button doesn't work. Currently it functions the same as Add button. Why I can't use value: findViewById?
                findViewById(R.id.edit_button).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(MainActivity.this, AddCardActivity.class);
                        intent.putExtra("editQuestion", "AddedQuestion");
                        intent.putExtra("editOption1", "AddedCorrectAnswer");
                        intent.putExtra("editOption2", "AddedWrongAnswer1");
                        intent.putExtra("editOption3", "AddedWrongAnswer2");
                        startActivityForResult(intent, 100);
                    }
                });
                */
            }
        });


        findViewById(R.id.next_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Animation leftOutAnim = AnimationUtils.loadAnimation(v.getContext(), R.anim.left_out);
                final Animation rightInAnim = AnimationUtils.loadAnimation(v.getContext(), R.anim.right_in);

                if (allFlashcards.size() == 0)
                    return;

                current_card_displayed_index++;

                findViewById(R.id.question).startAnimation(leftOutAnim);

                if (findViewById(R.id.option1).getVisibility() == View.VISIBLE) {
                    findViewById(R.id.option1).startAnimation(leftOutAnim);
                    findViewById(R.id.option2).startAnimation(leftOutAnim);
                    findViewById(R.id.option3).startAnimation(leftOutAnim);
                }

                leftOutAnim.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) { // this method is called when the animation first starts
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) { // this method is called when the animation is finished playing

                        findViewById(R.id.question).startAnimation(rightInAnim);

                        if (findViewById(R.id.option1).getVisibility() == View.VISIBLE) {
                            findViewById(R.id.option1).startAnimation(rightInAnim);
                            findViewById(R.id.option2).startAnimation(rightInAnim);
                            findViewById(R.id.option3).startAnimation(rightInAnim);
                        }

                        if(current_card_displayed_index >= allFlashcards.size()) {
                            Snackbar.make(findViewById(R.id.question),
                                    "End of the cards. Back to start.",
                                    Snackbar.LENGTH_SHORT).show();
                            current_card_displayed_index = 0;
                        }

                        // set the question and answer TextViews with data from the database
                        allFlashcards = flashcardDatabase.getAllCards();
                        Flashcard flashcard = allFlashcards.get(current_card_displayed_index);

                        displayCard(flashcard);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) { // we don't need to worry about this method
                    }
                });
            }
        });


        findViewById(R.id.delete_button).setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                flashcardDatabase.deleteCard(((TextView) findViewById(R.id.question)).getText().toString());
                allFlashcards = flashcardDatabase.getAllCards();

                if (current_card_displayed_index < allFlashcards.size()){ // if there is a next card, display the next card
                    // To do later
                } else if (current_card_displayed_index >= allFlashcards.size()){ // if we deleted the last card, display the first one

                } else  //Empty state (if we deleted the only card in the list)
                if (allFlashcards.size() == 0) {

                    //empty_card has the same layout of question so when question becomes invisible, an image representing empty state is displayed
                    ((TextView) findViewById(R.id.question)).setVisibility(View.INVISIBLE);
                    ((ImageView) findViewById(R.id.empty_card)).setVisibility(View.VISIBLE);

                    //option1 is the first multiple choice
                    ((TextView) findViewById(R.id.option1)).setVisibility(View.VISIBLE);
                    ((TextView) findViewById(R.id.option1)).setText("No cards to show");

                    //option2 is the second multiple choice
                    ((TextView) findViewById(R.id.option2)).setVisibility(View.VISIBLE);
                    ((TextView) findViewById(R.id.option1)).setText("Tap the Plus button to add more cards");

                    ((TextView) findViewById(R.id.option3)).setVisibility(View.INVISIBLE);
                }
            }
        });


        TextView answer = findViewById(R.id.option1);
        TextView wrong1 = findViewById(R.id.option2);
        TextView wrong2 = findViewById(R.id.option3);
        TextView question = findViewById(R.id.question);
        answer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                answer.setTextColor(Color.rgb(0, 128, 0));
                answer.setBackgroundColor(Color.rgb(153, 255, 153));
            }
        });
        wrong1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Animation shake = AnimationUtils.loadAnimation(v.getContext(), R.anim.shake);
                wrong1.startAnimation(shake);
                wrong1.setTextColor(Color.rgb(255, 0, 0));
                wrong1.setBackgroundColor(Color.rgb(250, 160, 160));
            }
        });
        wrong2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Animation shake = AnimationUtils.loadAnimation(v.getContext(), R.anim.shake);
                wrong2.startAnimation(shake);
                wrong2.setTextColor(Color.rgb(255, 0, 0));
                wrong2.setBackgroundColor(Color.rgb(250, 160, 160));
            }
        });


        flashcardDatabase = new FlashcardDatabase(getApplicationContext());
        allFlashcards = flashcardDatabase.getAllCards();
        if (allFlashcards != null && allFlashcards.size() > 0) {
            ((TextView) findViewById(R.id.question)).setText(allFlashcards.get(0).getQuestion());
            ((TextView) findViewById(R.id.option1)).setText(allFlashcards.get(0).getAnswer());
            ((TextView) findViewById(R.id.option2)).setText(allFlashcards.get(0).getWrongAnswer1());
            ((TextView) findViewById(R.id.option3)).setText(allFlashcards.get(0).getWrongAnswer2());
        }
    }

    private void displayCard(Flashcard flashcard) {
        ((TextView) findViewById(R.id.question)).setText(flashcard.getQuestion());
        ((TextView) findViewById(R.id.option1)).setText(flashcard.getAnswer());
        ((TextView) findViewById(R.id.option2)).setText(flashcard.getWrongAnswer1());
        ((TextView) findViewById(R.id.option3)).setText(flashcard.getWrongAnswer2());

        ((TextView) findViewById(R.id.option1)).setTextColor(Color.BLACK);
        ((TextView) findViewById(R.id.option2)).setTextColor(Color.BLACK);
        ((TextView) findViewById(R.id.option3)).setTextColor(Color.BLACK);


    }


    //Cannot have nested methods in Java, so whenever need to create new method, always put that outside onCreate()

    //Since we create the new Activity with startActivityOnResult, after the new Activity is dismissed
    //the onActivityResult() method of the Main Activity will be called

    //When RESULT_OK != null is not called,
    //Java complains that it can't call the method getExtras(), because the object calling
    // that method is null. This means that the data variable on line 41 is null at the point
    // when line 41 is trying to run. This makes sense that data is null when we hit the
    // 'Cancel' button, because no data was inputted and saved by the user.

    @Override //What is Override?
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 100 && resultCode == RESULT_OK) { //data != null or resultCode == RESULT_OK are fine

            Snackbar.make(findViewById(R.id.question), "Card Successfully Created", Snackbar.LENGTH_SHORT).show();

            String newQuestion = data.getExtras().getString("AddedQuestion");
            String newCorrectAnswer = data.getExtras().getString("AddedCorrectAnswer");
            String newWrongAnswer1 = data.getExtras().getString("AddedWrongAnswer1");
            String newWrongAnswer2 = data.getExtras().getString("AddedWrongAnswer2");

            ((TextView) findViewById(R.id.question)).setText(newQuestion);
            ((TextView) findViewById(R.id.option1)).setText(newCorrectAnswer);
            ((TextView) findViewById(R.id.option2)).setText(newWrongAnswer1);
            ((TextView) findViewById(R.id.option3)).setText(newWrongAnswer2);

            ((TextView) findViewById(R.id.option1)).setTextColor(Color.BLACK);
            ((TextView) findViewById(R.id.option2)).setTextColor(Color.BLACK);
            ((TextView) findViewById(R.id.option3)).setTextColor(Color.BLACK);

            //Pass the data from MainActivity to Database
            flashcardDatabase.insertCard(new Flashcard(newQuestion, newCorrectAnswer, newWrongAnswer1, newWrongAnswer2));
            allFlashcards = flashcardDatabase.getAllCards();

            //Pass the data from Database to MainActivity when the app is closed and relaunched
        }
    }
}