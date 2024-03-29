package com.example.geoquiz;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private static final String TAG ="QuizActivity";
    private static final String KEY_INDEX="index"; //добвление ключа для сохранение значения QuizActivity
    private Button mTrueButton;
    private Button mFalseButton;
    private Button mNextButton;
    private Button mPrevButton;
    private Button mCheatButton;
    private TextView mQuestionTextView;
    private int mCurrentIndex = 0;
    private static final int REQUEST_CODE_CHEAT = 0;
    private boolean mIsCheater;

    private void updateQuestion(){
        //Log.d(TAG, "Updating question text for question #"+mCurrentIndex, new Exception()); // сохранение записи трассировки  стека
        int question = mQuestionBank[mCurrentIndex].getTextResId();
        mQuestionTextView.setText(question);
    }

    private void checkAnswer(boolean userPressedTrue){
        boolean answerIsTrue = mQuestionBank[mCurrentIndex].isAnswerTrue();

        int messageResId = 0;
        if(mIsCheater) {
            messageResId = R.string.judgment_toast;
        } else {
            if (userPressedTrue == answerIsTrue) {
                messageResId = R.string.correct_toast;
            } else {
                messageResId = R.string.incorrect_toast;
            }
        }
         Toast.makeText(this, messageResId, Toast.LENGTH_SHORT).show();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      //  Log.d(TAG, "onCreate(Bundle) called");
        setContentView(R.layout.activity_main);

        mQuestionTextView = (TextView)findViewById(R.id.question_text_view);
        mQuestionTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCurrentIndex = (mCurrentIndex +1)% mQuestionBank.length;
                updateQuestion();
            }
        });

        mTrueButton = (Button) findViewById(R.id.true_button);
        mTrueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(true);
            }
        });

        mFalseButton = (Button) findViewById(R.id.false_button);
        mFalseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(false);
            }
        });

        mNextButton = (Button)findViewById(R.id.next_button);
        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mCurrentIndex==4){
                mIsCheater = false; //задаем значение переменной читер = false для контроля подсказок
                }
                else{
                mCurrentIndex = (mCurrentIndex +1)% mQuestionBank.length;
                mIsCheater = false; //задаем значение переменной читер = false для контроля подсказок
                updateQuestion();}


        }
        });

        mPrevButton = (Button)findViewById(R.id.prev_button);
        mPrevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mCurrentIndex==0){
                mCurrentIndex = (mCurrentIndex) % mQuestionBank.length;}
                else{
                mCurrentIndex = (mCurrentIndex - 1) % mQuestionBank.length;
                updateQuestion();}

            }

        });
        mCheatButton = (Button)findViewById(R.id.cheat_button);
        mCheatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { //запуск CheatActivity
               boolean answerIsTrue = mQuestionBank[mCurrentIndex].isAnswerTrue();
                Intent i = CheatActivity.newIntent(MainActivity.this, answerIsTrue);
                    startActivityForResult(i, REQUEST_CODE_CHEAT);
            }
        });

        if(savedInstanceState !=null){
            mCurrentIndex = savedInstanceState.getInt(KEY_INDEX, 0);
        }


        updateQuestion();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){

        if(resultCode != Activity.RESULT_OK){
            return;
        }
        if(requestCode == REQUEST_CODE_CHEAT){
            if(data == null){
                return;
            }
            mIsCheater = CheatActivity.wasAnswerShown(data);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState){
        super.onSaveInstanceState(savedInstanceState);
        Log.i(TAG, "onSaveInstanceState");
        savedInstanceState.putInt(KEY_INDEX, mCurrentIndex);
    }


    private Question[] mQuestionBank = new Question[]{
                new Question(R.string.question_oceans, true),
                new Question(R.string.question_mideast, false),
                new Question(R.string.question_africa, false),
                new Question(R.string.question_americas, true),
                new Question(R.string.question_asia, true),
    };
        //регистрация жизненных циклов
    @Override
    public  void  onStart(){
        super.onStart();
        Log.d(TAG, "onStart() called");
    }
    public  void  onPause(){
        super.onPause();
        Log.d(TAG, "onPause() called");
    }public  void  onResume(){
        super.onResume();
        Log.d(TAG, "onResume() called");
    }
    public  void  onStop(){
        super.onStop();
        Log.d(TAG, "onStop() called");
    }
    public  void  onDestroy(){
        super.onDestroy();
        Log.d(TAG, "onDestroy() called");
    }
}