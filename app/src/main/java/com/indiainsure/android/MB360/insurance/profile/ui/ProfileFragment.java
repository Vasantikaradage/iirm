package com.indiainsure.android.MB360.insurance.profile.ui;


import static com.indiainsure.android.MB360.BuildConfig.AUTH_EMAIL;
import static com.indiainsure.android.MB360.BuildConfig.AUTH_LOGIN_ID;
import static com.indiainsure.android.MB360.BuildConfig.AUTH_LOGIN_TYPE;
import static com.indiainsure.android.MB360.BuildConfig.AUTH_PHONE_NUMBER;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.FitCenter;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.textfield.TextInputLayout;
import com.indiainsure.android.MB360.R;
import com.indiainsure.android.MB360.databinding.FragmentProfileBinding;
import com.indiainsure.android.MB360.insurance.profile.repository.ProfileViewModel;
import com.indiainsure.android.MB360.insurance.profile.response.ProfileResponse;
import com.indiainsure.android.MB360.insurance.profile.response.UserDocumentsDetail;
import com.indiainsure.android.MB360.insurance.repository.LoadSessionViewModel;
import com.indiainsure.android.MB360.insurance.repository.responseclass.GroupGMCPolicyEmployeeDatum;
import com.indiainsure.android.MB360.insurance.repository.selectedPolicyRepo.SelectedPolicyViewModel;
import com.indiainsure.android.MB360.onboarding.SplashScreenActivity;
import com.indiainsure.android.MB360.utilities.EncryptionPreference;
import com.indiainsure.android.MB360.utilities.FileDownloader;
import com.indiainsure.android.MB360.utilities.FileUtil;
import com.indiainsure.android.MB360.utilities.LogMyBenefits;
import com.indiainsure.android.MB360.utilities.UtilMethods;

import java.io.File;
import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ProfileFragment extends Fragment implements DocumentOnClickListener {

    ProfileViewModel profileViewModel;
    ProfileDocumentAdapter adapter;
    NavController navController;
    ActivityResultLauncher<Intent> fileLauncherActivity;
    File file;
    Uri fileUri;


    //to upload
    String groupChild = "";
    String oeGrpBasInfoSrNo = "";
    String employeeSrNo = "";


    FragmentProfileBinding binding;
    View view;

    //view-models

    LoadSessionViewModel loadSessionViewModel;
    SelectedPolicyViewModel selectedPolicyViewModel;

    EncryptionPreference encryptionPreference;


    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //encryption preference
        encryptionPreference = new EncryptionPreference(requireContext());


    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        view = binding.getRoot();

        //To Navigate
        NavHostFragment navHostFragment = (NavHostFragment) requireActivity().getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_fragment);
        navController = navHostFragment.getNavController();

        loadSessionViewModel = new ViewModelProvider(requireActivity()).get(LoadSessionViewModel.class);
        selectedPolicyViewModel = new ViewModelProvider(requireActivity()).get(SelectedPolicyViewModel.class);
        profileViewModel = new ViewModelProvider(requireActivity()).get(ProfileViewModel.class);


        loadProfile();


        return view;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

//personal information
        binding.icEdit.setOnClickListener(v -> {

            if (binding.llEditable.getVisibility() == View.GONE) {

                binding.llEditable.setVisibility(View.VISIBLE);
                binding.icEdit.setRotation(180);

                // scrollProfile.scrollTo(btnReset.getScrollX(), btnReset.getScrollY() + btnReset.getBottom());
                new Handler().postDelayed(() -> binding.scrollProfile.fullScroll(View.FOCUS_DOWN), 1000);

            } else {
                binding.llEditable.setVisibility(View.GONE);
                binding.icEdit.setRotation(0);
            }
        });

        //personal details
        binding.icEditDocument.setOnClickListener(v -> {

            if (binding.llEditableDocument.getVisibility() == View.GONE) {

                binding.llEditableDocument.setVisibility(View.VISIBLE);
                binding.icEditDocument.setRotation(180);

                // scrollProfile.scrollTo(btnReset.getScrollX(), btnReset.getScrollY() + btnReset.getBottom());
                new Handler().postDelayed(() -> binding.scrollProfile.fullScroll(View.FOCUS_DOWN), 1000);

            } else {
                binding.llEditableDocument.setVisibility(View.GONE);
                binding.icEditDocument.setRotation(0);
            }
        });

        //logout
        binding.logoutbtn.setOnClickListener(v -> {
            final Dialog dialog = new Dialog(getContext());
            dialog.setContentView(R.layout.smslayout);
            Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.nhborder));

            AppCompatTextView lblSMS = dialog.findViewById(R.id.lblSMS);
            final AppCompatEditText smsContact = dialog.findViewById(R.id.smsContact);
            AppCompatButton btnSubmit = dialog.findViewById(R.id.btnSubmit);
            AppCompatButton btnCancel = dialog.findViewById(R.id.btnCancel);
            AppCompatTextView lblSMSHeader = dialog.findViewById(R.id.lblSMSHeader);
            lblSMS.setText(R.string.my_benifits);
            lblSMSHeader.setText(R.string.logout_warning);
            btnSubmit.setText(getString(R.string.yes));
            btnCancel.setText(getString(R.string.no));
            smsContact.setVisibility(View.GONE);


            btnSubmit.setOnClickListener(v1 -> {
                UtilMethods.logout(requireActivity());
            });

            btnCancel.setOnClickListener(v2 -> {
                dialog.dismiss();
            });

            dialog.show();

        });

        profileViewModel.getReloginState().observe(requireActivity(), relogin -> {
            if (relogin) {
                UtilMethods.RedirectToLogin(requireActivity());
            } else {
            }
        });

    }

    private void loadProfile() {
        loadSessionViewModel.getLoadSessionData().observe(getViewLifecycleOwner(), loadSessionResponse -> {
            groupChild = loadSessionResponse.getGroupInfoData().getGroupchildsrno();
            GroupGMCPolicyEmployeeDatum groupGMCPolicyEmployeeDatum = loadSessionResponse.getGroupPoliciesEmployees().get(0).getGroupGMCPolicyEmployeeData().get(0);
            oeGrpBasInfoSrNo = groupGMCPolicyEmployeeDatum.getOeGrpBasInfSrNo();
            employeeSrNo = groupGMCPolicyEmployeeDatum.getEmployeeSrNo();

            profileViewModel.getProfile(groupChild, oeGrpBasInfoSrNo, employeeSrNo);


        });

        profileViewModel.getProfileData().observe(getViewLifecycleOwner(), profileResponse -> {

            if (profileResponse != null) {
                binding.userName.setText(profileResponse.getUserPersonalDetails().getEmployeeName());
                binding.txtdesignation.setText(profileResponse.getUserOfficialDetails().getDesignation());
                binding.lblBirth.setText(profileResponse.getUserPersonalDetails().getDateOfBirth());
                binding.lblMob.setText(profileResponse.getUserPersonalDetails().getCellphoneNumber());
                binding.lblEmail.setText(profileResponse.getUserOfficialDetails().getOfficialEMailId());
                binding.lblContact.setText(getString(R.string.not_available));
                binding.lblRelation.setText(profileResponse.getUserPersonalDetails().getRelation());
                binding.lblPEmail.setText(profileResponse.getUserOfficialDetails().getOfficialEMailId());
                String address = profileResponse.getUserAddressDetails().getEmpPerAddrLine1() + "," +
                        profileResponse.getUserAddressDetails().getEmpPerAddrLine2() + "," +
                        profileResponse.getUserAddressDetails().getEmpCity() + "-" +
                        profileResponse.getUserAddressDetails().getEmpPincode() + "," +
                        profileResponse.getUserAddressDetails().getEmpState();
                binding.lblAddress.setText(address);
                if (profileResponse.getUserProfileImageUrl() != null) {
                    Glide.with(requireContext())
                            .load(profileResponse.getUserProfileImageUrl())
                            .transform(new FitCenter(), new RoundedCorners(28))
                            .transition(DrawableTransitionOptions.withCrossFade())
                            .into(binding.userImage);
                } else {

                    if (profileResponse.getUserPersonalDetails().getGender().equalsIgnoreCase("MALE")) {

                        //userImage
                        Glide.with(requireContext())
                                .load(R.drawable.user_male)
                                .transform(new FitCenter(), new RoundedCorners(28))
                                .transition(DrawableTransitionOptions.withCrossFade())
                                .into(binding.userImage);
                    } else {
                        //userImage
                        Glide.with(requireContext())
                                .load(R.drawable.user_female)
                                .transform(new FitCenter(), new RoundedCorners(28))
                                .transition(DrawableTransitionOptions.withCrossFade())
                                .into(binding.userImage);
                    }
                }


                setUpDocumentsList(profileResponse);
            }
        });
    }

    private void setUpDocumentsList(ProfileResponse profileResponse) {
        //set up the recyclerview for documents
        adapter = new ProfileDocumentAdapter(profileResponse.getUserDocumentsDetails(), requireContext(), this);
        binding.documentCycle.setAdapter(adapter);
    }

    //logout
    private void logout() {

        // Store null values to SharedPreferences for logging out a user
        encryptionPreference.setEncryptedDataString(AUTH_LOGIN_ID, null);
        encryptionPreference.setEncryptedDataString(AUTH_EMAIL, null);
        encryptionPreference.setEncryptedDataString(AUTH_PHONE_NUMBER, null);
        encryptionPreference.setEncryptedDataString(AUTH_LOGIN_TYPE, null);

        Intent logoutIntent = new Intent(requireActivity(), SplashScreenActivity.class);
        startActivity(logoutIntent);
        requireActivity().finish();


    }

    @Override
    public void onDocumentClicked(UserDocumentsDetail document) {
        //download the document and show!
        new DownloadFile(requireContext(), requireActivity(), "pdf").downloadFilePDF(document.getDocumentType(), document.getDocumentPath());

    }

    @Override
    public void onDocumentUploadClick(String docType) {
//navigate to upload fragment
      //  uploadDocuments(requireContext(), docType);
    }

    //download file class
    //creates background thread for downloading the files
    static class DownloadFile {
        Context context;
        Activity activity;
        String fileUrl, fileName;
        File file;
        String extension;


        public DownloadFile(Context context, Activity activity, String extension) {
            this.context = context;
            this.activity = activity;
            this.extension = extension;
        }

        public void downloadFilePDF(String fileName, String fileUrl) {
            //we can show the loading animation here
            //showLoading()
            ExecutorService executors = Executors.newSingleThreadExecutor();

            Handler handler = new Handler(Looper.getMainLooper());
            executors.execute(() -> {
                //runnable thread
                if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE)
                        == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        == PackageManager.PERMISSION_GRANTED) {
                    this.fileUrl = fileUrl;
                    this.fileName = fileName.toLowerCase();

                    File folder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);

                    file = new File(context.getFilesDir(), this.fileName);
                    LogMyBenefits.d("", "downloadFilePDF: created a new File " + file.getAbsolutePath());

                    try {
                        //noinspection ResultOfMethodCallIgnored
                        if (file.exists()) {
                            file.createNewFile();
                        } else {
                            //read file directly
                            readFile(this.fileName, extension);
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                        //todo show the user that file isn't created
                    }
                } else {
                    ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.READ_EXTERNAL_STORAGE}, 100);
                }
                FileDownloader.downloadFile(fileUrl, file, activity, context);

                handler.post(() -> {
                    if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE)
                            == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            == PackageManager.PERMISSION_GRANTED) {
                        try {
                            File file = new File(context.getFilesDir(), fileName);
                            if (file.exists()) {
                                Uri path = FileProvider.getUriForFile(context, "com.indiainsure.android.MB360.provider", file);
                                Intent pdfIntent = new Intent(Intent.ACTION_VIEW);
                                pdfIntent.setDataAndType(path, "application/" + extension);
                                pdfIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                pdfIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                                activity.startActivity(pdfIntent);
                            } else {
                                Toast.makeText(context, "File not found", Toast.LENGTH_SHORT).show();
                            }

                        } catch (ActivityNotFoundException e) {
                            Toast.makeText(context, "No Application available to view PDF", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                Manifest.permission.READ_EXTERNAL_STORAGE}, 100);

                    }
                    //todo hide loading
                    //hideLoading()
                });

            });
        }

        private void readFile(String fileName, String extension) {
            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(() -> {

                if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE)
                        == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        == PackageManager.PERMISSION_GRANTED) {

                    try {
                        File outputFile = new File(context.getFilesDir(), fileName + "." + extension);
                        if (outputFile.exists()) {
                            Intent pdfIntent = new Intent(Intent.ACTION_VIEW);
                            Uri path = FileProvider.getUriForFile(context, "com.indiainsure.android.MB360.provider", outputFile);
                            pdfIntent.setDataAndType(path, "application/" + extension);
                            pdfIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            pdfIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        } else {
                            Toast.makeText(context, "File not found", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else {
                    ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.READ_EXTERNAL_STORAGE}, 100);

                }
                //todo hide loading
                //hideLoading()
            });
        }
    }

    private void uploadDocuments(Context context, String docType) {

        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context);
     //   bottomSheetDialog.setContentView(R.layout.fragment_upload_documents_profile_dialog);

        //to upload the documents


        AppCompatButton submit;
        TextInputLayout documentNumberLayout;
        ImageView pdfSelect;

       // documentNumberLayout = bottomSheetDialog.findViewById(R.id.document_text_input_layout);
        submit = bottomSheetDialog.findViewById(R.id.btnSubmit);
      //  pdfSelect = bottomSheetDialog.findViewById(R.id.file_selector);

        // no need
      /*  pdfSelect.setOnClickListener(v -> {
            //get the documents from the user.
            getDocument();
        });*/

        submit.setOnClickListener(v -> {
          /*  String docNumber = documentNumberLayout.getEditText().getText().toString().trim();
            if (docNumber != null && !docNumber.isEmpty()) {
                profileViewModel.uploadDocuments(fileUri, groupChild, oeGrpBasInfoSrNo, employeeSrNo, docType, docNumber).observe(getViewLifecycleOwner(),
                        message -> {
                            if (message != null) {
                                LogMyBenefits.d(LogTags.PROFILE_ACTIVITY, "uploadDocuments: " + message.getMessage());
                                Toast.makeText(context, message.getMessage(), Toast.LENGTH_SHORT).show();
                                bottomSheetDialog.dismiss();
                            } else {
                                Toast.makeText(context, "Document Upload Not Successful", Toast.LENGTH_SHORT).show();
                            }
                        });
            } else {

            }
*/
        });


        bottomSheetDialog.show();
    }

    private void getDocument() {
        try {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("application/pdf");
            intent.addCategory(Intent.CATEGORY_OPENABLE);

            Intent.createChooser(intent, "Select a File to Upload");
            fileLauncherActivity.launch(intent);

        } catch (ActivityNotFoundException exception) {
            exception.printStackTrace();
            Toast.makeText(requireContext(), "No application found to show pdf files", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        fileLauncherActivity = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        try {
                            file = FileUtil.from(context, data.getData());
                            fileUri = data.getData();

                        } catch (Exception exception) {
                            exception.printStackTrace();
                        }
                    }
                });
    }
}