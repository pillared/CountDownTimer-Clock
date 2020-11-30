package com.example.hello;


import android.os.Bundle;
import android.os.CountDownTimer;
import androidx.appcompat.app.AppCompatActivity;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import java.util.Calendar;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    
    static TextView textView;                                           //declare textview for timer
    Button datePickerButton, timePickerButton, resetButton;             //declare buttons
    static EditText textDate = null, textTime = null;                   //declare textboxes
    static CountDownTimer CDT;                                          //declare actual timer
    private int Year, Month, Day, Hour, Min;                            //declare time vars
    protected void onCreate(Bundle savedInstanceState) {                //on the app creation
        super.onCreate(savedInstanceState);                             //load save
        setContentView(R.layout.activity_main);                         //set view to xml file
        datePickerButton=(Button)findViewById(R.id.date_button);        //bound physical button to xmlview
        timePickerButton=(Button)findViewById(R.id.time_button);        //bound physical button to xmlview
        resetButton=(Button)findViewById(R.id.reset_button);            //bound physical button to xmlview
        textDate=(EditText)findViewById(R.id.date_input);               //bound physical textbox to xmlview
        textTime=(EditText)findViewById(R.id.time_input);               //bound physical textbox to xmlview
        datePickerButton.setOnClickListener(this);                      //set a listener to check for click event
        timePickerButton.setOnClickListener(this);                      //set a listener to check for click event
        resetButton.setOnClickListener(this);                           //set a listener to check for click event
        textView = findViewById(R.id.textView_timer);                   //bound physical textbox to xmlview
        textView.setText("");                                           //init timer text to nothing
        System.out.println();
        System.out.println("LATEST Date: " + textDate.getText().toString());
        System.out.println("LATEST Time: " + textTime.getText().toString());
    }
    @Override
    public void onClick(View v) {                                                                   //function for when a button is clicked
        if(v == resetButton){                                                                       //if button is a resetbutton
            System.out.println("WE GOT HERE!");                                                     //log and reset
            textDate.setText("");
            textTime.setText("");
            textView.setText("");
            CDT.cancel();                                                                           //cancel countdowntimer!
            System.out.println("TIMER CANCELED!");
        }
        if (v == datePickerButton) {                                                                //if button is a date picker button
            final Calendar c = Calendar.getInstance();                                              // Get Current Date
            Year = c.get(Calendar.YEAR);
            Month = c.get(Calendar.MONTH);
            Day = c.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(this,                  //create new date picker
                    new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(DatePicker view, int year,                            //check for setting of date picker
                                              int monthOfYear, int dayOfMonth) {

                            textDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);    //set the textdate to the date picked. because month starts at 0, add 1
                            //log
                            System.out.println("Input Date: " + textDate.getText().toString());
                            System.out.println("Current Date: " + Day + "-" + (Month + 1) + "-" + Year);
                            String txtdate = textDate.getText().toString();                         //retrieve current textDate and check if empty... only start countdowntimer if both are filled!
                            if (txtdate.matches("")) {
                                System.out.println("DATE-ERROR");
                            }else{
                                System.out.println("DATE - You made it!");
                                String txttime = textTime.getText().toString();                     //retrieve current textTime and check if empty... only start countdowntimer if both are filled!
                                if (txttime.matches("")) {
                                    System.out.println("ERRORERROR");
                                }else{                                                              //if both are set, start countdown timer!
                                    System.out.println("BOTHHHHHH YOu made it!");
                                    try {
                                        //call countdowntimer!!!
                                        calculateCountdown(txtdate, txttime, Day, Month, Year, Hour, Min);
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }
                    }, Year, Month, Day);
            datePickerDialog.show();

        }
        if (v == timePickerButton) {                                                                //if button is a timepicker button
            final Calendar c = Calendar.getInstance();                                              // Get Current Time
            Hour = c.get(Calendar.HOUR_OF_DAY);
            Min = c.get(Calendar.MINUTE);

            // Launch Time Picker Dialog
            TimePickerDialog timePickerDialog = new TimePickerDialog(this,                  //create new time picker
                    new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay,                       //check for setting of time picker
                                              int minute) {
                            textTime.setText(hourOfDay + ":" + minute);                             //set timeText to time picked
                            //log data
                            System.out.println("Input Time: " + textTime.getText().toString());
                            System.out.println("Current Time: " + Hour + ":" + Min);
                            String txttime = textTime.getText().toString();                         //retrieve current textTime and check if empty... only start countdowntimer if both are filled!
                            if (txttime.matches("")) {
                                System.out.println("TIME-ERROR");
                            }else{
                                System.out.println("TIME - You made it!");
                                String txtdate = textDate.getText().toString();                     //retrieve current textDate and check if empty... only start countdowntimer if both are filled!
                                if (txtdate.matches("")) {
                                    System.out.println("ERROR ERROR 2");
                                }else{                                                              //if both are set, start countdown timer!
                                    System.out.println("BOTH 2 YOu made it!");
                                    try {
                                        //call countdowntimer!!!
                                        calculateCountdown(txtdate, txttime, Day, Month, Year, Hour, Min);
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }
                    }, Hour, Min, false);
            timePickerDialog.show();
        }

    }

    /*
     * txtdate and txttime is the date and time of when we want alarm to go off
     * Day,Month,year,Hour,Min are the date and time of when the countdown was started / current
     */
    public void calculateCountdown(String txtdate, String txttime, int Day, int Month, int Year, int Hour, int Min)
    throws ParseException {
        String[] arrtxtdate = txtdate.split("-", 0);                                   //parse the textdate from a string to array of ints
        String[] arrtxttime = txttime.split(":", 0);                                   //parse the texttime from a string to arrar of ints
        int timeHour = Integer.parseInt(arrtxttime[0]);                                             //set time of hour to the parsed hour
        int timeMin = Integer.parseInt(arrtxttime[1]);                                              //set time of min to the parsed min

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);          //use SDF to convert a string to a date for time manipulation
        String seconddate = "" + Day + "-" + (Month + 1) + "-" + Year;                              //Create String consistent to other date string

        Date firstDate = sdf.parse(txtdate);                                                        //set time is hour to the parsed hour
        Date secondDate = sdf.parse(seconddate);                                                    //set time is hour to the parsed hour

        long delta_time = Math.abs(secondDate.getTime() - firstDate.getTime());                     //append time
        delta_time = delta_time - TimeUnit.HOURS.toMillis(Hour)
                    - TimeUnit.MINUTES.toMillis(Min)
                    + TimeUnit.HOURS.toMillis(timeHour)
                    + TimeUnit.MINUTES.toMillis(timeMin);

        CDT = new CountDownTimer(delta_time, 1000) {                               //create CountDown Timer with the time and decrement
                                                                                                    //every 1000ms, aka 1 second
            public void onTick(long time_left) {                                                    //every tick, determine time left and diplay in TextView
                // Used for formatting digit to be in 2 digits only
                System.out.println(time_left);
                NumberFormat f = new DecimalFormat("00");
                long d = ((time_left) / 3600000) / 24;
                long h = (time_left / 3600000) % 24; //% 24;
                long m = (time_left / 60000) % 60;
                long s = (time_left / 1000) % 60;
                textView.setText((d) + "D:" + (h) + "H:" + f.format(m) + "M:" + f.format(s) + "S");
            }
            public void onFinish() {                                                                //When timer reaches zero, notify and return
                textView.setText("00:00:00:00\nTimer Complete!");
                System.out.println("done");
                Toast.makeText(getApplicationContext(),"Alarm Complete!",Toast.LENGTH_SHORT).show();
            }
        }.start();

    }
}