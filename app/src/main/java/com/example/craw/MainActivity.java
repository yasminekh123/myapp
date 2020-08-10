package com.example.craw;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jsoup.Jsoup;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    TextView tvv;

    Button button;
    Boolean replied, isPerturbed;
    DatabaseReference reff;// refpertCount;
    Spinner spinner;

    Button bt;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //test cnx with db;
       // Toast.makeText(MainActivity.this,"Firebase connection Success",Toast.LENGTH_LONG).show();
        //Android Os Security and stuff

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);



       // List<String>
        //Les Communes
        final List<String> communes = Arrays.asList("", "Alger", "Sidi M'hamed", "El Madania ", "Belouizdad", "Bab El Oued ", "Bologhine", "Casbah ", "Oued Koriche", "Bir Mourad Rais ", "El Biar", "Bouzareah ", "Bir Khadem", "El Harrach ", "Baraki", "Oued S'mar ", "Bourouba", "Hussein Dey ", "Kouba", "Bach Djerrah ", "Dar el Beida", "Bab Ezzouar ", "Ben Aknoun", "Dely Brahim ", "Hammame", "Rais Hamidou ", "Djisr Kasentina", "El Mouradia ", "Hydra", "Mohammadia ", "Bordj el Kiffan", "El Magharia ", "Beni Messous ", "Les Eucalyptus ", "Birtouta ", "Tassala el Merdja ", "Ouled Chebel ", "Sidi Moussa ", "Ain Taya ", "Bordj el Bahri ", "El Marsa ", "Heuraoua ", "Rouiba ", "Reghaia ", "Ain Benian ", "Staouali ", "Zéralda ", "Mahelma ", "Rahmania ", "Souidania ", "Chéraga ", "Ouled Fayet ", "El Achour ", "Draria ", "Douera ", "Baba Hassan ", "Khraissia ", "Shaoula ", "TIPAZA ", "MENACEUR ", "LARHAT ", "DOUAOUDA ", "BOURKIKA ", "KHEMISTI ", "AGHBAL ", "HADJOUT ", "SIDI AMAR ", "GOURAYA ", "NADOR ", "CHAIBA ", "AIN TAGOURAIT ", "CHERCHELL ", "DAMOUS ", "MERAD ", "FOUKA ", "BOU ISMAIL ", "AHMER EL AIN ", "BOUHAROUN ", "SIDI GHILES ", "MESSELMOUN ", "SIDI RACHED ", "KOLEA ", "ATTATBA ", "SIDI SEMIANE", "BENI MILEUK ", "HADJERET ENNOUS");



        spinner = findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener((AdapterView.OnItemSelectedListener) this);
        //Creating the ArrayAdapter instance having the country list
        ArrayAdapter aa = new ArrayAdapter(this,android.R.layout.simple_spinner_item,communes);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        spinner.setAdapter(aa);
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, final int position, long id) {
        try {
            /*member.setCommuneID(Integer.toString(position));
            reff.child("member2").setValue(member);
            Toast.makeText(getApplicationContext(),"Data inserted Successfully! \n" + commune[position] +" id:" + Integer.toString(position) , Toast.LENGTH_LONG).show();
            */
            //TextView & stuff
            tvv = findViewById(R.id.tv);

            final List<String> communes = Arrays.asList("", "Alger", "Sidi M'hamed", "El Madania ", "Belouizdad", "Bab El Oued ", "Bologhine", "Casbah ", "Oued Koriche", "Bir Mourad Rais ", "El Biar", "Bouzareah ", "Bir Khadem", "El Harrach ", "Baraki", "Oued S'mar ", "Bourouba", "Hussein Dey ", "Kouba", "Bach Djerrah ", "Dar el Beida", "Bab Ezzouar ", "Ben Aknoun", "Dely Brahim ", "Hammame", "Rais Hamidou ", "Djisr Kasentina", "El Mouradia ", "Hydra", "Mohammadia ", "Bordj el Kiffan", "El Magharia ", "Beni Messous ", "Les Eucalyptus ", "Birtouta ", "Tassala el Merdja ", "Ouled Chebel ", "Sidi Moussa ", "Ain Taya ", "Bordj el Bahri ", "El Marsa ", "Heuraoua ", "Rouiba ", "Reghaia ", "Ain Benian ", "Staouali ", "Zéralda ", "Mahelma ", "Rahmania ", "Souidania ", "Chéraga ", "Ouled Fayet ", "El Achour ", "Draria ", "Douera ", "Baba Hassan ", "Khraissia ", "Shaoula ", "TIPAZA ", "MENACEUR ", "LARHAT ", "DOUAOUDA ", "BOURKIKA ", "KHEMISTI ", "AGHBAL ", "HADJOUT ", "SIDI AMAR ", "GOURAYA ", "NADOR ", "CHAIBA ", "AIN TAGOURAIT ", "CHERCHELL ", "DAMOUS ", "MERAD ", "FOUKA ", "BOU ISMAIL ", "AHMER EL AIN ", "BOUHAROUN ", "SIDI GHILES ", "MESSELMOUN ", "SIDI RACHED ", "KOLEA ", "ATTATBA ", "SIDI SEMIANE", "BENI MILEUK ", "HADJERET ENNOUS");

            //if the website replies this boolean becomes true
            replied = false;

            //if there's a perturbation this boolean becomes true
            isPerturbed = false;

            //if the website replies this is the response string
            final String[] reply = {""};

            //Commune ID (Pour Alger Centre 1)
            final String[] wilaya = {"Alger"};

            //Final Reply
            final String[] fReply = {""};

            //pertCount
            //final int pertCount = //getValue from database;


            final Response[] response = new Response[1];

            final Perturbation perturbation=new Perturbation();
            reff= FirebaseDatabase.getInstance().getReference().child("perturbation" /*+ Integer.toString(pertCount)*/);
            OkHttpClient client = new OkHttpClient();
            FormBody.Builder formBody = new FormBody.Builder();
            formBody.add("commune_id", Integer.toString(position)); //previous: String.valueOf(communeId)
            RequestBody formRequestBody = formBody.build();
            Request request = new Request.Builder()
                    .url("http://www.seaal.dz/BRQ/carte/get_ajax_pert")
                    .method("POST", formRequestBody)
                    .build();
            try {
                response[0] = client.newCall(request).execute();
                //reply[0] = Jsoup.parse(response[0].body().string()).text();
                reply[0] = response[0].body().string();
                replied = true;
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("IO EXCEPTION: " + e.toString());
            }

            String newReply = reply[0];
            System.out.println(newReply);
            if (replied){
                //Do things if everything worked well
                //To Define Wilaya
                if (position >= 58){
                    wilaya[0] = "Tipaza";
                } else {
                    wilaya[0] = "Alger";
                }


                String info = "";

                if((Jsoup.parse(newReply).text()).contains("coupure pour cette")){
                    info = Jsoup.parse(newReply).text();
                    perturbation.setCoupure(Jsoup.parse(newReply).text());
                }
                if((Jsoup.parse(newReply).text()).contains("Quartiers")){
                    isPerturbed = true;
                    ArrayList<String> firstSplit = new ArrayList<String>(10);
                    String[] forFirstSplit = newReply.split("<br");//Jsoup.parse(newReply).text()
                    System.out.println(forFirstSplit.length);


                    if (forFirstSplit.length > 0 ){
                        for (int i = 1; i < forFirstSplit.length ; i++ ){
                            firstSplit.add(forFirstSplit[i]);
                        }
                    }else System.out.println("Problem with forFirstSplit.length= " + forFirstSplit.length);
                    if ((Jsoup.parse(newReply).text()).contains("Commune :")){
                        info = "\n" + Jsoup.parse(forFirstSplit[1].substring(2)).text() + "\n" + Jsoup.parse(forFirstSplit[2].substring(2)).text() + "\n" + Jsoup.parse(forFirstSplit[3].substring(2)).text() + "\n" + Jsoup.parse(forFirstSplit[4].substring(2)).text();
                        perturbation.setCoupure(Jsoup.parse(forFirstSplit[1].substring(2)).text());
                        perturbation.setQuartiers(Jsoup.parse(forFirstSplit[2].substring(2)).text());
                        perturbation.setApartirde(Jsoup.parse(forFirstSplit[3].substring(2)).text());
                        perturbation.setRetour(Jsoup.parse(forFirstSplit[4].substring(2)).text());

                        if ((Jsoup.parse(newReply).text()).contains("------")){
                            info += "\n" + Jsoup.parse(forFirstSplit[7].substring(2)).text() + "\n" + Jsoup.parse(forFirstSplit[8].substring(2)).text() + "\n" + Jsoup.parse(forFirstSplit[9].substring(2)).text() + "\n" + Jsoup.parse(forFirstSplit[10].substring(2)).text() + "\n" + Jsoup.parse(forFirstSplit[11].substring(2)).text();
                            perturbation.setCoupure2(Jsoup.parse(forFirstSplit[7].substring(2)).text());
                            perturbation.setQuartiers2(Jsoup.parse(forFirstSplit[8].substring(2)).text());
                            perturbation.setApartirede2(Jsoup.parse(forFirstSplit[9].substring(2)).text());
                            perturbation.setRetour2(Jsoup.parse(forFirstSplit[10].substring(2)).text());

                        }
                    } else {
                        info = "\n" + Jsoup.parse(forFirstSplit[0]).text() + "\n" + Jsoup.parse(forFirstSplit[1].substring(2)).text() + "\n" + Jsoup.parse(forFirstSplit[2].substring(2)).text() + "\n" + Jsoup.parse(forFirstSplit[3].substring(2)).text();

                        perturbation.setCoupure(Jsoup.parse(forFirstSplit[0].substring(2)).text());
                        perturbation.setQuartiers( Jsoup.parse(forFirstSplit[1].substring(2)).text());
                        perturbation.setApartirde(Jsoup.parse(forFirstSplit[2].substring(2)).text());
                        perturbation.setRetour(Jsoup.parse(forFirstSplit[3].substring(2)).text());

                        if ((Jsoup.parse(newReply).text()).contains("------")){
                            info += "\n" + Jsoup.parse(forFirstSplit[6].substring(2)).text() + "\n" + Jsoup.parse(forFirstSplit[7].substring(2)).text() + "\n" + Jsoup.parse(forFirstSplit[8].substring(2)).text() + "\n" + Jsoup.parse(forFirstSplit[9].substring(2)).text() + "\n" + Jsoup.parse(forFirstSplit[10].substring(2)).text();
                            perturbation.setCoupure2(Jsoup.parse(forFirstSplit[0].substring(2)).text());
                            perturbation.setQuartiers2( Jsoup.parse(forFirstSplit[1].substring(2)).text());
                            perturbation.setApartirede2(Jsoup.parse(forFirstSplit[2].substring(2)).text());
                            perturbation.setRetour2(Jsoup.parse(forFirstSplit[3].substring(2)).text());
                        }
                    }
                }
                fReply[0] = "Wilaya: " + wilaya[0] + "\n" + "Commune: " + communes.get(position) + "\n" + info;
                //pertCount++;
                perturbation.setWilaya(wilaya[0]);
                perturbation.setCommune(communes.get(position));
                tvv.setText("Wilaya: " + wilaya[0] + "\n" + "Commune: " + communes.get(position) + "\n" + info);
                //tvv.setText(Jsoup.parse(newReply).text());



            }else{
                //Do thing if nothing worked well
                tvv.setText("Something Went Wrong!");
            }

            //refpertCount.child("pertCount").setValue(pertCount);

            // int ida=Integer.parseInt(idp.getText().toString().trim());
            int idk=0;
            //perturbation.setIdp(ida);
            perturbation.setIdp(idk);
            idk+=1;

            reff.child("perturbation1").setValue(perturbation);
            Toast.makeText(MainActivity.this,"data inserted succ",Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.toString());
        }
    }
//kima kan f 15 juillet
    //and no notif is here  ? 
    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}


