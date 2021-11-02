package com.lettherebelight;

import android.app.Activity;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import androidx.core.content.ContextCompat;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class AddNewPart extends BottomSheetDialogFragment {

    public static final String TAG = "ActionBottomDialog";
    private EditText newPartText;
    private Button newPartSaveBtn;
    FirebaseFirestore firestore;
    StorageReference storageReference;
    DatabaseReference databaseReference;
    FirebaseAuth fAuth;
    FirebaseUser fUser;
    public static AddNewPart newInstance(){
        return new AddNewPart();
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NORMAL, R.style.DialogStyle);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.new_lightingpackage, container, false);
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
        newPartText = getView().findViewById(R.id.newPartText);
        newPartSaveBtn = getView().findViewById(R.id.btnNewPart );

        firestore = FirebaseFirestore.getInstance();
        fAuth = FirebaseAuth.getInstance();
        fUser = fAuth.getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users");
        storageReference = FirebaseStorage.getInstance().getReference().child("LightingPackages");
        boolean isUpdate = false;
        final Bundle bundle = getArguments();
        if(bundle != null){
            isUpdate = true;
            String part = bundle.getString("part");
            newPartText.setText(part);
            if(part.length()>0){
                newPartSaveBtn.setTextColor(ContextCompat.getColor(getContext(), R.color.black));
            }
        }
        newPartText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(toString().equals("")){

                }else{
                    newPartSaveBtn.setEnabled(true);
                    newPartSaveBtn.setTextColor(Color.BLACK);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        boolean finalIsUpdate = isUpdate;
        newPartSaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = newPartText.getText().toString();
                DocumentReference documentReference = firestore.collection("../Projects/breads_projects/unit_types/bread_unit_types/lighting_package/breads_lighting_packages").document(fUser.getUid());
                documentReference.set(text);
                dismiss();
            }
        });


    }
    @Override
    public void onDismiss(DialogInterface dialog){
        Activity activity = getActivity();
        if(activity instanceof DialogCloseListener){
           ( (DialogCloseListener)activity).handleDialogClose(dialog);
        }
    }
}
