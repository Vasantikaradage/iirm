package com.indiainsure.android.MB360.utilities.rootdetection;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Debug;
import android.widget.Toast;

import com.indiainsure.android.MB360.R;
import com.indiainsure.android.MB360.utilities.FridaListener;
import com.indiainsure.android.MB360.utilities.LogMyBenefits;
import com.scottyab.rootbeer.RootBeer;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class RootDetection {


    public static boolean ROOTED = false;


    public static boolean isRooted(Activity activity, Context context, Boolean toCheck, FridaListener listener) {


        if (toCheck) {
            checkFridaDetection(listener);
            RootBeer rootBeer = new RootBeer(context);
            if (rootBeer.isRooted()) {
                Toast.makeText(activity, "Device is rooted", Toast.LENGTH_SHORT).show();
                return true;
            }
            if (rootBeer.detectPotentiallyDangerousApps()) {
                Toast.makeText(activity, "Detected inappropriate apps", Toast.LENGTH_SHORT).show();
                return true;
            }

            return isEmulator(context) ||
                    isEmulatorLevel2(context) ||
                    fridaEnvironment(context) ||
                    isFridaDetected(context) ||
                    isFridaRunning(context) ||
                    isFridaServerRunning(context) ||
                    detectTestKeys(context) ||
                    manualCheck(context) ||
                    !isDebuggable(context) ||
                    detect_threadCpuTimeNanos(context) ||
                    /* crcTest(context) ||*/
                    checkForSuBinary(context) ||
                    checkForBusyBoxBinary(context) ||
                    checkSuExists(context) ||
                    checkforHooks(context) ||
                    isAppInstalled(context, PACKAGE_NAMES);


        } else {
            return false;
        }

    }

    private static Boolean isEmulator(Context context) {
        Boolean isEmulator = (Build.MANUFACTURER.contains("Genymotion")
                || Build.MODEL.contains("google_sdk")
                || Build.MODEL.toLowerCase().contains("droid4x")
                || Build.MODEL.contains("Emulator")
                || Build.MODEL.contains("Android SDK built for x86")
                || Objects.equals(Build.HARDWARE, "goldfish")
                || Objects.equals(Build.HARDWARE, "vbox86")
                || Build.HARDWARE.toLowerCase().contains("nox")
                || Build.FINGERPRINT.startsWith("generic")
                || Objects.equals(Build.PRODUCT, "sdk")
                || Objects.equals(Build.PRODUCT, "google_sdk")
                || Objects.equals(Build.PRODUCT, "sdk_x86")
                || Objects.equals(Build.PRODUCT, "vbox86p")
                || Build.PRODUCT.toLowerCase().contains("nox")
                || Build.BOARD.toLowerCase().contains("nox")
                || (Build.BRAND.startsWith("generic") && Build.DEVICE.startsWith("generic")));
        if (isEmulator) {
            Toast.makeText(context, "Emulator found!", Toast.LENGTH_SHORT).show();
        }
        return isEmulator;
    }

    private static boolean isEmulatorLevel2(Context context) {


        ArrayList<String> GENY_FILES = new ArrayList<>();
        GENY_FILES.add("/dev/socket/genyd");
        GENY_FILES.add("/dev/socket/baseband_genyd");

        ArrayList<String> PIPES = new ArrayList<>();
        GENY_FILES.add("/dev/socket/qemud");
        GENY_FILES.add("/dev/qemu_pipe");

        ArrayList<String> X86_FILES = new ArrayList<>();
        GENY_FILES.add("ueventd.android_x86.rc");
        GENY_FILES.add("x86.prop");
        GENY_FILES.add("ueventd.ttVM_x86.rc");
        GENY_FILES.add("init.ttVM_x86.rc");
        GENY_FILES.add("fstab.ttVM_x86");
        GENY_FILES.add("fstab.vbox86");
        GENY_FILES.add("init.vbox86.rc");
        GENY_FILES.add("ueventd.vbox86.rc");


        ArrayList<String> ANDY_FILES = new ArrayList<>();
        GENY_FILES.add("fstab.andy");
        GENY_FILES.add("ueventd.andy.rc");


        ArrayList<String> NOX_FILES = new ArrayList<>();
        GENY_FILES.add("fstab.nox");
        GENY_FILES.add("init.nox.rc");
        GENY_FILES.add("ueventd.nox.rc");


        return (checkFiles(context, GENY_FILES)
                || checkFiles(context, ANDY_FILES)
                || checkFiles(context, NOX_FILES)
                || checkFiles(context, X86_FILES)
                || checkFiles(context, PIPES));
    }

    private static Boolean checkFiles(Context context, ArrayList<String> targets) {
        Boolean isEMULATOR = false;
        for (String pipe : targets) {
            File file = new File(pipe);
            if (file.exists()) {
                Toast.makeText(context, "Emulator found!!", Toast.LENGTH_SHORT).show();
                isEMULATOR = true;
            }
        }
        return isEMULATOR;
    }

    private static boolean fridaEnvironment(Context context) {

        boolean ROOTED = false;

        // Get currently running application processes
        final ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        final List<ActivityManager.RunningServiceInfo> list = activityManager.getRunningServices(Integer.MAX_VALUE);

        try {
            if (list != null) {
                String tempName;
                for (int i = 0; i < list.size(); ++i) {
                    tempName = list.get(i).process;

                    if (tempName.contains("fridaserver")) {
                        ROOTED = true;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ROOTED;
    }

    public static boolean isFridaDetected(Context context) {
        boolean result = false;
        try {
            String mapsFilename = "/proc/" + android.os.Process.myPid() + "/maps";
            File mapsFile = new File(mapsFilename);
            if (mapsFile.exists()) {
                BufferedReader reader = new BufferedReader(new FileReader(mapsFile));
                String line;
                while ((line = reader.readLine()) != null) {
                    if (line.endsWith("frida-agent.so")) {
                        Toast.makeText(context, "Frida Detected!", Toast.LENGTH_SHORT).show();
                        result = true;
                        break;
                    }
                }
                reader.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static void checkFridaDetection(final FridaListener listener) {
        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            boolean result = false;
            int[] fridaPorts = {27042, 27043}; // Common ports used by Frida
            try {
                for (int port : fridaPorts) {
                    LogMyBenefits.d("SOCKET", "CHECKING SOCKET");
                    Socket socket = new Socket();
                    socket.setSoTimeout(100);
                    try {
                        socket.connect(new InetSocketAddress("127.0.0.1", port));
                        result = true;
                        break;
                    } catch (IOException e) {
                        // Port is not open
                    } finally {
                        socket.close();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (listener != null) {
                listener.onFridaDetection(result);
            }
        });
    }

    public static boolean isFridaRunning(Context context) {
        try {
            Process process = Runtime.getRuntime().exec("su");
            DataOutputStream outputStream = new DataOutputStream(process.getOutputStream());
            outputStream.writeBytes("am start-activity -n com.package.name/.MainActivity\n");
            outputStream.flush();
            outputStream.writeBytes("exit\n");
            outputStream.flush();
            process.waitFor();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.contains("Frida")) {
                    Toast.makeText(context, "Frida is running!", Toast.LENGTH_SHORT).show();
                    return true;
                }
            }
            reader.close();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean isFridaServerRunning(Context context) {
        try {
            Process process = Runtime.getRuntime().exec("ps");
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.contains("frida-server")) {
                    Toast.makeText(context, "Frida is running!", Toast.LENGTH_SHORT).show();
                    return true;
                }
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    private static boolean detectTestKeys(Context context) {
        String buildTags = android.os.Build.TAGS;
        if (buildTags != null && buildTags.contains("test-keys")) {
            Toast.makeText(context, "Test keys found!", Toast.LENGTH_SHORT).show();
            return true;
        } else {
            return false;
        }
    }

    private static Boolean manualCheck(Context context) {
        Boolean ROOTED = false;
        String su = "su";
        ArrayList<String> locations = new ArrayList<>();
        locations.add("/system/bin/");
        locations.add("/system/xbin/");
        locations.add("/sbin/");
        locations.add("/system/sd/xbin/");
        locations.add("/system/xbin/");
        locations.add("/data/local/xbin/");
        locations.add("/data/local/bin/");
        locations.add("/data/local/");

        for (String location : locations
        ) {
            if (new File(location + su).exists()) {
                Toast.makeText(context, "SuperUser found!", Toast.LENGTH_SHORT).show();
                ROOTED = true;
            }
        }
        return ROOTED;
    }

    public static boolean isDebuggable(Context context) {


        if ((context.getApplicationContext().getApplicationInfo().flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0) {
            Toast.makeText(context, "App is in debug mode!", Toast.LENGTH_SHORT).show();
            return true;
        } else {
            return false;
        }


    }

    static boolean detect_threadCpuTimeNanos(Context context) {
        long start = Debug.threadCpuTimeNanos();

        for (int i = 0; i < 1000000; ++i)
            continue;

        long stop = Debug.threadCpuTimeNanos();

        if (stop - start < 10000000) {
            return false;
        } else {
            Toast.makeText(context, "Cpu time tampered", Toast.LENGTH_SHORT).show();
            return true;
        }
    }

    private static Boolean crcTest(Context context) {
        try {


            boolean modified = false;
            // required dex crc value stored as a text string.
            // it could be any invisible layout element
            long dexCrc = Long.parseLong(context.getString(R.string.dex_crc));

            ZipFile zf = new ZipFile(context.getPackageCodePath());
            ZipEntry ze = zf.getEntry("classes.dex");

            if (ze.getCrc() != dexCrc) {
                // dex has been modified
                return true;
            } else {
                // dex not tampered with
                return false;
            }
        } catch (Exception e) {
            return false;
        }

    }

    private static boolean checkForSuBinary(Context context) {
        return checkForBinary(context, "su"); // function is available below
    }

    private static boolean checkForBusyBoxBinary(Context context) {
        return checkForBinary(context, "busybox");//function is available below
    }

    private static boolean checkSuExists(Context context) {
        Process process = null;
        try {
            process = Runtime.getRuntime().exec(new String[]
                    {"/system /xbin/which", "su"});
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(process.getInputStream()));
            String line = in.readLine();
            process.destroy();

            if (line != null) {
                Toast.makeText(context, "SuperUser found!", Toast.LENGTH_SHORT).show();
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            if (process != null) {
                process.destroy();
            }
            return false;
        }
    }

    /**
     * @param filename - check for this existence of this
     *                 file("su","busybox")
     * @return true if exists
     */
    private static boolean checkForBinary(Context context, String filename) {
        for (String path : binaryPaths) {
            File f = new File(path, filename);
            boolean fileExists = f.exists();
            if (fileExists) {
                Toast.makeText(context, filename + " found!", Toast.LENGTH_SHORT).show();
                return true;
            }
        }
        return false;
    }

    private static final String[] binaryPaths = {
            "/data/local/",
            "/data/local/bin/",
            "/data/local/xbin/",
            "/sbin/",
            "/su/bin/",
            "/system/bin/",
            "/system/bin/.ext/",
            "/system/bin/failsafe/",
            "/system/sd/xbin/",
            "/system/usr/we-need-root/",
            "/system/xbin/",
            "/system/app/Superuser.apk",
            "/cache",
            "/data",
            "/dev"
    };

    /**
     * A variation on the checking for SU, this attempts a 'which su'
     * different file system check for the su binary
     *
     * @return true if su exists
     */


    // Package names to check
    private static String[] PACKAGE_NAMES = {
            "com.devadvance.rootcloak",
            "com.devadvance.rootcloakplus",
            "com.koushikdutta.superuser",
            "com.thirdparty.superuser",
            "com.topjohnwu.magisk"
    };

    //check for Hooks
    public static boolean checkforHooks(Context context) {
        boolean hooks = true;
        PackageManager packageManager = context.getPackageManager();
        List<ApplicationInfo> applicationInfoList = packageManager.getInstalledApplications(PackageManager.GET_META_DATA);

        for (ApplicationInfo applicationInfo : applicationInfoList) {
            if (applicationInfo.packageName.equals("de.robv.android.xposed.installer")) {
                LogMyBenefits.d("HookDetection", "Xposed found on the system.");
                hooks = true;
            } else if (applicationInfo.packageName.equals("com.saurik.substrate")) {
                LogMyBenefits.d("HookDetection", "Substrate found on the system.");
                hooks = true;
            } else {
                hooks = false;
            }
        }
        return hooks;
    }

    // Method to check if an app is installed
    public static boolean isAppInstalled(Context context, String[] packageName) {
        try {
            for (String packages : packageName
            ) {
                PackageInfo packageInfo = context.getPackageManager()
                        .getPackageInfo(packages, PackageManager.GET_ACTIVITIES);
                Toast.makeText(context, packages + " app found!", Toast.LENGTH_SHORT).show();
                return packageInfo != null;
            }
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
        return false;
    }

}
