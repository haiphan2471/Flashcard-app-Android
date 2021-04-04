package com.example.flashcard_mine;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class AddCardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_card);

        /*
        //This is commented as edit_button is MainActivity is commented
        String q = getIntent().getStringExtra("editQuestion");
        String op1 = getIntent().getStringExtra("editOption1");
        String op2 = getIntent().getStringExtra("editOption2");
        String op3 = getIntent().getStringExtra("editOption3");
        */

        findViewById(R.id.clear_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.left_in, R.anim.right_out);
            }
        });

        findViewById(R.id.done_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String stringA = ((EditText) findViewById(R.id.add_correctAnswer)).getText().toString();
                String stringW1 = ((EditText) findViewById(R.id.add_wrongAnswer1)).getText().toString();
                String stringW2 = ((EditText) findViewById(R.id.add_wrongAnswer2)).getText().toString();
                String stringQ = ((EditText) findViewById(R.id.add_question)).getText().toString();

                Intent data = new Intent(); // create a new Intent, this is where we will put our data
                data.putExtra("AddedQuestion", stringQ); // puts one string into the Intent, with the key as 'string1'
                data.putExtra("AddedCorrectAnswer", stringA);
                data.putExtra("AddedWrongAnswer1", stringW1);
                data.putExtra("AddedWrongAnswer2", stringW2);
                setResult(RESULT_OK, data); // set result code and bundle data for response
                finish();

                overridePendingTransition(R.anim.left_in, R.anim.right_out);
            }
        });

    }
}