#noinspection ShrinkerUnresolvedReference
-keepattributes JavascriptInterface
-keepattributes *Annotation*
-optimizations !method/inlining/*
-keepclasseswithmembers class * {
  public void onPayment*(...);
}


##missing
# Please add these rules to your existing keep rules in order to suppress warnings.
# This is generated automatically by the Android Gradle plugin.
-dontwarn com.caverock.androidsvg.R$styleable
-dontwarn org.apache.commons.math3.stat.descriptive.rank.Percentile
-dontwarn org.bouncycastle.jsse.BCSSLParameters
-dontwarn org.bouncycastle.jsse.BCSSLSocket
-dontwarn org.bouncycastle.jsse.provider.BouncyCastleJsseProvider
-dontwarn org.conscrypt.Conscrypt$Version
-dontwarn org.conscrypt.Conscrypt
-dontwarn org.conscrypt.ConscryptHostnameVerifier
-dontwarn org.jtransforms.fft.DoubleFFT_1D
-dontwarn org.openjsse.javax.net.ssl.SSLParameters
-dontwarn org.openjsse.javax.net.ssl.SSLSocket
-dontwarn org.openjsse.net.ssl.OpenJSSE
-dontwarn proguard.annotation.Keep
-dontwarn proguard.annotation.KeepClassMembers

-optimizationpasses 5
-dontusemixedcaseclassnames
-dontskipnonpubliclibraryclasses
-dontskipnonpubliclibraryclassmembers
-dontpreverify
-verbose
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*,!code/allocation/variable

#-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
# -keep public class * extends android.app.Fragment
#-keep public class * extends androidx.lifecycle.** {
 #                                                   *;
 #                                               }

-keepclasseswithmembers class com.indiainsure.android.MB360.onboarding.authentication.responseclass.** {*;}
-keepclasseswithmembers class com.indiainsure.android.MB360.insurance.repository.responseclass.** {*;}
-keepclasseswithmembers class com.indiainsure.android.MB360.onboarding.validation.responseclass.** {*;}
-keepclasseswithmembers class com.indiainsure.android.MB360.insurance.enrollment.repository.responseclass.** {*;}
-keepclasseswithmembers class com.indiainsure.android.MB360.insurance.claims.repository.responseclass.** {*;}
-keepclasseswithmembers class com.indiainsure.android.MB360.insurance.repository.responseclass.** {*;}
-keepclasseswithmembers class com.indiainsure.android.MB360.insurance.adminsetting.responseclass.** {*;}
-keepclasseswithmembers class com.indiainsure.android.MB360.insurance.claims.repository.responseclass.** {*;}
-keepclasseswithmembers class com.indiainsure.android.MB360.insurance.claims.repository.requestclass.** {*;}
-keepclasseswithmembers class com.indiainsure.android.MB360.insurance.adminsetting.responseclass.** {*;}
-keepclasseswithmembers class com.indiainsure.android.MB360.insurance.claimsprocedure.repository.responseclass.** {*;}
-keepclasseswithmembers class com.indiainsure.android.MB360.insurance.coverages.repository.responseclass.** {*;}
-keepclasseswithmembers class com.indiainsure.android.MB360.insurance.ecards.reponseclass.** {*;}
-keepclasseswithmembers class com.indiainsure.android.MB360.insurance.escalations.repository.responseclass.** {*;}
-keepclasseswithmembers class com.indiainsure.android.MB360.insurance.FAQ.repository.responseclass.** {*;}
-keepclasseswithmembers class com.indiainsure.android.MB360.insurance.hospitalnetwork.reponseclass.** {*;}
-keepclasseswithmembers class com.indiainsure.android.MB360.insurance.myclaims.responseclass.** {*;}
-keepclasseswithmembers class com.indiainsure.android.MB360.insurance.policyfeatures.repository.responseclass.** {*;}
-keepclasseswithmembers class com.indiainsure.android.MB360.insurance.profile.response.** {*;}
-keepclasseswithmembers class com.indiainsure.android.MB360.insurance.queries.responseclass.** {*;}
-keepclasseswithmembers class com.indiainsure.android.MB360.insurance.repository.selectedPolicyRepo.responseclass.** {*;}
-keepclasseswithmembers class com.indiainsure.android.MB360.insurance.servicenames.responseclass.** {*;}
-keepclasseswithmembers class com.indiainsure.android.MB360.insurance.utilities.repository.responseclass.** {*;}
-keepclasseswithmembers class com.indiainsure.android.MB360.utilities.token.responseclasses.** {*;}


-dontwarn javax.annotation.**

-dontwarn android.app.**
-dontwarn android.support.**
-dontwarn android.view.**
-dontwarn android.widget.**

-dontwarn com.google.common.primitives.**

-dontwarn **CompatHoneycomb
-dontwarn **CompatHoneycombMR2
-dontwarn **CompatCreatorHoneycombMR2

-keepclasseswithmembernames class * {
    native <methods>;
}

-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet);
}

-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet, int);
}

-keepclassmembers class * extends android.app.Activity {
   public void *(android.view.View);
}

-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}

-keepclassmembers class **.R$* {
    public static <fields>;
}

-keep class net.sqlcipher.** {
    *;
}

-keep class net.sqlcipher.database.** {
    *;
}


##retrofit
# Retrofit does reflection on generic parameters. InnerClasses is required to use Signature and
# EnclosingMethod is required to use InnerClasses.
-keepattributes Signature, InnerClasses, EnclosingMethod

# Retrofit does reflection on method and parameter annotations.
-keepattributes RuntimeVisibleAnnotations, RuntimeVisibleParameterAnnotations

# Keep annotation default values (e.g., retrofit2.http.Field.encoded).
-keepattributes AnnotationDefault

# Retain service method parameters when optimizing.
-keepclassmembers,allowshrinking,allowobfuscation interface * {
    @retrofit2.http.* <methods>;
}

# Ignore annotation used for build tooling.
-dontwarn org.codehaus.mojo.animal_sniffer.IgnoreJRERequirement

# Ignore JSR 305 annotations for embedding nullability information.
-dontwarn javax.annotation.**

# Guarded by a NoClassDefFoundError try/catch and only used when on the classpath.
-dontwarn kotlin.Unit

# Top-level functions that can only be used by Kotlin.
-dontwarn retrofit2.KotlinExtensions
-dontwarn retrofit2.KotlinExtensions$*

# With R8 full mode, it sees no subtypes of Retrofit interfaces since they are created with a Proxy
# and replaces all potential values with null. Explicitly keeping the interfaces prevents this.
-if interface * { @retrofit2.http.* <methods>; }
-keep,allowobfuscation interface <1>

# Keep inherited services.
-if interface * { @retrofit2.http.* <methods>; }
-keep,allowobfuscation interface * extends <1>

# Keep generic signature of Call, Response (R8 full mode strips signatures from non-kept items).
-keep,allowobfuscation,allowshrinking interface retrofit2.Call
-keep,allowobfuscation,allowshrinking class retrofit2.Response

# With R8 full mode generic signatures are stripped for classes that are not
# kept. Suspend functions are wrapped in continuations where the type argument
# is used.
-keep,allowobfuscation,allowshrinking class kotlin.coroutines.Continuation

-keepattributes LineNumberTable,SourceFile
-renamesourcefileattribute SourceFile