package com.estimote.examples.demos;

import android.app.Application;
import com.estimote.sdk.EstimoteSDK;

/**
 * Main {@link Application} object for Demos. It configures EstimoteSDK.
 *
 * @author wiktor@estimote.com (Wiktor Gworek)
 */
public class DemosApplication extends Application {

  @Override
  public void onCreate() {
    super.onCreate();

    // Initializes Estimote SDK with your App ID and App Token from Estimote Cloud.
    // You can find your App ID and App Token in the
    // Apps section of the Estimote Cloud (http://cloud.estimote.com).
    EstimoteSDK.initialize(this, "app_0ohobr6l7i", "3b3a624e4225fed6b5325c5ca382d527");

    // Configure verbose debug logging.
    EstimoteSDK.enableDebugLogging(true);
  }
}
