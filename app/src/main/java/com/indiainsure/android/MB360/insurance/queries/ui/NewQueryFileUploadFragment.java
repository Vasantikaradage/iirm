package com.indiainsure.android.MB360.insurance.queries.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.indiainsure.android.MB360.R;
import com.indiainsure.android.MB360.databinding.FragmentNewQueryBinding;
import com.indiainsure.android.MB360.insurance.queries.repository.QueryViewModel;
import com.indiainsure.android.MB360.insurance.repository.LoadSessionViewModel;
import com.indiainsure.android.MB360.utilities.AesNew;
import com.indiainsure.android.MB360.utilities.FileUtil;
import com.indiainsure.android.MB360.utilities.LogMyBenefits;
import com.indiainsure.android.MB360.utilities.LogTags;
import com.indiainsure.android.MB360.utilities.MediaTypes;

import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class NewQueryFileUploadFragment extends Fragment implements DeleteFileListerner {

    FragmentNewQueryBinding binding;
    View view;
    QueryViewModel queryViewModel;
    LoadSessionViewModel loadSessionViewModel;
    String category = "";
    String empSrNo = "";
    UplaodFileAttachmentAdapter uplaodFileAttachmentAdapter;


    private String[] filesExt = {"xlsx", "pdf", "png", "xls", "doc", "docx", "jpeg", "jpg"};
    ArrayList<String> pathArrayList = new ArrayList<>();
    ActivityResultLauncher<Intent> fileLauncherActivity;


    public NewQueryFileUploadFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        binding = FragmentNewQueryBinding.inflate(inflater, container, false);
        view = binding.getRoot();

        queryViewModel = new ViewModelProvider(requireActivity()).get(QueryViewModel.class);
        loadSessionViewModel = new ViewModelProvider(requireActivity()).get(LoadSessionViewModel.class);

        loadSessionViewModel.getLoadSessionData().observe(getViewLifecycleOwner(), response -> {
            empSrNo = response.getGroupPoliciesEmployees().get(0).getGroupGMCPolicyEmployeeData()
                    .get(0).getEmployeeSrNo();

        });
        //setUpCategorySpinner
        setUpSpinner();

        binding.categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                category = adapterView.getSelectedItem().toString();
                if (category.toLowerCase().equals("other")) {
                    binding.etCategoryLayout.setVisibility(View.VISIBLE);
                } else {
                    binding.etCategory.setText("");
                    binding.etCategoryLayout.setVisibility(View.GONE);
                }

                binding.errorCategoryLabel.setVisibility(View.GONE);
                binding.errorQueryLabel.setVisibility(View.GONE);
                binding.errorSelect.setVisibility(View.GONE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                adapterView.setSelection(0);
            }
        });

        binding.etMSGQry.setOnFocusChangeListener((view, b) -> {
            if (b) {
                binding.rvMSGQry.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.query_border_active));
            } else {
                binding.rvMSGQry.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.queryborder));
            }
        });

        binding.etMSGQry.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                binding.queryCounter.setText("Typed " + s.length() + " / 3000 characters");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        binding.btnSubmitQuery.setOnClickListener(v -> {
            binding.errorCategoryLabel.setVisibility(View.GONE);
            binding.errorQueryLabel.setVisibility(View.GONE);
            binding.errorSelect.setVisibility(View.GONE);

            if (category.toLowerCase().equals("other")) {
                if (binding.etCategory.getText().toString().trim().isEmpty()) {
                    binding.errorCategoryLabel.setVisibility(View.VISIBLE);
                    binding.errorQueryLabel.setVisibility(View.GONE);
                    binding.errorSelect.setVisibility(View.GONE);
                } else if (binding.etMSGQry.getText().toString().trim().isEmpty()) {
                    binding.errorCategoryLabel.setVisibility(View.GONE);
                    binding.errorQueryLabel.setVisibility(View.VISIBLE);
                    binding.errorSelect.setVisibility(View.GONE);
                } else {
                    try {
                        submitNewQuery();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } else if (category.toLowerCase().equals("select")) {
                binding.errorCategoryLabel.setVisibility(View.GONE);
                binding.errorQueryLabel.setVisibility(View.GONE);
                binding.errorSelect.setVisibility(View.VISIBLE);
            } else if (binding.etMSGQry.getText().toString().trim().isEmpty()) {
                binding.errorCategoryLabel.setVisibility(View.GONE);
                binding.errorQueryLabel.setVisibility(View.VISIBLE);
                binding.errorSelect.setVisibility(View.GONE);
            } else {
                try {
                    submitNewQuery();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });


        binding.uploadFile.setOnClickListener(view -> {
            if (pathArrayList.size() < 5) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("*/*");
                intent.addCategory(Intent.CATEGORY_OPENABLE);

                try {
                    OpenFileLauncher();
                } catch (android.content.ActivityNotFoundException ex) {
                    // Potentially direct the user to the Market with a Dialog
                    Toast.makeText(requireContext(), "Please install a File Manager.",
                            Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(requireContext(), "Cannot upload more than 5 files.", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    public void OpenFileLauncher() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        Intent.createChooser(intent, "Select a File to Upload");
        fileLauncherActivity.launch(intent);
    }

    private void submitNewQuery() throws Exception {
        binding.errorCategoryLabel.setVisibility(View.GONE);
        binding.errorQueryLabel.setVisibility(View.GONE);
        binding.errorSelect.setVisibility(View.GONE);


        //json object for replying a query
        //here isReply is true because we are letting user to reply the query
        JSONObject queryRequestJson = new JSONObject();
        queryRequestJson.put("empSrNo", AesNew.encrypt(empSrNo, getString(R.string.pass_phrase)));
        queryRequestJson.put("query", binding.etMSGQry.getText().toString().trim());

        //multipart form data
        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM);

        if (pathArrayList.isEmpty()) {
            builder.addFormDataPart("QueryData", queryRequestJson.toString());
        } else {
            for (int k = 0; k < pathArrayList.size(); k++) {

                File file = new File(pathArrayList.get(k));

                final MediaType MEDIA_TYPE = MediaTypes.fromFile(file);

                if (file.exists())
                    builder.addFormDataPart(file.getName(), FileUtil.getFileName(requireContext(), Uri.fromFile(file)),
                            RequestBody.create(MEDIA_TYPE, file));

                builder.addFormDataPart("QueryData", queryRequestJson.toString());

            }
        }


        RequestBody requestBody = builder.build();
        LogMyBenefits.d(LogTags.QUERY_ACTIVITY, "replyQuery: " + requestBody.toString());
        LogMyBenefits.d(LogTags.QUERY_ACTIVITY, "replyQuery: " + queryRequestJson.toString());
        queryViewModel.addQuery(requestBody).observe(getViewLifecycleOwner(), reply -> {
            AlertDialog.Builder alert = new AlertDialog.Builder(requireActivity());
            if (reply != null) {
                if (reply.getStatus()) {
                    binding.etMSGQry.setText(null);
                    pathArrayList.clear();

                    String message = "Query submitted successfully. Your Ticket No is:  " + reply.getMessage() + ". Please use this ticket number for all future correspondence for this query";
                    alert.setMessage(message);
                    alert.setCancelable(false);
                    alert.setPositiveButton("Okay", (dialog, which) -> {
                        try {
                            dialog.dismiss();
                            requireActivity().onBackPressed();
                        } catch (Exception e) {
                            dialog.dismiss();
                            e.printStackTrace();
                        }
                    });
                    AlertDialog alertDialog = alert.create();
                    if (!alertDialog.isShowing()) {
                        alertDialog.show();
                    }

                }
            } else {
                //something went wrong
                //already showed toast in repo class
            }
        });
    }

    private void setUpSpinner() {
        //adding the category
        List<String> categoryList = new ArrayList<>();
        categoryList.add("Select");
        categoryList.add("E-Cards");
        categoryList.add("Dependant Addiction/Correction/Deletion");
        categoryList.add("Policy features and Coverages Details");
        categoryList.add("Enrollment Process");
        categoryList.add("How to make a cashless claim");
        categoryList.add("How to make a reimbursement claim");
        categoryList.add("Claim Intimation");
        categoryList.add("Hospital related");
        categoryList.add("Contact Details");
        categoryList.add("Claim Tracking");
        categoryList.add("Other");

        NewQueryCategorySpinnerAdapter adapter = new NewQueryCategorySpinnerAdapter(requireContext(), categoryList);
        binding.categorySpinner.setAdapter(adapter);

    }

    public void UpdateList() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, true);
        binding.fileRecyclerview.setLayoutManager(layoutManager);
        uplaodFileAttachmentAdapter = new UplaodFileAttachmentAdapter(requireActivity(), pathArrayList, this);
        binding.fileRecyclerview.setAdapter(uplaodFileAttachmentAdapter);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        fileLauncherActivity = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        // There are no request codes
                        Intent data = result.getData();
                        try {
                            File file2 = FileUtil.from(context, data.getData());
                            // File file2 = new File(getPath(data.getData()));

                            String filePath = file2.getPath();
                            boolean FileExists = false;

                            String[] filenameArray = filePath.split("\\.");
                            String extension = filenameArray[filenameArray.length - 1];
                            if (Arrays.asList(filesExt).contains(extension.replace(".", "").toLowerCase())) {
                                for (int fileCheck = 0; fileCheck < pathArrayList.size(); fileCheck++) {
                                    FileExists = pathArrayList.get(fileCheck).equals(filePath);
                                }

                                if (FileExists)
                                    Toast.makeText(context, "File Already Attached", Toast.LENGTH_SHORT).show();
                                else {

                                    pathArrayList.add(filePath);
                                    UpdateList();

                                }
                            } else
                                Toast.makeText(context,
                                        "Entered file format should be one of the following : xls, xlsx, doc, docx, png, jpeg, jpg, pdf", Toast.LENGTH_SHORT).show();

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //new changes
        int index = NewQueryFileUploadFragmentArgs.fromBundle(getArguments()).getSpinnerItemIndex();
        binding.categorySpinner.setSelection(index);
    }

    @Override
    public void deleteFile(int position) {
        pathArrayList.remove(position);
        uplaodFileAttachmentAdapter.notifyDataSetChanged();

    }
}
