package com.indiainsure.android.MB360.insurance.queries.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.fragment.NavHostFragment;

import com.indiainsure.android.MB360.R;
import com.indiainsure.android.MB360.databinding.FragmentQueryFaqBinding;
import com.indiainsure.android.MB360.insurance.queries.responseclass.QueryFaqModel;

import java.util.ArrayList;
import java.util.List;

public class QueryFaqFragment extends Fragment {

    FragmentQueryFaqBinding binding;
    View view;
    NavController navController;
    OnQuerySubmittedSuccessfully onQuerySubmittedSuccessfully;
    List<QueryFaqModel> faqQueryList = new ArrayList<>();
    QueriesFaqAdapter queriesFaqAdapter;


    public QueryFaqFragment(OnQuerySubmittedSuccessfully onQuerySubmittedSuccessfully) {
        // Required empty public constructor
        this.onQuerySubmittedSuccessfully = onQuerySubmittedSuccessfully;
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
        binding = FragmentQueryFaqBinding.inflate(inflater, container, false);
        view = binding.getRoot();

        //To Navigate
        NavHostFragment navHostFragment = (NavHostFragment) requireActivity().getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        navController = navHostFragment.getNavController();


        setUpSpinner();

        binding.newQueryFaqFab.setOnClickListener(v -> {
            NavDirections action = MyQueriesFragmentDirections.actionMyQueriesFragmentToNewQueryFragment().setSpinnerItemIndex(binding.categorySpinner.getSelectedItemPosition());
            navController.navigate(action);
        });


        binding.categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                setupRecyclerView(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        return view;
    }

    private void setupRecyclerView(int position) {


        switch (position) {
            case 0:
                faqQueryList = new ArrayList<>();

                break;
            case 1:
                //e-cards;
                faqQueryList = new ArrayList<>();
                faqQueryList.add(new QueryFaqModel("Where is my E-Card?", "Your E-Card is available on the Dashboard. In the link available at the bottom of the page,next to Contact."));
                faqQueryList.add(new QueryFaqModel("Do I get a physical card?", "Yes. If your Employer insists on physical card then the same is couriered to your Corporate Address. Alternatively you can use a print-out of the E-Card."));
                faqQueryList.add(new QueryFaqModel("Why is my E-Card not visible?", "This happens when either your Enrollment Window is still open or your data is under process at the Insurance Company."));
                faqQueryList.add(new QueryFaqModel("In how many days do I get my card?", "E-Cards are visible within 7-10 Working Days on IIRM IBP from receipt of your Enrollment Details through HR or your Window Period Closure. The physical cards are dispatched to your HR, within 10-15 days from the receipt of enrollment details."));
                faqQueryList.add(new QueryFaqModel("Do I get individual or family E-Card?", "Yes. You get individual e-card for all the members who are enrolled in the policy."));
                faqQueryList.add(new QueryFaqModel("I can't see my family members E-Card?", "Either you have not provided your family members details to your HR for enrollment in the policy OR you have not added them on IIRM IBP during Enrollment Window period."));
                faqQueryList.add(new QueryFaqModel("There is a correction in my E-Card", "For any correction in your E-Card, you are requested to provide correct details to your Account manager/HR along with necessary copy of identity proof (e.g. Copy of Aadhaar Card or Pan Card or Driving License or Election Card or Passport or Marriage Certificate in case of newly married spouse)."));
                faqQueryList.add(new QueryFaqModel("How do I use my E-Card to make a claim?", "Your E-Card is an identification card only, and can be used exclusively for Cashless claims. Claimant will have to produce a government authorised identity proof along with the E-Card at the Admission Counter / TPA Desk at Network Hospital during cashless claims process. For Reimbursement claims, attach a copy of the E-Card along with the Claim Documents during submission."));
                faqQueryList.add(new QueryFaqModel("Can I use my E-Card as an Identity proof?", "No. Your E-Card cannot be used as an Identity proof because it does not carry any photo on it. Hence a Government Authorised photo identity proof should be produced along with your E-Card (viz. Aadhaar Card, Pan Card, Driving License, Election Card, Passport Copy etc.)"));
                faqQueryList.add(new QueryFaqModel("What is the validity of my E-Card?", "The validity of your E-Card is till the expiry of policy OR your date of leaving the organisation, whichever is earlier."));

                break;
            case 2:
                //dependants
                faqQueryList = new ArrayList<>();
                String dependantString = "Step 1: Login to IIRM IBP using login credentials mentioned in the Welcome Mail.\n" + "\n" + "Step 2:  Employee's are required to complete enrollment of dependants and opting for additional benefits within the Window Period mentioned in the Welcome Mail. Employee can Add/Modify/Delete Dependants as per policy terms and conditions.\n" + "\n" + "Step 3: Click on the Confirm Enrollment button after successfully updating dependant details.";

                faqQueryList.add(new QueryFaqModel("How do I add my dependants in the policy?", dependantString));
                faqQueryList.add(new QueryFaqModel("How do I remove my dependants from the policy?", "To remove existing dependants from the Policy, insured has submit details of dependants to be removed to the HR or Account Manager stating reason for deletion. Dependants will be deleted as per policy terms and conditions."));
                faqQueryList.add(new QueryFaqModel("My dependant details are incorrect.", "For any correction in your dependant details, you are requested to provide correct details alongwith necessary copy of identity proof (e.g.  Copy of Aadhaar Card or Pan Card or Driving License or Election Card or Passport or Marriage Certificate in case of newly married)."));
                faqQueryList.add(new QueryFaqModel("My dependant details are not visible/showing.", "Dependant details are updated in IIRM IBP as per data received from HR as per Policy Terms & Conditions. If you find discrepancies in the data request you to coordinate with the HR. If you are unable to add your dependants during your enrollment window, request you to get in touch with your Level 1 Account manager mentioned in Contact Details."));
                faqQueryList.add(new QueryFaqModel("I can't add my parents/in-laws.", "Dependant details are updated in IIRM IBP as per data received from HR as per Policy Terms & Conditions. If you find descripencies in the data request you to coordinate with the HR. If you are still unable to add your dependants during your enrollment window, request you to get in touch with your Level 1 Account manager mentioned in Contact Details. Please note mid-term addition of Parents/In-Laws is not permitted."));
                faqQueryList.add(new QueryFaqModel("I can't add my spouse.", "Mid-term inclusions are permitted only in the case of spouse on account of marriage. Intimation of the same to be given to HR Team within 15 days of the event, simultaneously the same can be updated on IIRM IBP if enrollment option is provided."));
                faqQueryList.add(new QueryFaqModel("I can't add my children.", "Mid-term inclusions are permitted only in the case of a New Born baby. Intimation of the same to be given to HR Team within 15 days of the event, simultaneously the same can be updated on IIRM IBP if enrollment option is provided."));
                faqQueryList.add(new QueryFaqModel("I got married recently. How do I add my spouse?", "Intimation of the same to be given to HR Team within 15 days of your date of marriage or the same can be updated on IIRM IBP if enrollment option is provided."));
                faqQueryList.add(new QueryFaqModel("How do I add my new born baby?", "Intimation of the same to be given to HR Team within 15 days of your date of birth of your child or the same can be updated on IIRM IBP if enrollment option is provided."));


                break;
            case 3:
                //policy features
                faqQueryList = new ArrayList<>();
                faqQueryList.add(new QueryFaqModel("What are my policy features and coverage details?", "Your Policy Terms and Coverage details are available in the Policy Features and My Coverages links in your Employee Login on India Insure app."));
                faqQueryList.add(new QueryFaqModel("What expenses are not covered under my policy?", "List of Expenses not covered in your policy are available in the Utilities tab under the heading Non-Payable expenses."));
                faqQueryList.add(new QueryFaqModel("What are Daycare procedures?", "Due to advancement in medical technologies, certain treatments do not require 24 hours hospitalizations. These are called Day-Care Procedures. Comprehensive list of the same is available in the Utilities tab under the heading Day-Care Procedures."));
                faqQueryList.add(new QueryFaqModel("What are co-payment deductions?", "Certain ailments in your policy might carry a percentage of expenses that have to be borne by the claimant incase of a claim. These are called co-payment deductions. List of ailments which carry co-payments if any are mentioned in your Policy Features."));

                break;
            case 4:
                //Enrollment Process
                faqQueryList = new ArrayList<>();
                String enrollmentDetailString = "There is two types of Enrollment Process viz. 1]Online Enrollment & 2] Offline Enrollment\n" + "\n" + "ONLINE ENROLLMENT\n" + "\n" + "Step 1:  Employee's Enrollment Data provided by the HR is uploaded in India Insure.\n" + "\n" + "Step 2:  Welcome Emailers are sent to all Employees on their registered/official email Id's explaining Login and Data updation process.\n" + "\n" + "Step 3:  Employee's are required to complete enrollment of dependants and opting for additional benefits within the Window Period mentioned in the Welcome Mail.\n" + "\n" + "Step 4: On Closure of the Window Period employee & dependant data is extracted from the portal and sent to Insurance company for the policy preparation / endorsments.\n" + "\n" + "Step 5: E-Cards will available in the portal within 7 days from enrollment completion.\n" + "\n" + "OFFLINE ENROLLMENT\n" + "\n" + "Step 1:  Employee's Enrollment Data provided by the HR is uploaded in PINC portal.\n" + "\n" + "Step 2: Welcome Emailers are sent to all Employees on their registered/official email Id's explaining Login process.\n" + "\n" + "Step 3: The data is sent to Insurance company for the policy preparation / endorsements.\n" + "\n" + "Step 4: E-cards will available in the portal within 7 days from receipt of policy / endorsements from the insurance company.";

                faqQueryList.add(new QueryFaqModel("What is enrollment?(online-offline)", enrollmentDetailString));
                faqQueryList.add(new QueryFaqModel("What is window period?", "The Window Period is a duration of time given to the Employee to login to IIRM IBP and add/modify/delete their dependant information, as per policy terms & conditions. This maximum period alloted for this is 15 days but it may vary for each Employer Group. Refer the welcome mail received from India Insure Risk Management & Insurance Broking pvt ltd for exact Window Period."));
                faqQueryList.add(new QueryFaqModel("My window period is over.", "The Window Period is a duration of time given to the Employee to login to IIRM IBP and add/modify/delete  their dependant information, as per policy terms & conditions. This maximum period alloted for this is 15 days but it may  vary for each Employer Group. Refer the welcome mail received from India Insure Risk Management & Insurance Broking pvt ltd for your exact Window Period. Your  Window Period closes after this stipulated time and you will not be able enroll dependants for Insurance Cover. In such an event contact your HR for futher guidance."));
                faqQueryList.add(new QueryFaqModel("Why can't I add my dependants?", "It means your  Window Period has closed and you have not enrolled dependants in the stipulated time. In such an event contact your HR for futher guidance."));
                faqQueryList.add(new QueryFaqModel("What are life events?", "Life Events refers to 'Mid-term inclusions of new born baby and spouse on account of marriage.' You are required to update the same on the IIRM IBP within 15 days of the event and also intimate the HR about the same."));

                break;
            case 5:
                faqQueryList = new ArrayList<>();
                String cashlessProcessString = "In order to avail the cashless claim facility, the insured has to be treated in a Network hospital. List of Network Hospitals can be found of India Insure app under the Network Hosptials link. By providing the details of the health insurance policy and presenting the e-card or other physical proof of the health insurance taken in the name of the policyholder, he or she can avail cashless hospitalization and treatment, if the illness/ injury is covered under the policy.\n" + "\n" + "Claimant needs to contact the TPA cell or cashless counter of the hospital and obtain the Cashless Hospitalization Form, fill it up and get it duly signed by the Hospital doctor / authority with estimated cost and details of line of treatment to be given. The responsibility of forwarding the cashless request to TPA lies with the Hospital. Informatively, the hospital would Fax or email the same to TPA for approval of pre- authorization.\n" + "\n" + "In case of planned Hospitalization, cashless request may be submitted by the hospital at least 24 hrs in advance.\n" + "\n" + "In case of emergency hospitalization, the patient may be admitted in the hospital and cashless hospitalization request may be sent within 24 Hours of such admission. The TPA is obliged to issue the pre-authorization within 24 hours in case of planned hospitalization and 2 hours in case of emergency hospitalizations. The authorized amount may vary from 80% to 100% of the estimate subject to overall entitlement. In the event of furnishing of insufficient information related to treatment by hospital, Cashless authorization may be delayed.\n" + "\n" + "Please refer the Claim Procedures Link on India Insure Risk Management & Insurance Broking pvt ltd app for detailed information.";

                String processString = "You may notice that even with the cashless service, there are some expenses that you will have to bear as  these expenses are not covered in your Health Insurance Policy. These expenses are listed below:\n" + "\n" + "a.] Registration or Admission fee.\n" + "\n" + "b.] Visitors / Attendantís fee.\n" + "\n" + "c.] Charges for diet.\n" + "\n" + "d.] Ambulance charges.\n" + "\n" + "e.] Toiletries.\n" + "\n" + "f.] Document charges.\n" + "\n" + "g.] Service charge.\n" + "\n" + "h.] Expenses for diapers, oxygen masks, nebulizers which are considered under medicines category and Co-payment deductions.";

                String deniedString = "A cashless request is denied by the TPA, based on various reasons, some of the common reasons are as below:\n" + "\n" + "a.] The ailment / disease for which claimant is hospitalised is not covered under your insurance policy.\n" + "\n" + "b.] Your Sum Insured is exhausted, due to settlement of previous claims.\n" + "\n" + "c.] The current hospitalization is purely for investigations and the TPA is not able to justify the same with the information provided by the network hospital.\n" + "\n" + "d.] Denial of \"Cashless Service\" is not denial of treatment. You can continue with the treatment, pay for the services to the hospital, and later send the claim for processing on reimbursement basis.\n";

                //How to make Cashless
                faqQueryList.add(new QueryFaqModel("How to make a cashless claim?", cashlessProcessString));
                faqQueryList.add(new QueryFaqModel("Which hospitals can I avail cashless facility?", "Cashless hospitalizations facility can be availed in any of the Network Hospitals empanelled with your TPA. List of Network Hospitals can be found of IIRM IBP under the Network Hosptials link."));
                faqQueryList.add(new QueryFaqModel("How long does it take to process a cashless claim?", "The TPA sanctions the initial cashless amount within 4-5 hours of cashless request from the hospital."));
                faqQueryList.add(new QueryFaqModel("Do I need to pay security deposit at hospital?", "In India, hospitals may charge deposits from customers before they are admitted. While in normal circumstances, customers are not required to pay any cash, if they have taken a cashless policy, it has been observed that some hospitals ask such customers to pay a flat deposit. This deposit amount charged by hospitals depends on the type of surgery and the choice of hospitalization made. This deposit is generally taken for expenses not covered in the policy."));
                faqQueryList.add(new QueryFaqModel("Do I need to pay anything at discharge?", processString));
                faqQueryList.add(new QueryFaqModel("How do I know the status of cashless claim?", "On receipt of the cashless request by the TPA from the Hospital and subsequent processing of the same, the Employee will receive automated SMS and email of the same on their registered mobile number and email id from India Insure at all stages of claim process and settlement. Real time status of the claim is also available on India Insure Risk Management & Insurance Broking pvt ltd under the Claim Status link."));
                faqQueryList.add(new QueryFaqModel("Who do I contact in case of emergency?", "Emergency Contact details are mentioned under the link Contact Details in your India Insure login."));
                faqQueryList.add(new QueryFaqModel("My cashless is denied.", deniedString));

                break;
            case 6:
                faqQueryList = new ArrayList<>();
                String claimProcedure = "In the event that an insured is hospitalized in any hospital / nursing home (within India) as defined in the policy and pays the treatment expenses at the time of discharge, he / she needs to file a claim with the TPA for the amount due under the policy. The following documents in original should be submitted to the TPA within seven days from the date of Discharge from the Hospital:\n" + "\n" + "a.] Claim Form duly filled and signed by the claimant\n" + "\n" + "b.] Discharge Certificate from the hospital\n" + "\n" + "c.] All documents pertaining to the illness starting from the date it was first detected i.e. Doctor's consultation reports/history\n" + "\n" + "d.] Bills, Receipts, Cash Memos from hospital supported by proper prescription\n" + "\n" + "e.] Receipt and diagnostic test report supported by a note from the attending medical practitioner/surgeon justifying such diagnostics.\n" + "\n" + "f.] Surgeon's certificate stating the nature of the operation performed and surgeon's bill and receipt.\n" + "\n" + "g.]Attending doctor's / consultant's / specialist's / anesthetist's bill and receipt, and certificate regarding diagnosis.\n" + "\n" + "h.] Details of previous policies if the details are not already with TPA or any other information needed by the TPA for considering the claim.\n" + "\n" + "Please refer the Claim Procedures Link on IIRM IBP for detailed information.";

                String claimClosedString = "Your claim is closed because of non compliance of required claim documents within the stipulated time limit given by the TPA / Insurance Company.\n" + "\n" + "ï If you have the required documents, you can submit the same to the TPA. TPA will represent the claim Insurance Company for the \"Reopening Approval of the Claim\".\n" + "\n" + "ï The Insurance Company will review the claim on its merits and decide on the further course of action. If the claim is deemed fit to processing, the Insurance Company instruct the TPA to process the claim as per th policy terms.\n";
                //How to make reimbursement
                faqQueryList.add(new QueryFaqModel("How to file reimbursement claim?", claimProcedure));
                faqQueryList.add(new QueryFaqModel("What is the timeline for reimbursement claim submission?", "Intimation of Reimbursement Claim has to be made within 72 hours of admission (intimation can also be made in advance in case of Planned Hospitalisation).Post Discharge, duly filled claim form along with the claim documents should be submitted within 15 days from date of discharge."));
                faqQueryList.add(new QueryFaqModel("What is the timeline for reimbursement claim settlement?", "15 working days from date of submission of complete documents."));
                faqQueryList.add(new QueryFaqModel("What is document deficiency?", "On Submission of your claim file is any documents required for processing the claim are missing, they are termed as Deficient Documents. In such a case, the claimant is required to submit these documents immediately to avoid delay in claim settlement."));
                faqQueryList.add(new QueryFaqModel("How document deficiency is intimated?", "Document deficiency is intimated through E-mail & SMS on claimants official email id & mobile number, within 7 working days from the receipt of claim documents by the TPA"));
                faqQueryList.add(new QueryFaqModel("What is a timeline to clear document deficiency?", "Deficient documents should be submitted within 7-10 working days from the date of receipt of email or sms by claimant stating document deficiency."));
                faqQueryList.add(new QueryFaqModel("What are the non-payable expenses?", "Your health insurance pays for reasonable and necessary medical expenditure as defined in the Policy. There are several items billed during hospitalization by some hospitals but not admissible under an insurance contract. These items will not be payable and expenditure towards such items will have to be borne by the claimant. List of Expenses not covered in your policy are available in the Utilities tab under the heading Non-Payable expenses."));
                faqQueryList.add(new QueryFaqModel("How do I know the status of reimbursement claim?", "On receipt of the Claim File by the TPA and subsequent processing of the same, the Employee will receive automated SMS and email of the same on their registered mobile number and email id from India Insure at all stages of claim process and settlement. Real time status of the claim is also available on India Insure Risk Management & Insurance Broking pvt ltd under the Claim Status link."));
                faqQueryList.add(new QueryFaqModel("My claim is partially paid", "If your claim is partially paid, that means the claim documents submitted by you are incomplete OR not submitted by you OR some documents submitted does not pertain to current hospitalization."));
                faqQueryList.add(new QueryFaqModel("What to do if my claim is rejected?My claim is closed. What to do now?", "Your claim can be rejected, if it is not covered under the Policy terms or you have not submitted complete claim documents . In case, you are not satisfied by the reasons for rejection, you can represent the same to the TPA / Insurance Company with a copy to your HR and India Insure Operation Team, within 15 days of such rejection."));
                faqQueryList.add(new QueryFaqModel("My claim is closed. What to do now?", "If you do not receive a response to your representation or if you are not satisfied with the response, you may write to Insurance Company's Grievance Cell and/or you may also call the Call Centre of Insurance Company.You also have the right to represent your case to the Insurance Ombudsman. The contact details of the office of the Insurance Ombudsman can be obtained from www.irda.gov.in"));

                break;
            case 7:
                //claim intimation
                faqQueryList = new ArrayList<>();
                faqQueryList.add(new QueryFaqModel("How to intimate claim?", "Claim can be intimated by filling up the Claim Intimation form under the Intimate Claim link on IIRM IBP."));
                faqQueryList.add(new QueryFaqModel("What is the claim intimation clause?", "Claim Intimation simply means intimating the TPA or the Insurance Company that a claim is going to be made in the near future. Some of the policies indicate a time frame of 24 hours or 7 days from the date of admission, most of the policies require that intimation has to be lodged immediately on admission. Non-compliance to this may make your claim inadmissible."));
                faqQueryList.add(new QueryFaqModel("What is claim intimation?", "Claim Intimation means informing about any hospitalization of insured person to the respective TPA. The same can be intimated to the TPA in writing by letter, e-mail, fax or on telephonic call providing all relevant information relating to claim which includes plan of treatment, health id card number, policy number, duration etc. within the prescribed time limit."));
                faqQueryList.add(new QueryFaqModel("Why claim intimation is necessary?", "Claim Intimation is necessary because timely claim intimation helps in smooth & faster disposal of claim. It is important from an Insurance Company perspective, as they have to record the loss of any claim in advance to maintain their book of accounts."));


                break;
            case 8:
                //Hospital related
                faqQueryList = new ArrayList<>();
                faqQueryList.add(new QueryFaqModel("What is pre/post hospitalization?", "Pre Hospitalization: Relevant medical expenses related to the treatment of the disease incurred before hospitalization for a period of 30 (THIRTY) days prior to the date of Hospitalization are payable.Post Hospitalization: Relevant medical expenses related to the treatment of the disease incurred after Discharge from the Hospital for a period of 60 (SIXTY) days after the date of discharge are payable."));
                faqQueryList.add(new QueryFaqModel("What expenses are covered in pre/post hospitalization?", "Pre-Hospitalization Expenses include various charges related to medical tests before an individual gets hospitalized. Doctors/physicians conduct a slew of tests to accurately diagnose the medical condition of a patient before prescribing treatment. It is important to note that the number of days which are covered, tends to vary depending upon the type of health insurance company. However, in most cases, charges incurred by an individual 30 days prior to his or her admission to any hospital fall within the ambit of pre-hospitalization expenses. For instance, several tests such as blood test, urine test and X-ray among others are categorised as pre-hospitalization expenses.Post-Hospitalization Expenses include all expenses or charges incurred by an individual after he or she is hospitalized. For instance, the consulting physician may prescribe certain tests to ascertain the progress or recovery of a patient. It is important to note that the number of days which are covered tends to vary depending upon the type of health insurance provider. However, in most cases, charges incurred by an individual for 60 days from the discharge date comes under the ambit of post hospitalization expenses. Expenses related to various therapies, namely, acupuncture and naturopathy are not included by insurance providers in the category of post hospitalization expenses. However, diagnostic charges, consulting fees and medicine costs are covered."));
                faqQueryList.add(new QueryFaqModel("What are the timelines for pre/post hospitalization claim submission?", "The timeline to submit your pre/post hospitalization claim is 15 days from the date of discharge & 7 days from the date of completion of your treatment (Date if Discharge) for post hospitalization claim."));

                break;
            case 9:
                //contact Details
                faqQueryList = new ArrayList<>();
                faqQueryList.add(new QueryFaqModel("Who do I contact for my health insurance queries?", "Please refer the Contact Details link in IIRM IBP for your Account Manager details. You can also raise a query ticket in the Employee Queries module. Your query will be resolved within 24 hrs of raising the query."));
                faqQueryList.add(new QueryFaqModel("What if my query is not answered?", "All queries raised on India Insure Risk Management & Insurance Broking pvt ltd portal are answered. IIRM IBP has a TAT of 24 hrs for query resolution."));
                faqQueryList.add(new QueryFaqModel("What if my query resolution is not satisfactory?", "If you find the query resolution unsatisfactory, you have an option to continue the query discussion till resolution or raise a new Query ticket."));

                break;
            case 10:
                faqQueryList = new ArrayList<>();
                String claimStatusString = "Yes - Insured  will receive automated SMS and email of claim on their registered mobile number and email id from India Insure at all stages of claim process and settlement. The stages of claim status are:\n" + "\n" + "                    i.) Claim Intimation\n" + "\n" + "                    ii.) Claim Acknowledgment Status\n" + "\n" + "                    iii.) Claim Deficiency Status\n" + "\n" + "                    iv.) Claim Deficiency Reminder\n" + "\n" + "                    v.) Claim Closure Status\n" + "\n" + "                    vi.) Claim Rejected Status\n" + "\n" + "                    vii.) Claim Settlement Letter";

                //claim tracking
                faqQueryList.add(new QueryFaqModel("How do I track my claim?", "Real time status of the claim is available of India Insure under the Claim Status link. Insured  will receive automated SMS and email of the same on their registered mobile number and email id from India Insure Risk Management & Insurance Broking pvt ltd at all stages of claim process and settlement."));
                faqQueryList.add(new QueryFaqModel("Will I be intimated about my updated claim status?", claimStatusString));

                break;
            case 11:
                //others
                faqQueryList = new ArrayList<>();
                NavDirections action = MyQueriesFragmentDirections.actionMyQueriesFragmentToNewQueryFragment().setSpinnerItemIndex(binding.categorySpinner.getSelectedItemPosition());
                navController.navigate(action);
                binding.categorySpinner.setSelection(0);

            default:
                break;
        }

        queriesFaqAdapter = new QueriesFaqAdapter(faqQueryList, requireContext());
        binding.faqQueryCycle.setAdapter(queriesFaqAdapter);


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
}