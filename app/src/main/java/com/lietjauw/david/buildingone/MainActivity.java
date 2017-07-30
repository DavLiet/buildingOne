package com.lietjauw.david.buildingone;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.estimote.sdk.Beacon;
import com.estimote.sdk.BeaconManager;
import com.estimote.sdk.EstimoteSDK;
import com.estimote.sdk.Region;
import com.estimote.sdk.SystemRequirementsChecker;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    private BeaconManager beaconManager;
    private DatabaseReference mDatabase;
    public int bikeCounter;

    final Region region1 = new Region("beacon-1", UUID.fromString("B9407F30-F5F8-466E-AFF9-25556B57FE6D"),17318,30884); //blueberry
    final Region region2 = new Region("beacon-2",UUID.fromString("B9407F30-F5F8-466E-AFF9-25556B57FE6D"),53267 , 19160 );





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        EstimoteSDK.initialize(getApplicationContext(), "sandbox-nasa-kua", "17f63377066b3b5a18f12e6930815972");
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Building1");
        beaconManager = new BeaconManager(getApplicationContext());
        mDatabase.push().setValue("working");
        beaconManager.setMonitoringListener(new BeaconManager.MonitoringListener() {

            @Override
            public void onEnteredRegion(Region region, List<Beacon> list) {
                bikeCounter++;
                if(region.getMajor().equals(17318)){
                    mDatabase.child("NumberBikes").setValue(bikeCounter);
                }else{
                    mDatabase.child("NumberBikes").setValue(bikeCounter);
                }
            }

            @Override
            public void onExitedRegion(Region region) {
                bikeCounter--;
                if(region.getMajor().equals(17318)) {
                    mDatabase.child("NumberBikes").setValue(bikeCounter);
                }else{
                    mDatabase.child("NumberBikes").setValue(bikeCounter);
                }
            }
        });

        beaconManager.connect(new BeaconManager.ServiceReadyCallback() {
            @Override
            public void onServiceReady() {
                beaconManager.startMonitoring(region1);
                beaconManager.startMonitoring(region2);
            }
        });
    }

    protected void onDestroy(){
        super.onDestroy();
    }

    protected void onResume() {
        super.onResume();

        SystemRequirementsChecker.checkWithDefaultDialogs(this); //required for android v23 or higher


    }
}
