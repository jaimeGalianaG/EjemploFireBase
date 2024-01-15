package jaime.galiana.ejemplofirebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import jaime.galiana.ejemplofirebase.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private DatabaseReference myPersona;
    private DatabaseReference myListPersonas;
    private ArrayList<Persona> listaPersonas;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        listaPersonas = new ArrayList<>();

        //por buena practica ponemos en el getInstance la url
        database = FirebaseDatabase.getInstance("https://ejemplo-firebase-1-1fe76-default-rtdb.europe-west1.firebasedatabase.app/");
        myRef = database.getReference("frase");
        myPersona = database.getReference("persona");
        myListPersonas = database.getReference("listapersonas");

        binding.btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myRef.setValue(binding.txtFrase.getText().toString());
                int edad = (int)(Math.random()*100);
                Persona p = new Persona(binding.txtFrase.getText().toString(), edad);
                myPersona.setValue(p);
                listaPersonas.add(p);
                myListPersonas.setValue(listaPersonas);

                Toast.makeText(MainActivity.this, "Informacion guardada", Toast.LENGTH_SHORT).show();
                //binding.txtFrase.setText("");
            }
        });

        //leer de base de datos
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                String texto = snapshot.getValue(String.class);
                binding.lbFrase.setText(texto);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Toast.makeText(MainActivity.this, error.toException().toString(), Toast.LENGTH_SHORT).show();
            }
        });

        myPersona.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Persona p = snapshot.getValue(Persona.class);
                binding.lbFrase.setText(p.toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //lista de persons
        myListPersonas.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                GenericTypeIndicator<ArrayList<Persona>> gti =
                        new GenericTypeIndicator<ArrayList<Persona>>() {
                };
                ArrayList<Persona> lista = snapshot.getValue(gti);
                binding.lbFrase.setText("Elementos en la lista: "+String.valueOf(lista.size()));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}