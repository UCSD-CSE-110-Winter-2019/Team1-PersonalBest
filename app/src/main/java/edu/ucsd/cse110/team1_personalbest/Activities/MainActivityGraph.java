package edu.ucsd.cse110.team1_personalbest.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import edu.ucsd.cse110.team1_personalbest.Firebase.Database;
import edu.ucsd.cse110.team1_personalbest.Firebase.IDataObject;
import edu.ucsd.cse110.team1_personalbest.Firebase.UserSession;
import edu.ucsd.cse110.team1_personalbest.Graph.Factories.BarGraphFactory;
import edu.ucsd.cse110.team1_personalbest.R;

public class MainActivityGraph extends AppCompatActivity {
    private int totalSteps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        Intent i = getIntent();
        String userName = i.getStringExtra("name" );
        int offset = i.getIntExtra("offset",0);
        Log.d("offset: ",Integer.toString(offset));

        Log.d("username: ", userName);



        //db = new Database(getApplicationContext());
        DateFormat format = new SimpleDateFormat("MM/dd/yyyy");
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE,-7*offset);
        Date d7 = calendar.getTime();

        calendar.add(Calendar.DATE,-1);
        Date d6 = calendar.getTime();
        calendar.add(Calendar.DATE,-1);
        Date d5 = calendar.getTime();
        calendar.add(Calendar.DATE,-1);
        Date d4 = calendar.getTime();
        calendar.add(Calendar.DATE,-1);
        Date d3 = calendar.getTime();
        calendar.add(Calendar.DATE,-1);
        Date d2 = calendar.getTime();
        calendar.add(Calendar.DATE,-1);
        Date d1 = calendar.getTime();

        setContentView(R.layout.activity_main_graph);

        BarGraphFactory factory = new BarGraphFactory();
        int d7Steps = UserSession.getUser(userName).getDailySteps(format.format(d7));
        int d7IntentionalSteps = UserSession.getUser(userName).getIntentionalSteps(format.format(d7));
        int goal = UserSession.getUser(userName).getStepGoal(format.format(d1));
        int d6Steps = UserSession.getUser(userName).getDailySteps(format.format(d6));
        int d6IntentionalSteps = UserSession.getUser(userName).getIntentionalSteps(format.format(d6));
        int d5Steps = UserSession.getUser(userName).getDailySteps(format.format(d5));
        int d5IntentionalSteps = UserSession.getUser(userName).getIntentionalSteps(format.format(d5));
        int d4Steps = UserSession.getUser(userName).getDailySteps(format.format(d4));
        int d4IntentionalSteps = UserSession.getUser(userName).getIntentionalSteps(format.format(d4));
        int d3Steps = UserSession.getUser(userName).getDailySteps(format.format(d3));
        int d3IntentionalSteps = UserSession.getUser(userName).getIntentionalSteps(format.format(d3));
        int d2Steps = UserSession.getUser(userName).getDailySteps(format.format(d2));
        int d2IntentionalSteps = UserSession.getUser(userName).getIntentionalSteps(format.format(d2));
        int d1Steps = UserSession.getUser(userName).getDailySteps(format.format(d1));
        int d1IntentionalSteps = UserSession.getUser(userName).getIntentionalSteps(format.format(d1));
        int[] dailySteps = {d1Steps,d2Steps,d3Steps,d4Steps,d5Steps,d6Steps,d7Steps};
        int[] intentionalSteps = {d1IntentionalSteps,d2IntentionalSteps,d3IntentionalSteps,
                d4IntentionalSteps,d5IntentionalSteps,d6IntentionalSteps,d7IntentionalSteps};

        //For Testing only
//        int[] dailySteps = {200*offset,3000,4000,3000,1500,1000,1500};
//        int[] intentionalSteps = {200*offset,1400,3000,2700,1400,0,700};
//        int goal = 500;
        DateFormat forTextView = new SimpleDateFormat("MM/dd");

        String endDate = forTextView.format(d7);
        String startDate = forTextView.format(d1);
        TextView dateText = findViewById(R.id.WeeklyStepsText);
        String dates = (startDate + " - " + endDate);
        dateText.setText(dates);

        GraphView graph = findViewById(R.id.weeklyBarGraph);

        factory.makeGraph(goal,intentionalSteps,dailySteps,graph);
        Button backButton = findViewById(R.id.returnToMain);

        backButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                finish();
            }
        });
        Button prevWeek = findViewById(R.id.previousWeekButton);
        Button nextWeek = findViewById(R.id.nextWeekButton);
        if(offset == 0){
            nextWeek.setVisibility(View.INVISIBLE);
        }
        else if(offset == 4){
            prevWeek.setVisibility(View.INVISIBLE);
        }
        else{
            nextWeek.setVisibility(View.VISIBLE);
            prevWeek.setVisibility(View.VISIBLE);
        }
        prevWeek.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                int newOffset = offset;
                newOffset++;
                Intent intent = getIntent();
                intent.putExtra("name", userName);
                intent.putExtra("offset", newOffset);
                startActivity(intent);
                finish();

            }
        });
        nextWeek.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                int newOffset = offset;
                newOffset--;

                Intent intent = getIntent();
                intent.putExtra("name", userName);
                intent.putExtra("offset", newOffset);
                startActivity(intent);
                finish();

            }
        });


    }

}




