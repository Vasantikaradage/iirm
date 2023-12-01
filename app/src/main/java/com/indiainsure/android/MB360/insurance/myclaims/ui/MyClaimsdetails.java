package com.indiainsure.android.MB360.insurance.myclaims.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.indiainsure.android.MB360.R;
import com.indiainsure.android.MB360.databinding.FragmentMyClaimsdetailsBinding;
import com.indiainsure.android.MB360.insurance.myclaims.repository.MyClaimsViewModel;
import com.indiainsure.android.MB360.insurance.myclaims.responseclassV1.ClaimInformation;
import com.indiainsure.android.MB360.insurance.myclaims.responseclassV1.claimsdetails.ClaimAilmentInformation;
import com.indiainsure.android.MB360.insurance.myclaims.responseclassV1.claimsdetails.ClaimCashlessInformation;
import com.indiainsure.android.MB360.insurance.myclaims.responseclassV1.claimsdetails.ClaimChargesInformation;
import com.indiainsure.android.MB360.insurance.myclaims.responseclassV1.claimsdetails.ClaimFileDtInformation;
import com.indiainsure.android.MB360.insurance.myclaims.responseclassV1.claimsdetails.ClaimHospitalInformation;
import com.indiainsure.android.MB360.insurance.myclaims.responseclassV1.claimsdetails.ClaimIncidentInformation;
import com.indiainsure.android.MB360.insurance.myclaims.responseclassV1.claimsdetails.ClaimPaymentInformation;
import com.indiainsure.android.MB360.insurance.myclaims.responseclassV1.claimsdetails.ClaimProcessInformation;
import com.indiainsure.android.MB360.insurance.myclaims.responseclassV1.claimsdetails.MemberInformation;
import com.indiainsure.android.MB360.insurance.repository.LoadSessionViewModel;
import com.indiainsure.android.MB360.insurance.repository.responseclass.LoadSessionResponse;
import com.indiainsure.android.MB360.insurance.repository.selectedPolicyRepo.responseclass.GroupPolicyData;
import com.indiainsure.android.MB360.utilities.UtilMethods;

import java.text.MessageFormat;

public class MyClaimsdetails extends Fragment {


    FragmentMyClaimsdetailsBinding binding;
    View view;
    MyClaimsViewModel myClaimsViewModel;
    LoadSessionViewModel loadSessionViewModel;
    ClaimInformation claims;
    ClaimInformation declaims;
    String claimSrNo = "";

    public MyClaimsdetails() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentMyClaimsdetailsBinding.inflate(inflater, container, false);
        view = binding.getRoot();

        myClaimsViewModel = new ViewModelProvider(requireActivity()).get(MyClaimsViewModel.class);
        loadSessionViewModel = new ViewModelProvider(requireActivity()).get(LoadSessionViewModel.class);


        myClaimsViewModel.getLoading().observe(getViewLifecycleOwner(), loading -> {
            if (loading) {
                binding.progressBarLayout.setVisibility(View.VISIBLE);
                binding.claimLayout.setVisibility(View.GONE);
            } else {
                binding.progressBarLayout.setVisibility(View.GONE);
                binding.claimLayout.setVisibility(View.VISIBLE);
            }
        });


        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        claimSrNo = MyClaimsdetailsArgs.fromBundle(getArguments()).getClaimSrNo();
        getMyClaimsDetails(claimSrNo);
    }

    private void getMyClaimsDetails(String claimSrNo) {

        LoadSessionResponse loadSessionResponse = loadSessionViewModel.getLoadSessionData().getValue();

        String groupChildSrNo = "";
        String OeGrpBasInfoSrNo = "";
        if (loadSessionResponse != null) {

            try {

                for (GroupPolicyData policy : loadSessionResponse.getGroupPolicies().get(0).getGroupGMCPoliciesData()) {
                    if (policy.getPolicyType().equalsIgnoreCase("base")) {
                        OeGrpBasInfoSrNo = policy.getOeGrpBasInfSrNo();
                    }
                }
                binding.errorLayout2.setVisibility(View.GONE);

                myClaimsViewModel.getMyClaimsDetails(groupChildSrNo, OeGrpBasInfoSrNo, claimSrNo)
                        .observe(getViewLifecycleOwner(), claimInformation -> {
                            if (claimInformation != null) {

                                MemberInformation claimMemberInformation = claimInformation.getMemberInformation();
                                ClaimIncidentInformation claimIncidentInformation = claimInformation.getClaimIncidentInformation();
                                ClaimHospitalInformation claimHospitalInformation = claimInformation.getClaimHospitalInformation();
                                ClaimAilmentInformation claimAilmentInformation = claimInformation.getClaimAilmentInformation();
                                ClaimCashlessInformation claimCashLessInformation = claimInformation.getClaimCashlessInformation();
                                ClaimChargesInformation claimChargesInformation = claimInformation.getClaimChargesInformation();
                                ClaimFileDtInformation claimFileInformation = claimInformation.getClaimFileDtInformation();
                                ClaimProcessInformation claimProcessInformation = claimInformation.getClaimProcessInformation();
                                ClaimPaymentInformation claimPaymentInformation = claimInformation.getClaimPaymentInformation();

                                //member info
                                binding.tvBeneficiaryValue.setText(claimMemberInformation.getBeneficiaryName());
                                binding.tvRelationValue.setText(claimMemberInformation.getRelation());
                                binding.tvValue.setText(claimMemberInformation.getGender());
                                binding.tvAgeValue.setText(claimMemberInformation.getAge());
                                binding.tvTPAValue.setText(claimMemberInformation.getTPAId());
                                binding.tvDOBValue.setText(claimMemberInformation.getDateOfBirth());

                                //claim info
                                binding.tvClaimNumberValue.setText(claimIncidentInformation.getClaimNo());
                                binding.titleTxt2Date.setText(claimIncidentInformation.getClaimDate());
                                binding.tvValue2.setText(claimHospitalInformation.getHospitalName());
                                binding.tvDOAValue2.setText(claimHospitalInformation.getDateOfAdmission());
                                binding.tvDODValue.setText(claimHospitalInformation.getDateOfDischarge());

                                //cashless info
                                binding.tvDOAValue1.setText(claimCashLessInformation.getCashlessSentDate().trim());
                                binding.tvDODValue1.setText(claimCashLessInformation.getCashlessAmount().trim().equalsIgnoreCase("-") ? "-" : "₹ " + claimCashLessInformation.getCashlessAmount().trim());

                                //claim charge
                                binding.tvClaimNumberValue11.setText(UtilMethods.PriceFormat(claimChargesInformation.getFinalBillAmount()));
                                binding.tvValue66.setText(claimChargesInformation.getDeductionReasons());
                                binding.tvValue3.setText(claimChargesInformation.getNonPayableExpenses());

                                //claim ailment
                                binding.tvAilmentValue.setText(claimAilmentInformation.getAilment());

                                //file
                                binding.tvAmountValue11.setText(claimFileInformation.getFileReceivedDate());

                                //process
                                String claim_type = claimProcessInformation.getTypeOfClaim() + "_" + claimProcessInformation.getClaimStatus();
                                binding.tvAmountValue.setText(MessageFormat.format("{0}", UtilMethods.PriceFormat(claimProcessInformation.getReportedAmount())));

                                //payment
                                String claim_paid_date = claimProcessInformation.getClaimPaidDate();
                                String claimRejectedOn = claimProcessInformation.getClaimRejectedDate();
                                String claimDeficient = claimFileInformation.getDeficiencies();
                                String strChequeNo = claimPaymentInformation.getBankChequeNo();
                                String CoPaymentDeductions = claimChargesInformation.getCoPaymentDeduction();
                                String ClaimClosureReasons = claimProcessInformation.getCloseReasons();
                                String claimrejectedReasons = claimProcessInformation.getDenialReasons();
                                String strPaidAmount = claimProcessInformation.getPaidAmount();
                                String strcashAuth = claimProcessInformation.getClaimPaidDate();
                                String strDeductReasons = "";
                                String strDeductions = "";
                                String NonPayableExpenses = "";
                                NonPayableExpenses = claimChargesInformation.getNonPayableExpenses();
                                String claimDefStatus = claimProcessInformation.getOutstandingClaimStatus();
                                String coPaymentDeductions = claimChargesInformation.getCoPaymentDeduction();
                                int progressAnim = 6000;


                                int intClaimAmt = 0;
                                int intReportAmt = 0;

                                if (claim_type.toLowerCase().contains("cashless")) {
                                    binding.cashlessLayout.setVisibility(View.VISIBLE);
                                } else {
                                    binding.cashlessLayout.setVisibility(View.GONE);
                                }

                                binding.tvClaimExtensionValue.setText(claimInformation.getClaimIncidentInformation().getClaimExtension());

                                switch (claim_type.toString().toLowerCase()) {
                                    case "reimbursement_rejected":
                                        binding.lv5.setVisibility(View.VISIBLE);
                                        binding.lv8.setVisibility(View.GONE);
                                        binding.llLastCard11.setVisibility(View.GONE);

                                        binding.titleTxt3.setText(R.string.claim_reject);

                                        binding.llLastCard1.setVisibility(View.GONE);

                                        binding.tvKey2.setText("Deficiencies");
                                        binding.tvValue1.setText(claimDeficient);

                                        binding.tvKey4.setText(R.string.reject_reason);
                                        binding.tvValue4.setText(claimrejectedReasons);

                                        binding.tvKey3.setText(R.string.reject_on);
                                        binding.tvValue3.setPadding(0, 0, 0, 0);
                                        binding.tvValue3.setText(claimRejectedOn);

                                        binding.rsAmount.setVisibility(View.GONE);

                                        binding.img3.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.close_white));
                                        binding.rv3.setBackgroundResource(R.drawable.claim_end);

                                        binding.btnShowMore.setVisibility(View.GONE);

                                        break;

                                    /*Paid Amount&&Paid Date&&Cheque Number/NEFT Details&&Decutions&&Deduction Reasons*/
                                    case "reimbursement_paid":

                                        binding.titleTxt3.setText(R.string.claim_pay);

                                        binding.tvClaimNumber1.setText(R.string.paid_amt);
                                        binding.tvClaimRs.setVisibility(View.VISIBLE);
                                        binding.tvClaimNumberValue1.setText(UtilMethods.PriceFormat(strPaidAmount));

                                        binding.tvAmount1.setText(R.string.paid_date);
                                        binding.tvAmountValue1.setText(strcashAuth);

                                        binding.tvKey2.setText(R.string.chq_no);
                                        binding.tvValue1.setText(claimPaymentInformation.getBankChequeNo());

                                        binding.tvKey3.setText("Not Payable Expenses");

//                                    intClaimAmt = Integer.parseInt(strPaidAmount.replace(",", ""));
                                        intClaimAmt = Integer.parseInt(strPaidAmount);
                                        strDeductions = String.valueOf(intReportAmt - intClaimAmt);
                                        if (NonPayableExpenses.equals("-") | NonPayableExpenses.equals(""))
                                            binding.tvValue3.setText("-");
                                        else
                                            binding.tvValue3.setText(MessageFormat.format("{0} ", NonPayableExpenses));

                                        binding.coPayRs.setVisibility(View.VISIBLE);
                                        binding.lvCopayment.setVisibility(View.VISIBLE);

                                        binding.tvCoPayment.setText("Co-Payment Deductions");

                                        if (CoPaymentDeductions.equals("-") || CoPaymentDeductions.equals("") || CoPaymentDeductions.isEmpty()) {
                                            binding.tvCoPaymentValue.setText("-");
                                        } else if (CoPaymentDeductions.contains("-")) {
                                       /* tvCoPaymentValue.setText(MessageFormat.format("{0} ",
                                                CoPaymentDeductions));*/ //hide by maddy 02/03/2022
                                            binding.tvCoPaymentValue.setText("-"); // add by maddy 02/03/2022
                                        } else {
                                            binding.tvCoPaymentValue.setText(MessageFormat.format("{0} ", UtilMethods.PriceFormat(CoPaymentDeductions.replace(",", ""))));
                                        }

                                        binding.tvKey4.setText(R.string.deduct_reasons);
                                        binding.tvValue4.setText(strDeductReasons);
                                        break;


                                    case "cashless_paid":
                                    case "cashless_rejected":
                                    case "cashless_closed":
                                    case "cashless_denied":


                                        binding.titleTxt3.setText(R.string.claim_pay);

                                        binding.tvClaimNumber1.setText(R.string.paid_amt);
                                        binding.tvClaimRs.setVisibility(View.VISIBLE);
                                        binding.tvClaimNumberValue1.setText(UtilMethods.PriceFormat(claimPaymentInformation.getAmountPaidToMember()));

                                        binding.tvAmount1.setText(R.string.paid_date);
                                        binding.tvAmountValue1.setText(strcashAuth);

                                        binding.tvKey2.setText(R.string.chq_no);
                                        binding.tvValue1.setText(strChequeNo.equalsIgnoreCase("0") ? "-" : strChequeNo);

                                        binding.tvKey3.setText("Not Payable Expenses");

//                                    intClaimAmt = Integer.parseInt(strPaidAmount.replace(",", ""));
                                        intClaimAmt = Integer.parseInt(strPaidAmount);
                                        strDeductions = String.valueOf(intReportAmt - intClaimAmt);
                                        if (NonPayableExpenses.equals("-") || NonPayableExpenses.equals(""))
                                            binding.tvValue3.setText("-");
                                        else
                                            binding.tvValue3.setText(MessageFormat.format("{0} ", NonPayableExpenses));


                                        binding.coPayRs.setVisibility(View.VISIBLE);
                                        binding.lvCopayment.setVisibility(View.VISIBLE);

                                        binding.tvCoPayment.setText("Co-Payment Deductions");

                                        if (CoPaymentDeductions.equals("-") || CoPaymentDeductions.equals("") || CoPaymentDeductions.isEmpty()) {
                                            binding.tvCoPaymentValue.setText("-");
                                        } else if (CoPaymentDeductions.contains("-")) {
                                       /* tvCoPaymentValue.setText(MessageFormat.format("{0} ",
                                                CoPaymentDeductions));*/ //hide by maddy 02/03/2022
                                            binding.tvCoPaymentValue.setText("-"); // add by maddy 02/03/2022
                                        } else {
                                            binding.tvCoPaymentValue.setText(MessageFormat.format("{0} ", UtilMethods.PriceFormat(CoPaymentDeductions.replace(",", ""))));
                                        }
                                        binding.tvKey4.setText(R.string.deduct_reasons);
                                        binding.tvValue4.setText(strDeductReasons);
                                        //LogMyBenefits.d("strDeductReasons",strDeductReasons);
/*
                                    if (strDeductReasons.length() > 100)
                                        btnShowMore.setVisibility(View.VISIBLE);
                                    else
                                        btnShowMore.setVisibility(View.GONE);
                                    */
                                        break;

                                    case "cashless_outstanding":

                                        if (claimProcessInformation.getOutstandingClaimStatus().toLowerCase().contains("under process")) {

                                            binding.MyClaimCard.setVisibility(View.GONE);
                                            binding.titleTxt3.setText("");
                                            binding.rv3.setVisibility(View.GONE);
                                            binding.progressBar2.setVisibility(View.GONE);


                                            binding.titleTxt3.setText("");

                                            binding.tvClaimNumber1.setText(R.string.paid_amt);
                                            binding.tvClaimRs.setVisibility(View.VISIBLE);
                                            binding.tvClaimNumberValue1.setText(UtilMethods.PriceFormat(strPaidAmount));

                                            binding.tvAmount1.setText(R.string.paid_date);
                                            binding.tvAmountValue1.setText(strcashAuth);

                                            binding.tvKey2.setText(R.string.chq_no);
                                            binding.tvValue1.setText(strChequeNo);

                                            binding.tvKey3.setText("Not Payable Expenses");

//                                    intClaimAmt = Integer.parseInt(strPaidAmount.replace(",", ""));
                                            intClaimAmt = Integer.parseInt(strPaidAmount);
                                            strDeductions = String.valueOf(intReportAmt - intClaimAmt);
                                            if (NonPayableExpenses.equals("-") || NonPayableExpenses.equals(""))
                                                binding.tvValue3.setText("-");
                                            else
                                                binding.tvValue3.setText(MessageFormat.format("{0} ", NonPayableExpenses));


                                            binding.coPayRs.setVisibility(View.VISIBLE);
                                            binding.lvCopayment.setVisibility(View.VISIBLE);

                                            binding.tvCoPayment.setText("Co-Payment Deductions");

                                            if (CoPaymentDeductions.equals("-") || CoPaymentDeductions.equals("") || CoPaymentDeductions.isEmpty()) {
                                                binding.tvCoPaymentValue.setText("-");
                                            } else if (CoPaymentDeductions.contains("-")) {
                                       /* tvCoPaymentValue.setText(MessageFormat.format("{0} ",
                                                CoPaymentDeductions));*/ //hide by maddy 02/03/2022
                                                binding.tvCoPaymentValue.setText("-"); // add by maddy 02/03/2022
                                            } else {
                                                binding.tvCoPaymentValue.setText(MessageFormat.format("{0} ", UtilMethods.PriceFormat(CoPaymentDeductions.replace(",", ""))));
                                            }
                                            binding.tvKey4.setText(R.string.deduct_reasons);
                                            binding.tvValue4.setText(strDeductReasons);
                                            //LogMyBenefits.d("strDeductReasons",strDeductReasons);
/*
                                    if (strDeductReasons.length() > 100)
                                        btnShowMore.setVisibility(View.VISIBLE);
                                    else
                                        btnShowMore.setVisibility(View.GONE);
                                    */
                                        } else {
                                            binding.MyClaimCard.setVisibility(View.GONE);
                                            binding.titleTxt3.setText("");
                                            binding.rv3.setVisibility(View.GONE);
                                            binding.progressBar2.setVisibility(View.GONE);
                                        }
                                        break;

                                    case "reimbursement_closed":

                                        binding.titleTxt3.setText(R.string.claim_closed);
                                        binding.tvClaimNumber1.setText(R.string.claim_close_reason);
                                        binding.img3.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.close_white));
                                        binding.rv3.setBackgroundResource(R.drawable.claim_end);

                                        binding.tvClaimNumberValue1.setText(claimRejectedOn);

                                        binding.tvAmount1.setText(R.string.claim_close_on);
                                        binding.tvAmountValue1.setText(ClaimClosureReasons);

                                        binding.lv3.setVisibility(View.GONE);
                                        binding.lv4.setVisibility(View.GONE);
                                        binding.lv5.setVisibility(View.GONE);
                                        break;

                                    case "reimbursement_outstanding":
                                        if (!claimDefStatus.equals("Deficient")) {

                                            ConstraintLayout.LayoutParams conlay = new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

                                            conlay.bottomToBottom = R.id.rv2;
                                            progressAnim = 3000;
                                            binding.MyClaimCard.setVisibility(View.GONE);
                                            binding.rv3.setVisibility(View.GONE);
                                            binding.progressBar2.setVisibility(View.GONE);
                                            binding.titleTxt3.setVisibility(View.GONE);


                                        } else {

                                            binding.titleTxt3.setText(R.string.claim_deficient);
                                            binding.tvKey2.setVisibility(View.VISIBLE);
                                            binding.tvValue1.setVisibility(View.VISIBLE);
                                            binding.img3.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.outstanding));

                                            binding.tvKey2.setText(R.string.def_detail);
                                            binding.tvValue1.setText(claimDeficient);
                                            binding.rv3.setBackgroundResource(R.drawable.claim_end);
                                            binding.progressBar2.setVisibility(View.VISIBLE);


                                            binding.defLayout.setVisibility(View.VISIBLE);

                                            binding.def1Value.setText(claimFileInformation.getFirstDeficiencyLetterDate());
                                            binding.def2Value.setText(claimFileInformation.getSecondDeficiencyLetterDate());
                                            binding.def3Value.setText(claimFileInformation.getDeficienciesRetrievalDate());

                                            binding.tvAmount111.setText("Not Payable Expenses");
                                            binding.tvAmountValue11.setText("₹ " + UtilMethods.PriceFormat(claimChargesInformation.getNonPayableExpenses()));



                                            binding.tvCoPayment.setText(R.string.def_raised_on);
                                            if (coPaymentDeductions.isEmpty() || coPaymentDeductions.equals(null) || coPaymentDeductions.equals("0")) {
                                                binding.tvCoPaymentValue.setText("-");
                                            } else {
                                                binding.tvCoPaymentValue.setText(coPaymentDeductions);
                                            }


                                            binding.llLastCard11.setVisibility(View.GONE);
                                            binding.lv4.setVisibility(View.GONE);
                                            binding.lv5.setVisibility(View.GONE);
                                            binding.lv8.setVisibility(View.GONE);
                                            binding.lvCopayment.setVisibility(View.GONE);

                                        }
                                        break;
                                }

                            } else {
                                Toast.makeText(requireActivity(), "Something went Wrong!", Toast.LENGTH_SHORT).show();
                            }

                        });


            } catch (Exception e) {
                e.printStackTrace();
                binding.errorLayout2.setVisibility(View.VISIBLE);

            }

        }

    }


}