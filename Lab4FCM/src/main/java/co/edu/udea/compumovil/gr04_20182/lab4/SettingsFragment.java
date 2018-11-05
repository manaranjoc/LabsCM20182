package co.edu.udea.compumovil.gr04_20182.lab4;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.util.ArrayList;

import gun0912.tedbottompicker.TedBottomPicker;

public class SettingsFragment extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener {

    private FirebaseUser user;
    private UserProfileChangeRequest profileUpdates;
    private StorageReference storageReference;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.settings_screen,rootKey);

        user = FirebaseAuth.getInstance().getCurrentUser();
        storageReference = FirebaseStorage.getInstance().getReference();

        Preference picture = findPreference("profile");
        picture.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                PermissionListener permissionlistener = new PermissionListener() {
                    @Override
                    public void onPermissionGranted() {
                        TedBottomPicker tedBottomPicker = new TedBottomPicker.Builder(getContext())
                                .setOnImageSelectedListener(new TedBottomPicker.OnImageSelectedListener() {
                                    @Override
                                    public void onImageSelected(Uri uri) {

                                        StorageReference profileReference = storageReference.child("profile").child(user.getEmail());

                                        putProfileImage(profileReference, uri);
                                    }
                                })
                                .create();

                        tedBottomPicker.show(getFragmentManager());

                    }

                    @Override
                    public void onPermissionDenied(ArrayList<String> deniedPermissions) {
                        Toast.makeText(getContext(), "Permission Denied\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
                    }


                };
                TedPermission.with(getContext())
                        .setPermissionListener(permissionlistener)
                        .setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
                        .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        .check();
                return false;
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        getPreferenceManager().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        getPreferenceManager().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        int id = sharedPreferences.getInt("id",-1);
        UsersDbHelper usersDbHelper = new UsersDbHelper(getActivity().getApplicationContext());
        SQLiteDatabase db = usersDbHelper.getWritableDatabase();
        if(s.equals("email")){
            user.updateEmail(sharedPreferences.getString("email", ""))
            .addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        Log.d("Settings: ", "User email address updated");
                    }
                }
            });
        }else if(s.equals("name")){
            profileUpdates = new UserProfileChangeRequest.Builder()
                    .setDisplayName(sharedPreferences.getString("name", ""))
                    .build();

            user.updateProfile(profileUpdates)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Log.d("Settings: ", "User name updated");
                            }
                        }
                    });
        }else if(s.equals("password")){
            user.updatePassword(sharedPreferences.getString("password", ""))
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                Log.d("Settings: ", "User password updated");
                            }
                        }
                    });
        }else if(s.equals("interval")){
            Intent service = new Intent(getContext(), ReceiverService.class);
            service.putExtra("interval", Integer.parseInt(sharedPreferences.getString("interval", "60")));
            getContext().startService(service);
        }else if(s.equals("allow_notifications")){
            if(sharedPreferences.getBoolean(s, false)) {
                FirebaseMessaging.getInstance().subscribeToTopic("notifications");
            }else{
                FirebaseMessaging.getInstance().unsubscribeFromTopic("notifications");
            }
        }
    }

    public void putProfileImage(final StorageReference storageReference, Uri file){
        UploadTask uploadTask = storageReference.putFile(file);
        uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if(!task.isSuccessful()){
                    throw task.getException();
                }
                return storageReference.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    Uri downloadUri = task.getResult();
                    UserProfileChangeRequest profileUpdates;

                    profileUpdates = new UserProfileChangeRequest.Builder()
                            .setPhotoUri(downloadUri)
                            .build();

                    if(user != null) {
                        user.updateProfile(profileUpdates)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()){
                                            Log.d("Settings: ", "User image updated");
                                        }
                                    }
                                });
                    }

                } else {
                    Log.w("Profile Image: ", "Image Upload Task not successful");
                }
            }
        });
    }
}
