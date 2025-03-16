package com.example.studentscoremanagement.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.studentscoremanagement.CustomDialog;
import com.example.studentscoremanagement.DBHelper;
import com.example.studentscoremanagement.EventListener;
import com.example.studentscoremanagement.LoginActivity;
import com.example.studentscoremanagement.Model.TaiKhoan;
import com.example.studentscoremanagement.R;
import com.example.studentscoremanagement.UserInforActivity;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UserFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UserFragment extends Fragment implements EventListener {

    TaiKhoan user;
    Button btnUpdateInfor;
    LinearLayout llLogout;
    ImageView imUserAvatar;
    TextView txtAccount;
    EditText txtPassword, txtPhoneNumber;

    private static final int PICK_IMAGE = 100;
    DBHelper database;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_USERNAME = "userName";

    // TODO: Rename and change types of parameters
    private String userName;

    public UserFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment UserFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static UserFragment newInstance(String param1) {
        UserFragment fragment = new UserFragment();
        Bundle args = new Bundle();
        args.putString(ARG_USERNAME,param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            userName = getArguments().getString(ARG_USERNAME);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_user, container, false);
        database = new DBHelper(getContext());

        setControl(view);
        setEvent();
        setDataByBundle();
        return view;
    }

    private void setDataByBundle() {
        user = new TaiKhoan(userName);
        user.updateDataFromDataBase(database);

        txtAccount.setText("", TextView.BufferType.EDITABLE);
        txtPassword.setText("", TextView.BufferType.EDITABLE);
        txtPhoneNumber.setText("", TextView.BufferType.EDITABLE);

        txtAccount.setText(user.getTenTaiKhoan(), TextView.BufferType.EDITABLE);
        txtPassword.setText(user.getMatKhau(), TextView.BufferType.EDITABLE);
        txtPhoneNumber.setText(user.getSdt(), TextView.BufferType.EDITABLE);

        byte[] avatar = user.getAnh();
        if (avatar == null)
        {
            //set default avatar
        }
        else
        {
            Bitmap bitmap = BitmapFactory.decodeByteArray(avatar, 0, avatar.length);
            imUserAvatar.setImageBitmap(bitmap);
        }
    }

    private void setEvent() {
        //Log.d("print",userName);
        imUserAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery();
            }
        });

        llLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), LoginActivity.class);
                getActivity().finish();
                startActivity(intent);
            }
        });

        btnUpdateInfor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {


                    EventListener eventListener=new EventListener() {
                        @Override
                        public void doSomething() {
                            user.setMatKhau(txtPassword.getText().toString());
                            user.setSdt(txtPhoneNumber.getText().toString());

                            //save data
                            user.saveToDatabase(database);

                            //update successfully
                            Toast.makeText(getContext(), "Cập nhập thành công", Toast.LENGTH_SHORT).show();
                        }
                    };

                    //Do Trong fragment ko dùng getContent để thay cho tham số thứ 2 đc,
                    // vì getContext() thực chất là return về MainActivity ko có implement EventListener đc;
                    CustomDialog cdd=new CustomDialog(getContext(), eventListener);
                    cdd.show();
                }catch (Exception e){
                    Log.d("print",e.getMessage());
                }

            }
        });
    }

    private void openGallery() {
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery, PICK_IMAGE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == getActivity().RESULT_OK && requestCode == PICK_IMAGE) {
            Uri selectedImage = data.getData();
            Bitmap bm = null;
            try {
                bm = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), selectedImage);
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (bm == null)
            {
                Toast.makeText(getContext(), "image is null", Toast.LENGTH_SHORT).show();
                return;
            }

            //convert image to byte array
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            bm.compress(Bitmap.CompressFormat.PNG, 100, out);
            byte[] buffer = out.toByteArray();

            //set image to user
            user.setAnh(buffer);

            //display the image into avatar
            Bitmap bitmap = BitmapFactory.decodeByteArray(user.getAnh(), 0, user.getAnh().length);
            imUserAvatar.setImageBitmap(bitmap);
        }
    }

    private void setControl(View view) {
        btnUpdateInfor = view.findViewById(R.id.btnUdateInfor);
        llLogout = view.findViewById(R.id.llLogout);
        imUserAvatar = view.findViewById(R.id.imUserAvatarInUserInfor);
        txtAccount = view.findViewById(R.id.txtAccount);
        txtPassword = view.findViewById(R.id.txtPassword);
        txtPhoneNumber = view.findViewById(R.id.txtPhoneNumber);
    }

    @Override
    public void doSomething() {
        user.setMatKhau(txtPassword.getText().toString());
        user.setSdt(txtPhoneNumber.getText().toString());

        //save data
        user.saveToDatabase(database);

        //update successfully
        Toast.makeText(getContext(), "Cập nhập thành công", Toast.LENGTH_SHORT).show();
    }
}