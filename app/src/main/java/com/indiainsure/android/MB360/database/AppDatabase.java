package com.indiainsure.android.MB360.database;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;

import com.indiainsure.android.MB360.BuildConfig;
import com.indiainsure.android.MB360.database.Dao.ClaimProcedureDao;
import com.indiainsure.android.MB360.database.Dao.ClaimsDao;
import com.indiainsure.android.MB360.database.Dao.CoverageDao;
import com.indiainsure.android.MB360.database.Dao.EnrollmentWindowCountDao;
import com.indiainsure.android.MB360.database.Dao.EscalationsDao;
import com.indiainsure.android.MB360.database.Dao.FaqDao;
import com.indiainsure.android.MB360.database.Dao.LoadSessionDao;
import com.indiainsure.android.MB360.database.Dao.MyClaimsDao;
import com.indiainsure.android.MB360.database.Dao.MyQueryDao;
import com.indiainsure.android.MB360.database.Dao.PolicyFeatureDao;
import com.indiainsure.android.MB360.database.Dao.ProfileDao;
import com.indiainsure.android.MB360.database.Dao.ProviderNetworkDao;
import com.indiainsure.android.MB360.database.Dao.ServiceNamesDao;
import com.indiainsure.android.MB360.database.Dao.UtilitiesDao;
import com.indiainsure.android.MB360.insurance.FAQ.repository.responseclass.FaqResponse;
import com.indiainsure.android.MB360.insurance.adminsetting.responseclass.AdminSettingResponse;
import com.indiainsure.android.MB360.insurance.claims.repository.responseclass.ClaimsResponse;
import com.indiainsure.android.MB360.insurance.claims.repository.responseclass.LoadPersonsIntimationResponse;
import com.indiainsure.android.MB360.insurance.claimsprocedure.repository.responseclass.ClaimProcedureEmergencyContactResponse;
import com.indiainsure.android.MB360.insurance.claimsprocedure.repository.responseclass.ClaimProcedureImageResponse;
import com.indiainsure.android.MB360.insurance.claimsprocedure.repository.responseclass.ClaimProcedureLayoutInstructionInfo;
import com.indiainsure.android.MB360.insurance.claimsprocedure.repository.responseclass.ClaimProcedureTextResponse;
import com.indiainsure.android.MB360.insurance.claimsprocedure.repository.responseclass.ClaimsProcedureLayoutInfoResponse;
import com.indiainsure.android.MB360.insurance.coverages.repository.responseclass.CoverageDetailsResponse;
import com.indiainsure.android.MB360.insurance.coverages.repository.responseclass.CoverageResponse;
import com.indiainsure.android.MB360.insurance.escalations.repository.responseclass.EscalationsResponse;
import com.indiainsure.android.MB360.insurance.hospitalnetwork.reponseclass.DocumentElementCount;
import com.indiainsure.android.MB360.insurance.hospitalnetwork.reponseclass.Hospitals;
import com.indiainsure.android.MB360.insurance.hospitalnetwork.responseclassV1.HospitalInformation;
import com.indiainsure.android.MB360.insurance.myclaims.responseclass.DocumentElement;
import com.indiainsure.android.MB360.insurance.myclaims.responseclass.claimsdetails.ClaimsDetails;
import com.indiainsure.android.MB360.insurance.policyfeatures.repository.responseclass.PolicyFeaturesResponseOffline;
import com.indiainsure.android.MB360.insurance.profile.response.ProfileResponse;
import com.indiainsure.android.MB360.insurance.queries.responseclass.QueryDetails;
import com.indiainsure.android.MB360.insurance.queries.responseclass.QueryResponse;
import com.indiainsure.android.MB360.insurance.repository.responseclass.LoadSessionResponse;
import com.indiainsure.android.MB360.insurance.servicenames.responseclass.ServiceNamesResponse;
import com.indiainsure.android.MB360.insurance.utilities.repository.responseclass.UtilitiesResponse;

import net.sqlcipher.database.SQLiteDatabase;
import net.sqlcipher.database.SupportFactory;

@Database(entities = {FaqResponse.class,
        EscalationsResponse.class,
        UtilitiesResponse.class,
        PolicyFeaturesResponseOffline.class,
        ProfileResponse.class,
        QueryResponse.class,
        QueryDetails.class,
        ClaimsResponse.class,
        ClaimsProcedureLayoutInfoResponse.class,
        ClaimProcedureTextResponse.class,
        LoadPersonsIntimationResponse.class,
        ClaimProcedureLayoutInstructionInfo.class,
        ClaimProcedureEmergencyContactResponse.class,
        ClaimProcedureImageResponse.class,
        CoverageResponse.class,
        CoverageDetailsResponse.class,
        DocumentElement.class,
        ClaimsDetails.class,
        Hospitals.class,
        HospitalInformation.class,
        DocumentElementCount.class,
        LoadSessionResponse.class,
        ServiceNamesResponse.class,
        AdminSettingResponse.class}, version = 8, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    private static final String DATABASE_NAME = "IndiaInsure";

    public abstract FaqDao faqDao();

    public abstract EscalationsDao escalationsDao();

    public abstract UtilitiesDao utilitiesDao();

    public abstract PolicyFeatureDao policyFeatureDao();

    public abstract ProfileDao profileDao();

    public abstract MyQueryDao myQueryDao();

    public abstract ClaimsDao claimsDao();

    public abstract ClaimProcedureDao claimProcedureLayoutDao();

    public abstract CoverageDao coverageDao();

    public abstract MyClaimsDao documentElementDao();

    public abstract ProviderNetworkDao documentElementCountDao();

    public abstract LoadSessionDao loadSessionDao();

    public abstract ServiceNamesDao serviceNameDao();

    public abstract EnrollmentWindowCountDao enrollmentWindowCountDao();

    private static volatile AppDatabase INSTANCE;


    public static AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    //encryption
                    SupportFactory supportFactory = new SupportFactory(SQLiteDatabase.getBytes(BuildConfig.DATABASE_PASS_PHRASE.toCharArray()));

                    INSTANCE = Room.databaseBuilder(context, AppDatabase.class,
                                    DATABASE_NAME)
                            .fallbackToDestructiveMigration()
                            .allowMainThreadQueries()
                            /*.openHelperFactory(supportFactory)*/
                            .build();


                }
            }
        }
        return INSTANCE;
    }


    @Override
    public void clearAllTables() {

    }

    @NonNull
    @Override
    protected InvalidationTracker createInvalidationTracker() {
        return null;
    }

    @NonNull
    @Override
    protected SupportSQLiteOpenHelper createOpenHelper(@NonNull DatabaseConfiguration databaseConfiguration) {
        return null;
    }
}
