package group6.comp3330mobileapp;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class RegisterStudent extends AppCompatActivity {

    //delcare variable
    Button back;
    Button register;
    EditText username;
    EditText password;
    Spinner universitySpinner;
    EditText uid;
    EditText email;
    EditText assoname;
    String[] university = {"Select a University", "The University of Hong Kong",
            "The Chinese University of Hong Kong", "The Hong Kong University of Science and Technology",
            "City University of Hong Kong", "The Hong Kong Polytechnic University",
            "Hong Kong Baptist University", "Lingnan University", "The Education University of Hong Kong"};

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();
    DatabaseReference testRef = myRef.child("users");

    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    Query query = databaseReference.child("users").orderByKey().limitToLast(1);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_page_student);

        back = (Button) findViewById(R.id.back_button);
        register = (Button) findViewById(R.id.register_button);

        username = (EditText)findViewById(R.id.editText1);
        password = (EditText)findViewById(R.id.editText2);
        universitySpinner = (Spinner) findViewById(R.id.editText3);
        uid = (EditText)findViewById(R.id.editText4);
        email = (EditText)findViewById(R.id.editText5);

        assoname = findViewById(R.id.editTextAssoName);

        ArrayAdapter<String> adapterUni = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, university);
        universitySpinner.setAdapter(adapterUni);

        back.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent myIntent = new Intent(view.getContext(), RegisterMainPage.class);
                try {
                    startActivity(myIntent);
                    finish();
                }
                catch(android.content.ActivityNotFoundException e) {

                }

            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String usernameString = username.getText().toString();
                        String passwordString = password.getText().toString();
                        int thisuid = Integer.valueOf(uid.getText().toString());
                        String emailString = email.getText().toString();
                        String universityString = university[universitySpinner.getSelectedItemPosition()];

                        for (DataSnapshot data : dataSnapshot.getChildren()) {
                            String userIDString = data.child("userID").getValue().toString(); //retrieve from old user id
                            int userIDInt = Integer.valueOf(userIDString)+1; // new user id in integer
                            String userIDString_new = String.format("%03d",userIDInt); //reformat new user id

                            UserInfo createNewUser= new UserInfo(usernameString,passwordString,universityString, thisuid,emailString,"S",userIDString_new);
                            testRef.child(userIDString_new).setValue(createNewUser);

                            Toast.makeText(RegisterStudent.this, "Registration Succeed, Please Login In",
                                    Toast.LENGTH_SHORT).show();

                        }
                    }


                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        //Handle possible errors.
                    }
                });

                Intent myIntent = new Intent(view.getContext(), MainActivity.class);
                try {
                    startActivity(myIntent);
                    finish();
                }
                catch(android.content.ActivityNotFoundException e) {
                }
            }
        });
    }

    public class UserInfo{

        private String username;
        private String password;
        private String email;
        private int uid;
        private String userID;
        private String university;
        private String identity;

        public UserInfo(){}

        public UserInfo (String username, String password, String university, int uid, String email ,String identity, String userID){
            this.username = username;
            this.password = password;
            this.email = email;
            this.uid = uid;
            this.university = university;
            this.identity = identity;
            this.userID = userID;
        }

        public String getUsername(){return username;}
        public String getPassword(){return password;}
        public String getEmail(){return email;}
        public int getUid(){return uid;}
        public String getUniversity(){return university;}
        public String getIdentity(){return identity;}
        public String getuserID(){return userID;}
    }

}

