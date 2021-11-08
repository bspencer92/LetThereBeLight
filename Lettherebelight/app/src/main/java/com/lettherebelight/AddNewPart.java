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

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.lettherebelight.Adapter.LightingPackageAdapter;
import com.lettherebelight.Model.LightingPackageModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AddNewPart extends BottomSheetDialogFragment {

    public static final String TAG = "ActionBottomDialog";
    private FirebaseFirestore firestore;
    private StorageReference storageReference;
    private DatabaseReference databaseReference;
    private FirebaseAuth fAuth;
    private FirebaseUser fUser;
    private ArrayList<LightingPackageModel> partList = new ArrayList<>();
    private EditText newPartText;
    private Button newPartSaveBtn;

    public static AddNewPart newInstance() {
        return new AddNewPart();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NORMAL, R.style.DialogStyle);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.new_lightingpackage, container, false);
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        newPartText = getView().findViewById(R.id.newPartText);
        newPartSaveBtn = getView().findViewById(R.id.btnNewPart);

        firestore = FirebaseFirestore.getInstance();
        fAuth = FirebaseAuth.getInstance();
        fUser = fAuth.getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users");
        storageReference = FirebaseStorage.getInstance().getReference().child("LightingPackages");
        boolean isUpdate = false;
        final Bundle bundle = getArguments();
        if (bundle != null) {
            isUpdate = true;
            String part = bundle.getString("part");
            newPartText.setText(part);
            assert part != null;
            if (part.length() > 0) {

                newPartSaveBtn.setTextColor(ContextCompat.getColor(getContext(), R.color.black));
            }
        }
        newPartText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (toString().equals("")) {
                    newPartSaveBtn.setEnabled(false);

                } else {
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
                LightingPackageAdapter adapter = new LightingPackageAdapter();
                LightingPackageModel part = new LightingPackageModel();
                String text = newPartText.getText().toString();
                part.setPartName(text);
                // adapter.getLightingPackageList().add(part);
                partList.add(part);
                DocumentReference documentReference = firestore.collection("users").document(fUser.getUid() + "/lighting_packages/" + fUser.getUid() + "'s parts");

                Map<String, Object> partMap = new HashMap<>();
                partMap.put("partMap", partList);
                documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                documentReference.update("partMap", FieldValue.arrayUnion(part));

                            } else {
                                documentReference.set(partMap);

                            }
                        }
                        dismiss();
                    }
                });


            }
        });


    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        Activity activity = getActivity();
        if (activity instanceof DialogCloseListener) {
            ((DialogCloseListener) activity).handleDialogClose(dialog);


        }
    }
}
