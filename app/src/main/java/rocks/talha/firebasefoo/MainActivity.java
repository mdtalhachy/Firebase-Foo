package rocks.talha.firebasefoo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private Button logout;
    private EditText name;
    private Button add;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        logout = findViewById(R.id.logout);
        name = findViewById(R.id.name);
        add = findViewById(R.id.add);
        listView = findViewById(R.id.listView);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Toast.makeText(MainActivity.this, "Logged Out Successfully!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(MainActivity.this, StartActivity.class));
                finish();
            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name_text = name.getText().toString();
                name.setText("");
                if(name_text.isEmpty()){
                    Toast.makeText(MainActivity.this, "No Name Entered!", Toast.LENGTH_SHORT).show();
                }else{
                    FirebaseDatabase.getInstance().getReference().child("Stack").child("Name").setValue(name_text);
                }
            }
        });


        final ArrayList<String> list = new ArrayList<>();
        Context context;
        final ArrayAdapter adapter = new ArrayAdapter<String>(this, R.layout.list_item, list);
        listView.setAdapter(adapter);

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Information");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list.clear();

                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Information info = snapshot.getValue(Information.class);
                    String text = info.getName() + " : " + info.getEmail();
                    list.add(text);
                }

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        //Adding data to Firestore
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        Map<String, Object> city = new HashMap<>();
        city.put("Name", "Gopalganj");
        city.put("Division", "Dhaka");
        city.put("Country", "Bangladesh");

        db.collection("cities").document("HOL").set(city).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(MainActivity.this, "Values added successfully!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        /*
        HashMap<String, Object> map = new HashMap<>();
        map.put("Name", "Emtici");
        map.put("Email", "mtc134@gmail.com");
        FirebaseDatabase.getInstance().getReference().child("UserDatabase").updateChildren(map);
        */
    }
}