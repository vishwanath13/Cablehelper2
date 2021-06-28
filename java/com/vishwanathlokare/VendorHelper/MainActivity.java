package com.vishwanathlokare.VendorHelper;

import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.FileList;
import com.vishwanathlokare.VendorHelper.PdfUtills.DriveServiceHelper;
import com.vishwanathlokare.VendorHelper.PdfUtills.UpdateHelper;

import com.vishwanathlokare.VendorHelper.ui.Database_helper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;

import androidx.core.app.ActivityCompat;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import java.util.Collections;


public class MainActivity extends AppCompatActivity implements UpdateHelper.OnUpdateCheckListener {

    private AppBarConfiguration mAppBarConfiguration;
    ImageView sign;


    OutputStream outputStream;
    GoogleSignInClient mGoogleSignInClient;
    public static final int SELECT_PHOTO = 15;
    public static final int RC_SIGN_IN = 99;
    public static final int Request100 = 100;



    MenuItem cloud;
    View headerView;
    TextView navuser;
    TextView navStore;
    MenuItem itemsign;
    ImageView userImage;
    String personName;
    String personGivenName;
    String personFamilyName;
    String personEmail;
    String personId;
    Uri personPhoto;
    DriveServiceHelper mDriveServiceHelper;
    Task<Void> th;
    Scope SCOPE_WRITE = new Scope("https://www.googleapis.com/auth/drive.file");
    final Database_helper database_helper = new Database_helper(MainActivity.this);

    @Override
    public void onBackPressed() {

        int count = getSupportFragmentManager().getBackStackEntryCount();
        if (count == 0) {
            super.onBackPressed();
        } else {
            getSupportFragmentManager().popBackStack();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        itemsign = menu.findItem(R.id.SignIn);
        cloud = menu.findItem(R.id.information);


        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);

        if (account == null) {


        } else {
                    /*InputStream is = new java.net.URL(personPhoto.toString()).openStream();
                    Bitmap bitmap = BitmapFactory.decodeStream(is);
                    userImage.setImageBitmap(bitmap);*/
            if (account.getPhotoUrl() != null) {
                LoadImage loadImage = new LoadImage();
                loadImage.execute(account.getPhotoUrl().toString());

            } else {

                itemsign.setIcon(null);

            }
            String[] names = account.getDisplayName().split(" ");
            StringBuilder sb = new StringBuilder();
            for (String name : names) {
                sb.append(name.charAt(0));
            }
            itemsign.setTitle(sb.toString().toUpperCase());

        }


        return true;
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);





        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);


        headerView = navigationView.getHeaderView(0);
        navuser = (TextView) headerView.findViewById(R.id.navUser);
        navStore = (TextView) headerView.findViewById(R.id.nav);
        userImage = headerView.findViewById(R.id.imageView);


        final Database_helper database_helper = new Database_helper(MainActivity.this);
        ArrayList<String> store = database_helper.get_Store();
        if (!store.isEmpty()) navStore.setText(store.get(0));


        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow, R.id.History,
                R.id.nav_renewal, R.id.complaint_Box_Fragment, R.id.customer_History, R.id.nav_worker, R.id.nav_line, R.id.nav_line_customer)
                .setDrawerLayout(drawer)
                .build();
        final NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                navController.navigate(R.id.new_customer_Fragment);

            }
        });
        UpdateHelper.with(this).OnUpdateCheck(this).check();

        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        // Check for existing Google Sign In account, if the user is already signed in
// the GoogleSignInAccount will be non-null.


        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
        boolean isfirstRun = prefs.getBoolean("FIRSTRUN", true);




        if (isfirstRun) {

            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, Request100);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean("FIRSTRUN",false);
            editor.commit();

        }
    }




    public void setActionBarTitle(String name) {
        getSupportActionBar().setTitle(name);
    }



    @Override
    public boolean onSupportNavigateUp() {

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();


    }


    @Override
    public void onUpdateCheckListener(final String urlApp) {
        AlertDialog dialog = new AlertDialog.Builder(this).
                setTitle("New Version Available").setMessage("Please update to newer version for better performance")
                .setPositiveButton("UPDATE", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(MainActivity.this, "" + urlApp, Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(urlApp));
                        startActivity(intent);
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                }).create();
        dialog.show();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){

            case R.id.Action_search:


                AlertDialog.Builder RealBuilder = new AlertDialog.Builder(MainActivity.this);
                RealBuilder.setTitle("DO YOU WANT TO GET DATA BACK FROM GOOGLE DRIVE");
                RealBuilder.setMessage("NOTE - ALL the data stored currently on Device will get deleted and new data will be Added").setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).setPositiveButton("yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface rialog, int which) {


                View view1 = LayoutInflater.from(getApplicationContext()).inflate(R.layout.progress_dialog, null);
                final AlertDialog dialog = new AlertDialog.Builder(MainActivity.this).setTitle("Please ".toUpperCase() +
                        "wait while We are getting data from cloud".toUpperCase())
                        .setView(view1).create();

                dialog.show();


                GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(MainActivity.this);

                if (account == null) {
                    Toast.makeText(getApplicationContext(), "Please Login first with your google id",
                            Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                } else {
                    if (!GoogleSignIn.hasPermissions(account, SCOPE_WRITE)) {
                        GoogleSignIn.requestPermissions(
                                MainActivity.this, 98, account, SCOPE_WRITE);
                    } else {

                        GoogleAccountCredential credential =
                                GoogleAccountCredential.usingOAuth2(
                                        getApplicationContext(), Collections.singleton(DriveScopes.DRIVE_FILE));
                        credential.setSelectedAccount(account.getAccount());
                        Drive googleDriveService =
                                new Drive.Builder(
                                        AndroidHttp.newCompatibleTransport(),
                                        new GsonFactory(),
                                        credential)
                                        .setApplicationName("VendorHelper")
                                        .build();
                        mDriveServiceHelper = new DriveServiceHelper(googleDriveService);
                        final Task<FileList> files = mDriveServiceHelper.queryFiles();
                        files.addOnSuccessListener(new OnSuccessListener<FileList>() {
                            @Override
                            public void onSuccess(FileList fileList) {
                                for (com.google.api.services.drive.model.File file : fileList.getFiles()) {
                                    if (file.getName().equals(getString(R.string.app_name) + ".json")) {
                                        mDriveServiceHelper.readFile(file.getId(), getApplicationContext())
                                                .addOnSuccessListener(new OnSuccessListener<File>() {
                                                    @Override
                                                    public void onSuccess(File file) {

                                                        try {
                                                            database_helper.readJsonfile(file);
                                                            Toast.makeText(getApplicationContext(),
                                                                    "Backup has been downloaded successfully",
                                                                    Toast.LENGTH_SHORT).show();
                                                            dialog.dismiss();

                                                            finish();
                                                            overridePendingTransition(0,0);
                                                            startActivity(getIntent());
                                                            overridePendingTransition(0,0);

                                                        } catch (FileNotFoundException e) {
                                                            e.printStackTrace();
                                                            Toast.makeText(getApplicationContext(),
                                                                    " Unable to get Data from Server",
                                                                    Toast.LENGTH_SHORT).show();
                                                            dialog.dismiss();
                                                        }
                                                    }
                                                }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(getApplicationContext(),
                                                        "Failure Occured Please Contact VendorHelper Developer" + e.getMessage(),

                                                        Toast.LENGTH_SHORT).show();
                                                dialog.dismiss();
                                            }
                                        });

                                    }
                                }
                            }
                        });
                        files.addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getApplicationContext(),
                                        "Could not find any backup", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            }
                        });
                    }
                }
                    }
                }).create().show();
                break;
            case R.id.information:
                android.app.AlertDialog.Builder builder1 = new android.app.AlertDialog.Builder(this);
                View v = LayoutInflater.from(this).inflate(R.layout.vendor_info_dialog,null,false);
                 sign =  v.findViewById(R.id.imageView2);
                final EditText store = v.findViewById(R.id.StoreName);
                final EditText name = v.findViewById(R.id.username);


                ArrayList<String> result = database_helper.get_Store();
                if(result.size() != 0){
                    store.setText(result.get(0));
                    name.setText(result.get(1));
                }
                if (new File(new StringBuilder(getAppPath()).append("Signature.jpg").toString()).exists()){

                    Uri u = Uri.parse(new StringBuilder(getAppPath()).append("Signature.jpg").toString());
                    sign.setImageURI(u);
                    sign.setBackground(null);
                }

                 sign.setOnClickListener(new View.OnClickListener() {
                     @Override
                     public void onClick(View v) {
                         Intent intent = new Intent(Intent.ACTION_GET_CONTENT).setType("image/*");
                         startActivityForResult(intent,SELECT_PHOTO);

                     }
                 });

                builder1.setView(v);
                builder1.setTitle("USERS INFORMATION").setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                }).setNeutralButton("RESET", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        final android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(MainActivity.this);
                        builder.setTitle("DO YOU REALLY WANT TO RESET ALL CUSTOMER").setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        }).setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                boolean b = database_helper.delete_paid();

                                finish();
                                overridePendingTransition(0,0);
                                startActivity(getIntent());
                                overridePendingTransition(0,0);

                                if(b) {
                                    Toast.makeText(MainActivity.this, "All Customer has been added Succesfully " + b, Toast.LENGTH_SHORT).show();
                                }
                                else{
                                    Toast.makeText(MainActivity.this, "Error while doing reset ", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                        builder.show();
                    }
                })
                        .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {


                        if (store.getText().toString().equals("") | name.getText().toString().equals("")) {
                            Toast.makeText(MainActivity.this, "Please Provide Store and User Name",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                             database_helper.update_Store(store.getText().toString(), name.getText().toString());

                            setActionBarTitle(store.getText().toString().toUpperCase());

                            if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE
                            ) == PackageManager.PERMISSION_GRANTED) {

                                try {
                                    BitmapDrawable drawable = (BitmapDrawable) sign.getDrawable();
                                    Bitmap bitmap = drawable.getBitmap();
                                    if (new File(new StringBuilder(getAppPath()).append("Signature.jpg").toString()).exists())
                                        new File(new StringBuilder(getAppPath()).append("Signature.jpg").toString()).delete();

                                    try {
                                        outputStream = new FileOutputStream(new File(new StringBuilder(getAppPath()).append("Signature.jpg").toString()));
                                    } catch (FileNotFoundException e) {
                                        e.printStackTrace();
                                    }
                                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);

                                    Toast.makeText(MainActivity.this, "Information Saved Successfully", Toast.LENGTH_SHORT).show();

                                    try {
                                        outputStream.flush();
                                        outputStream.close();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }


                            } else {
                                ActivityCompat.requestPermissions(MainActivity.this,
                                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, Request100);

                            }
                        }
                    }
                });

                builder1.create().show();
                break;
            case R.id.SignIn:
                if(itemsign.getTitle().toString().equals("SignIn")) {
                    signIn();

                }
                else {
                    Toast.makeText(MainActivity.this,itemsign.getTitle(),Toast.LENGTH_SHORT).show();
                    AlertDialog.Builder b = new AlertDialog.Builder(MainActivity.this);
                    b.setTitle("DO YOU REALLY WANT TO SIGN OUT")
                            .setMessage("Note - Backing up your new Data will delete your previous backup")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    signOut();
                                    revokeAccess();
                                    Toast.makeText(MainActivity.this,"SIGNED OUT SUCCESSFULLY",Toast.LENGTH_SHORT).show();
                                }
                            }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    }).setNeutralButton("BACKUP", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            View view1 = LayoutInflater.from(getApplicationContext()).inflate(R.layout.progress_dialog,null);
                            final AlertDialog dialog1 = new AlertDialog.Builder(MainActivity.this)
                                    .setTitle("Please wait while backup is being uploaded".toUpperCase())
                                    .setView(view1).create();
                            dialog1.show();
                            GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(MainActivity.this);

                            if(account == null){
                                Toast.makeText(getApplicationContext(),"Please Login with Your google id first",
                                        Toast.LENGTH_SHORT).show();
                                dialog1.dismiss();
                            }
                            else {
                                if (!GoogleSignIn.hasPermissions(account,SCOPE_WRITE)){
                                    GoogleSignIn.requestPermissions(
                                            MainActivity.this,98,account,SCOPE_WRITE);
                                }
                                else {

                                    GoogleAccountCredential credential =
                                            GoogleAccountCredential.usingOAuth2(
                                                    getApplicationContext(), Collections.singleton(DriveScopes.DRIVE_FILE));
                                    credential.setSelectedAccount(account.getAccount());
                                    Drive googleDriveService =
                                            new Drive.Builder(
                                                    AndroidHttp.newCompatibleTransport(),
                                                    new GsonFactory(),
                                                    credential)
                                                    .setApplicationName("VendorHelper")
                                                    .build();
                                    mDriveServiceHelper = new DriveServiceHelper(googleDriveService);
                                    final Task<FileList> files = mDriveServiceHelper.queryFiles();
                                    files.addOnSuccessListener(new OnSuccessListener<FileList>() {
                                        @Override
                                        public void onSuccess(FileList fileList) {
                                            for (com.google.api.services.drive.model.File file : fileList.getFiles()) {
                                                if (file.getName().equals(getString(R.string.app_name) + ".json")) {
                                                    th = mDriveServiceHelper.deletefile(file.getId()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void aVoid) {
                                                            Toast.makeText(getApplicationContext(),
                                                                    "Deleted Previous file",Toast.LENGTH_SHORT).show();
                                                        }
                                                    });

                                                }
                                            }
                                        }
                                    });


                                    }

                                final Task<String> g = mDriveServiceHelper.createFile(getApplicationContext());
                                g.addOnSuccessListener(
                                        new OnSuccessListener<String>() {
                                            @Override
                                            public void onSuccess(String s) {
                                                mDriveServiceHelper.saveJsonFile(s,getApplicationContext()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        Toast.makeText(getApplicationContext(),
                                                                "Backup saved Successfully", Toast.LENGTH_SHORT).show();
                                                        dialog1.dismiss();
                                                    }
                                                }).addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Toast.makeText(getApplicationContext(), e.getMessage()
                                                                , Toast.LENGTH_SHORT).show();
                                                        dialog1.dismiss();
                                                    }
                                                });

                                            }
                                        });
                                g.addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(getApplicationContext(), e.getMessage()
                                                , Toast.LENGTH_SHORT).show();
                                        dialog1.dismiss();
                                    }
                                });

                            }

                        }
                    })
                            .create().show();

                }

                break;

        }





        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {

            case Request100:
                if(grantResults[0] == PackageManager.PERMISSION_GRANTED)
                Toast.makeText(MainActivity.this,"Permission Granted",Toast.LENGTH_SHORT).show();
                else  Toast.makeText(MainActivity.this,"Permission Denied, Please Try again later",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == SELECT_PHOTO) {
            if (resultCode == RESULT_OK) {
                Uri selectImage = data.getData();
                InputStream inputStream = null;
                try {
                    assert selectImage != null;
                    inputStream = getContentResolver().openInputStream(selectImage);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                BitmapFactory.decodeStream(inputStream);
                sign.setImageURI(selectImage);
                sign.setBackground(null);

            }
        }
        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        else if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.

            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
        else {
            Toast.makeText(MainActivity.this, " Unable to Perform the Task", Toast.LENGTH_SHORT).show();
        }


    }


    private String getAppPath() {
        File dir = new File(Environment.getExternalStorageDirectory() + File.separator
                +getResources().getString(R.string.app_name) + File.separator);
        if(!dir.exists()) {
            dir.mkdir();
        }
        return dir.getPath() + File.separator;
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(MainActivity.this);
            

            if (acct != null) {
                String personName = acct.getDisplayName();
                String personGivenName = acct.getGivenName();
                String personFamilyName = acct.getFamilyName();
                String personEmail = acct.getEmail();
                String personId = acct.getId();
                Uri personPhoto = acct.getPhotoUrl();
                Toast.makeText(MainActivity.this,personName + " is logged",Toast.LENGTH_SHORT).show();


                if(personPhoto == null){
                    itemsign.setIcon(null);
                }
                else {

                    LoadImage loadImage = new LoadImage();
                    loadImage.execute(personPhoto.toString());


                }


                String[] names = personName.split(" ");
                StringBuilder sb = new StringBuilder();
                for(String name : names){
                    sb.append(name.charAt(0));
                }
                itemsign.setTitle(sb.toString().toUpperCase());


                if(!(personName == null)) navuser.setText(personName);

            }
            else {
                Toast.makeText(MainActivity.this," Unable to login, Please Try again",Toast.LENGTH_SHORT).show();
            }



            // Signed in successfully, show authenticated UI.

        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.d("TAG1","signInResult:failed code=" + e.toString());
            Toast.makeText(MainActivity.this,"SomeThing Went Wrong",Toast.LENGTH_SHORT).show();

        }
    }

    private void signOut() {
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        // ...
                        itemsign.setTitle("SignIn");
                        itemsign.setIcon(R.mipmap.ic_launcher_round);
                        finish();
                        overridePendingTransition(0,0);
                        startActivity(getIntent());
                        overridePendingTransition(0,0);

                    }
                });
    }
    private void revokeAccess() {
        mGoogleSignInClient.revokeAccess()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        // ...
                    }
                });
    }

    public class LoadImage extends AsyncTask<String,Void,Bitmap> {

        public LoadImage() {

        }


        @Override
        protected Bitmap doInBackground(String... strings) {
            String urlink = strings[0];
            Bitmap bitmap = null;
            try {
                InputStream inputStream = new java.net.URL(urlink).openStream();
                bitmap = BitmapFactory.decodeStream(inputStream);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            RoundedBitmapDrawable roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(getResources(),bitmap);
            roundedBitmapDrawable.setCornerRadius(50.0f);
            roundedBitmapDrawable.setAntiAlias(true);
            userImage.setImageDrawable(roundedBitmapDrawable);
            RoundedBitmapDrawable roundedBitmapDrawable1 = RoundedBitmapDrawableFactory.create(getResources(),
                    resizeImage(bitmap,250,250));
            roundedBitmapDrawable1.setCornerRadius(50.0f);
            roundedBitmapDrawable1.setAntiAlias(true);

            itemsign.setIcon(roundedBitmapDrawable1);
        }
    }

    private Bitmap resizeImage(Bitmap roundedBitmapDrawable, int i, int i1) {
        int width = roundedBitmapDrawable.getWidth();
        int height = roundedBitmapDrawable.getHeight();
        int newWidth = i;
        int newHeight = i1;
        float scaleW = ((float) newWidth ) /width ;
        float scaleH = ((float) newHeight ) / height ;
        Matrix matrix = new Matrix();
        matrix.postScale(scaleW,scaleH);
        Bitmap resizedBitmap = Bitmap.createBitmap(roundedBitmapDrawable,0,0,width,height,matrix,true);
        return resizedBitmap;

    }


}