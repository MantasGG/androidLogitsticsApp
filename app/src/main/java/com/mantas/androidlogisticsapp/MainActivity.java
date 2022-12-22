package com.mantas.androidlogisticsapp;

import android.content.Context;
import android.os.Bundle;
import android.util.AndroidRuntimeException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.mantas.androidlogisticsapp.databinding.ActivityMainBinding;
import com.mantas.androidlogisticsapp.model.DtoCheckpoints;
import com.mantas.androidlogisticsapp.model.DtoPostCheckpoint;
import com.mantas.androidlogisticsapp.model.DtoRoutes;
import com.mantas.androidlogisticsapp.model.DtoUser;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    private EditText routeId, routeDescription, routeDate, userId;
    private TextView responseTV;
    private ProgressBar loadingPB;
    private ListView routesList;
    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("http://158.129.232.167:8080")
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    RestApi restApi = retrofit.create(RestApi.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);
    }

    public void getUsers(View v) throws IOException {
        TextView usersText = findViewById(R.id.usersText);
        Call<List<DtoUser>> get = restApi.getUsers();

        get.enqueue(new Callback<List<DtoUser>>() {
            @Override
            public void onResponse(Call<List<DtoUser>> call, Response<List<DtoUser>> response) {
                if(!response.isSuccessful()){
                    usersText.setText("Code: " + response.code());
                    return;
                }

                List<DtoUser> users = response.body();

                usersText.setText("");

                for(DtoUser user : users){
                    String content = "";
                    content += "id: " + user.getId() + "\n";
                    content += "firstName: " + user.getFirstName() + "\n";
                    content += "lastName: " + user.getLastName() + "\n";
                    content += "gender: " + user.getGender() + "\n";
                    content += "phone: " + user.getPhone() + "\n";
                    content += "email: " + user.getEmail() + "\n";
                    content += "password: " + user.getPassword() + "\n";
                    content += "role: " + user.getRole() + "\n";
                    content += "address: " + user.getAddress() + "\n\n";

                    usersText.append(content);
                }
            }

            @Override
            public void onFailure(Call<List<DtoUser>> call, Throwable t) {
                usersText.setText(t.getMessage());
            }
        });
    }

    public void getRoutes(View v) throws IOException {
//        TextView routesText = findViewById(R.id.routesText);
        Call<List<DtoRoutes>> get = restApi.getRoutes();

        routesList = findViewById(R.id.routesList);

        get.enqueue(new Callback<List<DtoRoutes>>() {
            @Override
            public void onResponse(Call<List<DtoRoutes>> call, Response<List<DtoRoutes>> response) {
                if(!response.isSuccessful()){
//                    routesText.setText("Code: " + response.code());
                    return;
                }

                List<DtoRoutes> routes = response.body();

                ArrayAdapter adapter = new RoutesAdapter(getApplicationContext(), routes);

                routesList.setAdapter(adapter);
//                routesText.setText("");

                for(DtoRoutes route : routes){
                    String content = "";
                    content += "id: " + route.getId() + "\n";
                    content += "assignedUserId: " + route.getAssignedUserId() + "\n";
                    content += "pointA: " + route.getPointA() + "\n";
                    content += "pointB: " + route.getPointB() + "\n";
                    content += "startDate: " + route.getStartDate() + "\n";
                    content += "endDate: " + route.getEndDate() + "\n\n";

//                    routesText.append(content);
                }
            }

            @Override
            public void onFailure(Call<List<DtoRoutes>> call, Throwable t) {
//                routesText.setText(t.getMessage());
            }
        });
    }

    public void getCheckpoints(View v) throws IOException {
        TextView checkpointsText = findViewById(R.id.checkpointsText);
        Call<List<DtoCheckpoints>> get = restApi.getCheckpoints();

        get.enqueue(new Callback<List<DtoCheckpoints>>() {
            @Override
            public void onResponse(Call<List<DtoCheckpoints>> call, Response<List<DtoCheckpoints>> response) {
                if(!response.isSuccessful()){
                    checkpointsText.setText("Code: " + response.code());
                    return;
                }

                List<DtoCheckpoints> checkpoints = response.body();

                checkpointsText.setText("");

                for(DtoCheckpoints checkpoint : checkpoints){
                    String content = "";
                    content += "id: " + checkpoint.getId() + "\n";
                    content += "routeId: " + checkpoint.getRouteId() + "\n";
                    content += "description: " + checkpoint.getDescription() + "\n";
                    content += "date: " + checkpoint.getDate() + "\n\n";

                    checkpointsText.append(content);
                }
            }

            @Override
            public void onFailure(Call<List<DtoCheckpoints>> call, Throwable t) {
                checkpointsText.setText(t.getMessage());
            }
        });
    }

    public void createCheckpoint(View v) {

        routeId = findViewById(R.id.routeId);
        routeDescription = findViewById(R.id.routeDescription);
        routeDate = findViewById(R.id.routeDate);
        responseTV = findViewById(R.id.responseTV);
        loadingPB = findViewById(R.id.idLoadingPB);

        loadingPB.setVisibility(View.VISIBLE);

        String date = routeDate.getText().toString().replace(" ", "T");

        if(routeId.getText().toString().isEmpty()){
            loadingPB.setVisibility(View.GONE);
            Toast.makeText(MainActivity.this, "Route id can not be empty", Toast.LENGTH_SHORT).show();
            return;
        }

//        try {
//            date = dateFormatter.parse("2022-02-02 10:00:00");
//            System.out.println("Dateeeee: " + date);
//        }
//        catch (RuntimeException | ParseException e){
//            Toast.makeText(MainActivity.this, "Bad date input. Should be yyyy-MM-dd HH:mm:ss", Toast.LENGTH_SHORT).show();
//            return;
//        }

        DtoPostCheckpoint modal = new DtoPostCheckpoint(routeId.getText().toString(), routeDescription.getText().toString(), date);

        Call<DtoPostCheckpoint> call = restApi.createCheckpoint(modal);

        call.enqueue(new Callback<DtoPostCheckpoint>() {
            @Override
            public void onResponse(Call<DtoPostCheckpoint> call, Response<DtoPostCheckpoint> response) {

                loadingPB.setVisibility(View.GONE);

                if(response.code() == 400){
                    Toast.makeText(MainActivity.this, "Failed to create", Toast.LENGTH_SHORT).show();
                    String responseString = "Response Code : " + response.code() + "\nFailed";
                    responseTV.setText(responseString);
                    return;
                }

                Toast.makeText(MainActivity.this, "Data added to API", Toast.LENGTH_SHORT).show();

                DtoPostCheckpoint responseFromAPI = response.body();

                String responseString = "Response Code : " + response.code() + "\nCreated";

                responseTV.setText(responseString);
            }

            @Override
            public void onFailure(Call<DtoPostCheckpoint> call, Throwable t) {
                System.out.println(t.getMessage());
                responseTV.setText("Error found is : " + t.getMessage());
            }
        });
    }

    public void deleteUser(View v) {

        userId = findViewById(R.id.userId);

        if(userId.getText().toString().isEmpty()){
            loadingPB.setVisibility(View.GONE);
            Toast.makeText(MainActivity.this, "User id can not be empty", Toast.LENGTH_SHORT).show();
            return;
        }

        Call<ResponseBody> call = restApi.deleteCheckpoint(userId.getText().toString());

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                if(response.code() == 400){
                    Toast.makeText(MainActivity.this, "Failed to delete", Toast.LENGTH_SHORT).show();
                    return;
                }

                Toast.makeText(MainActivity.this, "User deleted", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public class RoutesAdapter extends ArrayAdapter<DtoRoutes> {
        public RoutesAdapter(Context context, List<DtoRoutes> routes){
            super(context, 0, routes);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // Get the data item for this position
            DtoRoutes route = getItem(position);
            // Check if an existing view is being reused, otherwise inflate the view
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_route, parent, false);
            }
            // Lookup view for data population
            TextView id = convertView.findViewById(R.id.rId);
            TextView assignedUserId = convertView.findViewById(R.id.rAssignedUserId);
            TextView pointA = convertView.findViewById(R.id.rPointA);
            TextView pointB = convertView.findViewById(R.id.rPointB);
            TextView startDate = convertView.findViewById(R.id.rStartDate);
            TextView endDate = convertView.findViewById(R.id.rEndDate);
            // Populate the data into the template view using the data object
            id.setText("Route id: " + route.getId());
            assignedUserId.setText("Assigned user id: " + route.getAssignedUserId());
            pointA.setText("Point A: " + route.getPointA());
            pointB.setText("Point B: " + route.getPointB());
            startDate.setText("Start date: " + route.getStartDate());
            endDate.setText("End date: " + route.getEndDate());

            // Return the completed view to render on screen
            return convertView;
        }
    }
}