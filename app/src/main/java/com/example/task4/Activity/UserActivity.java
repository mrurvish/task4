package com.example.task4.Activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.task4.Adapter.UserAdapter;
import com.example.task4.DataModels.AllUsers;
import com.example.task4.DataModels.Country;
import com.example.task4.DataModels.RidesRespons;
import com.example.task4.DataModels.User;
import com.example.task4.Fragment.CardDialoug;
import com.example.task4.Network.ApiPath;
import com.example.task4.Network.RetrofitClient;
import com.example.task4.Preference.SharedPreferencesManager;
import com.example.task4.R;
import com.example.task4.Utils.FileUtils;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserActivity extends AppCompatActivity {
    public int pagenum = 1, filterpagenum = 1, searchpage = 1;
    SharedPreferencesManager manager;
    RecyclerView recyclerview;
    Toolbar toolbar;
    RidesRespons rideresponse;
    String[] codes;
    List<Country> country;

    List<User> ridelist = new ArrayList<>();
    List<User> filteredlist = new ArrayList<>();

    UserAdapter adapter;
    String searchtext = "";
    String token;
    int totelpage, totelfilterpage;
    final int PICK_IMAGE_FROM_GALLERY = 1;
    final int CAPTURE_IMAGE_FROM_CAMERA = 2;
    ImageView profile,adduser,profile2;
    MultipartBody.Part part =null;
    File file=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        recyclerview = findViewById(R.id.user_recyclerview);
        manager = new SharedPreferencesManager(this);
adduser = findViewById(R.id.add_user);
        toolbar = findViewById(R.id.usertoolbar);

        Menu menu = toolbar.getMenu();
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                onBackPressed();
                finish();
            }
        });
        token = manager.getToken();
        if (!token.isEmpty()) {
            getcodes(token);
            getallusers(pagenum, token);
        }
        MenuItem searchItem = menu.findItem(R.id.app_bar_search);

        SearchView search = (SearchView) searchItem.getActionView();
        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                filteredlist.clear();
                searchpage = 1;
                searchtext = query;
             getsearchusers(token,searchpage,searchtext);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                //filter(newText);
                return false;
            }

        });
        search.setOnCloseListener(new SearchView.OnCloseListener() {

            @Override
            public boolean onClose() {
                // Perform your action when the close button is pressed
                // For example, clear the search results or do something else

                    filteredlist.clear();
                   adapter.filterlist(ridelist);
                   adapter.notifyDataSetChanged();

                // Return true if you want to consume the event, false otherwise
                return false;
            }
        });
        recyclerview.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View view, int i, int i1, int i2, int i3) {
                boolean val = view.canScrollVertically(1);
                if (searchtext.isEmpty()) {
                    if (pagenum < totelpage) {
                        if (!val) {
                            pagenum++;
                            getallusers(pagenum, token);
                        }
                    }
                } else {

                    if (searchpage < totelfilterpage) {
                        if (!val) {
                            searchpage++;
                            getsearchusers(token, searchpage, searchtext);
                        }
                    }
                }
            }
        });
        adduser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAddDialoug(UserActivity.this);
            }
        });

    }
    public void getallusers(int pagenum, String token)
    {
        String modifiedtoken = "Bearer " + token;
        filteredlist = new ArrayList<>();
        ApiPath path = RetrofitClient.getRetrofitInstance().create(ApiPath.class);

        Call<AllUsers> call = path.getAllusers(modifiedtoken, String.valueOf(pagenum),"");
        call.enqueue(new Callback<AllUsers>() {
            @Override
            public void onResponse(Call<AllUsers> call, Response<AllUsers> response) {
                if (response.isSuccessful())
                {
                    totelpage = response.body().getPageCount();
                    // Add the new rides to the existing list
                    ridelist.addAll(response.body().getUsers());
                    if (adapter == null) {
                        adapter = new UserAdapter(UserActivity.this, ridelist);
                        recyclerview.setLayoutManager(new LinearLayoutManager(UserActivity.this));
                        recyclerview.setAdapter(adapter);
                        adapter.setOnItemClickListener(new UserAdapter.OnItemClickListener() {

                            @Override
                            public void onItemClick(int position) {

                            }

                            @Override
                            public void oneditUserclick(int position) {
                                if (searchtext.isEmpty()) {
                                    shoeEditDialoug(UserActivity.this, ridelist.get(position));
                                }else {
                                    shoeEditDialoug(UserActivity.this, filteredlist.get(position));
                                }
                            }

                            @Override
                            public void onDeleteClick(int position) {
                                if (searchtext.isEmpty()) {
                                    deleteUser(token, ridelist.get(position));
                                }else {
                                    deleteUser(token, filteredlist.get(position));
                                }
                            }

                            @Override
                            public void onCardClick(int position) {
                                CardDialoug card= new CardDialoug();

                                if (searchtext.isEmpty()) {
                                    card.setid(ridelist.get(position).getId(),token);
                                }else {
                                    card.setid(filteredlist.get(position).getId(),token);
                                }
                                card.show(getSupportFragmentManager(),"hello");
                            }

                        });

                    } else {
                        adapter.filterlist(ridelist);
                        adapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onFailure(Call<AllUsers> call, Throwable t) {

            }
        });
    }
    public void getsearchusers(String token, int pagenum, String searchtext)
    {
        String modifiedtoken = "Bearer " + token;
        filteredlist = new ArrayList<>();
        ApiPath path = RetrofitClient.getRetrofitInstance().create(ApiPath.class);

        Call<AllUsers> call = path.getAllusers(modifiedtoken, String.valueOf(pagenum),searchtext);
        call.enqueue(new Callback<AllUsers>() {
            @Override
            public void onResponse(Call<AllUsers> call, Response<AllUsers> response) {
                if (response.isSuccessful()) {

                    totelfilterpage = response.body().getPageCount();
                    filteredlist.addAll(response.body().getUsers());
                    adapter.filterlist(filteredlist);
                    adapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(UserActivity.this, "No rides found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<AllUsers> call, Throwable t) {

            }
        });
    }
    public  void shoeEditDialoug(Context context,User user) {
        // Inflate the layout for the dialog
        View view = LayoutInflater.from(context).inflate(R.layout.edit_user, null);

        // Find views in the layout
         EditText name = view.findViewById(R.id.name_edit_user);
        EditText email = view.findViewById(R.id.email_edit_user);
        Spinner country = view.findViewById(R.id.code_edit_user);
        EditText phone = view.findViewById(R.id.phone_edit_user);
        ImageView chose=view.findViewById(R.id.chose_edit_user);
        profile=view.findViewById(R.id.profile_pic_edit_image);
chose.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        PopupMenu popupMenu = new PopupMenu(UserActivity.this, view);
        popupMenu.getMenuInflater().inflate(R.menu.chose, popupMenu.getMenu());
        final int MENU_ITEM_1 = R.id.gelary;
        final int MENU_ITEM_2 = R.id.camera;

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == MENU_ITEM_1) {

                    Intent galleryIntent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                    galleryIntent.addCategory(Intent.CATEGORY_OPENABLE);
                    galleryIntent.setType("image/*");
                    startActivityForResult(galleryIntent, PICK_IMAGE_FROM_GALLERY);


                }
                if (item.getItemId() == MENU_ITEM_2) {
                    Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, CAPTURE_IMAGE_FROM_CAMERA);

                }

                return false;
            }
        });

        popupMenu.show();

    }
});
name.setText(user.getName());
email.setText(user.getEmail());
phone.setText(user.getPhone());
        String path = "http://192.168.0.215:3000/" + user.getProfile();

        Picasso.get()
                .load(path)
                .into(profile);
        // Create an AlertDialog builder
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setView(view);
        ArrayAdapter<String> arrayadapter = new ArrayAdapter<>(UserActivity.this, android.R.layout.simple_list_item_1, codes);
        arrayadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        country.setAdapter(arrayadapter);
        // Set positive button
        builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Handle the feedback submission here
                if (phone.getText().toString().length() != 10)
                {
                    return;
                }
                if (!Patterns.EMAIL_ADDRESS.matcher(email.getText().toString()).matches())
                {
                    return;
                }
                if (country.getSelectedItem()==null)
                {
                    return;
                }
                if (name.getText().toString().isEmpty() || email.getText().toString().isEmpty())
                {
                    return;
                }
                AllUsers.Useredit body = new AllUsers.Useredit(name.getText().toString(),email.getText().toString(),country.getSelectedItem().toString(),phone.getText().toString(),user.getId(),part);
                edituser(token,body);
            }
        });

        // Set negative button
        builder.setNegativeButton("Discard", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Handle cancellation if needed
                dialog.dismiss();
            }
        });
        builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {

            }
        });

        // Show the AlertDialog
        builder.create().show();
    }
    private void edituser(String token, AllUsers.Useredit body) {
        Toast.makeText(this, "Hello world...", Toast.LENGTH_SHORT).show();
        String modifiedtoken = "Bearer " + this.token;
        ApiPath path = RetrofitClient.getRetrofitInstance().create(ApiPath.class);
//        RequestBody fileRequestBody = RequestBody.create(MediaType.parse("application/octet-stream"), file);
        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);

        RequestBody id = RequestBody.create(MediaType.parse("multipart/form-data"), body.getId());
        RequestBody name = RequestBody.create(MediaType.parse("multipart/form-data"), body.getName());
        RequestBody email = RequestBody.create(MediaType.parse("multipart/form-data"), body.getEmail());
        RequestBody phoneCode = RequestBody.create(MediaType.parse("multipart/form-data"), body.getPhoneCode());
        RequestBody phone = RequestBody.create(MediaType.parse("multipart/form-data"), body.getPhone());
        // Create the file request body
        RequestBody fileBody = RequestBody.create(MediaType.parse("image/*"), file);

        builder.addFormDataPart("id", body.getId())
                .addFormDataPart("name",body.getName())
                .addFormDataPart("phone",body.getPhone())
                .addFormDataPart("phoneCode",body.getPhoneCode())
                .addFormDataPart("email",body.getEmail());
//                .addFormDataPart("profile",file.getName(), RequestBody.create(MultipartBody.FORM, file));
        RequestBody requestBody = builder.build();

        Call<User> call = path.updateUser(modifiedtoken,id,name,email,phoneCode,phone,part);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful())
                {
                    adapter.updateRide(response.body());
                }else {
                    Toast.makeText(UserActivity.this, "404", Toast.LENGTH_SHORT).show();
                }
                part=null;
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                part=null;
                Toast.makeText(UserActivity.this, "failed", Toast.LENGTH_SHORT).show();
            }
        });

    }
    private void showAddDialoug(Context context){
        View view = LayoutInflater.from(context).inflate(R.layout.edit_user, null);

        // Find views in the layout
        EditText name = view.findViewById(R.id.name_edit_user);
        EditText email = view.findViewById(R.id.email_edit_user);
        Spinner country = view.findViewById(R.id.code_edit_user);
        EditText phone = view.findViewById(R.id.phone_edit_user);
        ImageView chose=view.findViewById(R.id.chose_edit_user);
        profile=view.findViewById(R.id.profile_pic_edit_image);
        profile.setImageResource(0);
        chose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(UserActivity.this, view);
                popupMenu.getMenuInflater().inflate(R.menu.chose, popupMenu.getMenu());
                final int MENU_ITEM_1 = R.id.gelary;
                final int MENU_ITEM_2 = R.id.camera;

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        if (item.getItemId() == MENU_ITEM_1) {

                            Intent galleryIntent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                            galleryIntent.addCategory(Intent.CATEGORY_OPENABLE);
                            galleryIntent.setType("image/*");
                            startActivityForResult(galleryIntent, PICK_IMAGE_FROM_GALLERY);


                        }
                        if (item.getItemId() == MENU_ITEM_2) {
                            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            startActivityForResult(cameraIntent, CAPTURE_IMAGE_FROM_CAMERA);

                        }

                        return false;
                    }
                });

                popupMenu.show();

            }
        });


        // Create an AlertDialog builder
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setView(view);
        ArrayAdapter<String> arrayadapter = new ArrayAdapter<>(UserActivity.this, android.R.layout.simple_list_item_1, codes);
        arrayadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        country.setAdapter(arrayadapter);
        // Set positive button
        builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Handle the feedback submission here
                if (phone.getText().toString().length() != 10)
                {
                    return;
                }
                if (!Patterns.EMAIL_ADDRESS.matcher(email.getText().toString()).matches())
                {
                    return;
                }
                if (country.getSelectedItem()==null)
                {
                    return;
                }
                if (name.getText().toString().isEmpty() || email.getText().toString().isEmpty())
                {
                    return;
                }
                if (part == null)
                {
                    return;
                }
                AllUsers.Useredit body = new AllUsers.Useredit(name.getText().toString(),email.getText().toString(),country.getSelectedItem().toString(),phone.getText().toString());
                adduser(token,body);
            }
        });

        // Set negative button
        builder.setNegativeButton("Discard", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Handle cancellation if needed
                dialog.dismiss();
            }
        });
        builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {

            }
        });

        // Show the AlertDialog
        builder.create().show();
    }

    private void adduser(String token, AllUsers.Useredit body) {
        String modifiedtoken = "Bearer " + this.token;
        ApiPath path = RetrofitClient.getRetrofitInstance().create(ApiPath.class);
//        RequestBody fileRequestBody = RequestBody.create(MediaType.parse("application/octet-stream"), file);
        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);


        RequestBody name = RequestBody.create(MediaType.parse("multipart/form-data"), body.getName());
        RequestBody email = RequestBody.create(MediaType.parse("multipart/form-data"), body.getEmail());
        RequestBody phoneCode = RequestBody.create(MediaType.parse("multipart/form-data"), body.getPhoneCode());
        RequestBody phone = RequestBody.create(MediaType.parse("multipart/form-data"), body.getPhone());
        Call<User> call = path.addUser(modifiedtoken,name,email,phoneCode,phone,part);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful())
                {

                    Toast.makeText(UserActivity.this, "User added succesfully...", Toast.LENGTH_SHORT).show();

                }
                part=null;
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                part=null;
            }
        });
    }
private void deleteUser(String token,User body){
    String modifiedtoken = "Bearer " + this.token;
    ApiPath path = RetrofitClient.getRetrofitInstance().create(ApiPath.class);
    Call<User> call = path.deleteUser(body.getId(),modifiedtoken);
    call.enqueue(new Callback<User>() {
        @Override
        public void onResponse(Call<User> call, Response<User> response) {
            if(response.isSuccessful())
            {
                adapter.removeRide(body);
                Toast.makeText(UserActivity.this, response.body().getMsg(), Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onFailure(Call<User> call, Throwable t) {

        }
    });
}
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PICK_IMAGE_FROM_GALLERY:
                    Uri selectedImageUri = data.getData();
                    Picasso.get()
                            .load(selectedImageUri)
                            .into(profile);
                    String filePath =  FileUtils.getRealPath(this,selectedImageUri);
                   file = new File(filePath);
                    String name = file.getName();
                    RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
                    part =MultipartBody.Part.createFormData("profile",file.getName(),requestFile);
                    break;

                case CAPTURE_IMAGE_FROM_CAMERA:
                    // Handle captured image from the camera
                    Bitmap photo = (Bitmap) data.getExtras().get("data");
                    // Update the image view in the dialog or perform any other desired action
                    // For example, profileImageView.setImageBitmap(photo);
                   profile.setImageBitmap(photo);
                    break;
            }
        }
    }


    private void getcodes(String token) {

        String modifiedtoken = "Bearer " + token;

        ApiPath path = RetrofitClient.getRetrofitInstance().create(ApiPath.class);
        Call<List<Country>> call = path.getGountries(modifiedtoken);
        call.enqueue(new Callback<List<Country>>() {
            @Override
            public void onResponse(Call<List<Country>> call, Response<List<Country>> response) {
                country = response.body();

                if (country != null) {
                    codes = new String[country.size()];
                    for (int i = 0; i < country.size(); i++) {
                        codes[i] = country.get(i).getCode();
                    }

                }



            }

            @Override
            public void onFailure(Call<List<Country>> call, Throwable t) {

                Toast.makeText(UserActivity.this, "Error!!!!!!!!!", Toast.LENGTH_SHORT).show();
                Log.d("createrides", t.getMessage());
            }
        });

    }
}