package com.indiainsure.android.MB360.insurance.claims.repository.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.indiainsure.android.MB360.R;
import com.indiainsure.android.MB360.databinding.FragmentLoadIntimatedClaimsBinding;
import com.indiainsure.android.MB360.insurance.claims.repository.ClaimsViewModel;
import com.indiainsure.android.MB360.insurance.repository.LoadSessionViewModel;
import com.indiainsure.android.MB360.insurance.repository.responseclass.GroupGMCPolicyEmployeeDatum;
import com.indiainsure.android.MB360.insurance.repository.selectedPolicyRepo.SelectedPolicyViewModel;
import com.indiainsure.android.MB360.utilities.UtilMethods;

public class LoadIntimatedClaimsFragment extends Fragment {

    FragmentLoadIntimatedClaimsBinding binding;
    View view;
    LoadSessionViewModel loadSessionViewModel;
    SelectedPolicyViewModel selectedPolicyViewModel;
    ClaimsViewModel claimsViewModel;
    ClaimAdapter adapter;
    String PRODUCT_CODE = "GMC";

    public LoadIntimatedClaimsFragment() {
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
        binding = FragmentLoadIntimatedClaimsBinding.inflate(inflater, container, false);
        view = binding.getRoot();

        loadSessionViewModel = new ViewModelProvider(requireActivity()).get(LoadSessionViewModel.class);
        selectedPolicyViewModel = new ViewModelProvider(requireActivity()).get(SelectedPolicyViewModel.class);
        claimsViewModel = new ViewModelProvider(this).get(ClaimsViewModel.class);

        getClaims();

        claimsViewModel.getLoadingState().observe(getViewLifecycleOwner(), loading -> {
            if (loading) {
                showLoading();
            } else {
                hideLoading();
            }
        });

        claimsViewModel.getReloginState().observe(requireActivity(), relogin -> {
            if (relogin) {
                UtilMethods.RedirectToLogin(requireActivity());
            } else {
            }
        });

        return view;
    }


    private void getClaims() {
        //to get the  claims data we need some parameters from load session values

        try {


            PRODUCT_CODE = "GMC";
            loadSessionViewModel.getLoadSessionData().observe(requireActivity(), loadSessionResponse -> {
                try {

                    PRODUCT_CODE = "GMC";
                    String employeeSrNo = "";
                    String groupChildSrvNo = "";
                    String oeGrpBasInfSrNo = "";

                    GroupGMCPolicyEmployeeDatum groupGMCPolicyEmployeeDatum;
                    groupGMCPolicyEmployeeDatum = loadSessionResponse.getGroupPoliciesEmployees().get(0).getGroupGMCPolicyEmployeeData().get(0);

                    //queries for claims
                    employeeSrNo = groupGMCPolicyEmployeeDatum.getEmployeeSrNo();
                    groupChildSrvNo = groupGMCPolicyEmployeeDatum.getGroupchildsrno();
                    oeGrpBasInfSrNo = groupGMCPolicyEmployeeDatum.getOeGrpBasInfSrNo();


                    claimsViewModel.getClaims(employeeSrNo, groupChildSrvNo, oeGrpBasInfSrNo);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            });

            claimsViewModel.getClaimsData().observe(getViewLifecycleOwner(), claimsResponse -> {
                if (claimsResponse != null) {
                    if (claimsResponse.getClaimslist() != null) {

                        if (claimsResponse.getClaimslist().isEmpty()) {
                            binding.errorLayout.setVisibility(View.VISIBLE);
                            binding.messageTextView.setText(getString(R.string.no_intimations_found));
                            binding.imgError.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.noclaim));
                        } else {
                            adapter = new ClaimAdapter(requireContext(), claimsResponse.getClaimslist());
                            binding.claimsRecyclerView.setAdapter(adapter);
                            adapter.notifyItemRangeChanged(0, claimsResponse.getClaimslist().size());
                            binding.errorLayout.setVisibility(View.GONE);
                        }
                        if (!claimsResponse.getResult().getStatus()) {
                            binding.messageTextView.setVisibility(View.VISIBLE);
                            binding.messageTextView.setText("Error: " + claimsResponse.getResult().getMessage());
                            binding.errorLayout.setVisibility(View.VISIBLE);
                        }
                    } else {
                        //No Data found
                        binding.messageTextView.setVisibility(View.VISIBLE);
                        binding.messageTextView.setText(claimsResponse.getResult().getMessage().toLowerCase().startsWith("unable") ?
                                "Unable to reach server.\nPlease try again later." :
                                getString(R.string.no_intimations_found));
                        binding.imgError.setImageDrawable(claimsResponse.getResult().getMessage().toLowerCase().startsWith("unable") ? ContextCompat.getDrawable(requireContext(), R.drawable.ic_api_error_state) : ContextCompat.getDrawable(requireContext(), R.drawable.noclaim));
                        binding.errorLayout.setVisibility(View.VISIBLE);

                    }
                } else {
                    binding.messageTextView.setVisibility(View.VISIBLE);
                    binding.messageTextView.setText(getString(R.string.something_went_wrong));
                    binding.imgError.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.ic_api_error_state));
                    binding.errorLayout.setVisibility(View.VISIBLE);
                }

            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void hideLoading() {
        binding.progressBar.setVisibility(View.GONE);
    }

    private void showLoading() {
        binding.progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void onResume() {
        super.onResume();
        getClaims();
    }
}