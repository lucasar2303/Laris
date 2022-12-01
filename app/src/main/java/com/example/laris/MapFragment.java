package com.example.laris;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;


public class MapFragment extends Fragment {

    public double lat, lon;
    private GoogleMap mMap;
    private LocationManager locationManager;
    private LocationListener locationListener;
    private static final String[] LOCATION_PERMS= new String[]{
            Manifest.permission.ACCESS_FINE_LOCATION
    };
    private static final int INITIAL_REQUEST=1337;
    private static final int LOCATION_REQUEST=INITIAL_REQUEST+3;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Permissoes.validarPermissoes(LOCATION_PERMS, getActivity(), 1);

        View view = inflater.inflate(R.layout.fragment_map, container, false);

        SupportMapFragment supportMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);

        Bundle dados = getArguments();
        if (dados!=null){

            String rua = dados.getString("rua");
            String num = dados.getString("num");
            String nomeLocal = dados.getString("endereco");

            String enderecoDestino = rua + ", " + num;
            Address addressDestino = recuperarEndereco(enderecoDestino);
            if (addressDestino!=null){
                supportMapFragment.getMapAsync(new OnMapReadyCallback() {
                    @Override
                    public void onMapReady(@NonNull GoogleMap googleMap) {
                        mMap = googleMap;
                        LatLng endSelected = new LatLng(addressDestino.getLatitude(), addressDestino.getLongitude());
                        mMap.addMarker(new MarkerOptions().position(endSelected).title(nomeLocal));
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(endSelected));
                        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                                endSelected, 16
                        ));

                    }
                });
            }

        }else{
            supportMapFragment.getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(@NonNull GoogleMap googleMap) {
                    mMap = googleMap;
                    LatLng endSelected = new LatLng(-23.026419, -45.566035);
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(endSelected));
                    googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                            endSelected, 12
                    ));
                    recuperarLocalizacaoUsuario();


                }
            });
        }

        return view;
    }

    private Address recuperarEndereco(String endereco){

        Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());
        try {
            List<Address> listaEnderecos = geocoder.getFromLocationName(endereco, 1);
            if (listaEnderecos != null && listaEnderecos.size()>0){
                Address address = listaEnderecos.get(0);

                return address;

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @SuppressLint("MissingPermission")
    private void recuperarLocalizacaoUsuario(){



        locationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
                double latitudeUser = location.getLatitude();
                double longitudeUser = location.getLongitude();

                LatLng locUser = new LatLng(latitudeUser, longitudeUser);
                mMap.clear();
                mMap.addMarker(new MarkerOptions().position(locUser).title("Sua posição"));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(locUser));
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(locUser, 16));
//                Toast.makeText(getContext(), latitudeUser+"", Toast.LENGTH_LONG).show();

            }

            @Override
            public void onProviderEnabled(@NonNull String provider) {

            }

            @Override
            public void onProviderDisabled(@NonNull String provider) {

            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }
        };

        if (ContextCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ) {
            locationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER, 10000, 10, locationListener
            );
        } else {
            Toast.makeText(getContext(), "ERRO DE PERMISSAO", Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        for (int permissaoResultado : grantResults){
            if (permissaoResultado == PackageManager.PERMISSION_DENIED){
                Toast.makeText(getContext(), "O APP necessita de permissão", Toast.LENGTH_SHORT).show();
            }else if (permissaoResultado == PackageManager.PERMISSION_GRANTED){
                recuperarLocalizacaoUsuario();
            }
        }

    }

}