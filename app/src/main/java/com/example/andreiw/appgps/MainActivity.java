package com.example.andreiw.appgps;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LocationListener { //usa o implement locationlistener

    private TextView txtLatitude;
    private TextView txtLongitude;
    private TextView txtCidade;
    private TextView txtEstado;
    private TextView txtPais;
    private TextView txtEndereco;

    double latitude =0.0;
    double longitude=0.0;
    boolean GpsAtivo;

    private Location location; // retorna a cordenada
    private LocationManager locationManager;  //verica qual serviço de localização disponivel

    private Address endereco;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtLatitude = (TextView) findViewById(R.id.txtLatitude);
        txtLongitude = (TextView) findViewById(R.id.txtLongitude);
        txtCidade = (TextView)findViewById(R.id.txtcidade);
        txtEstado =(TextView) findViewById(R.id.txtestado);
        txtPais = (TextView) findViewById(R.id.txtpais);
        txtEndereco =(TextView) findViewById(R.id.txtEndereco);

        ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},0);

        /*Verifica as permissões para usar o recurso do gps, pois apartir da api 23
        * o Android da a possibilidade doo usuário revogar as permissões de segurança
        * está verificação visualiza se ainda temos apermissão para usar o recurso*/

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED){

            //solicitar novas permissões para acesso do recurso
            Toast.makeText(this,"Permissão denegada", Toast.LENGTH_SHORT).show();


        }else {
            locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
            GpsAtivo = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

            if (GpsAtivo){
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,6000,10, (LocationListener) this);
                location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

            }

        if (location!=null){

            longitude = location.getLongitude(); // recupera a longitude
            latitude = location.getLatitude(); // recupera a latitude

          //  Toast.makeText(this,"longitude" +longitude, Toast.LENGTH_SHORT).show();

        }

        }




    }

    public void BuscarLocal(View v){
        /*Intent buscar = new Intent(MainActivity.this,Localizacao.class);
        startActivity(buscar);*/

        txtLatitude.setText("Latitude: "+latitude);
        txtLongitude.setText("Longitude: "+longitude);
        try {
           endereco = BuscaEndereco(latitude,longitude);
           txtCidade.setText("Cidade: "+endereco.getLocality());
           txtEstado.setText("Estado: "+endereco.getAdminArea());
           txtPais.setText("Pais: "+endereco.getCountryName());
           txtEndereco.setText("Endereco: "+endereco.getAddressLine(0));


        }catch (IOException e){
            Log.i("log",e.getMessage());
        }
        Toast.makeText(this,"Latitude " +latitude + "\n" +"Longitude " + longitude, Toast.LENGTH_SHORT).show();


    }

    public  Address BuscaEndereco(double latitude, double longitude) throws IOException{

        Geocoder geocoder;
        Address address = null;
        List<Address>addresses;

        geocoder = new Geocoder(getApplicationContext());

        addresses = geocoder.getFromLocation(latitude,longitude,1);
        if(addresses.size()>0){
            address = addresses.get(0);
        }
        return address;
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
