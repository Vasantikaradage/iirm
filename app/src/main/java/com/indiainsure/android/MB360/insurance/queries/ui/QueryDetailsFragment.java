package com.indiainsure.android.MB360.insurance.queries.ui;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputFilter;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.indiainsure.android.MB360.R;
import com.indiainsure.android.MB360.databinding.FragmentQueryDetailsBinding;
import com.indiainsure.android.MB360.insurance.queries.repository.QueryViewModel;
import com.indiainsure.android.MB360.insurance.queries.responseclass.AllQuery;
import com.indiainsure.android.MB360.insurance.queries.responseclass.Attachment;
import com.indiainsure.android.MB360.utilities.FileUtil;
import com.indiainsure.android.MB360.utilities.LogTags;
import com.indiainsure.android.MB360.utilities.MediaTypes;
import com.indiainsure.android.MB360.utilities.UtilMethods;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.Arrays;
import java.util.Objects;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class QueryDetailsFragment extends Fragment implements FileDownloadHelper {

    AllQuery queryInfo;
    FragmentQueryDetailsBinding binding;
    View view;
    QueryDetailAdapter adapter;
    private String[] filesExt = {"xlsx", "pdf", "png", "xls", "doc", "docx", "jpeg", "jpg"};
    public String[] PathHolder = new String[5];
    //max files
    private int i = 0;

    Attachment attachment;
    int position;
    int queryPosition;

    MenuProvider menuProvider;

    private LinearLayout[] fileview;
    private AppCompatTextView[] dots;
    private AppCompatImageView[] fileType;
    private AppCompatButton[] delete;


    ActivityResultLauncher<Intent> fileLauncherActivity;


    QueryViewModel queryViewModel;

    // Declare a permission request launcher using the new API
    private ActivityResultLauncher<String[]> requestPermissionLauncher;

    QueryPermissionHelper permissionHelper = new QueryPermissionHelper() {
        @Override
        public void granted() {
            try {

                String[] file_name = attachment.getFileName().split("\\\\");

                Intent pdfIntent = FileAttachmentDownloader.downloadFile(attachment.getFileName(),
                        new File(requireContext().getFilesDir(), file_name[file_name.length - 1]),
                        requireActivity(), requireContext());
                if (pdfIntent != null) {
                    requireContext().startActivity(pdfIntent);
                } else {
                    //null
                }
            } catch (ActivityNotFoundException e) {
                e.printStackTrace();
                Toast.makeText(requireContext(), "No application found to view this file!", Toast.LENGTH_SHORT).show();
            }
            onFinishDownload(position, queryPosition);
        }
    };

    public QueryDetailsFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }


        fileLauncherActivity = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            queryViewModel.setLoadingFromFilePicker();
            Log.d("FILE-PICKER", "filepicker: started");
            if (result.getResultCode() == Activity.RESULT_OK) {
                // There are no request codes
                Log.d("FILE-PICKER", "filepicker: result ok");
                Intent data = result.getData();
                Log.d("FILE-PICKER", "filepicker: " + result.getData().getType());
                try {
                    File file2 = FileUtil.from(requireContext(), data.getData());
                    // File file2 = new File(getPath(data.getData()));

                    String filePath = file2.getPath();
                    boolean FileExists = false;

                    String[] filenameArray = filePath.split("\\.");
                    String extension = filenameArray[filenameArray.length - 1];
                    if (Arrays.asList(filesExt).contains(extension.replace(".", "").toLowerCase())) {
                        for (int fileCheck = 0; fileCheck < i; fileCheck++) {
                            FileExists = PathHolder[fileCheck].equals(filePath);
                            if (FileExists) {
                                break;
                            }
                        }

                        if (FileExists)
                            Toast.makeText(requireContext(), "File Already Attached", Toast.LENGTH_SHORT).show();
                        else {

                            PathHolder[i] = filePath;

                            UpdateList();

                        }
                    } else
                        Toast.makeText(requireContext(), "Entered file format should be one of the following : xls, xlsx, doc, docx, png, jpeg, jpg, pdf", Toast.LENGTH_SHORT).show();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentQueryDetailsBinding.inflate(inflater, container, false);
        view = binding.getRoot();

        setHasOptionsMenu(true);

        queryViewModel = new ViewModelProvider(requireActivity()).get(QueryViewModel.class);

        queryViewModel.getLoadingDetails().observe(getViewLifecycleOwner(), loading -> {
            if (loading) {
                binding.loadingLayout.setVisibility(View.VISIBLE);
            } else {
                binding.loadingLayout.setVisibility(View.GONE);

            }
        });

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        queryInfo = QueryDetailsFragmentArgs.fromBundle(getArguments()).getGetQueryInfo();
        binding.ticketNoTxt.setText(String.format("Ticket #:%s", queryInfo.getTicketNumber()));

        Log.d("QUERYDETAILS", queryInfo.toString());

        menuProvider = new MenuProvider() {
            @Override
            public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
                inflater.inflate(R.menu.query_menu, menu);
            }

            @Override
            public boolean onMenuItemSelected(@NonNull MenuItem item) {

                int id = item.getItemId();

                if (id == R.id.mark_solve) {
                    final Dialog dialog = new Dialog(requireContext());
                    dialog.setContentView(R.layout.smslayout);
                    Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.nhborder));

                    AppCompatTextView lblSMS = dialog.findViewById(R.id.lblSMS);
                    final AppCompatEditText smsContact = dialog.findViewById(R.id.smsContact);
                    AppCompatButton btnSubmit = dialog.findViewById(R.id.btnSubmit);
                    AppCompatButton btnCancel = dialog.findViewById(R.id.btnCancel);
                    LinearLayout llTitle = dialog.findViewById(R.id.llTitle);
                    AppCompatTextView lblSMSHeader = dialog.findViewById(R.id.lblSMSHeader);
                    llTitle.setVisibility(View.VISIBLE);
                    lblSMS.setText(R.string.app_name);
                    lblSMSHeader.setText("Do you want to mark this query as solved ?");
                    btnSubmit.setText("Yes");
                    btnCancel.setText("No");
                    smsContact.setVisibility(View.GONE);
                    btnSubmit.setOnClickListener(v1 -> {
                        queryViewModel.marksolve(queryInfo.getEqCustQrySrNo(), queryInfo.getEmployeeSrNo()).observe(getViewLifecycleOwner(), markedResponse -> {
                            if (markedResponse != null) {
                                if (markedResponse.getStatus()) {
                                    queryViewModel.getQueries(queryInfo.getEmployeeSrNo());
                                    binding.topView.setVisibility(View.VISIBLE);
                                    binding.chatViewId.setVisibility(View.GONE);
                                    Toast.makeText(requireContext(), "Marked as resolved!", Toast.LENGTH_SHORT).show();
                                    requireActivity().removeMenuProvider(menuProvider);
                                    dialog.dismiss();
                                }
                            } else {
                                Toast.makeText(requireContext(), "Unable to reach server, please try again later!", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            }
                        });

                    });
                    btnCancel.setOnClickListener(v -> dialog.dismiss());
                    dialog.show();

                    return true;
                }
                return false;
            }
        };

        if (queryInfo.getEqCustQrySolved().equals("1")) {
            binding.topView.setVisibility(View.VISIBLE);
            binding.chatViewId.setVisibility(View.GONE);
            requireActivity().removeMenuProvider(menuProvider);
        } else if (queryInfo.getEqCustQryEnded().equalsIgnoreCase("1")) {
            binding.topView.setVisibility(View.VISIBLE);
            binding.topView.setText("Your query has ended.");
            binding.chatViewId.setVisibility(View.GONE);
            requireActivity().removeMenuProvider(menuProvider);
        } else {
            binding.topView.setVisibility(View.GONE);
            binding.chatViewId.setVisibility(View.VISIBLE);
            requireActivity().addMenuProvider(menuProvider, getViewLifecycleOwner());
        }

        queryViewModel.getQueryDetails(queryInfo.getEqCustQrySrNo()).observe(getViewLifecycleOwner(), queryDetails -> {
            if (queryDetails != null) {
                if (queryDetails.getListOfQueries() != null) {
                    adapter = new QueryDetailAdapter(queryDetails.getListOfQueries(), requireContext(), requireActivity(), this);
                    binding.queryDetailCycle.setAdapter(adapter);

                }
            }
        });
        binding.attachFile.setOnClickListener(v -> {
            if (i < 5) {
                try {
                    OpenFileLauncher();
                } catch (android.content.ActivityNotFoundException ex) {
                    // Potentially direct the user to the Market with a Dialog
                    ex.printStackTrace();
                    Toast.makeText(requireContext(), "Please install a File Manager.", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(requireContext(), "Cannot upload more than 5 files.", Toast.LENGTH_SHORT).show();
            }
        });

        binding.submitQuery.setOnClickListener(v -> {
            try {
                if (!binding.queryText.getText().toString().trim().equalsIgnoreCase("")) {
                    replyQuery();
                } else {
                    Toast.makeText(requireContext(), "Enter the Query before submitting", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });

        queryViewModel.getReloginState().observe(requireActivity(), relogin -> {
            if (relogin) {
                UtilMethods.RedirectToLogin(requireActivity());
            } else {
            }
        });

    }

    public void OpenFileLauncher() {

        Intent intent
                = new Intent(Intent.ACTION_GET_CONTENT);
        String[] mimeTypes = {"image/*", "application/msword", "application/vnd.openxmlformats-officedocument.wordprocessingml.document",  // .doc & .docx
                "application/vnd.ms-excel", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",  // .xls & .xlsx
                "application/pdf"};
        intent.setType("*/*");
        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);


        Intent.createChooser(intent, "Select a File to Upload");
        fileLauncherActivity.launch(intent);
    }

    private void replyQuery() throws JSONException {


        //json object for replying a query
        //here isReply is true because we are letting user to reply the query
        JSONObject queryRequestJson = new JSONObject();
        queryRequestJson.put("empSrNo", queryInfo.getEmployeeSrNo());
        queryRequestJson.put("query", binding.queryText.getText().toString().trim());
        queryRequestJson.put("custQuerySrNo", queryInfo.getEqCustQrySrNo());
        queryRequestJson.put("isReply", true);

        //multipart form data
        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);

        if (i == 0) {
            builder.addFormDataPart("QueryData", queryRequestJson.toString());

        } else {
            for (int k = 0; k < i; k++) {

                File file = new File(PathHolder[k]);

                final MediaType MEDIA_TYPE = MediaTypes.fromFile(file);

                if (file.exists())
                    builder.addFormDataPart(file.getName(), FileUtil.getFileName(requireContext(), Uri.fromFile(file)), RequestBody.create(MEDIA_TYPE, file));
            }

            builder.addFormDataPart("QueryData", queryRequestJson.toString());

        }

        RequestBody requestBody = builder.build();
        Log.d(LogTags.QUERY_ACTIVITY, "replyQuery: " + requestBody.toString());
        Log.d(LogTags.QUERY_ACTIVITY, "replyQuery: " + queryRequestJson.toString());
        queryViewModel.addQuery(requestBody).observe(getViewLifecycleOwner(), reply -> {
            if (reply != null) {
                if (reply.getStatus()) {
                    //to get new reply
                    queryViewModel.getQueryDetails(queryInfo.getEqCustQrySrNo());

                    //need to clear the edittext and list
                    binding.queryText.setText(null);
                    binding.attachmentLayout.removeAllViews();
                    PathHolder = new String[5];
                    i = 0;
                }
            } else {
                //something went wrong
                //already showed toast in repo class
            }
        });
    }

    public void UpdateList() {
        try {
            fileview = new LinearLayout[i + 1];

            dots = new AppCompatTextView[i + 1];
            fileType = new AppCompatImageView[i + 1];
            delete = new AppCompatButton[i + 1];

            binding.attachmentLayout.removeAllViews();

            int pixels1 = (int) (15 * getResources().getDisplayMetrics().density + 0.5f);
            int pixels2 = (int) (15 * getResources().getDisplayMetrics().density + 0.5f);

            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1f);
            LinearLayout.LayoutParams layoutParams2 = new LinearLayout.LayoutParams(pixels1, pixels2);

            layoutParams2.setMargins(4, 10, 4, 10);
            layoutParams.setMargins(4, 10, 4, 10);

            for (int j = 0; j < dots.length; j++) {

                int o = j;
                dots[j] = new AppCompatTextView(requireContext());
                delete[j] = new AppCompatButton(requireContext());
                fileType[j] = new AppCompatImageView(requireContext());
                fileview[j] = new LinearLayout(requireContext());
                delete[j].setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.close));
                fileview[j].setOrientation(LinearLayout.HORIZONTAL);
                if (dots.length > 1) binding.attachmentLayout.setWeightSum((float) dots.length);
                else fileview[j].setWeightSum(2.0f);

                fileview[j].setGravity(Gravity.CENTER_VERTICAL);
                dots[j].setTextSize(10.0f);
                int maxLength = 10;
                InputFilter[] fArray = new InputFilter[1];
                fArray[0] = new InputFilter.LengthFilter(maxLength);
                dots[j].setFilters(fArray);

                String[] s = PathHolder[j].split("/");
                dots[j].setText(s[s.length - 1]);
                dots[j].setPadding(10, 0, 10, 0);
                String[] filenameArray = PathHolder[j].split("\\.");
                String extension = filenameArray[filenameArray.length - 1];

                switch (extension.toLowerCase()) {
                    case "pdf":
                        fileType[j].setBackgroundDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.ic_pdf));
                        break;
                    case "xlsx":
                    case "xls":
                        fileType[j].setBackgroundDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.ic_excel));
                        break;
                    case "docx":
                    case "doc":
                        fileType[j].setBackgroundDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.ic_word));
                        break;
                    case "png":
                    case "jpg":
                    case "jpeg":
                        fileType[j].setBackgroundDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.ic_img));
                        break;
                }
                fileview[j].setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.ripple_effect_file));
                fileview[j].addView(fileType[j], layoutParams2);
                fileview[j].addView(dots[j], layoutParams);
                fileview[j].addView(delete[j], layoutParams2);
                binding.attachmentLayout.addView(fileview[j], layoutParams);
                dots[j].setLayoutParams(layoutParams);

                delete[j].setOnClickListener(view -> {
                    try {

                        String[] PathHoler = new String[5];
                        int fileCouunt = 0;
                        for (int l = 0; l < i; l++) {
                            if (PathHolder[l] != PathHolder[o]) {
                                PathHoler[fileCouunt] = PathHolder[l];
                                fileCouunt++;
                            }
                        }

                        i = i - 1;
                        PathHolder = new String[5];
                        PathHolder = PathHoler;

                        UpdateList();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                });
            }
            i++;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        queryViewModel.resetDetails();
    }


    @Override
    public void onStartDownload(int position, int queryPosition) {
        adapter.startDownloading(position);
        adapter.notifyItemChanged(queryPosition);
    }

    @Override
    public void onFinishDownload(int position, int queryPosition) {
        adapter.finishDownloading(position);
        adapter.notifyItemChanged(queryPosition);
    }

    @Override
    public void requestPermission(int position, Attachment attachment, int queryPosition) {

        this.attachment = attachment;
        this.position = position;
        this.queryPosition = queryPosition;
        String[] permissionsArray = {Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE};
        requestPermissionLauncher.launch(permissionsArray);


    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        fileLauncherActivity = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            queryViewModel.setLoadingFromFilePicker();
            Log.d("FILE-PICKER", "filepicker: started");
            if (result.getResultCode() == Activity.RESULT_OK) {
                // There are no request codes
                Log.d("FILE-PICKER", "filepicker: result ok");
                Intent data = result.getData();
                Log.d("FILE-PICKER", "filepicker: " + result.getData().getType());
                try {
                    File file2 = FileUtil.from(context, data.getData());
                    // File file2 = new File(getPath(data.getData()));

                    String filePath = file2.getPath();
                    boolean FileExists = false;

                    String[] filenameArray = filePath.split("\\.");
                    String extension = filenameArray[filenameArray.length - 1];
                    if (Arrays.asList(filesExt).contains(extension.replace(".", "").toLowerCase())) {
                        for (int fileCheck = 0; fileCheck < i; fileCheck++) {
                            FileExists = PathHolder[fileCheck].equals(filePath);
                            if (FileExists) {
                                break;
                            }
                        }

                        if (FileExists)
                            Toast.makeText(context, "File Already Attached", Toast.LENGTH_SHORT).show();
                        else {

                            PathHolder[i] = filePath;

                            UpdateList();

                        }
                    } else
                        Toast.makeText(context, "Entered file format should be one of the following : xls, xlsx, doc, docx, png, jpeg, jpg, pdf", Toast.LENGTH_SHORT).show();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        requestPermissionLauncher =
                registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), permissions -> {

                    if (Boolean.TRUE.equals(permissions.getOrDefault(Manifest.permission.READ_EXTERNAL_STORAGE, false))
                            && Boolean.TRUE.equals(permissions.getOrDefault(Manifest.permission.WRITE_EXTERNAL_STORAGE, false))) {
                        onStartDownload(position, queryPosition);
                        permissionHelper.granted();

                    } else {
                        Toast.makeText(requireContext(), "Permission denied", Toast.LENGTH_SHORT).show();
                        onFinishDownload(position, queryPosition);
                    }
                });
    }
}