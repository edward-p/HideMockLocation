package xyz.edward_p.hidemocklocation;

import android.os.Bundle;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;


public class XposedModule implements IXposedHookLoadPackage {

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) {

        // Google Play Services
        // Breaks safety net check. If you don't want, don't bring gms into the scope in your Xposed Installer
        XposedHelpers.findAndHookMethod("android.location.Location", lpparam.classLoader, "getExtras",
                new XC_MethodHook() {
                    @Override
                    protected void afterHookedMethod(MethodHookParam param) {
                        Bundle extras = (Bundle) param.getResult();
                        if (extras != null && extras.getBoolean("mockLocation"))
                            extras.putBoolean("mockLocation", false);
                    }
                });

        // SDK 18+
        XposedHelpers.findAndHookMethod("android.location.Location", lpparam.classLoader,
                "isFromMockProvider", new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) {
                        param.setResult(false);
                    }
                });

    }

}