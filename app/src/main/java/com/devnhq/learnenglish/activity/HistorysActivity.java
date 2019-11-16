package com.devnhq.learnenglish.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.devnhq.learnenglish.BuildConfig;
import com.devnhq.learnenglish.R;
import com.devnhq.learnenglish.model.History;
import com.devnhq.learnenglish.model.ResultTest;
import com.devnhq.learnenglish.model.User;
import com.devnhq.learnenglish.network.ConnectivityHelper;
import com.devnhq.learnenglish.viewholder.HistoryHolder;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.shreyaspatil.firebase.recyclerpagination.DatabasePagingOptions;
import com.shreyaspatil.firebase.recyclerpagination.FirebaseRecyclerPagingAdapter;
import com.shreyaspatil.firebase.recyclerpagination.LoadingState;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class HistorysActivity extends AppCompatActivity {
    private static final int CAMERA_REQUEST_CODE = 0;
    private static final int GALLERY_REQUEST_CODE = 1;
    FirebaseAuth mAuth;
    DatabaseReference reference;
    FirebaseUser fuser;
    StorageReference storageReference;
    DatabaseReference mDatabase;
    DatabaseReference mdelete;
    FirebaseRecyclerPagingAdapter<ResultTest, HistoryHolder> mAdapter;
    String totalcount, rateapp, point, totalscore, name, cameraFilePath, nameEdit, url, pass, email;
    private StorageReference mountairef;
    private CircleImageView img_user, img_edit_user;
    private StorageTask uploadTask;
    private List<User> listUsers;
    private List<History> histories;
    private TextView tvName, tvMail;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private LinearLayout background;
    private EditText editName, edtPass, edtnewPass, edtconfirmPass;
    private BottomSheetDialog dialog;
    private Button btnDone, btnCancel;
    private Boolean check = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historys);
        final ProgressDialog pd = new ProgressDialog(HistorysActivity.this);
        pd.setMessage("Loading...");
        pd.getWindow();
        pd.show();
        pd.setCancelable(true);
//        background = findViewById(R.id.background);
        if (!ConnectivityHelper.isConnectedToNetwork(HistorysActivity.this)) {
//            background.setVisibility(View.GONE);
            refresh();
            Toast.makeText(HistorysActivity.this, "no internet access", Toast.LENGTH_SHORT).show();
        } else {
//            background.setVisibility(View.VISIBLE);
            img_user = findViewById(R.id.profile_image);
            tvName = findViewById(R.id.tvName);
            tvMail = findViewById(R.id.tvEmail);
            listUsers = new ArrayList<>();
            histories = new ArrayList<>();
            mAuth = FirebaseAuth.getInstance();
            recyclerView = findViewById(R.id.rcv_history);
            StaggeredGridLayoutManager mManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
            recyclerView.setLayoutManager(mManager);
            recyclerView.setHasFixedSize(true);
            mSwipeRefreshLayout = findViewById(R.id.srlLayout);
            fuser = FirebaseAuth.getInstance().getCurrentUser();
            mDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(fuser.getUid()).child("history");

            storageReference = FirebaseStorage.getInstance().getReference("uploads");
            reference = FirebaseDatabase.getInstance().getReference("Users").child(fuser.getUid());
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    User user = dataSnapshot
                            .getValue(User.class);
                    listUsers.add(user);
                    name = listUsers.get(0).getName();
                    url = listUsers.get(0).getUrl();
                    email = listUsers.get(0).getEmail();
                    nameEdit = listUsers.get(0).getName();
                    totalcount = listUsers.get(0).totalcount;
                    totalscore = listUsers.get(0).totalscore;
                    rateapp = listUsers.get(0).rateapp;
                    pass = listUsers.get(0).password;
                    tvName.setText(name);
                    tvMail.setText(email);
                    if (url.equals("default")) {
                        img_user.setImageResource(R.drawable.loaduser);
                    } else {
                        Picasso.get().load(url).placeholder(R.drawable.opasity).error(R.drawable.opasity).into(img_user);
                    }
                    pd.dismiss();
                    if (!totalcount.equals("0")) {
                        PagedList.Config config = new PagedList.Config.Builder()
                                .setEnablePlaceholders(false)
                                .setInitialLoadSizeHint(3)
                                .setPrefetchDistance(3)
                                .setPageSize(2)
                                .build();
                        DatabasePagingOptions<ResultTest> options = new DatabasePagingOptions.Builder<ResultTest>()
                                .setLifecycleOwner(HistorysActivity.this)
                                .setQuery(mDatabase, config, ResultTest.class)
                                .build();
                        //
                        mAdapter = new FirebaseRecyclerPagingAdapter<ResultTest, HistoryHolder>(options) {
                            @NonNull
                            @Override
                            public HistoryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                                return new HistoryHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_history, parent, false));
                            }

                            @Override
                            protected void onBindViewHolder(@NonNull final HistoryHolder holder,
                                                            final int position,
                                                            @NonNull final ResultTest model) {
                                holder.setItem(model);
                                holder.itemView.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        AlertDialog.Builder builder = new AlertDialog.Builder(HistorysActivity.this);
                                        builder.setTitle("Delete this item?");
                                        builder.setMessage("confirm delete! ");
                                        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                final ProgressDialog pd = new ProgressDialog(HistorysActivity.this);
                                                pd.setMessage("Waiting...");
                                                pd.getWindow();
                                                pd.show();
                                                pd.setCancelable(true);
                                                FirebaseDatabase.getInstance().getReference().child("Users").child(fuser.getUid()).child("history").child(model.id).addValueEventListener(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                        History history = dataSnapshot.getValue(History.class);
                                                        histories.add(history);
                                                        point = histories.get(0).getPoint();
                                                        HashMap<String, Object> map = new HashMap<>();
                                                        String n = "1";
                                                        map.put("totalcount", (Integer.parseInt(totalcount) - Integer.parseInt(n)) + "");
                                                        map.put("totalscore", (Double.parseDouble(totalscore) - Double.parseDouble(point)) + "");
                                                        FirebaseDatabase.getInstance().getReference("Users").child(fuser.getUid()).updateChildren(map, new DatabaseReference.CompletionListener() {
                                                            @Override
                                                            public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                                                                if (databaseError == null) {
                                                                    FirebaseDatabase.getInstance().getReference().child("Users").child(fuser.getUid()).child("history").child(model.id).removeValue();
                                                                    Intent intent = new Intent(HistorysActivity.this, HistorysActivity.class);
                                                                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                                                    startActivity(intent);
                                                                    finish();
                                                                    pd.dismiss();
                                                                    Toast.makeText(HistorysActivity.this, "Delete item complete", Toast.LENGTH_SHORT).show();
                                                                } else {
                                                                    pd.dismiss();
                                                                }
                                                            }
                                                        });

                                                    }

                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                                    }
                                                });


                                            }
                                        });
                                        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                            }
                                        });
                                        builder.show();
                                    }
                                });
                            }

                            @SuppressLint("ResourceAsColor")
                            @Override
                            protected void onLoadingStateChanged(@NonNull LoadingState state) {
                                switch (state) {
                                    case LOADING_INITIAL:
                                    case LOADING_MORE:
                                        mSwipeRefreshLayout.setColorSchemeColors(R.color.colorPrimary);
                                        mSwipeRefreshLayout.setNestedScrollingEnabled(true);
                                        mSwipeRefreshLayout.setRefreshing(true);
                                        break;

                                    case LOADED:
                                        mSwipeRefreshLayout.setRefreshing(false);
                                        mSwipeRefreshLayout.setNestedScrollingEnabled(false);
                                        mSwipeRefreshLayout.setColorSchemeColors(R.color.colorPrimary);
                                        break;

                                    case FINISHED:
                                        mSwipeRefreshLayout.setRefreshing(false);
                                        break;

                                    case ERROR:
                                        retry();
                                        break;
                                }
                            }

                            @Override
                            protected void onError(@NonNull DatabaseError databaseError) {
                                super.onError(databaseError);
                                mSwipeRefreshLayout.setRefreshing(false);
                                databaseError.toException().printStackTrace();
                            }
                        };
                        recyclerView.setAdapter(mAdapter);
                        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                            @Override
                            public void onRefresh() {
                                mAdapter.refresh();
                            }
                        });

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }

    public void EditName(View view) {
        String[] a = {"Cập nhật thông tin cá nhân", "Thay đổi mật khẩu"};
        AlertDialog.Builder builder = new AlertDialog.Builder(HistorysActivity.this);
        builder.setCancelable(true);
        builder.setTitle("Select")
                .setItems(a, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0) {
                            editProfile();
                        }
                        if (which == 1) {
                            editPass();
                        }
                    }
                });
        builder.create().show();
    }

    private void Load() {
        if (editName.getText().toString().trim().equals("")) {
            editName.setError(getText(R.string.emty));
        }
        if (editName.getText().toString().trim().length() > 20 || editName.getText().toString().trim().length() < 3) {
            editName.setError(getText(R.string.lenght));
        } else {
            final ProgressDialog pd = new ProgressDialog(HistorysActivity.this);
            pd.setMessage("Uploading");
            pd.show();
            pd.setCancelable(false);
            Calendar calendar = Calendar.getInstance();
            mountairef = storageReference.child("imge" + calendar.getTimeInMillis() + ".jpg");
            img_edit_user.setDrawingCacheEnabled(true);
            img_edit_user.buildDrawingCache();
            Bitmap bitmap = img_edit_user.getDrawingCache();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] data = baos.toByteArray();
            uploadTask = mountairef.putBytes(data);
            if (rateapp.equals("yes")) {
                uploadTask.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(HistorysActivity.this, "Error", Toast.LENGTH_SHORT).show();
                        pd.dismiss();
                    }
                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Task<Uri> result = taskSnapshot.getMetadata().getReference().getDownloadUrl();
                        result.addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                String url = uri.toString();
                                String n = editName.getText().toString().trim().replaceAll("\\n", "");
                                final HashMap<String, Object> map = new HashMap<>();
                                map.put("name", n);
                                map.put("url", url);
                                FirebaseDatabase.getInstance().getReference("Users").child(fuser.getUid()).updateChildren(map, new DatabaseReference.CompletionListener() {
                                    @Override
                                    public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                                        if (databaseError == null) {
                                            FirebaseDatabase.getInstance().getReference("Rate").child(fuser.getUid()).updateChildren(map, new DatabaseReference.CompletionListener() {
                                                @Override
                                                public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                                                    if (databaseError == null) {
                                                        Toast.makeText(HistorysActivity.this, "Thành công", Toast.LENGTH_SHORT).show();
                                                        pd.dismiss();
                                                        Intent refresh = new Intent(HistorysActivity.this, HistorysActivity.class);
                                                        startActivity(refresh);
                                                        finish();
                                                    } else {
                                                        pd.dismiss();
                                                        Toast.makeText(HistorysActivity.this, "Error", Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });
                                        } else {
                                            pd.dismiss();
                                            Toast.makeText(HistorysActivity.this, "Error", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            }
                        });
                    }
                });
            } else {
                uploadTask.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(HistorysActivity.this, "Error", Toast.LENGTH_SHORT).show();
                        pd.dismiss();
                    }
                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Task<Uri> result = taskSnapshot.getMetadata().getReference().getDownloadUrl();
                        result.addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                String url = uri.toString();
                                String n = editName.getText().toString().trim();
                                HashMap<String, Object> map = new HashMap<>();
                                map.put("name", n);
                                map.put("url", url);
                                FirebaseDatabase.getInstance().getReference("Users").child(fuser.getUid()).updateChildren(map, new DatabaseReference.CompletionListener() {
                                    @Override
                                    public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                                        if (databaseError == null) {
                                            Toast.makeText(HistorysActivity.this, "Thanh cong!", Toast.LENGTH_SHORT).show();
                                            pd.dismiss();
                                            Intent refresh = new Intent(HistorysActivity.this, HistorysActivity.class);
                                            startActivity(refresh);
                                            finish();
                                        } else {
                                            pd.dismiss();
                                            Toast.makeText(HistorysActivity.this, "Error", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            }
                        });
                    }
                });
            }
        }
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DCIM), "Camera");
        File image = File.createTempFile(
                imageFileName,
                ".jpg",
                storageDir
        );
        cameraFilePath = "file://" + image.getAbsolutePath();
        return image;
    }

    private void captureFromCamera() {
        try {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID + ".provider", createImageFile()));
            startActivityForResult(intent, CAMERA_REQUEST_CODE);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK)
            switch (requestCode) {
                case GALLERY_REQUEST_CODE:
                    Uri selectedImage = data.getData();
                    img_edit_user.setImageURI(selectedImage);
                    break;
                case CAMERA_REQUEST_CODE:
                    img_edit_user.setImageURI(Uri.parse(cameraFilePath));
                    break;
            }
    }

    public void editPass() {
        View dialogView = getLayoutInflater().inflate(R.layout.bottom_sheet_editpass, null);
        dialog = new BottomSheetDialog(this);
        btnCancel = dialogView.findViewById(R.id.cancel);
        btnDone = dialogView.findViewById(R.id.done);
        edtPass = dialogView.findViewById(R.id.edt_pass);
        edtnewPass = dialogView.findViewById(R.id.edt_newpass);
        edtconfirmPass = dialogView.findViewById(R.id.edt_confirm_newpass);
        dialog.setContentView(dialogView);
        dialog.show();

        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!edtPass.getText().toString().trim().equals(pass)) {
                    edtPass.setError(getText(R.string.pass));
                } else if (edtPass.getText().toString().trim().equals("") || edtnewPass.getText().toString().trim().equals("")) {
                    edtnewPass.setError(getText(R.string.emty));
                    edtnewPass.setError(getText(R.string.emty));
                } else if (!edtnewPass.getText().toString().trim().equals(edtconfirmPass.getText().toString().trim())) {
                    edtnewPass.setError(getText(R.string.newpass));
                } else if (edtnewPass.length() < 6 || edtnewPass.length() > 15) {
                    edtnewPass.setError(getText(R.string.lenght));
                } else {
                    final ProgressDialog pd = new ProgressDialog(HistorysActivity.this);
                    pd.setMessage("Change...");
                    pd.show();
                    pd.setCancelable(false);
                    String newpassword = edtnewPass.getText().toString().trim();
                    final HashMap<String, Object> map = new HashMap<>();
                    map.put("password", newpassword);
                    FirebaseDatabase.getInstance().getReference("Users").child(fuser.getUid()).updateChildren(map, new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                            if (databaseError == null) {
                                pd.dismiss();
                                dialog.dismiss();
                                startActivity(new Intent(HistorysActivity.this, HistorysActivity.class));
                                finish();
                                Toast.makeText(HistorysActivity.this, "changed succesfully", Toast.LENGTH_SHORT).show();
                            } else {
                                pd.dismiss();
                                dialog.dismiss();
                                Toast.makeText(HistorysActivity.this, "Error", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edtPass.getText().toString().trim().equals("") || edtnewPass.getText().toString().trim().equals("") || edtconfirmPass.getText().toString().trim().equals("")) {
                    dialog.dismiss();
                } else {
                    backEdit();
                }
            }
        });


    }

    public void editProfile() {
        View dialogView = getLayoutInflater().inflate(R.layout.bottom_sheet_editfprofile, null);
        dialog = new BottomSheetDialog(this);
        btnCancel = dialogView.findViewById(R.id.cancel);
        btnDone = dialogView.findViewById(R.id.done);
        editName = dialogView.findViewById(R.id.edt_edit_name);
        img_edit_user = dialogView.findViewById(R.id.edit_img_user);
        editName.setText(nameEdit);
        if (url.equals("default")) {
            img_user.setImageResource(R.drawable.loaduser);
        } else {
            Picasso.get().load(url).placeholder(R.drawable.loaduser).error(R.drawable.loaduser).into(img_edit_user);
        }
        dialog.setContentView(dialogView);
        dialog.show();
        img_edit_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] a = {"Choose from the device's library", "Take photos from camara"};
                AlertDialog.Builder builder = new AlertDialog.Builder(HistorysActivity.this);
                builder.setTitle("Select")
                        .setItems(a, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                if (which == 0) {
                                    Intent intent = new Intent(Intent.ACTION_PICK);
                                    intent.setType("image/*");
                                    String[] mimeTypes = {"image/jpeg", "image/png"};
                                    intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
                                    startActivityForResult(intent, GALLERY_REQUEST_CODE);
                                    check = true;
                                }
                                if (which == 1) {
                                    captureFromCamera();
                                }
                            }
                        });
                builder.create().show();
            }
        });
        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Load();
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!editName.getText().toString().trim().replaceAll("\\n", "").equals("") || editName.getText().toString().trim().replaceAll("\\n", "").equals(nameEdit) || check == true) {
                    backEdit();
                } else {
                    dialog.dismiss();
                }
            }
        });


    }

    public void backEdit() {
        AlertDialog.Builder builder = new AlertDialog.Builder(HistorysActivity.this);
        builder.setTitle("Thay đổi chưa được lưu");
        builder.setMessage("Thoát");
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        builder.show();
    }

    public void Back(View view) {
        onBackPressed();
        finish();
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(HistorysActivity.this, MainActivity.class));
        finish();
    }

    public void refresh() {
        Intent refresh = new Intent(HistorysActivity.this, HistorysActivity.class);
        startActivity(refresh);
        finish();
    }

}
