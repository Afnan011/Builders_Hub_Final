package dev.afnan.builders_hub.UserModule;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import dev.afnan.builders_hub.Common.Common;
import dev.afnan.builders_hub.ViewHolder.ListAdapter;
import dev.afnan.builders_hub.R;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import de.hdodenhof.circleimageview.CircleImageView;
import dev.afnan.builders_hub.auth.loginActivity;

import static android.content.Context.MODE_PRIVATE;

//import com.firebase.ui.database.FirebaseRecyclerOptions;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;


public class ProfileFragment extends Fragment {

    private FirebaseAuth mAuth;
    private ProgressBar progressBar;
    private String name, phone, email;
    private ListView listView;
    private CircleImageView userImage;
    private FirebaseStorage storage;
    private Uri imageURI;
    private StorageReference storageReference;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        progressBar = view.findViewById(R.id.user_progressBar);
        listView = view.findViewById(R.id.user_profile_list_view);
        userImage = view.findViewById(R.id.user_imageView);

        mAuth = FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance();

        storageReference = storage.getReference().child("Users/" + FirebaseAuth.getInstance().getUid() + "/profile.jpg");
        progressBar.setVisibility(View.VISIBLE);

        loadProfileImageFromFirebase();


        try {
            SharedPreferences sp = requireActivity().getSharedPreferences("userInfo", MODE_PRIVATE);
            name = sp.getString("name", name);
            phone = sp.getString("phone", phone);
            email = sp.getString("email", email);

        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        displayList(name, phone, email);
        if (name != null && phone != null && email != null) {
            progressBar.setVisibility(View.GONE);
        } else {
            progressBar.setVisibility(View.VISIBLE);
        }

        progressBar.setVisibility(View.VISIBLE);


        userImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 33);

            }
        });

        progressBar.setVisibility(View.GONE);

        Handler handler = new Handler();
        handler.postDelayed(() -> progressBar.setVisibility(View.GONE), 5000);

        return view;
    }

    public void displayList(String name, String phone, String email) {

        int[] imageIDs = {
                R.drawable.profile_icon, R.drawable.mobile_icon, R.drawable.email_icon,
                R.drawable.share_icon, R.drawable.start_icon, R.drawable.logout_icon
        };

        String[] itemNames = {
                "Name", "Mobile", "Email", "Refer a Friend", "Rate Builders Hub", "LOG OUT"
        };

        String[] data = {
                name, phone, email, " ", " ", " "
        };

        ListAdapter listAdapter = new ListAdapter(getActivity(), imageIDs, itemNames, data);
        listView.setAdapter(listAdapter);

        listView.setOnItemClickListener((parent, view, position, id) -> {
            int selectedItemPosition = position + 1;
            switch (selectedItemPosition) {
                case 1:
                    Toast.makeText(getActivity(), "Name", Toast.LENGTH_SHORT).show();
                    break;

                case 2:
                    Toast.makeText(getActivity(), "Phone", Toast.LENGTH_SHORT).show();
                    break;

                case 3:
                    Toast.makeText(getActivity(), "Email", Toast.LENGTH_SHORT).show();
                    break;

                case 4:
                    Intent myIntent = new Intent(Intent.ACTION_SEND);
                    myIntent.setType("text/plain");
                    String body = "Download Builder Hub now!/n {appLink}";
                    String sub = "Share with your friends";
                    myIntent.putExtra(Intent.EXTRA_SUBJECT, sub);
                    myIntent.putExtra(Intent.EXTRA_TEXT, body);
                    startActivity(Intent.createChooser(myIntent, "Share Using"));
                    break;

                case 5:
                    new AlertDialog.Builder(getActivity())
                            .setIcon(R.drawable.start_icon)
                            .setTitle("Rate this app")
                            .setMessage(R.string.rate_dialog_message)
                            .setPositiveButton("Rate It Now", (dialog, which) -> {

                            })
                            .setNeutralButton("Remind Me Later", (dialog, which) -> {

                            })
                            .setNegativeButton("No, Thanks", (dialog, which) -> {

                            })
                            .show();
                    break;

                case 6:

                    new AlertDialog.Builder(getActivity())
                            .setIcon(R.drawable.warning_icon)
                            .setTitle("LOGOUT")
                            .setMessage("Do you really want to log out?")
                            .setPositiveButton("Yes", (dialog, which) -> {
                                progressBar.setVisibility(View.VISIBLE);
                                mAuth.signOut();
                                progressBar.setVisibility(View.GONE);
                                Intent intent = new Intent(getActivity(), loginActivity.class);
                                startActivity(intent);
                            })
                            .setNegativeButton("No", null)
                            .show();
                    break;

            }

        });

    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (data != null) {
                imageURI = data.getData();
                Common.userProfileImage = imageURI;

                userImage.setImageURI(imageURI);
                uploadImageToFirebase(imageURI);
            }
        }

    }

    private void uploadImageToFirebase(Uri imageURI) {

        ProgressDialog dialog = new ProgressDialog(getActivity());
        dialog.setMessage("Uploading...");
        dialog.show();

        if (imageURI != null) {

            storageReference = storage.getReference().child("Users/" + FirebaseAuth.getInstance().getUid() + "/profile.jpg");


            storageReference.putFile(imageURI).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Log.d("imageUpload", "success");
                    Toast.makeText(getActivity(), "Profile Uploaded Successfully", Toast.LENGTH_SHORT).show();
                    loadProfileImageFromFirebase();
                    dialog.dismiss();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d("imageUpload", e.toString());
                    Toast.makeText(getActivity(), "error uploading photo", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }
            });

        }

    }

    private void loadProfileImageFromFirebase() {

        storageReference = storage.getReference().child("Users/" + FirebaseAuth.getInstance().getUid() + "/profile.jpg");

        ProgressDialog dialog = new ProgressDialog(getActivity());
        dialog.setTitle("Please Wait");
        dialog.setMessage("Loading...");
        dialog.show();

        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                try {
                    Glide
                            .with(requireActivity())
                            .load(uri)
                            .placeholder(R.drawable.user_profile_progress_bar)
                            .into(userImage);
                } catch (Exception e) {
                    Log.d("GlideError", "something went wrong!" + e.toString());
                }
            }

        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("storageReferenceError", "Failed to load image!");
            }
        });

        Handler handler = new Handler();
        handler.postDelayed(dialog::dismiss, 1300);


    }


}